package com.fpt.ruby.business.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fpt.ruby.business.config.SpringMongoConfig;
import com.fpt.ruby.business.model.MovieTicket;
import com.fpt.ruby.business.model.TVProgram;


@Service
public class MovieTicketService {
	private static long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
	private static long ONE_DAY = 24 * 60 * 60 * 1000;
	private static final Logger logger = LoggerFactory.getLogger(MovieTicketService.class);
	private final String MT_MOVIE = "movie";
	private final String MT_CINEMA = "cinema";
	private final String MT_DATE = "date";
	private final String MT_TYPE = "type";
	private final String MT_CITY = "city";
	private MongoOperations mongoOperations;
	
	private static boolean equalDate(Date date1, Date date2){
		if (date1 == null && date2 == null) return true;
		try{
			if (Math.abs(date1.getTime() - date2.getTime()) < 1000)
				return true;
			else return false;
		}
		catch (Exception ex){
			return false;
		}
	}
	
	public void cleanOldData(){
		Date now = new Date(new Date().getTime() - ONE_WEEK);
		Query query = new Query(Criteria.where("start_date" ).lt( now ));
		List<MovieTicket> movieTickets = mongoOperations.find(query, MovieTicket.class);
		for (MovieTicket movieTicket : movieTickets){
			mongoOperations.remove(movieTicket);
		}
	}
	
	public void clearDataOnSpecificDay(int beforeToday) {
		Date cur = new Date(new Date().getTime() + beforeToday * ONE_DAY);
		cur.setSeconds(0);
		cur.setMinutes(0);
		cur.setHours(0);
		
		Date afterCur = new Date(cur.getTime() + ONE_DAY);
		Query query = new Query(Criteria.where("start_date").gte(cur).lt(afterCur));
		
		System.out.println(query.toString());
		List<MovieTicket> toRemove = mongoOperations.find(query, MovieTicket.class);
		for (MovieTicket mt : toRemove) {
			mongoOperations.remove(mt);
		}
	}
	
	public MovieTicket findById(String ticketId){
		return mongoOperations.findById(ticketId, MovieTicket.class);
	}
	
	public List<MovieTicket> findTicketToShow(int day){
		Date date = new Date(new Date().getTime() + day * ONE_DAY);
		date.setHours(0);date.setMinutes(0);date.setSeconds(0);
		Query query = new Query(Criteria.where("date").gt( date )).with( new Sort( Direction.ASC, "date" ) );
		List<MovieTicket> results = mongoOperations.find(query, MovieTicket.class);
		return results;
	}
	
	private List<MovieTicket> findMatch(MovieTicket movieTicket){
		Query query = new Query();
		query.addCriteria(Criteria.where(MT_MOVIE).regex("^" + movieTicket.getMovie() + "$","i"));
		query.addCriteria(Criteria.where(MT_CINEMA).regex("^" + movieTicket.getCinema() + "$","i"));
		query.addCriteria(Criteria.where(MT_TYPE).is(movieTicket.getType()));
		query.addCriteria(Criteria.where(MT_CITY).is(movieTicket.getCity()));
		List<MovieTicket> movieTickets = mongoOperations.find(query,MovieTicket.class);
		List<MovieTicket> results = new ArrayList<MovieTicket>();
		for (MovieTicket moTicket : movieTickets){
			if (equalDate(moTicket.getDate(), movieTicket.getDate()))
				results.add(moTicket);
		}
		return results;
	}
	
	/*public MovieTicketService(MongoOperations mongoOperations){
		this.mongoOperations = mongoOperations;
	}*/
	
	public MovieTicketService(){
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		this.mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
	}
	
	public void save(MovieTicket movieTicket){
		mongoOperations.save(movieTicket);
	}
	
	/*public void update(MovieTicket movieTicket){
		List<MovieTicket> movieTickets = findMatch(movieTicket);
		for (MovieTicket moTicket : movieTickets){
			mongoOperations.
		}
	}*/
	
	public void delete(MovieTicket movieTicket){
		List<MovieTicket> movieTickets = findMatch(movieTicket);
		System.out.println("size: " + movieTickets.size());
		for (MovieTicket moTicket : movieTickets){
			mongoOperations.remove(moTicket);
		}
	}
	
	public List<MovieTicket> findAll(){
		return mongoOperations.findAll(MovieTicket.class);
	}
	
	public List<MovieTicket> findMoviesMatchCondition(MovieTicket matchMovieTicket,Date beforeDate, Date afterDate){
		if( beforeDate != null && afterDate != null )
			logger.info( "Find Movie Ticket match condition: " + beforeDate.toLocaleString() + " | " + afterDate.toLocaleString() );
		List<MovieTicket> movieTickets = mongoOperations.findAll(MovieTicket.class);
		List<MovieTicket> results = new ArrayList<MovieTicket>();
		List<MovieTicket> matches = new ArrayList<MovieTicket>();
		for (MovieTicket movieTicket : movieTickets){
			if ( (matchMovieTicket.getCinema() == null || (movieTicket.getCinema().toLowerCase().contains(matchMovieTicket.getCinema().toLowerCase()))) 
			 &&	 (matchMovieTicket.getCity() == null || (movieTicket.getCity().equals(matchMovieTicket.getCity())))
			 &&  (matchMovieTicket.getMovie() == null || (movieTicket.getMovie().toLowerCase().contains(matchMovieTicket.getMovie().toLowerCase())))
			 &&  (matchMovieTicket.getType() == null || (movieTicket.getType().equals(matchMovieTicket.getType())))
			 &&  (matchMovieTicket.getDate() == null || (movieTicket.getDate().equals(matchMovieTicket.getDate()))) )
		    matches.add(movieTicket);
		}
		if (beforeDate == null && afterDate == null){
			return matches;
		}
		else if (beforeDate == null && afterDate != null){
			for (MovieTicket movieTicket : matches){
				if (movieTicket.getDate().before(afterDate))
					results.add(movieTicket);
			}
			return results;
		}
		else if (beforeDate != null && afterDate == null ){
			for (MovieTicket movieTicket : matches){
				if (movieTicket.getDate().after(beforeDate))
					results.add(movieTicket);
			}
			return results;
		}
		else {
			for (MovieTicket movieTicket : matches){
				if (movieTicket.getDate() != null)
				{
					logger.debug(movieTicket.getDate().toLocaleString() + " | " + beforeDate.toLocaleString()
							+ " | " + afterDate.toLocaleString() + " | " + (movieTicket.getDate().before(afterDate) && movieTicket.getDate().after(beforeDate)) );
					if (movieTicket.getDate().before(afterDate) && movieTicket.getDate().after(beforeDate))
						results.add(movieTicket);
				}
			}
			return results;
		}
		
	}
	
	public boolean existedInDb(MovieTicket movTicket){
		List<MovieTicket> movieTickets = findMatch(movTicket);
		if (movieTickets.size() == 0){
			return false;
		}
		return true;
	}
	
	public static void main(String[] args){
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		MongoOperations mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
		MovieTicket movieTicket = new MovieTicket();
		movieTicket.setCinema("Lotte");
		movieTicket.setCity("Ha Noi");
		//mongoOperations.save(movieTicket);
		List<MovieTicket> movieTickets = mongoOperations.findAll(MovieTicket.class);
		for (MovieTicket movTicket : movieTickets){
			movTicket.setMovie("Biệt đội đánh thuê");
			mongoOperations.save(movTicket);
		}
		System.out.println("DONE");
	}
}
