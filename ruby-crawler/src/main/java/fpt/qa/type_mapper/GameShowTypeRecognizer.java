package fpt.qa.type_mapper;


import com.fpt.ruby.commons.constants.ProgramType;

public class GameShowTypeRecognizer extends TypeRecognizer {
	public GameShowTypeRecognizer() {
		SetType(ProgramType.GAMESHOW);
		
		String[] dedicatedChannels = new String[] {};
		String[] typeKeywords = new String[] { "gameshow" };
		
		super.loadConfig(dedicatedChannels, typeKeywords);
	}
}
