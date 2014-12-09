package fpt.qa.answerEngine;

import com.fpt.ruby.business.service.*;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import fpt.qa.domainclassifier.DomainClassifier;

public class NLPInfoWrapper {
    DomainClassifier classifier;
    MovieFlyService mfs;
    MovieTicketService mts;
    CinemaService cins;
    TVProgramService tps;
    ConjunctionHelper dia;
    LogService log;
    NameMapperService nameMapperService;
    public NLPInfoWrapper() {
    }
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
