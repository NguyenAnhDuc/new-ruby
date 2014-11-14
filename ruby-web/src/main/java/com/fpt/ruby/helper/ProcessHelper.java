package com.fpt.ruby.helper;

import com.fpt.ruby.business.constants.IntentConstants;
import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.*;
import com.fpt.ruby.business.service.*;
import com.fpt.ruby.intent.detection.MovieIntentDetection;
import com.fpt.ruby.intent.detection.NonDiacriticMovieIntentDetection;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import com.fpt.ruby.nlp.AnswerMapper;
import com.fpt.ruby.nlp.NlpHelper;
import fpt.qa.mdnlib.util.string.DiacriticConverter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessHelper {
	private static final Logger logger = LoggerFactory
			.getLogger(ProcessHelper.class);

	public static void init(NameMapperService nameMapperService) {
		String dir = (new RedisHelper()).getClass().getClassLoader()
				.getResource("").getPath();
	}

	public static RubyAnswer getAnswer(String question,
			QuestionStructure questionStructure,
			MovieFlyService movieFlyService,
			MovieTicketService movieTicketService) {
		RubyAnswer rubyAnswer = new RubyAnswer();
		rubyAnswer.setAnswer("this is answer of ruby");
		// rubyAnswer.setAnswer(getAnswer(question, movieFlyService,
		// movieTicketService));
		// rubyAnswer.setAnswer(getSimsimiResponse(question));
		rubyAnswer.setQuestionStructure(questionStructure);
		return rubyAnswer;
	}

	public static RubyAnswer getAnswer(String question,
			MovieFlyService movieFlyService,
			MovieTicketService movieTicketService, CinemaService cinemaService,
			LogService logService,
			ConjunctionHelper conjunctionHelperNoneDiacritic,
			ConjunctionHelper conjunctionHelperWithDiacritic) {
		if (DiacriticConverter.hasDiacriticAccents(question)) {
			System.out.println("DIACRITIC");
			RubyAnswer rubyAnswerDiacritic = getAnswer(true, question,
					movieFlyService, movieTicketService, cinemaService,
					logService, conjunctionHelperNoneDiacritic,
					conjunctionHelperWithDiacritic);
			return rubyAnswerDiacritic;
		}
		System.out.println("NONE DIACRITIC");
		RubyAnswer rubyAnswerNoneDiacritic = getAnswer(false, question,
				movieFlyService, movieTicketService, cinemaService, logService,
				conjunctionHelperNoneDiacritic, conjunctionHelperWithDiacritic);
		return rubyAnswerNoneDiacritic;
		/*
		 * RubyAnswer rubyAnswerDiacritic = getAnswer(true, question,
		 * movieFlyService, movieTicketService, cinemaService, logService);
		 * return rubyAnswerDiacritic;
		 */
	}

	private static RubyAnswer getAnswer(boolean isDiacritic, String question,
			MovieFlyService movieFlyService,
			MovieTicketService movieTicketService, CinemaService cinemaService,
			LogService logService,
			ConjunctionHelper conjunctionHelperNoneDiacritic,
			ConjunctionHelper conjunctionHelperWithDiacritic) {
		// Conjunction
		ConjunctionHelper conjunctionHelper;
		if (isDiacritic)
			conjunctionHelper = conjunctionHelperWithDiacritic;
		else
			conjunctionHelper = conjunctionHelperNoneDiacritic;

		String intent = "";

		//Time Extracter
		TimeExtract timeExtract = NlpHelper.getTimeCondition(question
				.replaceAll("(\\d+)(h)", "$1 giờ"));

		if (question.contains("đang") || question.contains("dang")
				|| question.contains("hiện tại")
				|| question.contains("hien tai")
				|| question.contains("hien")
				|| question.contains("hiện")) {
			timeExtract = NlpHelper.getTimeCondition("hôm nay");
		}
		// Intent
		if (isDiacritic)
			intent = MovieIntentDetection.getIntent(question);
		else {
			question = DiacriticConverter.removeDiacritics(question);
			intent = NonDiacriticMovieIntentDetection.getIntent(question);
		}
		System.out.println("[ProcessHelper] Intent: " + intent);
		RubyAnswer rubyAnswer = new RubyAnswer();
		rubyAnswer.setQuestion(question);
		rubyAnswer.setIntent(intent);

		String questionType = AnswerMapper.getTypeOfAnswer(intent, question);
		rubyAnswer.setAnswer("Xin lỗi, tôi không trả lời được câu hỏi này");
		System.out.println("[ProcessHelper] Question Type: " + questionType);
		// static question
		try {
			if (intent.equals(IntentConstants.CIN_DIS)
					|| intent.equals(IntentConstants.CIN_MAP)
					|| intent.equals(IntentConstants.CIN_SERVICETIME)) {
				rubyAnswer.setSuccessful(true);
				return rubyAnswer;
			}

			if (intent.equals(IntentConstants.MOV_AWARD)
					|| intent.equals(IntentConstants.MOV_WRITER)
					|| intent.equals(IntentConstants.TICKET_PRICE)
					|| intent.equals(IntentConstants.TICKET_STATUS)
					|| intent.equals(IntentConstants.UNDEF)) {
				rubyAnswer.setSuccessful(true);
				return rubyAnswer;
			}

			QueryParamater queryParamater = new QueryParamater();
			if (questionType.equals(AnswerMapper.Static_Question)) {
				if (intent.equals(IntentConstants.CIN_ADD)) {
					String cinName = conjunctionHelper.getCinemaName(question);
					System.out.println("[Process Helper] Cin name: " + cinName);
					List<Cinema> cinemas = cinemaService.findByName(cinName);
					queryParamater.setCinName(cinName);
					rubyAnswer.setAnswer(AnswerMapper.getCinemaStaticAnswer(
							intent, cinemas));
				} else {
					String movieTitle = conjunctionHelper
							.getMovieTitle(question);
					System.out.println("Movie Title: " + movieTitle);
					List<MovieFly> movieFlies = movieFlyService
							.findByTitle(movieTitle);
					queryParamater.setMovieTitle(movieTitle);
					rubyAnswer.setAnswer(AnswerMapper.getStaticAnswer(intent,
							movieFlies));
				}
				rubyAnswer.setQueryParamater(queryParamater);
				rubyAnswer.setQuestionType(AnswerMapper.Static_Question);

			} else if (questionType.equals(AnswerMapper.Dynamic_Question)) {
				System.out.println("Dynamic ....");
				MovieTicket matchMovieTicket = conjunctionHelper
						.getMovieTicket(question);

				if (timeExtract.getBeforeDate() == null) {
					queryParamater.setBeginTime(timeExtract.getBeforeDate());
					rubyAnswer.setBeginTime(NlpHelper.getTimeCondition(
							"hôm nay").getBeforeDate());
				}
				if (timeExtract.getAfterDate() != null) {
					queryParamater.setBeginTime(timeExtract.getBeforeDate());
					rubyAnswer.setBeginTime(timeExtract.getBeforeDate());
				}
				if (timeExtract.getAfterDate() != null) {
					queryParamater.setEndTime(timeExtract.getAfterDate());
					rubyAnswer.setEndTime(timeExtract.getAfterDate());
				}
				List<MovieTicket> movieTickets = movieTicketService
						.findMoviesMatchCondition(matchMovieTicket,
								rubyAnswer.getBeginTime(),
								rubyAnswer.getEndTime());
				System.out.println("Size: " + movieTickets.size());


				queryParamater.setMovieTitle(matchMovieTicket.getMovie());
				queryParamater.setCinName(matchMovieTicket.getCinema());
				rubyAnswer.setQueryParamater(queryParamater);
				rubyAnswer.setAnswer(AnswerMapper.getDynamicAnswer(intent,
						movieTickets, matchMovieTicket,
						true));
				rubyAnswer.setQuestionType(AnswerMapper.Dynamic_Question);
				System.out.println("DONE Process");
			} else {
				System.out.println("Feature ..");
				MovieTicket matchMovieTicket = conjunctionHelper
						.getMovieTicket(question);
				Date today = new Date();
				System.out.println("afterdate: " + today);
				if (timeExtract.getBeforeDate() != null) {
					queryParamater.setBeginTime(timeExtract.getBeforeDate());
					rubyAnswer.setBeginTime(timeExtract.getBeforeDate());
				}

				if (timeExtract.getAfterDate() != null) {
					queryParamater.setBeginTime(timeExtract.getAfterDate());
					rubyAnswer.setEndTime(timeExtract.getAfterDate());
				}

				if (rubyAnswer.getBeginTime() == null) {
					queryParamater.setBeginTime(timeExtract.getAfterDate());
					rubyAnswer.setBeginTime(NlpHelper.getTimeCondition(
							"hôm nay").getBeforeDate());
				}
				// list movie tickets for the duration of one day
				List<MovieTicket> movieTickets = movieTicketService
						.findMoviesMatchCondition(matchMovieTicket,
								queryParamater.getBeginTime(),
								queryParamater.getEndTime());
				System.out.println("No of returned tickets: "
						+ movieTickets.size());
				queryParamater.setMovieTitle(matchMovieTicket.getMovie());
				queryParamater.setCinName(matchMovieTicket.getCinema());
				rubyAnswer.setQueryParamater(queryParamater);
				rubyAnswer.setAnswer(AnswerMapper.getFeaturedAnswer(question,
						movieTickets, movieFlyService));
				rubyAnswer.setQuestionType(AnswerMapper.Featured_Question);
				rubyAnswer.setMovieTicket(matchMovieTicket);
			}
		} catch (Exception ex) {
			System.out.println("Exception! " + ex.getMessage());
			ex.printStackTrace();
		}

		if (!rubyAnswer.getAnswer().contains(
				"Xin lỗi, tôi không trả lời được câu hỏi này")) {
			rubyAnswer.setSuccessful(true);
		}

		return rubyAnswer;
	}

	private static boolean isUdfAnswer(String answer, List<String> answers) {
		return answers.stream().anyMatch(a -> answer.contains(a));
	}

	public static String getAIMLAnswer(String question, String botId,
			String token) {
		List<String> udfAnswers = new ArrayList<String>();
		udfAnswers.add("Tôi không biết");
		try {
			String url = "http://tech.fpt.com.vn/AIML/api/bots/" + botId
					+ "/chat?token=" + token + "&request="
					+ URLEncoder.encode(question, "UTF-8");
			String jsonString = HttpHelper.sendGet(url);
			JSONObject json = new JSONObject(jsonString);
			String answer = json.getString("response");
			System.out.println("AIML get answer: " + answer);
			if (answer.contains("sraix_wiki"))
				answer = new JSONObject(answer).getJSONObject("response")
						.getString("content").trim();
			if (!answer.isEmpty() && isUdfAnswer(answer, udfAnswers))
				return null;
			return answer;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public static RubyAnswer getAnswerFromSimsimi(String question,
			QuestionStructure questionStructure) {
		RubyAnswer rubyAnswer = new RubyAnswer();
		rubyAnswer.setQuestionStructure(questionStructure);
		return rubyAnswer;
	}

	public static QuestionStructure getQuestionStucture(String question,
			QuestionStructureService questionStructureService) {
		QuestionStructure questionStructure = new QuestionStructure();
		String key = NlpHelper.normalizeQuestion(question);
		// If in cache
		if (questionStructureService.isInCache(key)) {
			questionStructure = questionStructureService.getInCache(key);
			return questionStructure;
		}
		// If not in cache
		questionStructure = NlpHelper.processQuestionStructure(question);
		// Cache new question
		questionStructureService.cached(questionStructure);
		// Save to mongodb
		questionStructureService.save(questionStructure);
		return questionStructure;
	}

}