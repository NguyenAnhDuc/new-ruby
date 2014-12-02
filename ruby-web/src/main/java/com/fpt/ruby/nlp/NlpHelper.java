package com.fpt.ruby.nlp;

import java.util.ArrayList;

import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.QuestionStructure;
import com.fpt.ruby.business.model.TimeExtract;
import com.fpt.ruby.intent.detection.MovieTypeDetection;
import com.fpt.ruby.intent.detection.NonDiacriticMovieIntentDetection;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;

import fpt.qa.additionalinformation.modifier.AbsoluteTime;
import fpt.qa.additionalinformation.modifier.AbsoluteTime.TimeResult;

public class NlpHelper {
	private static ConjunctionHelper conjunctionHelper;
	private static AbsoluteTime absoluteTime;
	public static void init() {
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		MovieTypeDetection.init(dir + "/qc/movie", dir + "/dicts");
		NonDiacriticMovieIntentDetection.init( dir + "/qc/movie/non-diacritic", dir + "/dicts/non-diacritic" );
		//conjunctionHelper = new ConjunctionHelper(dir);
		absoluteTime = new AbsoluteTime( NlpHelper.class.getClassLoader().getResource("").getPath() + "vnsutime/" );
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
		return question.toLowerCase().substring(0,j+1);
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
	
	public static void main(String[] args) {
		System.out.println("aaaaaaaaa".hashCode());
		MovieTypeDetection.init("/home/ngan/Work/AHongPhuong/Intent_detection/data/qc/2",
				"/home/ngan/Work/AHongPhuong/RubyWeb/rubyweb/data/dicts");
		System.out.println(MovieTypeDetection.getIntent(normalizeQuestion("tối nay có phim gì hay/")));
	}

	
	
}
