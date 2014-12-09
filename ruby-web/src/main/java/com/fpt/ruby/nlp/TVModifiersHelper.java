package com.fpt.ruby.nlp;

import com.fpt.ruby.commons.constants.ProgramType;
import com.fpt.ruby.commons.entity.modifiers.TVModifiers;
import com.fpt.ruby.commons.service.NameMapperService;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import fpt.qa.genreclassifier.GenreExtractor;
import fpt.qa.langclassifier.LangExtractor;
import fpt.qa.mdnlib.diacritic.DiacriticConverter;
import fpt.qa.mdnlib.struct.pair.Pair;
import fpt.qa.typeclassifier.ProgramTypeExtractor;
import fpt.qa.vnTime.vntime.AbsoluteTime;

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
        String dir = (new TVModifiersHelper()).getClass().getClassLoader()
                .getResource("").getPath();
        timeParser = new AbsoluteTime(dir + "/vnsutime");
        typeExtractor = new ProgramTypeExtractor();

        // ignore program title
        String[] ignoreTitles = new String[]{
                "thể thao", "game show", "bóng đá", "đá bóng", "thời sự", "tin tức", "tennis", "ten nít", "ca nhạc", "tenis",
        "phim mỹ", "phim hoạt hình", "phim gia đình", "phim hành động", "phim thể thao", "phim tâm lý", "phim tình cảm", "phim", "ca nhạc", "chương trình ca nhạc"};
        ignores.clear();
        for (String title : ignoreTitles) {
            ignores.add(title);
            ignores.add(DiacriticConverter.removeDiacritics(title));
        }
    }


    public static TVModifiers getTVModifiers(String question, ConjunctionHelper conjunctionHelper){
        TVModifiers mod = new TVModifiers();
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

        if (mod.getTypes() != null && (mod.getTypes().contains(ProgramType.FILM.toString()) || mod.getTypes().contains(ProgramType.CARTOON.toString()))) {
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
            mod.setTypes((new ArrayList<>(new HashSet<>(mod.getTypes()))));
        }
        System.out.println("mod.type = " + mod.getTypes());
        return mod;
    }
}