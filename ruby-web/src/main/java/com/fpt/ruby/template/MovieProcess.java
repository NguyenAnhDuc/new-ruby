package com.fpt.ruby.template;

import com.fpt.ruby.business.constants.IntentConstants;
import com.fpt.ruby.business.model.MovieFly;
import com.fpt.ruby.business.model.MovieTicket;
import com.fpt.ruby.business.model.TimeExtract;
import com.fpt.ruby.business.service.MovieFlyService;
import com.fpt.ruby.business.service.MovieTicketService;
import com.fpt.ruby.business.template.DomainType;
import com.fpt.ruby.business.template.IConstants;
import com.fpt.ruby.business.template.MovieModifiers;
import com.fpt.ruby.intent.detection.MovieTypeDetection;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import com.fpt.ruby.nlp.AnswerMapper;
import com.fpt.ruby.nlp.NlpHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MovieProcess extends RubyProcess {
	private static final Logger logger = Logger.getLogger(MovieTicketService.class.getName());

	RubyAnswer rubyAnswer = new RubyAnswer();

	public RubyAnswer getRubyAnswer() {
		return rubyAnswer;
	}

	private final String Question_Type_Static = "static";
	private final String Question_Type_Dynamic = "dynamic";
	private final String Question_Type_Featured = "featured";
	private final String Default_Time = "hôm nay";

	private ConjunctionHelper conjunctionHelper;
	private MovieFlyService movieFlyService;
	private MovieTicketService movieTicketService;

	MovieModifiers movieModifiers;
	List<MovieFly> movieFlies = new ArrayList<MovieFly>();
	List<MovieTicket> movieTickets = new ArrayList<MovieTicket>();
	public MovieProcess(ConjunctionHelper conjunctionHP, MovieFlyService movieFlySV, MovieTicketService movieTicketSV){
		logger.info("Init Movie Process");
		System.out.println("INITE MOVIE PROCESS");
		conjunctionHelper = conjunctionHP;
		movieFlyService = movieFlySV;
		movieTicketService =  movieTicketSV;
	}
	@Override
	void normalize(String question) {
		rubyAnswer.setQuestion(question);
		rubyAnswer.setDomain(DomainType.MOVIE.toString());
	}

	@Override
	void getIntent() {
		String  intent = MovieTypeDetection.getIntent(rubyAnswer.getQuestion());
		rubyAnswer.setIntent(intent);
	}

	@Override
	void getModifiers() {
		movieModifiers = conjunctionHelper.getMovieModifiers(rubyAnswer.getQuestion());
		rubyAnswer.setRubyModifiers(movieModifiers);
	}

	private String getQuestionType(String intent, String question){
		if (intent.equals(IntentConstants.MOV_DATE)) return Question_Type_Dynamic;
		if (intent.equals(IntentConstants.CIN_NAME)) return Question_Type_Dynamic;
		if (intent.equals(IntentConstants.MOV_TITLE)){
			com.fpt.ruby.nlp.MovieModifiers mod = com.fpt.ruby.nlp.MovieModifiers.getModifiers(question);
			if (question.contains("nhất") || question.contains("hay") || question.contains("nhat")){
				return Question_Type_Featured;
			}

			if (mod.getTitle() == null && mod.atLeastOneOtherFeatureNotNull()){
				return Question_Type_Featured;
			}

			return Question_Type_Dynamic;
		}
		if (intent.equals(IntentConstants.MOV_TYPE)) return Question_Type_Dynamic;
		return Question_Type_Static;
	}

	@Override
	void extractTime() {
		TimeExtract timeExtract = NlpHelper.getTimeCondition(rubyAnswer.getQuestion());
		if (timeExtract.getBeforeDate() == null)  timeExtract = NlpHelper.getTimeCondition(Default_Time);
		rubyAnswer.setTimeExtract(timeExtract);
	}

	@Override
	void getCandidates() {
		String questionType = getQuestionType(rubyAnswer.getIntent(), rubyAnswer.getQuestion());
		switch (questionType){
			case Question_Type_Static:
				try{
					movieFlies = movieFlyService.findByTitle(movieModifiers.getMovieTitle());
				}
				catch (Exception ex){
					ex.printStackTrace();
				}
				break;
			case Question_Type_Dynamic:
			case Question_Type_Featured:
				movieTickets = movieTicketService.filterMoviesMatchCondition(movieModifiers,rubyAnswer.getTimeExtract().getBeforeDate(),rubyAnswer.getTimeExtract().getAfterDate());
				break;
		}
	}

	@Override
	void getAnswer() {
		switch (getQuestionType(rubyAnswer.getIntent(), rubyAnswer.getQuestion())){
			case Question_Type_Static:
				rubyAnswer.setAnswer(AnswerMapper.getStaticAnswer(rubyAnswer.getIntent(),movieFlies));
				break;
			case Question_Type_Dynamic:
				rubyAnswer.setAnswer(AnswerMapper.getDynamicAnswer(rubyAnswer.getIntent(),movieTickets));
				break;
			case Question_Type_Featured:
				try {
					rubyAnswer.setAnswer(AnswerMapper.getFeaturedAnswer(rubyAnswer.getIntent(), movieTickets, movieFlyService));
				}
				catch (Exception ex){
					rubyAnswer.setAnswer(IConstants.ANSWER_ERROR);
				}
				break;
		}
	}
}
