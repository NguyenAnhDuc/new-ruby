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

    private final int limitSizeAnswer = 10;
    private TVTypeDetection intentDetector = new TVTypeDetection();
    private TVTypeDetection nonDiacritic = new TVTypeDetection();
    private TVProgramService tps = new TVProgramService();

    public void init() {
        String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();

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

        rubyAnswer.setQuestionType(mod.getChannel());

        // get Time condition
        TimeExtract timeExtract = NlpHelper.getTimeCondition(question.replaceAll("(\\d+)(h)", "$1 giờ"));
        Date start = timeExtract.getBeforeDate();
        Date end = timeExtract.getAfterDate();


      if (question.contains("đang") && !question.contains("đang làm gì") ||
                question.contains("bây giờ") || question.contains("hiện tại") || question.contains("hiện giờ")) {
            start = new Date();
            end = start;
        }

        if (question.contains("dang") && !question.contains("dang lam gi") ||
                question.contains("bay gio") || question.contains("hien tai")
                || question.contains("sap") || question.contains("tiep theo") || question.contains("hien gio")) {
            start = new Date();
            end = start;
        }

        if (start == null && end == null ) {
            start = NlpHelper.getTimeCondition("hôm nay").getBeforeDate();
            if (intent.equals(IntentConstants.TV_CHN)) start = null;
        }
        mod.setStart(start);
        mod.setEnd(end);

        QueryParamater queryParamater = new QueryParamater();

        rubyAnswer.setBeginTime(mod.getStart());
        rubyAnswer.setEndTime(mod.getEnd());
        // end time processing
        System.out.println("Find list TV Program");
        List<TVProgram> progs = tps.getList(mod);
        System.out.println("List TVProgram Size: " + progs.size());
        // Log
        System.out.println("[TVANSWERMAPPERIMPL]: WRITE LOG");
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM hh:mm a");
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