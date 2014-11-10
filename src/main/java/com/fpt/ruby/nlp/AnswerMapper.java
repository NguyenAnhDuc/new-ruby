package com.fpt.ruby.nlp;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.fpt.ruby.business.constants.IntentConstants;
import com.fpt.ruby.business.service.MovieFlyService;
import com.fpt.ruby.helper.FeaturedMovieHelper;
import com.fpt.ruby.model.Cinema;
import com.fpt.ruby.model.MovieFly;
import com.fpt.ruby.model.MovieTicket;


public class AnswerMapper {
	private static MovieAnswerMapper mam = new MovieAnswerMapperImpl();
	private static TicketAnswerMapper tam = new TicketAnswerMapperImpl();
	
	public static String Static_Question = "static";
	public static String Dynamic_Question = "dynamic";
	public static String Featured_Question = "featured";
	
	public static String Default_Answer = "Xin lỗi, chúng tôi không tìm thấy câu trả lời cho câu hỏi cảu bạn";
	
	// detect question is static or dynamic
	public static String getTypeOfAnswer(String intent, String question){
		if (intent.equals(IntentConstants.MOV_DATE)) return Dynamic_Question; 
		if (intent.equals(IntentConstants.CIN_NAME)) return Dynamic_Question; 
		if (intent.equals(IntentConstants.MOV_TITLE)){
			MovieModifiers mod = MovieModifiers.getModifiers(question);
			if (question.contains("nhất") || question.contains("hay") || question.contains("nhat")){
				return Featured_Question;
			}
			
			if (mod.getTitle() == null && mod.atLeastOneOtherFeatureNotNull()){
				return Featured_Question;
			}
				
			return Dynamic_Question;
		}
		if (intent.equals(IntentConstants.MOV_TYPE)) return Dynamic_Question; 
		return Static_Question;
	}
	
	public static String getStaticAnswer(String intent, List<MovieFly> ans){
		if(intent.equals(IntentConstants.MOV_ACTOR)){
			return mam.getActorMovieAnswer(ans);
		}
		
		if(intent.equals(IntentConstants.MOV_AUDIENCE)){
			return mam.getAudienceMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_AWARD)){
			return mam.getCountryMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_COUNTRY)){
			return mam.getCountryMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_DIRECTOR)){
			return mam.getDirectorMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_GENRE)){
			return mam.getGenreMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_IMDBRATING)){
			return mam.getImdbRatingMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_LANG)){
			return mam.getLangMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_PLOT)){
			return mam.getPlotMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_RELEASE)){
			return mam.getReleaseMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_RUNTIME)){
			return mam.getRuntimeMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_TITLE)){
			return mam.getTitleMovieAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_YEAR)){
			return mam.getYearMovieAnswer(ans);
		}
		
		return "Xin lỗi, chúng tôi chưa có câu trả lời cho câu hỏi của bạn";
	}
	
	public static String getCinemaStaticAnswer(String intent, List<Cinema> ans){
		
		if (intent.equals(IntentConstants.CIN_ADD)){
			return mam.getCinemaAddressAnswer(ans);
		}
		
		return "Xin lỗi, chúng tôi chưa có câu trả lời cho câu hỏi của bạn";
	}
	
	public static String getDynamicAnswer(String intent, List<MovieTicket> ans, MovieTicket matchMovieTicket, boolean haveTimeInfo){
		if (intent.equals(IntentConstants.MOV_DATE)){
			return tam.getDateTicketAnswer(ans, matchMovieTicket, haveTimeInfo);
		}
		
		if (intent.equals(IntentConstants.CIN_NAME)){
			return tam.getCinemaTicketAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_TYPE)){
			return tam.getTypeTicketAnswer(ans);
		}
		
		if (intent.equals(IntentConstants.MOV_TITLE)){
			return tam.getTitleTicketAnswer(ans);
		}
		
		return "Xin lỗi, chúng tôi chưa có câu trả lời cho câu hỏi của bạn";
	}
	
	
	public static String getFeaturedAnswer(String question, List<MovieTicket> ans, MovieFlyService movieFlyService) throws UnsupportedEncodingException{
		List<String> titles = getDistinctMovieTitle(ans);
		List<MovieFly> movieFlies = movieFlyService.findByListTitle(titles);
		if (question.contains("nhất") || question.contains("hay")){
			return FeaturedMovieHelper.filterByImdb(movieFlies);
		}
		
		MovieModifiers mod = MovieModifiers.getModifiers(question);
		if (mod.getActor() != null){
			return FeaturedMovieHelper.filterByActor(mod.getActor(), movieFlies);
		}
		
		if (mod.getDirector() != null) {
			return FeaturedMovieHelper.filterByDirector(mod.getDirector(), movieFlies);
		}
		
		if (mod.getAward() != null){
			return FeaturedMovieHelper.filterByAward(mod.getAward(), movieFlies);
		}
		
		if (!mod.getGenre().isEmpty()){
			return FeaturedMovieHelper.filterByGenre(mod.getGenre(), movieFlies);
		}
		
		if (mod.getCountry() != null){
			return FeaturedMovieHelper.filterByCountry(mod.getCountry(), movieFlies);
		}
		
		if (mod.getLang() != null) {
			return FeaturedMovieHelper.filterByLang(mod.getLang(), movieFlies);
		}
		
		return Default_Answer;
	}
	
	private static List<String> getDistinctMovieTitle(List<MovieTicket> ans){
		List<String> titles = new ArrayList<String>();
		
		for (MovieTicket mt : ans){
			String curTitle = mt.getMovie();
			String[] someTiles = curTitle.split("-");
			if (someTiles.length == 1) curTitle = someTiles[0].trim();
			if (someTiles.length == 2) curTitle = someTiles[1].trim();
			if (!titles.contains(curTitle)){
				titles.add(curTitle);
			}
		}
		
		return titles;
	}
	
	
	
}