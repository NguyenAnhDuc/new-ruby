package fpt.qa.answerEngine;


import com.fpt.ruby.business.service.*;
import com.fpt.ruby.business.template.DomainType;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.template.MovieProcess;
import com.fpt.ruby.template.TVProcess;
import fpt.qa.domainclassifier.DomainClassifier;

public class NLPAnswerEngine extends AnswerEngine {
    public static final String UDF_ANS = "Xin lỗi, chúng tôi không trả lời được câu hỏi của bạn";
    private static DomainClassifier classifier;
    private static MovieTicketService mts;
    private static MovieFlyService mfs;
    private static CinemaService cins;
    private static TVProgramService tps;
    private static NameMapperService nameMapperService;
    private static LogService lgs;
//    private static NameMapperService nms;
//    private static ReportQuestionService mqs;
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

        System.out.printf("[%s DOMAIN]\n", domain.toUpperCase());

        try {
            if (domain.equalsIgnoreCase("tv")) {
                TVProcess tvProcess = new TVProcess(tps,diaConj);
                tvProcess.process(key);
                ans.setAnswer(tvProcess.getRubyAnswer().getAnswer());
                ans.setDomain(DomainType.TV.toString());
                ans.setIntent(tvProcess.getRubyAnswer().getIntent());
            } else {
                MovieProcess movieProcess = new MovieProcess(diaConj,mfs,mts);
                movieProcess.process(key);
                ans.setAnswer(movieProcess.getRubyAnswer().getAnswer());
                ans.setDomain(DomainType.TV.toString());
                ans.setIntent(movieProcess.getRubyAnswer().getIntent());
            }
        } catch (Exception e) {
            e.printStackTrace();
            ans.setAnswer(UDF_ANS);
        } finally {
            ans.setQuestion(getQuestion());
        }
        ans.setDomain(domain);
        setAnswer(ans);
        System.err.println("Nlp Answer Time: " + (System.currentTimeMillis() - start));
    }
}
