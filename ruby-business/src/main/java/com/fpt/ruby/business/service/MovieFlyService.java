package com.fpt.ruby.business.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fpt.ruby.config.SpringMongoConfig;
import com.fpt.ruby.business.helper.HttpHelper;
import com.fpt.ruby.model.MovieFly;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

@Service
public class MovieFlyService {
	private static final String RT_apikey = "squrt6un22xe46uy5wmxej8e";
	private MongoOperations mongoOperations;
	/*public MovieFlyService(MongoOperations mongoOperations){
		this.mongoOperations = mongoOperations;
	}*/
	
	public MovieFlyService(){
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		this.mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
	}
	
	public List<MovieFly> findAll(){
		return mongoOperations.findAll(MovieFly.class);
	}
	
	public List<MovieFly> findByTitle(String title) throws UnsupportedEncodingException{
		Query query = new Query(Criteria.where("title").regex("^" + title + "$","i"));
		List<MovieFly> movieFlies = mongoOperations.find(query, MovieFly.class);
		if  (movieFlies.size() > 0) return movieFlies;
		MovieFly movieFly = searchOnImdbByTitle(title);
		if (movieFly!=null) {
			query = new Query(Criteria.where("title").regex("^.*" + title + ".*","i"));
			movieFlies = mongoOperations.find(query, MovieFly.class);
			if (movieFlies.size() == 0){
				save(movieFly);
			}
			movieFlies.add(movieFly);
		}
		return movieFlies;
	}
	
	public boolean remove (String title, int year){
		Query query = new Query(Criteria.where("title").regex("^" + title + "$","i").and("year").is(year));
		WriteResult res = mongoOperations.remove(query, MovieFly.class);
		if (res != null){
			System.out.println(res);
			return true;
		}
		return false;
	}
	
	public void save(MovieFly movieFly){
		try{
			mongoOperations.save(movieFly);
		}catch(MongoException ex){
			System.out.println("mongo exception");
		}
	}
	
	public void insert(MovieFly movieFly){
		try{
			mongoOperations.insert(movieFly);
		}catch(MongoException ex){
			System.out.println("mongo exception");
		}
	}
	
	public void dropCollection(){
		mongoOperations.dropCollection(MovieFly.class);
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
	
	public  MovieFly searchOnImdbById(String id){
		MovieFly movieFly = new MovieFly();
		String url = "http://www.omdbapi.com/?i=" + id;
		try{
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
			try{
				utilDate = format.parse(jsonImdb.getString("Released"));
			}catch (Exception e){
				e.printStackTrace();
			}
			if (utilDate != null){
				movieFly.setReleased(new Date(utilDate.getTime()));
			}
		}
		catch (Exception ex){
			System.out.println("Exception ex: " + ex.getMessage());
		}
		return movieFly;
	}
	
	public  MovieFly searchOnImdbByTitle(String title) throws UnsupportedEncodingException{
		System.out.println("[Search on imdb by title]: " + title);
		if (title==null) return null;
		MovieFly movieFly = new MovieFly();
		String url = "http://www.omdbapi.com/?t=" + URLEncoder.encode(title,"UTF-8") ;
		try{
			String jsonImdbString = HttpHelper.sendGet(url);
			System.out.println(jsonImdbString);
			JSONObject jsonImdb = new JSONObject(jsonImdbString);
			if (jsonImdb.getString("Response").equals("False")) return null;
			if (!jsonImdb.getString("Title").equals("N/A")) movieFly.setTitle(jsonImdb.getString("Title"));
			if (!jsonImdb.getString("Genre").equals("N/A")) movieFly.setGenre(jsonImdb.getString("Genre"));
			try{
				if (!jsonImdb.getString("Year").equals("N/A")) movieFly.setYear(Integer.parseInt(jsonImdb.getString("Year")));
			}
			catch (Exception ex){
			}
			if (!jsonImdb.getString("Actors").equals("N/A")) movieFly.setActor(jsonImdb.getString("Actors"));
			if (!jsonImdb.getString("Runtime").equals("N/A")) movieFly.setRuntime(jsonImdb.getString("Runtime"));
			if (!jsonImdb.getString("imdbRating").equals("N/A")) movieFly.setImdbRating(Float.parseFloat(jsonImdb.getString("imdbRating")));
			if (!jsonImdb.getString("imdbID").equals("N/A")) movieFly.setImdbId(jsonImdb.getString("imdbID"));
			if (!jsonImdb.getString("Writer").equals("N/A")) movieFly.setWriter(jsonImdb.getString("Writer"));
			if (!jsonImdb.getString("Awards").equals("N/A")) movieFly.setAwards(jsonImdb.getString("Awards"));
			if (!jsonImdb.getString("Country").equals("N/A")) movieFly.setCountry(jsonImdb.getString("Country"));
			if (!jsonImdb.getString("Language").equals("N/A")) movieFly.setLanguage(jsonImdb.getString("Language"));
			if (!jsonImdb.getString("Director").equals("N/A")) movieFly.setDirector(jsonImdb.getString("Director"));
			if (!jsonImdb.getString("Plot").equals("N/A")) movieFly.setPlot(jsonImdb.getString("Plot"));
			if (!jsonImdb.getString("Released").equals("N/A")){
				SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
				java.util.Date utilDate = format.parse(jsonImdb.getString("Released"));
				movieFly.setReleased(new Date(utilDate.getTime()));
			}
		}
		catch (Exception ex){
			System.out.println("Exception ex: " + ex.getMessage());
			return null;
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
	
	public List<MovieFly> findByListTitle(List<String> titles) throws UnsupportedEncodingException {
		List<MovieFly> movieFlies = new ArrayList<MovieFly>();
		
		for (String t : titles){
			movieFlies.addAll(findByTitle(t));
		}
		return movieFlies;
	}
	
	private List<MovieFly> getAllMovieFrom2013(){
		Query query = new Query(Criteria.where("year").gt(2013));
		return mongoOperations.find(query, MovieFly.class);
	}
	
	public static void main(String[] args) throws Exception {
		MovieFlyService movieFlyService = new MovieFlyService();
		//List<MovieFly> movieFlies = movieFlyService.getAllMovieFrom2013();
		/*List<MovieFly> movieFlies = movieFlyService.findByTitle("the damned");
		for (MovieFly movieFly : movieFlies) {
			System.out.println("Title: " + movieFly.getTitle() + " | " + movieFly.getYear()  + " | " + movieFly.getGenre());
		}*/
		MovieFly movieFly = new MovieFly();
		movieFly.setTitle("test111");
		movieFlyService.save(movieFly);
		System.out.println("DONE");
		/*MovieFlyService movieFlyService = new MovieFlyService();
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		MovieService movieService = (MovieService) context.getBean("movieService");
		//movieFlyService.dropCollection();
		List<Movie> movies = movieService.findAll();
		int  count = 0;
		for (Movie movie : movies){
			if (movie.getId()>5878){
				System.out.println(movie.getId() + " | "  +  movie.getTitle() + " | " + movie.getOriginal_title());
				MovieFly movieFly = movieFlyService.searchOnImdbByTitle(movie.getTitle());
				if (movieFly == null) {
					continue;
				}
				count ++;
				if (movieFly != null) movieFlyService.save(movieFly);
			}
			
		}*/
	}

}
