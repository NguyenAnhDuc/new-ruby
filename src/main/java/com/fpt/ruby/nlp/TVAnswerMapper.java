package com.fpt.ruby.nlp;

import java.util.List;

import com.fpt.ruby.business.service.LogService;
import com.fpt.ruby.conjunction.ConjunctionHelper;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.model.TVProgram;

public interface TVAnswerMapper{
	void init();
	RubyAnswer getAnswer ( String question, LogService logService, ConjunctionHelper conjunctionHelper );
	
	String getTime ( List< TVProgram > progs );
	String getTitle ( List< TVProgram > progs );
	String getChannel ( List< TVProgram > progs );
	String getTitleAndTime ( List< TVProgram > progs );
	String getChannelAndProgram ( List< TVProgram > progs );
	String getChannelAndTime ( List< TVProgram > progs );
	String getChannelProgAndTime ( List< TVProgram > progs );
	String getEndDate ( List< TVProgram > progs );
}
