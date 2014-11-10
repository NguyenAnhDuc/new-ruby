package com.fpt.ruby.nlp;

import java.util.ArrayList;
import java.util.List;

import com.fpt.ruby.business.constants.ProgramType;
import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.service.NameMapperService;
import com.fpt.ruby.conjunction.ConjunctionHelper;
import com.fpt.ruby.model.TVModifiers;

import fpt.qa.additionalinformation.modifier.AbsoluteTime;
import fpt.qa.mdnlib.struct.pair.Pair;
import fpt.qa.typeclassifier.ProgramTypeExtractor;

public class TVModifiersHelper {
	private static final String CHANNEL = "chanel_title";
	private static final String PROGRAM = "program_title";
	private static long ONE_HOUR = 60 * 60 * 1000;
	private static long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;

	static AbsoluteTime timeParser;
	static ProgramTypeExtractor typeExtractor;

	// init the conjunction helper object
	public static void init(NameMapperService nameMapperService) {
		String dir = (new RedisHelper()).getClass().getClassLoader()
				.getResource("").getPath();
		timeParser = new AbsoluteTime(dir + "/vnsutime");
		typeExtractor = new ProgramTypeExtractor();
	}


	public static TVModifiers getModifiers(String question,
			ConjunctionHelper conjHelper) {
		TVModifiers mod = new TVModifiers();
		List<Pair<String, String>> conjunctions = conjHelper
				.getConjunction(question);
		for (Pair<String, String> conjunction : conjunctions) {
			if (mod.getChannel() == null && conjunction.second.equals(CHANNEL)) {
				mod.setChannel(conjunction.first);
				continue;
			}

			if (mod.getProg_title() == null
					&& conjunction.second.equals(PROGRAM)) {
				mod.setProg_title(conjunction.first);
				continue;
			}

		}

		// Need to add code to get type in here
		// Thien BUI-DUC
		if (mod.getProg_title() == null || mod.getProg_title() == ""
				|| mod.getProg_title().equalsIgnoreCase("thể thao")
				|| mod.getProg_title().equalsIgnoreCase("game show")
				|| mod.getProg_title().equalsIgnoreCase("bóng đá")
				|| mod.getProg_title().equalsIgnoreCase("thời sự")) {

			if (mod.getType() == null) {
				List<ProgramType> ptype = typeExtractor.getTypes(question);
				if (ptype != null) {
					List<String> listType = new ArrayList<>();
					for (ProgramType type : ptype) {
						if (!listType.contains(type.toString())) {
							listType.add(type.toString());
						}
					}
					mod.setType(listType);
				} else {
					mod.setType(null);
				}

			}

		} else {
			mod.setType(null);
		}
		return mod;
	}

	
}
