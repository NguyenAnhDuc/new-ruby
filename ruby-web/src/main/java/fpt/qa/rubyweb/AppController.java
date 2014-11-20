package fpt.qa.rubyweb;

import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.Log;
import com.fpt.ruby.business.model.QueryParamater;
import com.fpt.ruby.business.service.*;
import com.fpt.ruby.helper.ProcessHelper;
import com.fpt.ruby.model.ReportQuestion;
import com.fpt.ruby.model.RubyAnswer;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import com.fpt.ruby.nlp.*;
import com.fpt.ruby.service.ReportQuestionService;
import fpt.qa.answerEngine.AIMLInfoWrapper;
import fpt.qa.answerEngine.AnswerFinder;
import fpt.qa.answerEngine.NLPInfoWrapper;
import fpt.qa.domainclassifier.DomainClassifier;
import io.keen.client.java.JavaKeenClientBuilder;
import io.keen.client.java.KeenClient;
import io.keen.client.java.KeenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class AppController {
    public static final String UDF_ANS = "Xin lỗi, chúng tôi không trả lời được câu hỏi của bạn";
    private static final Logger logger = LoggerFactory
            .getLogger(AppController.class);
    static TVAnswerMapper tam;
    static DomainClassifier classifier;
    @Autowired
    HttpServletRequest request;
    @Autowired
    MovieTicketService movieTicketService;
    @Autowired
    MovieFlyService movieFlyService;
    @Autowired
    CinemaService cinemaService;
    @Autowired
    LogService logService;
    @Autowired
    NameMapperService nameMapperService;
    @Autowired
    BingSearchService bingSearchService;
    @Autowired
    ReportQuestionService reportQuestionService;
    //Conjunction
    ConjunctionHelper conjunctionHelperWithDiacritic, conjunctionHelperNoneDiacritic;
    @Value("${aimlBotID}")
    private String botId;
    @Value("${aimlToken}")
    private String token;
    // Keen
    private KeenClient keenClient;
    @Value("${keenProjectID}")
    private String KEEN_PROJECT_ID;
    @Value("${keenWriteKey}")
    private String KEEN_WRITE_KEY;
    @Value("${keenReadKey}")
    private String KEEN_READ_KEY;

    AnswerFinder god;

    @PostConstruct
    public void init() {
        tam = new TVAnswerMapperImpl();
        tam.init();
        NlpHelper.init();
        ProcessHelper.init(nameMapperService);
        TVModifiersHelper.init(nameMapperService);
        NonDiacriticNlpHelper.init(nameMapperService);

        String dir = (new RedisHelper()).getClass().getClassLoader()
                .getResource("").getPath();
        conjunctionHelperWithDiacritic = new ConjunctionHelper(dir, nameMapperService);
        conjunctionHelperNoneDiacritic = new ConjunctionHelper(dir + "/non-diacritic", nameMapperService);

        classifier = new DomainClassifier(dir, nameMapperService);
        keenClient = new JavaKeenClientBuilder().build();
        KeenProject keenProject = new KeenProject(KEEN_PROJECT_ID,
                KEEN_WRITE_KEY, KEEN_READ_KEY);
        keenClient.setDefaultProject(keenProject);

        AIMLInfoWrapper aimlInfo = new AIMLInfoWrapper(botId, token);
        NLPInfoWrapper nlpInfo = new NLPInfoWrapper();
        nlpInfo.setCins(cinemaService);
        nlpInfo.setClassifier(classifier);
        nlpInfo.setDia(conjunctionHelperWithDiacritic);
        nlpInfo.setNonDia(conjunctionHelperNoneDiacritic);
        nlpInfo.setMfs(movieFlyService);
        nlpInfo.setMts(movieTicketService);
        nlpInfo.setTvans(tam);
        nlpInfo.setLog(logService);

        god = new AnswerFinder(aimlInfo, nlpInfo);
        System.err.println("CONTRUCT DONE!!!");
    }

    private void track(String filter, Map<String, Object> event) {
        keenClient.addEvent(filter, event);
    }

    @RequestMapping(value = "/getAnswer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public RubyAnswer prototypeGetAnswer(
            @RequestParam("question") String question,
            @RequestParam(value = "userID", defaultValue = "") String appUserID,
            @RequestParam(value = "inputType", defaultValue = "text") String inputType,
            @CookieValue(value = "userID", defaultValue = "") String browserUserID) {

        // Log
        long pivot1 = (new Date()).getTime();

        String userID = browserUserID;
        if (!inputType.equals("text")) inputType = "voice";
        if (!appUserID.isEmpty()) userID = appUserID;
        logger.info("UserID : " + userID);

        Log log = new Log();
        log.setInputType(inputType);
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setQuestion(question);
        log.setDate(new Date());

        RubyAnswer ans = god.getAnswer(question);
        long pivot2 = (new Date()).getTime();
        TrackingThread ti = new TrackingThread(ans, log, userID, inputType);
        ti.start();

        long pivot3 = (new Date()).getTime();
        System.out.println((new Date()).getTime() + " . DONE LOG AND ANALYTICS  ");
        System.out.println("question -> answer: " + (pivot2 - pivot1) / 1000.0 + " seconds");
        System.out.println("answer -> log: " + (pivot3 - pivot2) / 1000.0 + " seconds");
        return ans;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model,
                       @CookieValue(value = "userID", defaultValue = "") String userID,
                       HttpServletResponse response) {
        logger.info("HOME CONTROLLER");
        logger.info("userID from cookies: " + userID);
        if (userID.isEmpty()) {
            Cookie cookie = new Cookie("userID", UUID.randomUUID().toString());
            response.addCookie(cookie);
        }

        return "chat";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(Model model) {
        return "chat";
    }


    @RequestMapping(value = "/report", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String reportQuestion(@RequestParam("question") String question,
                                 @RequestParam("answer") String answer,
                                 @RequestParam("intent") String intent,
                                 @RequestParam("domain") String domain) {
        try {
            ReportQuestion re = new ReportQuestion();
            re.setQuestion(question);
            re.setAnswer(answer);
            re.setDomain(domain);
            re.setIntent(intent);
            reportQuestionService.save(re);
            return "success";
        } catch (Exception ex) {
            return "error";
        }
    }

    private class TrackingThread extends Thread {
        String userID, inputType;
        RubyAnswer ans;
        Log log;

        public TrackingThread(RubyAnswer ans, Log log, String userID, String inputType) {
            this.ans = ans;
            this.log = log;
            this.userID = userID;
            this.inputType = inputType;
        }

        public void run() {
            log.setAnswer(ans.getAnswer());
            log.setIntent(ans.getIntent());
            log.setDomain(ans.getDomain());

            QueryParamater qu = new QueryParamater();
            qu.setBeginTime(ans.getBeginTime());
            qu.setEndTime(ans.getEndTime());
            qu.setMovieTicket(ans.getMovieTicket());
            qu.setMovieTitle(ans.getMovieTitle());
            log.setQueryParamater(ans.getQueryParamater());

            logService.save(log);

            // Analytic
            Map<String, Object> event = new HashMap<>();
            event.put("userID", userID);
            event.put("inputType", inputType);
            event.put("domain", ans.getDomain());
            event.put("intent", ans.getIntent());
            event.put("question", ans.getQuestion());
            event.put("answer", ans.getAnswer());
            track("userActivity", event);
        }
    }
}