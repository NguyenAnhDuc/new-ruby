package fpt.qa.rubyweb;

import com.fpt.ruby.business.helper.DisplayAnswerHelper;
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
import fpt.qa.domainclassifier.DomainClassifier;
import fpt.qa.mdnlib.util.string.DiacriticConverter;
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

/*import net.sf.uadetector.ReadableUserAgent;
 import net.sf.uadetector.UserAgentStringParser;
 import net.sf.uadetector.service.UADetectorServiceFactory;*/

@Controller
@RequestMapping("/")
public class AppController {
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
	@Autowired	ReportQuestionService reportQuestionService;
	
	static TVAnswerMapper tam = new TVAnswerMapperImpl();
	static DomainClassifier classifier;
	private static final Logger logger = LoggerFactory
			.getLogger(AppController.class);
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
	
	//Conjunction 
	ConjunctionHelper conjunctionHelperWithDiacritic, conjunctionHelperNoneDiacritic;
	
	// get user agent
	/*
	 * private String getUserAgent() { return request.getHeader("user-agent"); }
	 */
	public static final String UDF_ANS = "Xin lỗi, chúng tôi không trả lời được câu hỏi của bạn";
	@PostConstruct
	public void init() {
		tam.init();
		NlpHelper.init();
		ProcessHelper.init(nameMapperService);
		TVModifiersHelper.init(nameMapperService);
		NonDiacriticNlpHelper.init(nameMapperService);
		String dir = (new RedisHelper()).getClass().getClassLoader()
				.getResource("").getPath();
		conjunctionHelperWithDiacritic = new ConjunctionHelper(dir, nameMapperService);
		conjunctionHelperNoneDiacritic = new ConjunctionHelper(dir+ "/non-diacritic", nameMapperService);
		classifier = new DomainClassifier(dir, nameMapperService);
		keenClient = new JavaKeenClientBuilder().build();
		KeenProject keenProject = new KeenProject(KEEN_PROJECT_ID,
				KEEN_WRITE_KEY, KEEN_READ_KEY);
		keenClient.setDefaultProject(keenProject);
	}

	private void track(String filter, Map<String, Object> event) {
		keenClient.addEvent(filter, event);
	}

