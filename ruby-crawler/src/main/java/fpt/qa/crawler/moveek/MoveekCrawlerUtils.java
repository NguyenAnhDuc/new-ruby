package fpt.qa.crawler.moveek;

import com.fpt.ruby.business.helper.CrawlerHelper;
import org.jsoup.nodes.*;
import jmdn.struct.pair.Pair;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoveekCrawlerUtils {
    static final long MIN = 60 * 1000;
    static final long HOUR = 60 * MIN;

    public static List<Pair<String, Pair<String, String>>> GetMoviesByCinemas(String url, String urlParameters) {
        List<Pair<String, Pair<String, String>>> res = new ArrayList();
        try {
            String response = CrawlerHelper.sendGet(url, urlParameters);

            return parseMovieList(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private static List<Pair<String, Pair<String, String>>> parseMovieList(String response) {
        List<Pair<String, Pair<String,String>>> res = new ArrayList();

        Document doc = Jsoup.parse(response);
        Elements eles = doc.getElementsByTag("li");
        if (eles.size() == 0) return res;

        for (Element e: eles) {
            String encodedTitle, name1, name2;
            encodedTitle = e.attr("data-slug");
            name1 = e.getElementsByClass("title-main").get(0).text().trim();
            name2 = e.getElementsByClass("title-alias").get(0).text().trim();
            res.add(new Pair(encodedTitle, new Pair(name1, name2)));
        }

        return res;
    }

    public static List<Pair<String, Date>> getSessiones(String url, String mov) {
        List<Pair<String, Date>> res = new ArrayList();
        try {
            Document doc = Jsoup.parse(CrawlerHelper.sendGet(url, "p[]=" + mov));

            Elements versions = doc.getElementsByClass("showtime-detail");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            for (Element e: versions) {
                // Crawl date
                Elements dates = e.getElementsByClass("showtime-version");
                if (dates.size() == 0) break;
                String[] part = dates.get(0).text().trim().split(" ");
                Date crawlDay = format.parse(part[part.length - 1]);
                System.out.println(crawlDay);

                Elements showtimeWrapper = e.getElementsByClass("showtime-wrapper");
                for (Element w: showtimeWrapper) {
                    Elements wrapperChildren = w.getElementsByTag("a");
                    if (wrapperChildren.size() == 0) break;
                    String type = wrapperChildren.get(0).text();
                    for (int i = 1; i < wrapperChildren.size(); ++i) {
                        long sessionTime = getTime(wrapperChildren.get(i).text());
                        Date sessionDate = new Date(crawlDay.getTime() + sessionTime);
                        res.add(new Pair<>(type, sessionDate));
                        System.out.println(mov + " " + type + " " + sessionDate.toString());
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(String.format("[ERROR] url = %s; mov = %s; msg = %s", url, mov, ex.getMessage()));
            ex.printStackTrace();
        }

        return res;
    }

    private static long getTime(String timeStr) {
        int idx = timeStr.indexOf(":");
        if (idx < 0) {
            return 0;
        }
        int hour = Integer.parseInt(timeStr.substring(0, idx));
        int mins = Integer.parseInt(timeStr.substring(idx + 1));
        return hour * HOUR + mins * MIN;
    }

    private static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.length() < 14)
            return null;

        int hour = Integer.parseInt(dateStr.substring(0, 2));
        int mins = Integer.parseInt(dateStr.substring(3, 5));
        String date = dateStr.substring(6);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date tmp = format.parse(date);
            return new Date(tmp.getTime() + 60 * 60 * 1000 * hour + 60 * 1000 * mins);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {

    }

}
