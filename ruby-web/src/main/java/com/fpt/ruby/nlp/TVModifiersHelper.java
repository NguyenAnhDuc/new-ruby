package com.fpt.ruby.nlp;

import com.fpt.ruby.business.constants.ProgramType;
import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.TVModifiers;
import com.fpt.ruby.business.service.NameMapperService;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import fpt.qa.additionalinformation.modifier.AbsoluteTime;
import fpt.qa.genreclassifier.GenreExtractor;
import fpt.qa.langclassifier.LangExtractor;
import fpt.qa.mdnlib.diacritic.DiacriticConverter;
import fpt.qa.mdnlib.struct.pair.Pair;
import fpt.qa.typeclassifier.ProgramTypeExtractor;

import java.util.*;

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
        String[] ignoreTitles = new String[]{"thể thao", "game show", "bóng đá", "đá bóng", "thời sự", "tin tức", "tennis", "ten nít", "ca nhạc", "tenis"};
        ignores.clear();
        for (String title : ignoreTitles) {
            ignores.add(title);
            ignores.add(DiacriticConverter.removeDiacritics(title));
        }
    }


    public com.fpt.ruby.template.TVModifiers getTVModifiers(String question, ConjunctionHelper conjunctionHelper){
        com.fpt.ruby.template.TVModifiers mod = new com.fpt.ruby.template.TVModifiers();
        List<Pair<String, String>> conjunctions = conjunctionHelper
                .getConjunction(question);
        for (Pair<String, String> conjunction : conjunctions) {
            if (mod.getTvChannel() == null && conjunction.second.equals(CHANNEL)) {
                mod.setTvChannel(conjunction.first);
                continue;
            }

            if (mod.getTvTitle() == null
                    && conjunction.second.equals(PROGRAM)) {
                mod.setTvTitle(conjunction.first);
                continue;
            }

        }

        String title = mod.getTvTitle();
        if (title == null || title.isEmpty() || ignores.contains(title.toLowerCase())) {
            mod.setTvTitle(null);
            if (mod.getTypes() == null) {
                List<ProgramType> ptype = typeExtractor.getTypes(question);
                if (ptype != null) {
                    List<String> listType = new ArrayList<>();
                    for (ProgramType type : ptype) {
                        if (!listType.contains(type.toString())) {
                            listType.add(type.toString());
                        }
                    }
                    mod.setTypes(listType);
                } else {
                    mod.setTypes(null);
                }
            }
        } else {
            mod.setTypes(null);
        }

        if (mod.getTypes() != null && mod.getTypes().contains(ProgramType.FILM.toString())) {
            System.err.println("|||||||||||||||||||||||||||||||||||||||||||");
            boolean specific = false;
            GenreExtractor genreExtractor = new GenreExtractor();
            LangExtractor langExtractor = new LangExtractor();
            List<String> genre = genreExtractor.getGenre(question);

            if (genre != null) {
                specific = true;
                List<String> types = mod.getTypes();
                types.addAll(genre);
                mod.setTypes(types);
            }

            List<String> langs = langExtractor.getLanguage(question);
            if (langs != null) {
                specific = true;
                List<String> types = mod.getTypes();
                types.addAll(langs);
                mod.setTypes(types);
            }

            if (specific) {
                mod.getTypes().remove(ProgramType.FILM.toString());
            }
        }
        System.out.println("mod.type = " + mod.getTypes());
        return mod;
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

        if (mod.getType() != null && mod.getType().contains(ProgramType.FILM.toString())) {
            System.err.println("|||||||||||||||||||||||||||||||||||||||||||");
            boolean specific = false;
            GenreExtractor genreExtractor = new GenreExtractor();
            LangExtractor langExtractor = new LangExtractor();
            List<String> genre = genreExtractor.getGenre(question);

            if (genre != null) {
                specific = true;
                List<String> types = mod.getType();
                types.addAll(genre);
                mod.setType(types);
            }

            List<String> langs = langExtractor.getLanguage(question);
            if (langs != null) {
                specific = true;
                List<String> types = mod.getType();
                types.addAll(langs);
                mod.setType(types);
            }

            if (specific) {
                mod.getType().remove(ProgramType.FILM.toString());
            }
        }
        System.out.println("mod.type = " + mod.getType());
        return mod;
    }
}