	@RequestMapping(value = "/getAnswer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public RubyAnswer prototypeGetAnswer(
			@RequestParam("question") String question,
			@RequestParam(value="userID", defaultValue = "") String appUserID,
			@RequestParam(value="inputType", defaultValue = "text") String inputType,
			@RequestParam(value="useWebSearch", defaultValue = "yes") String useWebSearch,
			@CookieValue(value = "userID", defaultValue = "") String browserUserID) {
		/*
		 * UserAgentStringParser parser =
		 * UADetectorServiceFactory.getResourceModuleParser(); ReadableUserAgent
		 * agent = parser.parse(request.getHeader("User-Agent"));
		 * System.out.println("Operating system: " +
		 * agent.getOperatingSystem().getName());
		 * System.out.println("Device category: " +
		 * agent.getDeviceCategory().getName() ); System.out.println("Family: "
		 * + agent.getFamily() );
		 */
		System.out.println("Question: " + question);
		String userID = browserUserID;
		if (!inputType.equals("text")) inputType = "voice";
		if (!appUserID.isEmpty()) userID = appUserID;
		logger.info("UserID: " + userID);
		Log log = new Log();
		log.setInputType(inputType);
		log.setUserAgent(request.getHeader("User-Agent"));
		log.setQuestion(question);
		log.setDate(new Date());
		RubyAnswer rubyAnswer = new RubyAnswer();
		// AIML Layer First
		String answer = ProcessHelper.getAIMLAnswer(question, botId, token);
		if (answer != null && !answer.trim().equalsIgnoreCase("")) {
			rubyAnswer.setAnswer(answer);
			rubyAnswer.setQuestion(question);
			rubyAnswer.setDomain("aiml");
			rubyAnswer.setIntent("aiml");
			log.setAnswer(rubyAnswer.getAnswer());
			log.setIntent("AIML");
			log.setDomain("AIML");
			logService.save(log);
		} else {
			long sTime = System.currentTimeMillis();
			String key = NlpHelper.normalizeQuestion(question);
			String domain = classifier.getDomain(key);
			logger.info("Current time: " + new Date() + " | domain: " + domain);

			// rubyAnswer.setInCache(this.questionStructureService.isInCache(key));
			// rubyAnswer.setQuestion(question);
			// Process question
			// QuestionStructure questionStructure =
			// ProcessHelper.getQuestionStucture(question,
			// questionStructureService );
			// QuestionStructure questionStructure = new QuestionStructure();
			// Process answer
			try{
				if (domain.equals("tv")) {
					// if ( question.startsWith( "tv" ) ){
					System.err.println("[AppController] Domain TV");
					rubyAnswer = tam.getAnswer(key, logService, conjunctionHelperWithDiacritic);
					// Neu khong tra loi duoc cau hoi co dau, thi chuyen cau hoi do
					// ve cau hoi khong dau va xu ly
					if (DiacriticConverter.hasDiacriticAccents(key)
							&& rubyAnswer.getAnswer().contains(	TVAnswerMapperImpl.UDF_ANS)) {
						rubyAnswer = tam.getAnswer(DiacriticConverter.removeDiacritics(key),logService, conjunctionHelperWithDiacritic);
					}
				} else {
					System.err.println("[AppController] Domain Movie");
					rubyAnswer = ProcessHelper.getAnswer(key, movieFlyService,
							movieTicketService, cinemaService, logService, conjunctionHelperNoneDiacritic, conjunctionHelperWithDiacritic);
				}
				rubyAnswer.setDomain(domain);
			}
			catch (Exception ex){
				rubyAnswer.setAnswer(UDF_ANS);
			}
			
			
			// If can't answer, take result from Bing Search
			if (rubyAnswer.getAnswer().toLowerCase().contains("xin lỗi,")){
				// If on the mobile
				/*rubyAnswer.setAnswer("Xin lỗi chúng tôi chưa có thông tin cho câu hỏi của bạn nhưng tôi có thể search cho bạn: " +
						"<a href=\"searchWeb?question=" + question +  "\">Search on the Web</a>");*/

				//If on the web
				rubyAnswer.setAnswer("Xin lỗi chúng tôi chưa có thông tin cho câu hỏi của bạn nhưng tôi có thể search cho bạn: " +
						"<a href=\"#\" class=\"btn simpleConfirm\" onclick=\"searchWeb('"
						 +  question  +
						"')\">Search on the Web</a> ");
				//rubyAnswer.setAnswer(DisplayAnswerHelper.display(bingSearchService.getDocuments(question, 5)));
			}
			
			System.out.println("Total time = "+ (System.currentTimeMillis()-sTime));
		}

		// Log
		log.setAnswer(rubyAnswer.getAnswer());
		log.setDomain(rubyAnswer.getDomain());
		log.setIntent(rubyAnswer.getIntent());
		QueryParamater queryParamater = new QueryParamater();
		queryParamater.setBeginTime(rubyAnswer.getBeginTime());
		queryParamater.setEndTime(rubyAnswer.getEndTime());
		queryParamater.setMovieTitle(rubyAnswer.getMovieTitle());
		queryParamater.setMovieTicket(rubyAnswer.getMovieTicket());
		log.setQueryParamater(rubyAnswer.getQueryParamater());
		logService.save(log);
		
		// Analytic
		Map<String, Object> event = new HashMap<String, Object>();
		event.put("userID", userID);
		event.put("inputType", inputType);
		event.put("domain", rubyAnswer.getDomain());
		event.put("intent", rubyAnswer.getIntent());
		event.put("question", rubyAnswer.getQuestion());
		event.put("answer", rubyAnswer.getAnswer());
		track("userActivity", event);

		logger.info("Returned answer:\n" + rubyAnswer.getAnswer());
		if (rubyAnswer.getAnswer().length() < 50) rubyAnswer.setAnswer(rubyAnswer.getAnswer() + " ^_^");
		return rubyAnswer;
		// return app.getAnswer(question);
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
	public String test(Model model){
		return "chat";
	}
	
	
	@RequestMapping(value = "/report", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reportQuestion(@RequestParam("question") String question,
								 @RequestParam("answer") String answer,
								 @RequestParam("intent") String intent,
								 @RequestParam("domain") String domain){
		try{
			ReportQuestion re = new ReportQuestion();
			re.setQuestion(question);
			re.setAnswer(answer);
			re.setDomain(domain);
			re.setIntent(intent);
			reportQuestionService.save(re);
			return "success";
		}
		catch (Exception ex){
			return "error";
		}
	}

	@RequestMapping(value = "/searchWeb", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public RubyAnswer confirmWebSearch(@RequestParam("question") String question){
		RubyAnswer rubyAnswer = new RubyAnswer();
		try{
			rubyAnswer.setAnswer(DisplayAnswerHelper.display(bingSearchService.getDocuments(question, 5)));
			return rubyAnswer;
		}
		catch (Exception ex){
			rubyAnswer.setAnswer("Some thing went wrong! Please try again!");
			return rubyAnswer;
		}
	}
	/*
	 * @RequestMapping(value="/allCinema", method = RequestMethod.GET, produces
	 * = "application/json; charset=UTF-8")
	 * 
	 * @ResponseBody public BasicDBObject testMovie(){ BasicDBObject result =
	 * new BasicDBObject(); List<MovieTicket> movieTickets =
	 * mongoOperation.findAll(MovieTicket.class); Set<String> cinemas = new
	 * HashSet<String>(); Set<String> movies = new HashSet<String>(); for
	 * (MovieTicket movieTicket : movieTickets) {
	 * System.out.println(movieTicket.getCinema() + " " +
	 * movieTicket.getMovie()); cinemas.add(movieTicket.getCinema());
	 * movies.add(movieTicket.getMovie()); } result.append("cinemas", cinemas);
	 * result.append("movies", movies ); return result; }
	 */

}