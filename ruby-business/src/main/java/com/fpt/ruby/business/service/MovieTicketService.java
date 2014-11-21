package com.fpt.ruby.business.service;

import com.fpt.ruby.business.config.SpringMongoConfig;
import com.fpt.ruby.business.model.MovieTicket;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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

    private static boolean equalDate(Date date1, Date date2) {
        if (date1 == null && date2 == null) return true;
        try {
            if (Math.abs(date1.getTime() - date2.getTime()) < 1000)
                return true;
            else return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public void cleanOldData() {
        Date now = new Date(new Date().getTime() - ONE_WEEK);
        Query query = new Query(Criteria.where("start_date").lt(now));
        List<MovieTicket> movieTickets = mongoOperations.find(query, MovieTicket.class);
        movieTickets.forEach(mongoOperations::remove);
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
        toRemove.forEach(mongoOperations::remove);
    }

    public MovieTicket findById(String ticketId) {
        return mongoOperations.findById(ticketId, MovieTicket.class);
    }

    public List<MovieTicket> findTicketToShow(int day) {
        Date date = new Date(new Date().getTime() + day * ONE_DAY);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        Query query = new Query(Criteria.where("date").gt(date)).with(new Sort(Direction.ASC, "date"));
        List<MovieTicket> results = mongoOperations.find(query, MovieTicket.class);
        return results;
    }

    private List<MovieTicket> findMatch(MovieTicket movieTicket) {
        Query query = new Query();
        query.addCriteria(Criteria.where(MT_MOVIE).regex(movieTicket.getMovie(), "i"));
        query.addCriteria(Criteria.where(MT_CINEMA).regex(movieTicket.getCinema(), "i"));
        query.addCriteria(Criteria.where(MT_TYPE).is(movieTicket.getType()));
        query.addCriteria(Criteria.where(MT_CITY).is(movieTicket.getCity()));
        List<MovieTicket> movieTickets = mongoOperations.find(query, MovieTicket.class);
        List<MovieTicket> results = movieTickets.stream().filter(moTicket -> equalDate(moTicket.getDate(), movieTicket.getDate())).collect(Collectors.toList());
        return results;
    }

    public MovieTicketService() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        this.mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
    }

    public void save(MovieTicket movieTicket) {
        NameMapperService.save(movieTicket);
        mongoOperations.save(movieTicket);
    }

    public void delete(MovieTicket movieTicket) {
        List<MovieTicket> movieTickets = findMatch(movieTicket);
        System.out.println("size: " + movieTickets.size());
        for (MovieTicket moTicket : movieTickets) {
            mongoOperations.remove(moTicket);
        }
    }

    public List<MovieTicket> findAll() {
        return mongoOperations.findAll(MovieTicket.class);
    }

    public List<MovieTicket> findMoviesMatchCondition(MovieTicket matchMovieTicket, Date beforeDate, Date afterDate) {
        long start = System.currentTimeMillis();
        if (beforeDate != null && afterDate != null)
            logger.info("Find Movie Ticket match condition: " + beforeDate.toLocaleString() + " | " + afterDate.toLocaleString());
        Query query = new Query();
        if (beforeDate == null && afterDate == null) {
            System.out.println("[MovieTicketService]: beforeDate-afterDate null");
            if (matchMovieTicket.getCinema() == null && matchMovieTicket.getMovie() == null)
                return  new ArrayList<>();

            else if (matchMovieTicket.getCinema() == null)
                query =  new Query(Criteria.where(MT_MOVIE).regex(matchMovieTicket.getMovie(), "i"));
            else if (matchMovieTicket.getMovie() == null)
                query =  new Query(Criteria.where(MT_CINEMA).is(matchMovieTicket.getCinema()));
            else query = new Query(Criteria.where(MT_MOVIE).regex(matchMovieTicket.getMovie(), "i").and(MT_CINEMA).is(matchMovieTicket.getCinema()));
        }
        else if (beforeDate == null && afterDate != null) {
            if (matchMovieTicket.getCinema() == null && matchMovieTicket.getMovie() == null)
                query =  new Query(Criteria.where(MT_DATE).lte(afterDate));
            else if (matchMovieTicket.getCinema() == null)
                query =  new Query(Criteria.where(MT_MOVIE).regex(matchMovieTicket.getMovie(), "i").and(MT_DATE).lte(afterDate));
            else if (matchMovieTicket.getMovie() == null)
                query =  new Query(Criteria.where(MT_CINEMA).is(matchMovieTicket.getCinema()).and(MT_DATE).lte(afterDate));
            else query = new Query(Criteria.where(MT_MOVIE).regex(matchMovieTicket.getMovie(),"i").and(MT_CINEMA).is(matchMovieTicket.getCinema()).and(MT_DATE).lte(afterDate));
        }
        else if (beforeDate != null && afterDate == null) {
            System.out.println("[MovieTicketService]: beforeDate-null");
            if (matchMovieTicket.getCinema() == null && matchMovieTicket.getMovie() == null)
                query =  new Query(Criteria.where(MT_DATE).gte(beforeDate));
            else if (matchMovieTicket.getCinema() == null)
                query =  new Query(Criteria.where(MT_MOVIE).regex(matchMovieTicket.getMovie(), "i").and(MT_DATE).gte(beforeDate));
            else if (matchMovieTicket.getMovie() == null)
                query =  new Query(Criteria.where(MT_CINEMA).is(matchMovieTicket.getCinema()).and(MT_DATE).gte(beforeDate));
            else query = new Query(Criteria.where(MT_MOVIE).regex(matchMovieTicket.getMovie(), "i").and(MT_CINEMA).is(matchMovieTicket.getCinema()).and(MT_DATE).gte(beforeDate));
        }
        else {
            if (matchMovieTicket.getCinema() == null && matchMovieTicket.getMovie() == null)
                query =  new Query(Criteria.where(MT_DATE).lte(afterDate).gte(beforeDate));
            else if (matchMovieTicket.getMovie() == null)
                query =  new Query(Criteria.where(MT_CINEMA).is(matchMovieTicket.getCinema()).and(MT_DATE).lte(afterDate).gte(beforeDate));
            else if (matchMovieTicket.getCinema() == null)
                query =  new Query(Criteria.where(MT_MOVIE).regex(matchMovieTicket.getMovie(),"i").and(MT_DATE).lte(afterDate).gte(beforeDate));
            else query = new Query(Criteria.where(MT_MOVIE).regex(matchMovieTicket.getMovie(),"i").and(MT_CINEMA).is(matchMovieTicket.getCinema()).and(MT_DATE).lte(afterDate).gte(beforeDate));
        }
        System.out.println("[MovieTicketService] Time query: " + (System.currentTimeMillis() - start));
        return mongoOperations.find(query,MovieTicket.class);
    }

    public boolean existedInDb(MovieTicket movTicket) {
        List<MovieTicket> movieTickets = findMatch(movTicket);
        if (movieTickets.size() == 0) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        MongoOperations mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
        MovieTicket movieTicket = new MovieTicket();
        movieTicket.setCinema("Lotte");
        movieTicket.setCity("Ha Noi");
        //mongoOperations.save(movieTicket);
        List<MovieTicket> movieTickets = mongoOperations.findAll(MovieTicket.class);
        for (MovieTicket movTicket : movieTickets) {
            movTicket.setMovie("Biệt đội đánh thuê");
            mongoOperations.save(movTicket);
        }
        System.out.println("DONE");
    }
}