package com.fpt.ruby.nlp;

import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.QuestionStructure;
import com.fpt.ruby.business.model.TimeExtract;
import com.fpt.ruby.intent.detection.MovieTypeDetection;
import com.fpt.ruby.intent.detection.NonDiacriticMovieIntentDetection;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import fpt.qa.additionalinformation.modifier.AbsoluteTime;
import fpt.qa.additionalinformation.modifier.AbsoluteTime.TimeResult;
import fpt.qa.spellchecker.SpellCheckAndCorrector;

import java.util.ArrayList;

public class NlpHelper {
	private static ConjunctionHelper conjunctionHelper;
	private static AbsoluteTime absoluteTime;
	private static SpellCheckAndCorrector spellCheckAndCorrector ;

	public static void init() {
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		MovieTypeDetection.init(dir + "/qc/movie", dir + "/dicts");
		NonDiacriticMovieIntentDetection.init( dir + "/qc/movie/non-diacritic", dir + "/dicts/non-diacritic" );
		absoluteTime = new AbsoluteTime( NlpHelper.class.getClassLoader().getResource("").getPath() + "vnsutime/" );
		spellCheckAndCorrector = new SpellCheckAndCorrector(dir);
	}
	
	public String getIntent(boolean isDiacritic, String question){
		if (isDiacritic) return MovieTypeDetection.getIntent(question);
		else return NonDiacriticMovieIntentDetection.getIntent(question);
	}
	
	
	
	
	public static QuestionStructure processQuestionStructure(String question){
		QuestionStructure questionStructure = new QuestionStructure();
		questionStructure.setKey(normalizeQuestion(question));
		questionStructure.setHead(question.isEmpty() ? "" : MovieTypeDetection.getIntent(normalizeQuestion(question)));
		questionStructure.setModifiers(new ArrayList<String>());
		return questionStructure;
	}
	
	public static String normalizeQuestion(String ques){
		String question = ques.toLowerCase();
		if(question.isEmpty()){
			return "";
		}

		int j = question.length() - 1;
		// remove question mark or special character
		for (; j >= 0; j--){
			char c = question.charAt(j);
			if (Character.isLetter(c) || Character.isDigit(c)){
				break;
			}
		}
		question =  question.toLowerCase().substring(0,j+1);
		return spellCheckAndCorrector.completed(question);

	}
	
	public static TimeExtract getTimeCondition(String text){
		try{
			TimeResult timeResult = absoluteTime.getAbsoluteTime(text);
			TimeExtract timeExtract = new TimeExtract();
			timeExtract.setBeforeDate(timeResult.getBeginTime());
			timeExtract.setAfterDate(timeResult.getEndTime());
			System.err.println("Time Extract: " + timeResult.getBeginTime() + " | " + timeResult.getEndTime());
			return timeExtract;
		}
		catch (Exception ex){
			System.out.println("Time Exception!");
			return new TimeExtract();
		}
	}
}
