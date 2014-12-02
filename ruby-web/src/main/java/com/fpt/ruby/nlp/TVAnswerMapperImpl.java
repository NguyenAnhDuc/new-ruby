package com.fpt.ruby.nlp;

import com.fpt.ruby.business.constants.IntentConstants;
import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.*;
import com.fpt.ruby.business.service.LogService;
import com.fpt.ruby.business.service.TVProgramService;
import com.fpt.ruby.intent.detection.TVTypeDetection;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import fpt.qa.mdnlib.util.string.DiacriticConverter;
import fpt.qa.typeclassifier.ProgramTypeExtractor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TVAnswerMapperImpl implements TVAnswerMapper {
    public static final String DEF_ANS = "Chúng tôi không tìm thấy thông tin gì.";
    private static final int ONE_HOUR = 60 * 60 * 1000;
    private final int limitSizeAnswer = 10;
    private TVTypeDetection intentDetector = new TVTypeDetection();
    private TVTypeDetection nonDiacritic = new TVTypeDetection();
    private TVProgramService tps;

    public void init(TVProgramService tvProgramService) {
        String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
        tps = tvProgramService;
        intentDetector.init(dir + "/qc/tv", dir + "/dicts");
        nonDiacritic.init(dir + "/qc/tv/non-diacritic", dir + "/dicts/non-diacritic");
    }

    public RubyAnswer getAnswer(String question, LogService logService, ConjunctionHelper conjunctionHelper) {
        RubyAnswer rubyAnswer = new RubyAnswer();
        String tmp = "\t" + question + "\n";

        String intent = intentDetector.getIntent(question);
        tmp += "\t" + "TV Intent: " + intent + "\n";

        String intent2 = nonDiacritic.getIntent(question);
        tmp += "\t" + "Non-diacritic TV Intent: " + intent2 + "\n";

        if (!DiacriticConverter.hasDiacriticAccents(question)) {
            intent = intent2;
        }

        rubyAnswer.setQuestion(question);
        rubyAnswer.setIntent(intent);
        rubyAnswer.setAnswer(DEF_ANS);

        if (intent.equalsIgnoreCase(IntentConstants.TV_UDF))
            return rubyAnswer;

        TVModifiers mod = TVModifiersHelper.getModifiers(question, conjunctionHelper);
        tmp += "\t" + question.replaceAll("\\s+", " ") + "\n";
        tmp += "\t" + mod + "\n";


        // get Time condition
        TimeExtract timeExtract = NlpHelper.getTimeCondition(question.replaceAll("(\\d+)(h)", "$1 giờ"));
        Date start = timeExtract.getBeforeDate();
        Date end = timeExtract.getAfterDate();


      if (question.contains("đang") || question.contains("dang") ||
          question.contains("bây giờ") || question.contains("bay gio") ||
          question.contains("hiện tại") || question.contains("hien tai") ||
          question.contains("hiện giờ") || question.contains("hien gio")) {
            start = new Date();
            end = start;
        }

        if (question.contains("sắp") && question.contains("sap") ||
            question.contains("tiếp theo") || question.contains("tiep theo")){
            start = new Date(new Date().getTime() + ONE_HOUR);
            end = start;
        }

        mod.setStart(start);
        mod.setEnd(end);

        QueryParamater queryParamater = new QueryParamater();

        // end time processing
        List<TVProgram> progs = tps.getList(mod);
        // Log
        Log log = new Log();
        queryParamater = new QueryParamater();
        queryParamater.setBeginTime(mod.getStart());
        queryParamater.setEndTime(mod.getEnd());
        queryParamater.setTvProTitle(mod.getProg_title());
        queryParamater.setTvChannel(mod.getChannel());
        queryParamater.setTypes(new ProgramTypeExtractor().typeString(question));
        rubyAnswer.setQueryParamater(queryParamater);

        // Now extract the needed information from the list of returned item
        // to generate the answer

        if (progs.size() > 0) rubyAnswer.setAnswer(getChannelProgAndTime(progs));
        return rubyAnswer;
    }

    public String getChannelProgAndTime(List<TVProgram> progs) {
        if (progs.isEmpty())
            return DEF_ANS;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM hh:mm a");
        String info = "";

        int limit = limitSizeAnswer;
        if (progs.size() < limitSizeAnswer) {
            limit = progs.size();
        }
        for (int i = 0; i < limit; i++) {
            TVProgram tv = progs.get(i);
            info += tv.getChannel() + " : " + sdf.format(tv.getStart_date()) + " : " + tv.getTitle() + "</br>";
        }
        return info;
    }

}