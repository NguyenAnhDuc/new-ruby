package fpt.qa.answerEngine;

import com.fpt.ruby.business.service.*;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import com.fpt.ruby.nlp.TVAnswerMapper;
import fpt.qa.domainclassifier.DomainClassifier;

public class NLPInfoWrapper {
    DomainClassifier classifier;
    TVAnswerMapper tvans;
    MovieFlyService mfs;
    MovieTicketService mts;
    CinemaService cins;
    TVProgramService tps;
    ConjunctionHelper dia;
    ConjunctionHelper nonDia;
    LogService log;
    NameMapperService nameMapperService;
    public NLPInfoWrapper() {
    }

//    public NLPInfoWrapper(DomainClassifier classifier, TVAnswerMapper tvans, MovieFlyService mfs, MovieTicketService mts, CinemaService cins, ConjunctionHelper dia, ConjunctionHelper nonDia) {
//        this.classifier = classifier;
//        this.tvans = tvans;
//        this.mfs = mfs;
//        this.mts = mts;
//        this.cins = cins;
//        this.dia = dia;
//        this.nonDia = nonDia;
//    }

    public LogService getLog() {
        return log;
    }

    public void setLog(LogService log) {
        this.log = log;
    }

    public DomainClassifier getClassifier() {
        return classifier;
    }

    public void setClassifier(DomainClassifier classifier) {
        this.classifier = classifier;
    }

    public TVAnswerMapper getTvans() {
        return tvans;
    }

    public void setTvans(TVAnswerMapper tvans) {
        this.tvans = tvans;
    }

    public MovieFlyService getMfs() {
        return mfs;
    }

    public void setMfs(MovieFlyService mfs) {
        this.mfs = mfs;
    }

    public MovieTicketService getMts() {
        return mts;
    }

    public void setMts(MovieTicketService mts) {
        this.mts = mts;
    }

    public CinemaService getCins() {
        return cins;
    }

    public void setCins(CinemaService cins) {
        this.cins = cins;
    }

    public ConjunctionHelper getDia() {
        return dia;
    }

    public void setDia(ConjunctionHelper dia) {
        this.dia = dia;
    }

    public ConjunctionHelper getNonDia() {
        return nonDia;
    }

    public void setNonDia(ConjunctionHelper nonDia) {
        this.nonDia = nonDia;
    }

    public TVProgramService getTps() {
        return tps;
    }

    public void setTps(TVProgramService tps) {
        this.tps = tps;
    }

    public NameMapperService getNameMapperService() {
        return nameMapperService;
    }

    public void setNameMapperService(NameMapperService nameMapperService) {
        this.nameMapperService = nameMapperService;
    }
}
