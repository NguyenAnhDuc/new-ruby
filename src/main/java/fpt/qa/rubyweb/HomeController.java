package fpt.qa.rubyweb;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fpt.ruby.helper.HttpHelper;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/home")
public class HomeController {
	private final String LIST_ITEM_LINK = "http://localhost:8080/rubyweb/movie_ticket/allCinema";
	private final String MOVIES_FIELD = "movies";
	private final String CINEMAS_FIELD = "cinemas";
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private static List<String> movies;
	private static List<String> cinemas;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws Exception 
	 */
	
	
	public  void init() throws Exception{
		// Get list movies and list cinemas
		movies = new ArrayList<String>();
		cinemas = new ArrayList<String>();
		String jsonString = HttpHelper.sendGet(LIST_ITEM_LINK);
		JSONObject json = new JSONObject(jsonString);
		JSONArray movieArray = json.getJSONArray(MOVIES_FIELD);
		JSONArray cinemaArray = json.getJSONArray(CINEMAS_FIELD);
		for (int i=0;i < movieArray.length(); i++) {
			movies.add(movieArray.getString(i).trim().split("-")[0]);
		}
		for (int i=0;i < cinemaArray.length(); i++) {
			cinemas.add(cinemaArray.getString(i).trim());
		}
		Collections.sort(movies);
		Collections.sort(cinemas);
	}
	
	@RequestMapping(value = "/old", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		try{
			init();
		}
		catch (Exception ex){
		}
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		model.addAttribute("movies", movies);
		model.addAttribute("cinemas", cinemas);
		return "chat";
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String test(Model model){
		return "chat";
	}
	
	@RequestMapping(value="/debug", method = RequestMethod.GET)
	public String debug(Model model){
		return "debug";
	}

	
}
