package com.fpt.ruby.business.service;

import com.fpt.ruby.business.config.SpringMongoConfig;
import com.fpt.ruby.business.model.MovieTicket;
import com.fpt.ruby.business.template.MovieModifiers;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
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

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class MovieTicketService {
    private static long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
    private static long ONE_DAY = 24 * 60 * 60 * 1000;
    private static long ONE_HOUR =  60 * 60 * 1000;
    private static long ONE_MINUTE =  60 * 1000;
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

    public List<MovieTicket> filterMoviesMatchCondition(MovieModifiers match, Date beforeDate, Date afterDate){
        List<MovieTicket> movieTickets = getAllTickets();
        if (match.getCinName() != null && !match.getCinName().isEmpty())
            movieTickets = movieTickets.stream().filter(t->t.getCinema().equalsIgnoreCase(match.getCinName())).collect(Collectors.toList());
        if (match.getMovieTitle() != null && !match.getMovieTitle().isEmpty())
            movieTickets = movieTickets.stream().filter(t->t.getMovie().equalsIgnoreCase(match.getMovieTitle())).collect(Collectors.toList());
        if (beforeDate != null && afterDate != null)
            movieTickets = movieTickets.stream().filter(t->
                    t.getDate().after(beforeDate) && t.getDate().before(afterDate)).collect(Collectors.toList());
        else if (beforeDate != null)
            movieTickets = movieTickets.stream().filter(t->t.getDate().after(beforeDate)).collect(Collectors.toList());
        else if (afterDate != null)
            movieTickets = movieTickets.stream().filter(t->t.getDate().before(afterDate)).collect(Collectors.toList());
        return movieTickets;
    }

    public List<MovieTicket> filterMoviesMatchCondition(MovieTicket match, Date beforeDate, Date afterDate){
        List<MovieTicket> movieTickets = getAllTickets();
        if (match.getCinema() != null && !match.getCinema().isEmpty())
            movieTickets = movieTickets.stream().filter(t->t.getCinema().equalsIgnoreCase(match.getCinema())).collect(Collectors.toList());
        if (match.getMovie() != null && !match.getMovie().isEmpty())
            movieTickets = movieTickets.stream().filter(t->t.getMovie().equalsIgnoreCase(match.getMovie())).collect(Collectors.toList());
        if (beforeDate != null && afterDate != null)
            movieTickets = movieTickets.stream().filter(t->
                    t.getDate().after(beforeDate) && t.getDate().before(afterDate)).collect(Collectors.toList());
        else if (beforeDate != null)
            movieTickets = movieTickets.stream().filter(t->t.getDate().after(beforeDate)).collect(Collectors.toList());
        else if (afterDate != null)
            movieTickets = movieTickets.stream().filter(t->t.getDate().before(afterDate)).collect(Collectors.toList());
        return movieTickets;
    }

    public List<MovieTicket> findMoviesMatchCondition(MovieTicket match, Date beforeDate, Date afterDate) {
        long start = System.currentTimeMillis();
        Query query = new Query();
        if (match.getCinema() != null && !match.getCinema().isEmpty())
            query.addCriteria(Criteria.where(MT_CINEMA).regex(match.getCinema(),"i"));
        if (match.getMovie() != null && !match.getMovie().isEmpty())
            query.addCriteria(Criteria.where(MT_MOVIE).regex(match.getMovie(),"i"));
        if (beforeDate != null && afterDate != null)
            query.addCriteria(Criteria.where(MT_DATE).gte(beforeDate).lte(afterDate));
        else if (beforeDate != null)
            query.addCriteria(Criteria.where(MT_DATE).gte(beforeDate));
        else if (afterDate != null)
            query.addCriteria(Criteria.where(MT_DATE).lte(afterDate));
        System.out.println("[MovieTicketService] Time query: " + (System.currentTimeMillis() - start));
        return mongoOperations.find(query,MovieTicket.class);
    }

    public boolean matchTitle(String title) {
        Query q = new Query(Criteria.where("title").regex("^" + title + "$", "i"));
        List<MovieTicket> x = mongoOperations.find(q, MovieTicket.class);
        return x.size() > 0;
    }

    public boolean existedInDb(MovieTicket movTicket) {
        List<MovieTicket> movieTickets = findMatch(movTicket);
        if (movieTickets.size() == 0) {
            return false;
        }
        return true;
    }

    private Supplier<List<MovieTicket>> allTickets = Suppliers.memoizeWithExpiration(
            new Supplier<List<MovieTicket>>() {
                public List<MovieTicket> get() {
                    return findAll();
                }
            }, ONE_HOUR, TimeUnit.MILLISECONDS);

    public List<MovieTicket> getAllTickets() {
        return allTickets.get();
    }
}
