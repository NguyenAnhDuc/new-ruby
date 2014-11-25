package fpt.qa.answerEngine;


import com.fpt.ruby.business.service.CinemaService;
import com.fpt.ruby.business.service.LogService;
import com.fpt.ruby.business.service.MovieFlyService;
import com.fpt.ruby.business.service.MovieTicketService;
import com.fpt.ruby.helper.ProcessHelper;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import com.fpt.ruby.nlp.NlpHelper;
import com.fpt.ruby.nlp.TVAnswerMapper;
import com.fpt.ruby.nlp.TVAnswerMapperImpl;
import fpt.qa.domainclassifier.DomainClassifier;
import fpt.qa.mdnlib.diacritic.DiacriticConverter;

public class NLPAnswerEngine extends AnswerEngine {
    public static final String UDF_ANS = "Xin lỗi, chúng tôi không trả lời được câu hỏi của bạn";
    private static TVAnswerMapper tvans;
    private static DomainClassifier classifier;
    private static MovieTicketService mts;
    private static MovieFlyService mfs;
    private static CinemaService cins;
    private static LogService lgs;
//    private static NameMapperService nms;
//    private static ReportQuestionService mqs;
    private static ConjunctionHelper diaConj, nonDiaConj;


    NLPAnswerEngine(ThreadGroup group, String name) {
        super(group, name);
    }

    public static void config(NLPInfoWrapper info) {
        classifier = info.getClassifier();
        tvans = info.getTvans();
        mfs = info.getMfs();
        mts = info.getMts();
        cins = info.getCins();
        diaConj = info.getDia();
        nonDiaConj = info.getNonDia();
        lgs = info.getLog();
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
                ans = tvans.getAnswer(key, lgs, diaConj);
                if (DiacriticConverter.hasDiacriticAccents(key) && ans.getAnswer().contains(TVAnswerMapperImpl.UDF_ANS))
                    ans = tvans.getAnswer(DiacriticConverter.removeDiacritics(key), lgs, nonDiaConj);
            } else {
                ans = ProcessHelper.getAnswer(key, mfs, mts, cins, lgs, nonDiaConj, diaConj);
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
