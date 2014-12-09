package fpt.qa.crawler.moveek;

import com.fpt.ruby.commons.entity.movie.MovieTicket;
import com.fpt.ruby.commons.service.MovieFlyService;
import com.fpt.ruby.commons.service.MovieTicketService;
import fpt.qa.config.SpringMongoConfig;
import jmdn.struct.pair.Pair;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class MoveekCrawler {

    private static HashMap<String, String> movie_urls = new HashMap<String, String>();
    private static HashMap<String, String> showtime_urls = new HashMap<String, String>();
    private static HashMap<String, String> cin_cities = new HashMap<String, String>();

    static {
        String dir = "./classes/dicts/"; // TODO: correct default dir to allow run singly.

        try {
            dir = (new MoveekCrawler()).getClass().getClassLoader().getResource("").getPath() + "/moveek";
        } catch (Exception ex) {
            System.out.println("Error init Moveek Crawler: " + ex.getMessage());
        } finally {
            System.out.println("Scan dir = " + dir);
        }

        loadMovieUrls(dir);
        loadshowTimeUrls(dir);
        loadCinCity(dir);
    }

    public static void doCrawl(MovieTicketService mts) {
        ApplicationContext context = new AnnotationConfigApplicationContext(
                SpringMongoConfig.class);
        MovieFlyService mfs = new MovieFlyService();
        Object[] keys = movie_urls.keySet().toArray();
        Set<String> movieNames = new HashSet<>();

        for (Object key : keys) {
            String cinName = (String) key;
            String city = cin_cities.get(cinName);

            List<Pair<String, Pair<String, String>>> movies = MoveekCrawlerUtils.GetMoviesByCinemas(movie_urls.get(cinName), "");
            for (Pair<String, Pair<String, String>> movie : movies) {
                System.err.println("CINAME: " + cinName);
                List<Pair<String, Date>> slots;
                slots = MoveekCrawlerUtils.getSessiones(showtime_urls.get(cinName), movie.first);

                for (Pair<String, Date> slot : slots) {
                    MovieTicket newTicket = new MovieTicket();
                    newTicket.setType(slot.first);
                    newTicket.setDate(slot.second);
                    newTicket.setCinema(cinName);

                    newTicket.setCity(city);
                    newTicket.setMovie(movie.second.first);
                    newTicket.setAlias(movie.second.second);
                    if (!mts.existedInDb(newTicket)) {
                        mts.save(newTicket);
                    }
//                    if (!mts.matchTitle(newTicket.getMovie().toLowerCase())) {
//                        mts.save(newTicket);
//                    }
                }

                movieNames.add(movie.second.first);
            }
        }

        // save moviefly
        for (String name : movieNames) {
            try {
                mfs.findByTitle(name);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadMovieUrls(String dir) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dir + "/moveek_movie_url.txt"));
            /*new InputStreamReader(
                    new FileInputStream(dir + "/moveek_movie_url.txt"), "UTF8")*/

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Line: " + line);
                int idx = line.indexOf("\t");
                if (idx < 0 || line.isEmpty()) {
                    continue;
                }
                String name = line.substring(0, idx);
                String url = line.substring(idx + 1);
                movie_urls.put(name, url);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void loadshowTimeUrls(String dir) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dir + "/moveek_showtime_url.txt"));
            //BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dir + "/moveek_showtime_url.txt"),"UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                int idx = line.indexOf("\t");
                if (idx < 0 || line.isEmpty()) {
                    continue;
                }

                String name = line.substring(0, idx);
                String url = line.substring(idx + 1);
                showtime_urls.put(name, url);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void loadCinCity(String dir) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dir + "/moveek_city.txt"));

            String line;
            while ((line = reader.readLine()) != null) {
                int idx = line.indexOf("\t");
                if (idx < 0 || line.isEmpty()) {
                    continue;
                }

                String name = line.substring(0, idx);
                String url = line.substring(idx + 1);
                cin_cities.put(name, url);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //doCrawl(new MovieTicketService());
    }
}
