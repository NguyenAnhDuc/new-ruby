package fpt.qa.type_mapper;


import com.fpt.ruby.commons.constants.ProgramType;

public class NewsTypeRecognizer extends TypeRecognizer {
	public NewsTypeRecognizer() {
		SetType(ProgramType.NEWS);

		String[] dedicatedChannels = new String[] { "cnn", "vnews", "dw"};
		String[] typeKeywords = new String[] { "tin tuc", "ban tin", "thoi su",
				"news", "tin nong" };
		super.loadConfig(dedicatedChannels, typeKeywords);
	}

}
