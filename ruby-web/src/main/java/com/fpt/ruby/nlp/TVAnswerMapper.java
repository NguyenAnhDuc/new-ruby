package com.fpt.ruby.nlp;

import java.util.List;

import com.fpt.ruby.business.model.TVProgram;
import com.fpt.ruby.business.service.LogService;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;

public interface TVAnswerMapper{
	void init();
	RubyAnswer getAnswer ( String question, LogService logService, ConjunctionHelper conjunctionHelper );
	
	String getChannelProgAndTime ( List< TVProgram > progs );
}
