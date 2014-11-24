package com.fpt.ruby.nlp;

import com.fpt.ruby.business.constants.ProgramType;
import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.TVModifiers;
import com.fpt.ruby.business.service.NameMapperService;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import fpt.qa.additionalinformation.modifier.AbsoluteTime;
import fpt.qa.mdnlib.diacritic.DiacriticConverter;
import fpt.qa.mdnlib.struct.pair.Pair;
import fpt.qa.typeclassifier.ProgramTypeExtractor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TVModifiersHelper {
    private static final String CHANNEL = "chanel_title";
    private static final String PROGRAM = "program_title";
    private static long ONE_HOUR = 60 * 60 * 1000;
    private static long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;

    static AbsoluteTime timeParser;
    static ProgramTypeExtractor typeExtractor;
    static Set<String> ignores = new HashSet<String>();

    // init the conjunction helper object
    public static void init(NameMapperService nameMapperService) {
        String dir = (new RedisHelper()).getClass().getClassLoader()
                .getResource("").getPath();
        timeParser = new AbsoluteTime(dir + "/vnsutime");
        typeExtractor = new ProgramTypeExtractor();

        // ignore program title
        String[] ignoreTitles = new String[]{"thể thao", "game show", "bóng đá", "đá bóng", "thời sự", "tin tức", "tennis"};
        ignores.clear();
        for (String title : ignoreTitles) {
            ignores.add(title);
            ignores.add(DiacriticConverter.removeDiacritics(title));
        }
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

        String title = mod.getProg_title();
        if (title == null || title.isEmpty() || ignores.contains(title.toLowerCase())) {
            mod.setProg_title(null);
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