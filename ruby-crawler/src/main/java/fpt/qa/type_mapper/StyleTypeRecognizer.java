package fpt.qa.type_mapper;

import com.fpt.ruby.business.constants.ProgramType;

public class StyleTypeRecognizer extends TypeRecognizer {
	public StyleTypeRecognizer() {
		SetType(ProgramType.STYLE);

		String[] dedicatedChannels = new String[] { "vtvcab12", "styletv"};
		String[] typeKeywords = new String[] { "style", "phong cach"};

		super.loadConfig(dedicatedChannels, typeKeywords);
	}
}
