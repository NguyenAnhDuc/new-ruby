package com.fpt.ruby.template;

import java.util.List;

import com.fpt.ruby.model.TimeExtract;

import edu.stanford.nlp.util.Pair;

public class MovieProcess extends RubyProcess {

	@Override
	String getIntent(String question) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	List<Pair<String, String>> getModifiers(String question) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	TimeExtract extractTime(String question) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	String getAnswer(String question) {
		normalize(question);
		
		// TODO Auto-generated method stub
		return null;
	}
	
}
