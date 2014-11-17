package fpt.qa.type_mapper;

import com.fpt.ruby.business.constants.ProgramType;

public class CartoonTypeRecognizer extends TypeRecognizer {
	public CartoonTypeRecognizer() {
		SetType(ProgramType.CARTOON);

		String[] dedicatedChannels = new String[] { "disney", "cartoon", "bibi", "boomerang", "vtvcab8"};
		String[] typeKeywords = new String[] { "hoat hinh", "cartoon", "disney", "animation", "animated"};

		super.loadConfig(dedicatedChannels, typeKeywords);
	}

}
