package com.fpt.ruby.template;

import com.fpt.ruby.commons.constants.DomainType;
import com.fpt.ruby.commons.constants.IConstants;
import com.fpt.ruby.commons.entity.modifiers.TVModifiers;
import com.fpt.ruby.commons.entity.objects.TimeExtract;
import com.fpt.ruby.commons.entity.tv.TVProgram;
import com.fpt.ruby.commons.service.TVProgramService;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.nlp.TVModifiersHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TVProcess extends RubyProcess {
    private RubyAnswer rubyAnswer = new RubyAnswer();

    public RubyAnswer getRubyAnswer() {
        return rubyAnswer;
    }

    TVModifiers tvModifiers;
    List<TVProgram> tvPrograms = new ArrayList<TVProgram>();
    private final int limitSizeAnswer = 10;
    private final long ONE_HOUR = 60 * 60 * 1000;
    private TVProgramService tvProgramService;
    private ConjunctionHelper conjunctionHelper;
    public TVProcess(TVProgramService tps, ConjunctionHelper cjh){
        tvProgramService = tps;
        conjunctionHelper = cjh;
    }

    @Override
    void normalize(String question) {
        rubyAnswer.setQuestion(question);
        rubyAnswer.setDomain(DomainType.TV.toString());
    }

    @Override
    void getIntent() {
        rubyAnswer.setIntent("tv_cal");
    }

    @Override
    void getModifiers() {
        tvModifiers = TVModifiersHelper.getTVModifiers(rubyAnswer.getQuestion(), conjunctionHelper);
        rubyAnswer.setRubyModifiers(tvModifiers);
    }

    @Override
    void extractTime() {
        String  question =  rubyAnswer.getQuestion();
        TimeExtract timeExtract = NlpHelper.getTimeCondition(question);
        if (question.contains("đang") || question.contains("dang") ||
                question.contains("bây giờ") || question.contains("bay gio") ||
                question.contains("hiện tại") || question.contains("hien tai") ||
                question.contains("hiện giờ") || question.contains("hien gio")) {
            timeExtract.setBeforeDate(new Date());
            timeExtract.setAfterDate(timeExtract.getBeforeDate());
        }

        if (question.contains("sắp") && question.contains("sap") ||
                question.contains("tiếp theo") || question.contains("tiep theo")){
            timeExtract.setBeforeDate(new Date(new Date().getTime() + ONE_HOUR));
            timeExtract.setAfterDate(timeExtract.getBeforeDate());
        }
        rubyAnswer.setTimeExtract(timeExtract);
    }

    @Override
    void getCandidates() {
        TimeExtract timeExtract = rubyAnswer.getTimeExtract();
        com.fpt.ruby.commons.entity.modifiers.TVModifiers tvModifiers1 = new  com.fpt.ruby.commons.entity.modifiers.TVModifiers();
        tvModifiers1.setTvChannel(tvModifiers.getTvChannel());tvModifiers1.setTvTitle(tvModifiers.getTvTitle());
        tvModifiers1.setTypes(tvModifiers.getTypes());
        tvModifiers1.setStart(timeExtract.getBeforeDate());tvModifiers1.setEnd(timeExtract.getAfterDate());
        tvPrograms = tvProgramService.getList(tvModifiers1);
    }

    @Override
    void getAnswer() {
        if (tvPrograms.isEmpty())
            rubyAnswer.setAnswer(IConstants.ANSWER_NO_MATCH);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM hh:mm a");
        String info = "";

        int limit = limitSizeAnswer;
        if (tvPrograms.size() < limitSizeAnswer) {
            limit = tvPrograms.size();
        }
        for (int i = 0; i < limit; i++) {
            TVProgram tv = tvPrograms.get(i);
            info += tv.getChannel() + " : " + sdf.format(tv.getStart_date()) + " : " + tv.getTitle() + "</br>";
        }
        rubyAnswer.setAnswer(info);
    }

}
