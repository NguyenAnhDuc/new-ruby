package fpt.qa.type_mapper;

import com.fpt.ruby.business.constants.ProgramType;

public class DiscoveryTypeRecognizer extends TypeRecognizer{
	public DiscoveryTypeRecognizer() {
		SetType(ProgramType.DISCOVERY);

		String[] dedicatedChannels = new String[] { "discovery", "national geographic", "outdoor channel", "asian food channel"};
		String[] typeKeywords = new String[] { "discovery", "kham pha", "tham hiem"};

		super.loadConfig(dedicatedChannels, typeKeywords);
	}
}
