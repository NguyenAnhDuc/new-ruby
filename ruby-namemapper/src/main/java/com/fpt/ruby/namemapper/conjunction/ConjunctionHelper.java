package com.fpt.ruby.namemapper.conjunction;

import com.fpt.ruby.commons.constants.IntentConstants;
import com.fpt.ruby.commons.entity.NameMapper;
import com.fpt.ruby.commons.entity.modifiers.MovieModifiers;
import com.fpt.ruby.commons.entity.movie.MovieTicket;
import com.fpt.ruby.commons.service.NameMapperService;
import fpt.qa.additionalinformation.modifier.ConjunctionWithType;
import fpt.qa.additionalinformation.modifier.NamedEntityExtractor;
import fpt.qa.mdnlib.struct.pair.Pair;

import java.util.List;



public class ConjunctionHelper {
	private ConjunctionWithType conjunctionWithType;
	public ConjunctionHelper(NameMapperService nameMapperService){
		String dir = (new NamedEntityExtractor()).getClass().getClassLoader().getResource("").getPath();
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

	public MovieModifiers getMovieModifiers(String question){
		MovieModifiers movieModifiers = new MovieModifiers();
		System.err.println("Conjunction Helper: " + question);
		List<Pair<String, String>> conjunctions = getConjunction(question);
		for (Pair<String, String> conjunction : conjunctions ){
			System.out.println("[Conjunction Helper - getCinemaName: ]" + conjunction.first + " | " + conjunction.second);
			if (movieModifiers.getCinName() == null && conjunction.second.equals(IntentConstants.CIN_NAME))
				movieModifiers.setCinName(conjunction.first.replace("{", "").replace("}", ""));
			if (movieModifiers.getMovieTitle() == null && conjunction.second.equals(IntentConstants.MOV_TITLE))
				movieModifiers.setMovieTitle(conjunction.first.replace("{", "").replace("}", ""));
		}
		return movieModifiers;
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