package fpt.qa.type_mapper;

import com.fpt.ruby.business.constants.ProgramType;

public class ScienceTypeRecognizer extends TypeRecognizer {
	public ScienceTypeRecognizer() {
		SetType(ProgramType.SCIENCE);

		String[] dedicatedChannels = new String[] {"discovery", "national geographic" };
		String[] typeKeywords = new String[] { "khoa hoc", "cong nghe"};
		super.loadConfig(dedicatedChannels, typeKeywords);
	}
}
