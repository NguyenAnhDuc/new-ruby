package fpt.qa.type_mapper;

import com.fpt.ruby.business.constants.ProgramType;

public class MusicTypeRecognizer extends TypeRecognizer {

	public MusicTypeRecognizer() {
		SetType(ProgramType.MUSIC);

		String[] dedicatedChannels = new String[] { "mtv" };
		String[] typeKeywords = new String[] { "ca nhac", "musik", "music",
				"muzik", "cakhuc", "giai dieu", "nhay", "tinh ca", "loi hat", "piano", "guitar", "organ", "giao huong", "thinh phong"};
	
		super.loadConfig(dedicatedChannels, typeKeywords);
	}
}
