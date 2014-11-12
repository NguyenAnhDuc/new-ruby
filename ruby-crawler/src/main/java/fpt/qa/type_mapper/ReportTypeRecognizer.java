package fpt.qa.type_mapper;

import com.fpt.ruby.business.constants.ProgramType;

public class ReportTypeRecognizer extends TypeRecognizer {
	public ReportTypeRecognizer() {
		SetType(ProgramType.REPORT);

		String[] dedicatedChannels = new String[] {};
		String[] typeKeywords = new String[] { "phong su" , "phim tai lieu"};
		super.loadConfig(dedicatedChannels, typeKeywords);
	}
}