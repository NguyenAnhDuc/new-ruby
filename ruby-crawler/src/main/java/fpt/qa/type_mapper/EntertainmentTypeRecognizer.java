package fpt.qa.type_mapper;


import com.fpt.ruby.commons.constants.ProgramType;

public class EntertainmentTypeRecognizer extends TypeRecognizer {
	public EntertainmentTypeRecognizer() {
		SetType(ProgramType.ENTERTAINMENT);

		String[] dedicatedChannels = new String[] {"hbo", "cinemax", "fox sport", "cartoon network", "walt disney", "star movie", "starmovie", "star", "vtvcab"};
		String[] typeKeywords = new String[] {"giai tri", "the thao", "film", "animated", "animation", "bong da", "tenis", "quaan vot", "vdqg"};

		super.loadConfig(dedicatedChannels, typeKeywords);
	}
}
