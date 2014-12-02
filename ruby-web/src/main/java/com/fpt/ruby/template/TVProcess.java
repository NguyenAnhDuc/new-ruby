package com.fpt.ruby.template;

import com.fpt.ruby.business.model.TimeExtract;
import com.fpt.ruby.business.service.TVProgramService;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.nlp.TVModifiersHelper;

public class TVProcess extends RubyProcess {
    private TVProgramService tvProgramService;
    private ConjunctionHelper conjunctionHelper;
    private TVModifiersHelper tvModifiersHelper;
    public TVProcess(TVProgramService tps, ConjunctionHelper cjh, TVModifiersHelper tvh){
        tvProgramService = tps;
        conjunctionHelper = cjh;
        tvModifiersHelper = tvh;
    }

    @Override
    String getIntent(String question) {
        // TODO Auto-generated method stub
        return "tv_cal";
    }

    @Override
    RubyModifiers getModifiers(String question) {
        // TODO Auto-generated method stub
        return tvModifiersHelper.getTVModifiers(question,conjunctionHelper);
    }

    @Override
    TimeExtract extractTime(String question) {
        return NlpHelper.getTimeCondition(question.replaceAll("(\\d+)(h)", "$1 giờ"));
    }

    @Override
    String getAnswer(String question) {

        // TODO Auto-generated method stub
        return "tv answer";
    }

}
