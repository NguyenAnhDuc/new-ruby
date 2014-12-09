package fpt.qa.crawler;


import com.fpt.ruby.commons.constants.ProgramType;
import com.fpt.ruby.commons.entity.movie.MovieFly;
import com.fpt.ruby.commons.entity.objects.Channel;
import com.fpt.ruby.commons.entity.tv.TVProgram;
import com.fpt.ruby.commons.helper.CrawlerHelper;
import com.fpt.ruby.commons.service.MovieFlyService;
import com.fpt.ruby.commons.service.TVProgramService;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import fpt.qa.configs.SpringMongoConfig;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CrawlerMyTV {
    List<String> crawlChannels = Collections.unmodifiableList(Arrays.asList("VTV1", "VTV2", "VTV3",
            "VTV4", "VTV5", "VTV6", "VTV9", "HBO", "STAR MOVIES", "MAX", "Hà Nội 1", "Hà Nội 2", "VTC1", "VTC2",
            "HTV7", "HTV9", "HTV1", "HTV1", "DISNEY", "CARTOON", "VITV", "O2 TV", "DISCOVERY", "ANTV", "VTVCAB1",
            "VTVCAB2", "STAR WORLD HD", "VOV", "K+1", "K+NS", "NATIONAL GEOGRAPHIC",
            "MTV", "ITV"));
    private static long ONE_DAY = 24 * 60 * 60 * 1000;
    private MovieFlyService mfs;
    // Check lai
    private static Set<String> filmChannel = new TreeSet<>(Arrays.asList("hbo", "max", "star movies"));
    private static Set<String> filmTitle = new TreeSet<>();

    public static String sendGet(String url) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        // add request header
        HttpResponse response = client.execute(request);

        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    public List<Channel> getChanel(ConjunctionHelper conjunctionHelper) throws Exception {
        List<Channel> channels = new ArrayList<Channel>();
        Document doc = Jsoup.parse(CrawlerHelper.getResponse("http://www.mytv.com.vn/lich-phat-song", "", "GET"));
//        System.out.println(doc.toString());
        Element chanel = doc.getElementById("channelId");
        Set<String> names = new HashSet<>();
        Elements chanElements = chanel.select("option");
        for (Element element : chanElements) {
            Channel channel = new Channel();
            channel.setId(element.val());

            if (element.text().trim().equalsIgnoreCase("star movies hd")) continue;

            String name = conjunctionHelper.getChannelName(element.text().trim());
            if (name != null) {
                if (names.contains(name)) continue;
                else names.add(name);
                channel.setName(name);
            }
            else
                channel.setName(element.text().trim());
            System.out.println("Channel name: " + element.text().trim() + " | " + channel.getName());
            channels.add(channel);
        }

        // FIX BUG: remove hd channel if sd channel exist. Prevent duplicate schedule.
//        List<Channel> toRemove = new ArrayList<>();
//        for (Channel c : channels) {
//            String name = c.getName().trim().toLowerCase();
//            if (name.endsWith("hd")) {
//                String origin = name.substring(0, name.length() -2 ).trim();
//
//                for (Channel c2: channels) {
//                    if (c2.getName().trim().toLowerCase().equals(origin)) {
//
//                    }
//                }
//            }
//        }
        return channels;
    }

    public void doCrawl(TVProgramService tvs, ConjunctionHelper cjh, int numdays) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(
                SpringMongoConfig.class);
        MongoOperations mongoOperations = (MongoOperations) context.getBean("mongoTemplate");
        mfs = new MovieFlyService(mongoOperations);
        System.out.println("Crawling from MYTV");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        List<Channel> channels = getChanel(cjh);
        Date today = new Date();

        for (Channel channel : channels) {
            if (crawlChannels.contains(channel.getName().toUpperCase())) {
                try {
                    System.out.println("Crawling from " + channel.getName());
                    for (int i = 0; i <= numdays; i++) {
                        String date = df.format(new Date(today.getTime() + ONE_DAY * i));
                        List<TVProgram> tvPrograms = crawlChannel(channel, date);

                        tvPrograms = CrawlerHelper.calculateEndTime(tvPrograms);
                        tvPrograms.forEach(tvs::save);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
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
                List<String> typesStr = types.stream().map(ProgramType::toString).collect(Collectors.toList());

                // HBO, CINEMAX, ...
                if (filmChannel.contains(channel.getName().toLowerCase().trim())) {
//                    MovieFly detail = mfs.searchOnImdbByTitle(tvProgram.getTitle().trim());
                    List<MovieFly> details = mfs.findByTitle(tvProgram.getTitle().trim());

                    if (details.size() > 0) {
                        MovieFly detail = details.get(0);

                        if (detail.getImdbRating() > 0) { // code -1.0 indicate no result found on imdb
                            String genre = detail.getGenre();
                            if (genre != null) {
                                typesStr.addAll(format(genre));
                            }

                            String lang = detail.getLanguage();
                            if (lang != null) {
                                typesStr.addAll(format(lang));
                            }

                            String country = detail.getCountry();
                            if (country != null) {
                                typesStr.addAll(format(country));
                            }

                            // Description
                            String description = detail.getPlot();
                            if (description != null) {
                                String oldDesc = tvProgram.getDescription() == null ? "" : tvProgram.getDescription();
                                tvProgram.setDescription(description + "\n" + oldDesc);
                            }
                        }
                    }
                }

                if (typesStr.size() > 0) {
                    tvProgram.setType(String.join(",", typesStr));
                } else {
                    tvProgram.setType(ProgramType.OTHER.toString());
                }
//                System.out.println(tvProgram.toString());

                tvPrograms.add(tvProgram);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tvPrograms;
    }

    private static List<String> format(String text) {
        String[] parts = text.split(",");
        List<String> types = new ArrayList<>();
        for (String part: parts) {
            types.add("film:" + part.trim());
        }
        return types;
    }

    public static void main(String[] args) throws Exception {
    }
}
