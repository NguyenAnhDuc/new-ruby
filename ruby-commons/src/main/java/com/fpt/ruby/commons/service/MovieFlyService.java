package com.fpt.ruby.commons.service;

import com.fpt.ruby.commons.entity.movie.MovieFly;
import com.fpt.ruby.commons.helper.HttpHelper;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieFlyService {
    private static final String RT_apikey = "squrt6un22xe46uy5wmxej8e";
    @Autowired
    private MongoOperations mongoOperations;
    public MovieFlyService(MongoOperations mongoOperations){
        this.mongoOperations = mongoOperations;
	}


    public static void main(String[] args) throws Exception {
       /* MovieFlyService movieFlyService = new MovieFlyService();
        MovieFly movieFly = new MovieFly();
        movieFly.setTitle("test111");
        movieFlyService.save(movieFly);
        System.out.println("DONE");*/
    }

    // TODO: should rewrite in smart way.
    public static String forRegex(String aRegexFragment) {
        final StringBuilder result = new StringBuilder();

        final StringCharacterIterator iterator =
                new StringCharacterIterator(aRegexFragment);
        char character = iterator.current();
        while (character != CharacterIterator.DONE) {
      /*
       All literals need to have backslashes doubled.
      */
            if (character == '.') {
                result.append("\\.");
            } else if (character == '\\') {
                result.append("\\\\");
            } else if (character == '?') {
                result.append("\\?");
            } else if (character == '*') {
                result.append("\\*");
            } else if (character == '+') {
                result.append("\\+");
            } else if (character == '&') {
                result.append("\\&");
            } else if (character == ':') {
                result.append("\\:");
            } else if (character == '{') {
                result.append("\\{");
            } else if (character == '}') {
                result.append("\\}");
            } else if (character == '[') {
                result.append("\\[");
            } else if (character == ']') {
                result.append("\\]");
            } else if (character == '(') {
                result.append("\\(");
            } else if (character == ')') {
                result.append("\\)");
            } else if (character == '^') {
                result.append("\\^");
            } else if (character == '$') {
                result.append("\\$");
            } else {
                //the char is not a special one
                //add it to the result as is
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }

    public List<MovieFly> findAll() {
        return mongoOperations.findAll(MovieFly.class);
    }

//    public boolean findAndUpdate(String title) {
//        String title =
//
//    }

    public List<MovieFly> findByTitle(String title2) throws UnsupportedEncodingException {
        // if title is null
        if (title2 == null || title2.isEmpty()) {
            return mongoOperations.findAll(MovieFly.class);
        }

        String title = forRegex(title2);
        Query query = new Query(Criteria.where("title").regex("^" + title + "$", "i")); // incase-sensitive match
        List<MovieFly> movieFlies = mongoOperations.find(query, MovieFly.class);

        if (movieFlies.size() > 0) return movieFlies;
        MovieFly movieFly = searchOnImdbByTitle(title);
        if (movieFly != null) {
            save(movieFly);
            if (!movieFly.getTitle().equalsIgnoreCase(title2)) {
                movieFly.setTitle(title2);
                save(movieFly);
            }

//            query = new Query(Criteria.where("title").regex("^" + title + "$", "i"));
//            movieFlies = mongoOperations.find(query, MovieFly.class);
//            if (movieFlies.size() == 0) {
//                MovieFly m2 = movieFly;
//                m2.setTitle(title2);
//                save(m2);
//            }
            movieFlies.add(movieFly);
        } else {
            // save with no result code. It inditcates that no result found on imdb.
            MovieFly cur = new MovieFly();
            cur.setTitle(title2);
            cur.setImdbRating((float) -1.0);
            mongoOperations.save(cur);
            System.out.println("save to prevent future query on imdb. Title = " + title);
        }

        return movieFlies;
    }

    public boolean remove(String title, int year) {
        Query query = new Query(Criteria.where("title").regex("^" + title + "$", "i").and("year").is(year));
        WriteResult res = mongoOperations.remove(query, MovieFly.class);
        if (res != null) {
            System.out.println(res);
            return true;
        }
        return false;
    }

    public void save(MovieFly movieFly) {
        try {
            mongoOperations.save(movieFly);
        } catch (MongoException ex) {
            System.out.println("mongo exception");
        }
    }

    public void insert(MovieFly movieFly) {
        try {
            mongoOperations.insert(movieFly);
        } catch (MongoException ex) {
            System.out.println("mongo exception");
        }
    }
	
	/*private static List<String> searchOnRT(String title) throws UnsupportedEncodingException{
		List<String> imdbIds = new ArrayList<String>();
		String url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q="+URLEncoder.encode(title,"UTF-8")
					+"&apikey=" + RT_apikey;
		try{
			String jsonRTString = HttpHelper.sendGet(url);
			JSONObject jsonRT = new JSONObject(jsonRTString);
			JSONArray movies = jsonRT.getJSONArray("movies");
			for (int i=0;i<movies.length();i++){
				JSONObject movieJson = movies.getJSONObject(i);
				if (!movieJson.toString().contains("alternate_ids")){
					continue;
				}
				imdbIds.add("tt"+movieJson.getJSONObject("alternate_ids").getString("imdb"));
			}
		}
		catch (Exception ex){
			System.out.println("Exception: " + ex.getMessage());
		}
		return imdbIds;
	}*/

    public void dropCollection() {
        mongoOperations.dropCollection(MovieFly.class);
    }

    public MovieFly searchOnImdbById(String id) {
        MovieFly movieFly = new MovieFly();
        String url = "http://www.omdbapi.com/?i=" + id;
        try {
            String jsonImdbString = HttpHelper.sendGet(url);
            System.out.println(jsonImdbString);
            JSONObject jsonImdb = new JSONObject(jsonImdbString);
            movieFly.setTitle(jsonImdb.getString("Title"));
            movieFly.setGenre(jsonImdb.getString("Genre"));
            movieFly.setYear(!jsonImdb.getString("Year").equals("N/A") ? Integer.parseInt(jsonImdb.getString("Year")) : null);
            movieFly.setActor(jsonImdb.getString("Actors"));
            movieFly.setRuntime(jsonImdb.getString("Runtime"));
            movieFly.setImdbRating(!jsonImdb.getString("imdbRating").equals("N/A") ? Float.parseFloat(jsonImdb.getString("imdbRating")) : null);
            movieFly.setImdbId(jsonImdb.getString("imdbID"));
            movieFly.setWriter(jsonImdb.getString("Writer"));
            movieFly.setAwards(jsonImdb.getString("Awards"));
            movieFly.setCountry(jsonImdb.getString("Country"));
            movieFly.setLanguage(jsonImdb.getString("Language"));
            movieFly.setDirector(jsonImdb.getString("Director"));
            movieFly.setPlot(jsonImdb.getString("Plot"));

            SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
            java.util.Date utilDate = null;
            try {
                utilDate = format.parse(jsonImdb.getString("Released"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (utilDate != null) {
                movieFly.setReleased(new Date(utilDate.getTime()));
            }
        } catch (Exception ex) {
            System.out.println("Exception ex: " + ex.getMessage());
        }
        return movieFly;
    }
	
	/*public List<MovieFly> searchOnImdb(String title) throws UnsupportedEncodingException {
		List<MovieFly> movieFlies = new ArrayList<MovieFly>();
		List<MovieFly> inDb = findByTitle(title);
		if (!inDb.isEmpty()){
			return inDb;
		}
		List<String> imdbIds = searchOnRT(title);
		for (String imdbId : imdbIds){
			MovieFly movieFly = searchOnImdbById(imdbId);
			String imdbTitle = movieFly.getTitle().toLowerCase();
			String searchTitle = title.toLowerCase();
			if (imdbTitle.contains(searchTitle)){
				movieFlies.add(movieFly);
				save(movieFly);
			}
		}
		return movieFlies;
	}*/

    public MovieFly searchOnImdbByTitle(String title) throws UnsupportedEncodingException {
        System.out.println("[Search on imdb by title]: " + title);
        if (title == null) return null;
        MovieFly movieFly = new MovieFly();
        String url = "http://www.omdbapi.com/?t=" + URLEncoder.encode(title, "UTF-8");
        try {
            String jsonImdbString = HttpHelper.sendGet(url);
            System.out.println(jsonImdbString);
            JSONObject jsonImdb = new JSONObject(jsonImdbString);
            if (jsonImdb.getString("Response").equals("False")) return null;
            if (!jsonImdb.getString("Title").equals("N/A")) movieFly.setTitle(jsonImdb.getString("Title"));
            if (!jsonImdb.getString("Genre").equals("N/A")) movieFly.setGenre(jsonImdb.getString("Genre"));
            try {
                if (!jsonImdb.getString("Year").equals("N/A"))
                    movieFly.setYear(Integer.parseInt(jsonImdb.getString("Year")));
            } catch (Exception ex) {
            }
            if (!jsonImdb.getString("Actors").equals("N/A")) movieFly.setActor(jsonImdb.getString("Actors"));
            if (!jsonImdb.getString("Runtime").equals("N/A")) movieFly.setRuntime(jsonImdb.getString("Runtime"));
            if (!jsonImdb.getString("imdbRating").equals("N/A"))
                movieFly.setImdbRating(Float.parseFloat(jsonImdb.getString("imdbRating")));
            if (!jsonImdb.getString("imdbID").equals("N/A")) movieFly.setImdbId(jsonImdb.getString("imdbID"));
            if (!jsonImdb.getString("Writer").equals("N/A")) movieFly.setWriter(jsonImdb.getString("Writer"));
            if (!jsonImdb.getString("Awards").equals("N/A")) movieFly.setAwards(jsonImdb.getString("Awards"));
            if (!jsonImdb.getString("Country").equals("N/A")) movieFly.setCountry(jsonImdb.getString("Country"));
            if (!jsonImdb.getString("Language").equals("N/A")) movieFly.setLanguage(jsonImdb.getString("Language"));
            if (!jsonImdb.getString("Director").equals("N/A")) movieFly.setDirector(jsonImdb.getString("Director"));
            if (!jsonImdb.getString("Plot").equals("N/A")) movieFly.setPlot(jsonImdb.getString("Plot"));
            if (!jsonImdb.getString("Released").equals("N/A")) {
                SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
                java.util.Date utilDate = format.parse(jsonImdb.getString("Released"));
                movieFly.setReleased(new Date(utilDate.getTime()));
            }
        } catch (Exception ex) {
            System.out.println("Exception ex: " + ex.getMessage());
            return null;
        }
        return movieFly;
    }

    public List<MovieFly> findByListTitle(List<String> titles) throws UnsupportedEncodingException {
        List<MovieFly> movieFlies = new ArrayList<MovieFly>();

        for (String t : titles) {
            movieFlies.addAll(findByTitle(t));
        }
        return movieFlies;
    }

    public boolean matchTitle(String title) {
        Query q = new Query(Criteria.where("title").regex("^" + title + "$", "i"));
        List<MovieFly> x = mongoOperations.find(q, MovieFly.class);
        return x.size() > 0;
    }

    private List<MovieFly> getAllMovieFrom2013() {
        Query query = new Query(Criteria.where("year").gt(2013));
        return mongoOperations.find(query, MovieFly.class);
    }

}