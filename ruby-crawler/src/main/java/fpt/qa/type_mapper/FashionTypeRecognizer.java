package fpt.qa.type_mapper;


import com.fpt.ruby.commons.constants.ProgramType;

public class FashionTypeRecognizer extends TypeRecognizer {
	public FashionTypeRecognizer() {
		SetType(ProgramType.FASHION);

		String[] dedicatedChannels = new String[] {};
		String[] typeKeywords = new String[] { "thoi trang", "fahsion"};

		super.loadConfig(dedicatedChannels, typeKeywords);
	}
}
