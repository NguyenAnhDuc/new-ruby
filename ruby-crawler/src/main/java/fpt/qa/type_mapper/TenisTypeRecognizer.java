package fpt.qa.type_mapper;

import com.fpt.ruby.business.constants.ProgramType;

public class TenisTypeRecognizer extends TypeRecognizer {
	public TenisTypeRecognizer() {
		SetType(ProgramType.TENNIS);

		String[] dedicatedChannels = new String[] { };
		String[] typeKeywords = new String[] { "atp world", "tenis", "wimbledon", "tennis"};

		super.loadConfig(dedicatedChannels, typeKeywords);
	}
}
