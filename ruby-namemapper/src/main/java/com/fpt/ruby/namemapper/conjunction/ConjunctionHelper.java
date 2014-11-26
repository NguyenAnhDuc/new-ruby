package com.fpt.ruby.namemapper.conjunction;

import java.io.IOException;
import java.util.List;

import com.fpt.ruby.business.constants.IntentConstants;
import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.MovieTicket;
import com.fpt.ruby.business.model.NameMapper;
import com.fpt.ruby.business.service.NameMapperService;

import fpt.qa.additionalinformation.modifier.ConjunctionWithType;
import fpt.qa.mdnlib.struct.pair.Pair;



public class ConjunctionHelper {
	private ConjunctionWithType conjunctionWithType;
	public ConjunctionHelper(NameMapperService nameMapperService){
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		conjunctionWithType = new ConjunctionWithType( dir, nameMapperService );
	}
	public ConjunctionHelper(String dir, NameMapperService nameMapperService){
		 System.out.println("Conjunction Helper init .......");
		 conjunctionWithType = new ConjunctionWithType( dir, nameMapperService );
	}
	
	public List<Pair<String, String>> getConjunction(String text){
		return conjunctionWithType.getOriginRelevantConjunctionsWithType(text); 
	}
	
	public boolean addTemporaryConjunction( NameMapper nameMapperEntry ){
		try{
			conjunctionWithType.addConjunctionWithType( nameMapperEntry.getName(), nameMapperEntry.getType(), nameMapperEntry.getDomain(), nameMapperEntry.getVariants() );
			return true;
		}catch( Exception e ){
			return false;
		}
	}
	
	public String getMovieTitle(String question){
		List<Pair<String, String>> conjunctions = getConjunction(question);
		for (Pair<String, String> conjunction : conjunctions ){
			System.out.println(conjunction.first + " | " + conjunction.second);
			if (conjunction.second.equals(IntentConstants.MOV_TITLE))
				return conjunction.first.replace("{", "").replace("}", "");
		}
		return null;
	}
	
	public String getCinemaName(String question){
		List<Pair<String, String>> conjunctions = getConjunction(question);
		for (Pair<String, String> conjunction : conjunctions ){
			System.out.println("[Conjunction Helper - getCinemaName: ]" + conjunction.first + " | " + conjunction.second);
			if (conjunction.second.equals(IntentConstants.CIN_NAME))
				return conjunction.first.replace("{", "").replace("}", "");
		}
		return null;
	}
	
	public String getChannelName(String text) {
		List<Pair<String, String>> conjunctions = getConjunction(text);
		System.out.println(conjunctions.size());
		for (Pair<String, String> conjunction : conjunctions ){
			if (conjunction.second.equals("chanel_title"))
				return conjunction.first.replace("{", "").replace("}", "");
		}
		return null;
	}
	
	public MovieTicket getMovieTicket(String question){
		System.err.println("Conjunction Helper: " + question);
		List<Pair<String, String>> conjunctions = getConjunction(question);
		MovieTicket movieTicket = new MovieTicket();
		for (Pair<String, String> conjunction : conjunctions ){
			System.out.println("[Conjunction Helper - getCinemaName: ]" + conjunction.first + " | " + conjunction.second);
			if (movieTicket.getCinema() == null && conjunction.second.equals(IntentConstants.CIN_NAME))
				movieTicket.setCinema(conjunction.first.replace("{", "").replace("}", ""));
			if (movieTicket.getMovie() == null && conjunction.second.equals(IntentConstants.MOV_TITLE))
				movieTicket.setMovie(conjunction.first.replace("{", "").replace("}", ""));
		}	
		return movieTicket;
	}
}