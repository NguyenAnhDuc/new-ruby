package fpt.qa.rubyweb;

import com.fpt.ruby.business.model.*;
import com.fpt.ruby.business.service.*;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("")
public class AdminController {
    @Autowired
    private MovieTicketService movieTicketService;
    @Autowired
    private TVProgramService tvProgramService;
    @Autowired
    private LogService logService;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private NameMapperService nameMapperService;


    @RequestMapping(value = "/addCinema", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public String addCinema(@RequestParam("cin_name") String cinName, @RequestParam("cin_address") String cin_address,
                            @RequestParam("mobile") String mobile, Model model) {
        try {
            Cinema cinema = new Cinema();
            cinema.setName(cinName.trim());
            cinema.setAddress(cin_address.trim());
            cinema.setMobile(mobile.trim());
            cinemaService.save(cinema);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("status", "failed");
            return "";
        }
        model.addAttribute("status", "success");
        List<Cinema> cinemas = cinemaService.findAll();
        model.addAttribute("cinemas", cinemas);
        return "showCinema";
    }

    @RequestMapping(value = "admin", method = RequestMethod.GET)
    public String admin(Model model) {
        return "admin-tools";
    }

    @RequestMapping(value = "admin-add-cinema", method = RequestMethod.GET)
    public String addCinema(Model model) {
        return "addCinema";
    }

    @RequestMapping(value = "admin-show-tickets", method = RequestMethod.GET)
    public String showTickets(@RequestParam("day") String day, Model model) {
        int numday = 0;
        try {
            numday = Integer.parseInt(day);
        } catch (Exception ex) {
        }

        List<MovieTicket> tickets = movieTicketService.findTicketToShow(numday);
        model.addAttribute("tickets", tickets);
        HashSet<String> movies = new HashSet<String>();
        HashSet<String> cinemas = new HashSet<String>();
        for (MovieTicket movieTicket : tickets) {
            movies.add(movieTicket.getMovie());
            cinemas.add(movieTicket.getCinema());
        }
        model.addAttribute("numMovie", movies.size());
        model.addAttribute("numCinema", cinemas.size());
        return "showTicket";
    }

    @RequestMapping(value = "admin-show-tvprograms", method = RequestMethod.GET)
    public String showTVPrograms(@RequestParam("day") String day, Model model) {
        int numday = 0;
        try {
            numday = Integer.parseInt(day);
        } catch (Exception ex) {
        }
        List<TVProgram> tvPrograms = tvProgramService.findProgramToShow(numday);
        model.addAttribute("tvPrograms", tvPrograms);
        return "showTV";
    }

    @RequestMapping(value = "admin-show-cinemas", method = RequestMethod.GET)
    public String showCinemas(Model model) {
        List<Cinema> cinemas = cinemaService.findAll();

        model.addAttribute("cinemas", cinemas);
        return "showCinema";
    }

    @RequestMapping(value = "admin-show-logs", method = RequestMethod.GET)
    public String showLogs(Model model, @RequestParam("num") String numString) {
        System.out.println("Admin Show Logs");
        List<Log> logs = Lists.reverse(logService.findLogToShow());
        int numLog = 0;
        try {
            numLog = Integer.parseInt(numString);
        } catch (Exception ex) {
            numLog = 0;
        }
        if (numLog == 0) {
            model.addAttribute("logs", logs);
            return "showLog";
        }

        model.addAttribute("logs", logs.subList(0, numLog));
        return "showLog";
    }

    @RequestMapping(value = "admin-show-name-mapper", method = RequestMethod.GET)
    public String showNameMapper(Model model) {
        List<NameMapper> nms = nameMapperService.findAll().subList(0, 200);
        model.addAttribute("nameMappers", nms);
        return "showNameMapper";
    }

    @RequestMapping(value = "/deleteTicket", method = RequestMethod.GET)
    public String deleteBot(@RequestParam("ticketId") String ticketId, Model model) {
        MovieTicket movieTicket = movieTicketService.findById(ticketId);
        movieTicketService.delete(movieTicket);
        List<MovieTicket> tickets = movieTicketService.findTicketToShow(0);
        model.addAttribute("tickets", tickets);
        return "showTicket";
    }

    @RequestMapping(value = "/testChartData", method = RequestMethod.POST)
    public List<String> getData() {
        List<String> data = new ArrayList<String>();
        data.add("Jan");
        data.add("Feb");
        return data;
    }

    @RequestMapping(value = "/admin-restart-cached-tv", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BasicDBObject crawlMyTV() {
        try {
            tvProgramService.restartCached();
        } catch (Exception ex) {
            return new BasicDBObject().append("status", "failed");
        }
        return new BasicDBObject().append("status", "success");
    }

}






/*
    @RequestMapping(value = "/admin-analytic", method = RequestMethod.GET)
	public String testChart(Model model) {
		int ONE_DAY = 24 * 3600 * 1000;
		// Line Chart: NumberOfRequest
		Date today = new Date();
		today.setHours(0);today.setMinutes(0);today.setSeconds(0);
		Date firstdayOfMonth = new Date(today.getTime() - (today.getDate() -1) * ONE_DAY);
		List<String> months = new ArrayList<String>();
		List<DataChart> series = new ArrayList<DataChart>();
		DataChart dataAll = new DataChart();
		dataAll.name = "All";
		dataAll.data = new ArrayList<Integer>();
		DataChart domainTV = new DataChart();
		domainTV.name = "TV Domain";
		domainTV.data = new ArrayList<Integer>();
		DataChart domainMovie = new DataChart();
		domainMovie.name = "Movie Domain";
		domainMovie.data = new ArrayList<Integer>();

		List<Log> logs = logService.findLogGtTime(firstdayOfMonth);
		for (int i = firstdayOfMonth.getDate(); i <= today.getDate(); i++ ){
			months.add(""+i);
			Date before = new Date(firstdayOfMonth.getTime() + i*ONE_DAY);
			Date after = new Date(firstdayOfMonth.getTime() + (i-1)*ONE_DAY);
			Stream<Log> streamLog =  logs.stream().filter(l -> ( l.getDate().after(after) &&  l.getDate().before(before)));
			domainTV.data.add((int)logs.stream().filter(l -> (l.getDomain() != null && l.getDomain().equals("tv") && l.getDate().after(after) &&  l.getDate().before(before))).count());
			domainMovie.data.add((int)logs.stream().filter(l -> (l.getDomain() != null && l.getDomain().equals("movie") && l.getDate().after(after) &&  l.getDate().before(before))).count());
			dataAll.data.add((int)logs.stream().filter(l -> (l.getDate().after(after) &&  l.getDate().before(before))).count());
		}
		series.add(dataAll);series.add(domainTV);series.add(domainMovie);
		ObjectMapper mapper = new ObjectMapper();
		String json = "", jsonAll = "", jsonTV = "", jsonMovie = "";
		try {
			json = mapper.writeValueAsString(months);
			jsonAll = mapper.writeValueAsString(series);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("data", json);
		model.addAttribute("jsonS", jsonAll);
		// Pie Chart: Intent
		logs = logService.findAll();
		List<DataPieChart> dataPies = new ArrayList<DataPieChart>();
		Map<String, Long> counted = logs.stream()
				.collect(Collectors.groupingBy(o -> o.getIntent(), Collectors.counting()));
		Iterator iterator = counted.keySet().iterator();
		while(iterator.hasNext()){
			Object key   = iterator.next();
			Object value = counted.get(key);
			DataPieChart dataPieChart = new DataPieChart();
			dataPieChart.name = key.toString();
			dataPieChart.y = Integer.parseInt(value.toString()) * 1.0 / logs.size();
			dataPies.add(dataPieChart);
		}
		String jsonPie = "";
		try{
			jsonPie = mapper.writeValueAsString(dataPies);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("jsonPie", jsonPie);

		return "admin-dashboard";
	}*/
