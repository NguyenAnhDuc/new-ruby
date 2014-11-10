package com.fpt.ruby.template;

import java.util.List;

import com.fpt.ruby.model.TimeExtract;

import edu.stanford.nlp.util.Pair;

public abstract class RubyProcess {
	String normalize(String question){
		return "";
	}
	abstract String getIntent(String question);
	abstract List<Pair<String, String>> getModifiers(String question);
	abstract TimeExtract extractTime(String question);
	abstract String getAnswer(String question);
	
	
}
