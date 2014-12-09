package fpt.qa.answerEngine;


import com.fpt.ruby.commons.constants.DomainType;
import com.fpt.ruby.commons.constants.IConstants;
import com.fpt.ruby.commons.service.*;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.template.MovieProcess;
import com.fpt.ruby.template.RubyAnswer;
import com.fpt.ruby.template.TVProcess;
import fpt.qa.domainclassifier.DomainClassifier;

public class NLPAnswerEngine extends AnswerEngine {
    private static DomainClassifier classifier;
    private static MovieTicketService mts;
    private static MovieFlyService mfs;
    private static CinemaService cins;
    private static TVProgramService tps;
    private static NameMapperService nameMapperService;
    private static LogService lgs;
    private static ConjunctionHelper diaConj, nonDiaConj;


    NLPAnswerEngine(ThreadGroup group, String name) {
        super(group, name);
    }

    public static void config(NLPInfoWrapper info) {
        classifier = info.getClassifier();
        mfs = info.getMfs();
        mts = info.getMts();
        cins = info.getCins();
        diaConj = info.getDia();
        tps = info.getTps();
        lgs = info.getLog();
        nameMapperService = info.getNameMapperService();
    }

    @Override
    public void doRun() {
        long start = System.currentTimeMillis();
        setAnswer(null);
        RubyAnswer ans = new RubyAnswer();
        String key = NlpHelper.normalizeQuestion(getQuestion());
        String domain = classifier.getDomain(key);
        try {
            if (domain.equalsIgnoreCase(DomainType.TV.toString())) {
                TVProcess tvProcess = new TVProcess(tps,diaConj);
                tvProcess.process(key);
                ans = tvProcess.getRubyAnswer();
            } else {
                MovieProcess movieProcess = new MovieProcess(diaConj,mfs,mts);
                movieProcess.process(key);
                ans = movieProcess.getRubyAnswer();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ans.setAnswer(IConstants.ANSWER_ERROR);
        } finally {
        }
        setAnswer(ans);
    }
}
