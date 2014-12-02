package com.fpt.ruby.template;

import com.fpt.ruby.business.model.TimeExtract;

public abstract class RubyProcess {
	String normalize(String question){
		return "";
	}
	abstract String getIntent(String question);
	abstract RubyModifiers getModifiers(String question);
	abstract TimeExtract extractTime(String question);
	abstract String getAnswer(String question);
}
