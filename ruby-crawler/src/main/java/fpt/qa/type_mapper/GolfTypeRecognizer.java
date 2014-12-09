package fpt.qa.type_mapper;


import com.fpt.ruby.commons.constants.ProgramType;

public class GolfTypeRecognizer extends TypeRecognizer {
	public GolfTypeRecognizer() {
		SetType(ProgramType.GOLF);

		String[] dedicatedChannels = new String[] {};
		String[] typeKeywords = new String[] {"golf", "cimb", "pga"};
	
		super.loadConfig(dedicatedChannels, typeKeywords);
	}
}
