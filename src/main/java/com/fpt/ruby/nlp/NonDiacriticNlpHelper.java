
package com.fpt.ruby.nlp;

import java.util.ArrayList;
import java.util.List;

import com.fpt.ruby.business.constants.IntentConstants;
import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.MovieTicket;
import com.fpt.ruby.business.model.QuestionStructure;
import com.fpt.ruby.business.model.TimeExtract;
import com.fpt.ruby.business.service.NameMapperService;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;

import fpt.qa.additionalinformation.modifier.AbsoluteTime;
import fpt.qa.additionalinformation.modifier.AbsoluteTime.TimeResult;
import fpt.qa.intent.detection.MovieIntentDetection;
import fpt.qa.intent.detection.NonDiacriticMovieIntentDetection;
import fpt.qa.mdnlib.struct.pair.Pair;

public class NonDiacriticNlpHelper{
	private static AbsoluteTime absoluteTime;
	
	public static void init(NameMapperService nameMapperService){
		String dir = ( new RedisHelper() ).getClass().getClassLoader().getResource( "" ).getPath();
		MovieIntentDetection.init(dir + "/qc/movie", dir + "/dicts");
		NonDiacriticMovieIntentDetection.init( dir + "/qc/movie/non-diacritic", dir + "/dicts/non-diacritic" );
		absoluteTime = new AbsoluteTime( NonDiacriticNlpHelper.class.getClassLoader().getResource( "" ).getPath()
				+ "vnsutime/" );
	}

	public static String getMovieTitle( String question, ConjunctionHelper conjNoneDiacritic ) {
		List< Pair< String, String >> conjunctions = conjNoneDiacritic.getConjunction( question );
		for( Pair< String, String > conjunction : conjunctions ){
			System.out.println( conjunction.first + " | " + conjunction.second );
			if( conjunction.second.equals( IntentConstants.MOV_TITLE ) )
				return conjunction.first.replace( "{", "" ).replace( "}", "" );
		}
		return null;
	}

	public static MovieTicket getMovieTicket( String question, ConjunctionHelper conjNoneDiacritic ) {
		List< Pair< String, String >> conjunctions = conjNoneDiacritic.getConjunction( question );
		MovieTicket movieTicket = new MovieTicket();
		for( Pair< String, String > conjunction : conjunctions ){
			System.out.println( conjunction.first + " | " + conjunction.second );
			if( conjunction.second.equals( IntentConstants.CIN_NAME ) )
				movieTicket.setCinema( conjunction.first.replace( "{", "" ).replace( "}", "" ) );
			if( conjunction.second.equals( IntentConstants.MOV_TITLE ) )
				movieTicket.setMovie( conjunction.first.replace( "{", "" ).replace( "}", "" ) );
		}
		return movieTicket;
	}

	public static QuestionStructure processQuestionStructure( String question ) {
		QuestionStructure questionStructure = new QuestionStructure();
		questionStructure.setKey( normalizeQuestion( question ) );
		questionStructure.setHead( question.isEmpty() ? "" : NonDiacriticMovieIntentDetection
				.getIntent( normalizeQuestion( question ) ) );
		questionStructure.setModifiers( new ArrayList< String >() );
		return questionStructure;
	}

	public static String normalizeQuestion( String question ) {
		if( question.isEmpty() ){
			return "";
		}
		int j = question.length() - 1;
		// remove question mark or special character
		for( ; j >= 0; j-- ){
			char c = question.charAt( j );
			if( Character.isLetter( c ) || Character.isDigit( c ) ){
				break;
			}
		}
		return question.toLowerCase().substring( 0, j );
	}

	public static TimeExtract getTimeCondition( String text ) {
		try{
			TimeResult timeResult = absoluteTime.getAbsoluteTime( text );
			TimeExtract timeExtract = new TimeExtract();
			timeExtract.setBeforeDate( timeResult.getBeginTime() );
			timeExtract.setAfterDate( timeResult.getEndTime() );
			return timeExtract;
		}catch ( Exception ex ){
			System.out.println( "Time Exception!" );
			return new TimeExtract();
		}

	}

	public static void main( String[] args ) {
	}

}
