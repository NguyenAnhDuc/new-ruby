package fpt.qa.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fpt.ruby.business.constants.ProgramType;
import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.Channel;
import com.fpt.ruby.business.model.TVProgram;
import com.fpt.ruby.business.service.NameMapperService;
import com.fpt.ruby.business.service.TVProgramService;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import fpt.qa.mdnlib.struct.conjunction.Conjunction;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import fpt.qa.type_mapper.TypeMapper;

@Component
public class CrawlerVTVCab {
	List<String> crawlChannels = Collections.unmodifiableList(Arrays
			.asList(new String[] { "VTVCAB3", "VTVCAB4", "VTVCAB5", "VTVCAB6",
					"VTVCAB7", "VTVCAB8", "VTVCAB12", "VTVCAB16", "VTVCAB17",
					"VTVCAB19","K+PM", "FOX SPORTS",
					"CINEMA WORLD", "WARNER", "GEM", "AXN", "ANIMAL PLANET",
					"CINEMAX", "SCREEN RED", "NGC", "TRAVEL LIVING",
					"DISCOVERY WORLD", "STARMOVIE"}));

	private final static long ONE_DAY = 24 * 60 * 60 * 1000;
	private final static long FUTUREDAY_CRAWL = 1;
	private final static String ROOT_URL = "http://www.vtvcab.vn/lich-phat-song";

	public static String getResponse(String url, String requestType)
			throws ClientProtocolException, IOException {
		requestType = requestType.toLowerCase();
		String response = "";

		switch (requestType) {
		case "get":
			response = sendGet(url);
			break;

		case "post":
			response = "";
			break;

		default:
			response = "ERROR: Unknown type";
			break;
		}

		return response;
	}

	private static String makeUrl(Channel channel, Date date) {
		System.out.println(date.toString());
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String url = String.format("%s?day=%d&month=%d&year=%d&channel=%d",
				ROOT_URL, cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR),
				Integer.parseInt(channel.getId()));
		System.out.println("URL = " + url);
		return url;
	}

	private static String sendGet(String url) throws ClientProtocolException,
			IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request);

		System.out.println("[CrawlerVTVCab] Response code: "
				+ response.getStatusLine().getStatusCode());

		BufferedReader br = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = br.readLine()) != null) {
			result.append(line);
		}

		return result.toString();
	}

	private static String getRscDir(String defaultPath) {
		String dir;

		try {
			dir = (new RedisHelper()).getClass().getClassLoader()
					.getResource("").getPath();
		} catch (Exception ex) {
			dir = defaultPath;
			System.out.println("[CrawlerVTVCab] life goes one dir " + dir);
		}

		return dir;
	}

	private static List<TVProgram> calculateEndTime(List<TVProgram> tvPrograms) {
		// TODO: should write comparator for TVProgram

		List<TVProgram> results = new ArrayList<TVProgram>();
		for (int i = 0; i < tvPrograms.size() - 1; i++) {
			TVProgram tvProgram = new TVProgram();
			tvProgram = tvPrograms.get(i);
			tvProgram.setEnd_date(tvPrograms.get(i + 1).getStart_date());
			results.add(tvProgram);
		}
		return results;
	}

	@SuppressWarnings("deprecation")
	public List<TVProgram> getPrograms(Channel channel, Date date)
			throws Exception {
		List<TVProgram> progs = new ArrayList<TVProgram>();
		TypeMapper tm = new TypeMapper();
		String url = makeUrl(channel, date);
		System.out.println(url);
		Document doc = Jsoup.parse(getResponse(url, "get"));
		Elements schedule = doc.getElementsByClass("table-schedules").get(0)
				.getElementsByTag("tr");

		boolean first = true;
		for (Element e : schedule) {
			if (first) {
				first = false;
				continue;
			}

			Elements info = e.getElementsByTag("td");
			if (info.size() < 3) break;

			String strTime = info.get(0).text();
			String[] parts = strTime.split(":");
			if (parts.length < 2) break;

			Date broadcastDate = (Date) date.clone();
			broadcastDate.setHours(Integer.parseInt(parts[0]));
			broadcastDate.setMinutes(Integer.parseInt(parts[1]));

			TVProgram prog = new TVProgram();
			prog.setChannel(channel.getName());
			prog.setTitle(StringEscapeUtils.unescapeJava(info.get(1).text()));
			prog.setStart_date(broadcastDate);
			prog.setDescription(info.get(2).text());

			List<ProgramType> types = TypeMapper.getType(prog.getChannel(), prog.getTitle(), prog.getDescription());
			List<String> typesStr = new ArrayList<String>();
			for (ProgramType t: types) {
				typesStr.add(t.toString());
			}
			//prog.setTypes(typesStr);
			if (typesStr.size() > 0) {
				prog.setType(String.join(",", typesStr));
			} else {
				prog.setType(ProgramType.OTHER.toString());
			}

			progs.add(prog);
			System.out.println(prog.toString());
		}

		return progs;
	}

	public List<Channel> getChannels(NameMapperService nameMapperService, ConjunctionHelper conjunctionHelperNoneDiacritic) throws Exception {
		List<Channel> channels = new ArrayList<Channel>();
		String dir = getRscDir("./classes/");
		Document doc = Jsoup.parse(getResponse(ROOT_URL, "GET"));

		Elements ces = doc.getElementById("channel").select("option");

		for (Element ce : ces) {
			Channel c = new Channel();
			c.setId(ce.val());

			try {
				System.out.println("PRE: " + ce.text().trim());
				String name = conjunctionHelperNoneDiacritic.getChannelName(ce.text().trim());

				if (!(name.equals(null) || name.isEmpty())) {
					c.setName(name);
				} else {
					c.setName(ce.text().trim());
				}

				channels.add(c);
				System.out.println("Channel name: " + ce.text().trim() + " | "
						+ c.getName() + " | " + c.getId());
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}

		return channels;
	}

	public void doCrawl(TVProgramService tvProgramService, NameMapperService nameMapperService, ConjunctionHelper conjunctionHelper) throws Exception {
		System.out.println("Crawling from VTVCAB");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		List<Channel> channels = getChannels(nameMapperService, conjunctionHelper);

		for (Channel channel : channels) {
			if (crawlChannels.contains(channel.getName().toUpperCase())) {
				try {
					System.out.println("Going to craw schedule for channel: "
							+ channel.getName());
					Date today = new Date();
					for (int i =  0; i <= FUTUREDAY_CRAWL; ++i) {
						Date crawlDate = new Date(today.getTime() + ONE_DAY * i);
						List<TVProgram> progs = getPrograms(channel, crawlDate);
						progs = calculateEndTime(progs);

						for (TVProgram prog : progs) {
							tvProgramService.save(prog);
						}
					}

				} catch (Exception ex) {
					ex.printStackTrace();
					continue;
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		TVProgramService ts = new TVProgramService();
		NameMapperService nms = new NameMapperService();

		String dir = "/home/timxad/ws/proj/ruby/new-ruby/ruby-web/src/main/resources/";
		ConjunctionHelper ch = new ConjunctionHelper(dir, nms);

		CrawlerVTVCab crawler = new CrawlerVTVCab();
		crawler.doCrawl(ts, nms, ch);
	}

}
