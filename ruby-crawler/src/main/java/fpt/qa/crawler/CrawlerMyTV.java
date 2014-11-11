package fpt.qa.crawler;

import com.fpt.ruby.business.constants.ProgramType;
import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.Channel;
import com.fpt.ruby.business.model.TVProgram;
import com.fpt.ruby.business.service.NameMapperService;
import com.fpt.ruby.business.service.TVProgramService;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import fpt.qa.type_mapper.TypeMapper;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CrawlerMyTV {
    List<String> crawlChannels = Collections.unmodifiableList(Arrays.asList(new String[]{"VTV1", "VTV2", "VTV3",
            "VTV4", "VTV5", "VTV6", "VTV9", "HBO", "STAR MOVIES", "MAX", "Hà Nội 1", "Hà Nội 2", "VTC1", "VTC2",
            "HTV7", "HTV9", "HTV1", "HTV1", "DISNEY", "CARTOON", "VITV", "O2 TV", "DISCOVERY", "ANTV", "VTVCAB1",
            "VTVCAB2", "STAR WORLD HD", "VOV", "K+1", "K+NS", "NATIONAL GEOGRAPHIC"}));
    private static long ONE_DAY = 24 * 60 * 60 * 1000;
    private static long FUTUREDAY_CRAWL = 1;

    public static String sendGet(String url) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        // add request header
        HttpResponse response = client.execute(request);

        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    public List<Channel> getChanel(ConjunctionHelper conjunctionHelper) throws Exception {
//        String dir = "./classes/"; // hack for standalone crawler and
//        // in-web-app crawler.
//        try {
//            dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
//        } catch (Exception e) {
//            System.out.println("[get Channel] Life goes on! Dir = " + dir);
//        }
        List<Channel> channels = new ArrayList<Channel>();
        Document doc = Jsoup.parse(sendGet("http://www.mytv.com.vn/lich-phat-song"));
        Element chanel = doc.getElementById("channelId");

        Elements chanElements = chanel.select("option");
        for (Element element : chanElements) {
            Channel channel = new Channel();
            channel.setId(element.val());
            String name = conjunctionHelper.getChannelName(element.text().trim());
            if (name != null)
                channel.setName(name);
            else
                channel.setName(element.text().trim());
            System.out.println("Channel name: " + element.text().trim() + " | " + channel.getName());
            channels.add(channel);
        }
        return channels;
    }

    public void crawlMyTV(TVProgramService tvProgramService) throws Exception {

        System.out.println("Crawling from MYTV");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        NameMapperService nameMapperService = new NameMapperService();
        ConjunctionHelper conjunctionHelper = new ConjunctionHelper(nameMapperService);
        List<Channel> channels = getChanel(conjunctionHelper);
        Date today = new Date();

        for (Channel channel : channels) {
            if (crawlChannels.contains(channel.getName().toUpperCase())) {
                try {
                    System.out.println("Crawling from " + channel.getName());
                    for (int i = 0; i <= FUTUREDAY_CRAWL; i++) {
                        String date = df.format(new Date(today.getTime() + ONE_DAY * i));
                        List<TVProgram> tvPrograms = crawlChannel(channel, date);

                        tvPrograms = calculateEndTime(tvPrograms);
                        for (TVProgram tvProgram : tvPrograms) {
                            tvProgramService.save(tvProgram);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
            }

        }

    }

    public List<TVProgram> calculateEndTime(List<TVProgram> tvPrograms) {
        List<TVProgram> results = new ArrayList<TVProgram>();
        for (int i = 0; i < tvPrograms.size() - 1; i++) {
            TVProgram tvProgram = new TVProgram();
            tvProgram = tvPrograms.get(i);
            tvProgram.setEnd_date(tvPrograms.get(i + 1).getStart_date());
            results.add(tvProgram);
        }
        return results;
    }

    public List<TVProgram> crawlChannel(Channel channel, String date) {
        List<TVProgram> tvPrograms = new ArrayList<TVProgram>();
        String url = "http://www.mytv.com.vn/module/ajax/ajax_get_schedule.php?channelId=" + channel.getId()
                + "&dateSchedule=" + date;
        System.out.println(url);
        Document doc;
        try {
            doc = Jsoup.parse(sendGet(url));
            Elements elements = doc.select("p");
            TypeMapper tm = new TypeMapper();

            for (Element element : elements) {
                String tmp = element.text().replace("<\\/strong>", "").replace("<\\/p>", "");
                String time = tmp.substring(0, 5);
                String programName = tmp.substring(5, tmp.length());
                // System.out.println("Time: " + time + " | " + "Program Name: "
                // +
                // StringEscapeUtils.unescapeJava(programName));

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date channelDate = formatter.parse(date);

                String[] times = time.split(":");
                channelDate.setHours(Integer.parseInt(times[0]));
                channelDate.setMinutes(Integer.parseInt(times[1]));

                TVProgram tvProgram = new TVProgram();
                tvProgram.setChannel(channel.getName());
                tvProgram.setTitle(StringEscapeUtils.unescapeJava(programName));
                tvProgram.setStart_date(channelDate);
                List<ProgramType> types = TypeMapper.getType(channel.getName(), tvProgram.getTitle());
                List<String> typesStr = new ArrayList<String>();
                for (ProgramType t : types) {
                    typesStr.add(t.toString());
                }

                if (typesStr.size() > 0) {
                    tvProgram.setType(String.join(",", typesStr));
                } else {
                    tvProgram.setType(ProgramType.OTHER.toString());
                }
                System.out.println(tvProgram.toString());

                tvPrograms.add(tvProgram);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tvPrograms;
    }

    public static void main(String[] args) throws Exception {
        TVProgramService tvProgramService = new TVProgramService();
        CrawlerMyTV crawlerMyTV = new CrawlerMyTV();
        crawlerMyTV.crawlMyTV(tvProgramService);
    }
}
