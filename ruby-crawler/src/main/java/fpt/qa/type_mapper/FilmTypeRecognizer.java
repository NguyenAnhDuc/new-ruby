package fpt.qa.type_mapper;

import com.fpt.ruby.business.constants.ProgramType;

public class FilmTypeRecognizer extends TypeRecognizer {

	public FilmTypeRecognizer() {
		SetType(ProgramType.FILM);
		
		String[] dedicatedChannels = new String[] { "hbo", "star movie", "starmovie", 
				"screenred", "vtvcab2", "disney", "cartoon", "vtvcab8",  "vtvcab7", "max"};
		String[] typeKeywords = new String[] {"film", "fim", "movie"};

		super.loadConfig(dedicatedChannels, typeKeywords);
	}

	protected Boolean customCode(String channel, String prog) {
		if (prog.contains("phim") && !prog.contains("phim tai lieu")) {
			return true;
		}
		return false;
	}
	
}
