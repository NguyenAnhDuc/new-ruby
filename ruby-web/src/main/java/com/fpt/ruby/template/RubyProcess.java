package com.fpt.ruby.template;

public abstract class RubyProcess {
	abstract void normalize(String question);
	abstract void getIntent();
	abstract void getModifiers();
	abstract void extractTime();
	abstract void getCandidates();
	abstract void getAnswer();

	public final void process(String question){
		normalize(question);
		getIntent();
		getModifiers();
		extractTime();
		getCandidates();
		getAnswer();
	}
}
