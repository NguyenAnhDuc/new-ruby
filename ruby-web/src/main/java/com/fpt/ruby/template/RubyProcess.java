package com.fpt.ruby.template;

import com.fpt.ruby.business.model.TimeExtract;
import com.fpt.ruby.cache.RubyCache;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;

public abstract class RubyProcess {
	String normalize(String question){
		return question;
	}
	abstract String getIntent(String question);
	abstract RubyModifiers getModifiers(String question);
	abstract TimeExtract extractTime(String question);
	abstract String getAnswer(String question);

	public RubyAnswer process(String question){
		RubyAnswer rubyAnswer = new RubyAnswer();
		question = normalize(question);
		rubyAnswer.setQuestion(question);
		rubyAnswer.setIntent(getIntent(question));
		rubyAnswer.setTimeExtract(extractTime(question));
		rubyAnswer.setAnswer(getAnswer(question));
		return rubyAnswer;
	}
}
