package com.fpt.ruby.business.service;

import com.fpt.ruby.business.config.SpringMongoConfig;
import com.fpt.ruby.business.model.TVModifiers;
import com.fpt.ruby.business.model.TVProgram;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class TVProgramService {
    private static long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
    private static long ONE_DAY = 24 * 60 * 60 * 1000;
    private MongoOperations mongoOperations;

    public TVProgramService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public TVProgramService() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(
                SpringMongoConfig.class);
        this.mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
    }

    public static void main(String[] args) throws Exception {
        List<String> t = new ArrayList<String>(Arrays.asList("sport", "footBALL ", "entertainment"));
        System.out.println(genRegex(t));
//        TVProgramService tvService = new TVProgramService();
//        ApplicationContext ctx = new AnnotationConfigApplicationContext(
//                SpringMongoConfig.class);
//        MongoOperations mongoOperations = (MongoOperations) ctx
//                .getBean("mongoTemplate");
//        mongoOperations.findAll(TVProgram.class);
//        System.out.println("DONE");

    }

    private static String genRegex(List<String> types) {
        if (types == null) return "";
        List<String> finedType = new ArrayList<String>();
        types.forEach((t) -> finedType.add(t.toLowerCase().trim()));
        return String.join("|", finedType);
    }

    public void cleanOldData() {
        Date now = new Date(new Date().getTime() - ONE_WEEK);
        Query query = new Query(Criteria.where("start_date").lt(now));
        List<TVProgram> tvPrograms = mongoOperations.find(query,
                TVProgram.class);
        for (TVProgram tvProgram : tvPrograms) {
            System.out.println("TV Program: " + tvProgram.getChannel() + " | "
                    + tvProgram.getTitle() + " | " + tvProgram.getStart_date());
            mongoOperations.remove(tvProgram);
        }
    }

    /*
     * Remove crawled data on specific day parameter: beforeToday 0 => today -1
     * : yester day -i : i day before today 1 : tomorrow i : i day after today
     */
    public void clearDataOnSpecificDay(int beforeToday) {
        Date cur = new Date(new Date().getTime() + beforeToday * ONE_DAY);
        cur.setSeconds(0);
        cur.setMinutes(0);
        cur.setHours(0);

        Date afterCur = new Date(cur.getTime() + ONE_DAY);
        Query query = new Query(Criteria.where("start_date").gte(cur)
                .lt(afterCur));

        System.out.println(query.toString());
        List<TVProgram> toRemove = mongoOperations.find(query, TVProgram.class);
        for (TVProgram prog : toRemove) {
            mongoOperations.remove(prog);
            System.out.println("TV Program: " + prog.getChannel() + " | "
                    + prog.getTitle() + " | " + prog.getStart_date());
        }
    }

    public List<TVProgram> getList(TVModifiers mod) {

        String channel = mod.getChannel();
        if (channel != null) channel = channel.toLowerCase().trim();

        String title = mod.getProg_title();
        if (title != null) title = title.toLowerCase().trim();

        Date start = mod.getStart();
        Date end = mod.getEnd();
        List<String> types = mod.getType();

        if (channel == null || channel.isEmpty()){
            if (title != null && !title.trim().isEmpty())
                return findByTitle(start,end,title);
            if (types != null && types.size() > 0)
                return findByType(start,end,types);
            return findAtPeriod(start,end);
        }
        else{
            if (title != null && !title.trim().isEmpty())
                return findByTitleAndChannel(start, end, title, channel);
            if (types != null && types.size() > 0)
                return findByTypeAndChannel(start, end, types, channel);
            return findByChannel(start,end,channel);
        }


    }

    public List<TVProgram> findAll() {
        return mongoOperations.findAll(TVProgram.class);
    }

    public List<TVProgram> findAtPeriod(Date start, Date end) {
        Query query = new Query();
        if (start == null && end ==null)
            query = new Query(Criteria.where("start_date"));
        else if (start != null && end !=null)
            if (start == end) query = new Query(Criteria.where("start_date").lte(start).and("end_date").gte(start));
            else query = new Query(Criteria.where("start_date").gte(start).lte(end));
        else if (start != null)
            query = new Query(Criteria.where("start_date").gte(start));
        else
            query = new Query(Criteria.where("start_date").lte(end));
        return mongoOperations.find(query, TVProgram.class);
    }

    public List<TVProgram> findByChannel(Date start, Date end,
                                         String channel) {
        Query query = new Query();
        if (start == null && end ==null)
            query = new Query(Criteria.where("channel").is(channel).and("start_date"));
        else if (start != null && end !=null)
            if (start == end) query = new Query(Criteria.where("channel").is(channel).and("start_date").lte(start).and("end_date").gte(start));
            else query = new Query(Criteria.where("channel").is(channel).and("start_date").gte(start).lte(end));
        else if (start != null)
            query = new Query(Criteria.where("channel").is(channel).and("start_date").gte(start));
        else
            query = new Query(Criteria.where("channel").is(channel).and("start_date").lte(end));
        return mongoOperations.find(query, TVProgram.class);
    }

    public List<TVProgram> findByTitle(Date start, Date end,
                                       String title) {
        Query query = new Query();
        if (start == null && end ==null)
            query = new Query(Criteria.where("title").is(title).and("start_date"));
        else if (start != null && end !=null)
            if (start == end) query = new Query(Criteria.where("title").is(title).and("start_date").lte(start).and("end_date").gte(start));
            else query = new Query(Criteria.where("title").is(title).and("start_date").gte(start).lte(end));
        else if (start != null)
            query = new Query(Criteria.where("title").is(title).and("start_date").gte(start));
        else
            query = new Query(Criteria.where("title").is(title).and("start_date").lte(end));
        return mongoOperations.find(query, TVProgram.class);
    }

    public List<TVProgram> findByTitleAndChannel(Date start, Date end,
                                         String title, String channel) {
        Query query = new Query();
        if (start == null && end ==null)
            query = new Query(Criteria.where("title").is(title).and("channel").is(channel).and("start_date"));
        else if (start != null && end !=null)
            if (start == end) query = new Query(Criteria.where("title").is(title).and("channel").is(channel).and("start_date").lte(start).and("end_date").gte(start));
            else query = new Query(Criteria.where("title").is(title).and("channel").is(channel).and("start_date").gte(start).lte(end));
        else if (start != null)
            query = new Query(Criteria.where("title").is(title).and("channel").is(channel).and("start_date").gte(start));
        else
            query = new Query(Criteria.where("title").is(title).and("channel").is(channel).and("start_date").lte(end));
        return mongoOperations.find(query, TVProgram.class);
    }

    public List<TVProgram> findByType(Date start, Date end,
                                       List<String> types) {
        Query query = new Query();
        if (start == null && end ==null)
            query = new Query(Criteria.where("type").regex(genRegex(types)).and("start_date"));
        else if (start != null && end !=null)
            if (start == end) query = new Query(Criteria.where("type").regex(genRegex(types)).and("start_date").lte(start).and("end_date").gte(start));
            else query = new Query(Criteria.where("type").regex(genRegex(types)).and("start_date").gte(start).lte(end));
        else if (start != null)
            query = new Query(Criteria.where("type").regex(genRegex(types)).and("start_date").gte(start));
        else
            query = new Query(Criteria.where("type").regex(genRegex(types)).and("start_date").lte(end));
        return mongoOperations.find(query, TVProgram.class);
    }

    public List<TVProgram> findByTypeAndChannel(Date start, Date end,
                                                 List<String> types, String channel) {
        Query query = new Query();
        if (start == null && end ==null)
            query = new Query(Criteria.where("type").regex(genRegex(types)).and("channel").is(channel).and("start_date"));
        else if (start != null && end !=null){
            if (start == end) query = new Query(Criteria.where("type").regex(genRegex(types)).and("start_date").lte(start).and("end_date").gte(start));
            else query = new Query(Criteria.where("type").regex(genRegex(types)).and("channel").is(channel).and("start_date").gte(start).lte(end));
        }
        else if (start != null)
            query = new Query(Criteria.where("type").regex(genRegex(types)).and("channel").is(channel).and("start_date").gte(start));
        else
            query = new Query(Criteria.where("type").regex(genRegex(types)).and("channel").is(channel).and("start_date").lte(end));
        return mongoOperations.find(query, TVProgram.class);
    }

    public List<TVProgram> findTaggedProgram(Date start, Date end) {
        Query query = new Query(Criteria.where("start_date").gte(start)
                .lte(end).and("types").not().regex("other"));

        List<TVProgram> progs = mongoOperations.find(query, TVProgram.class);
        return progs;
    }

    public void save(TVProgram tvProgram) {
        tvProgram.setTitle(tvProgram.getTitle().toLowerCase().trim()); // lower case to speed-up
        NameMapperService.save(tvProgram); // save to namemapper
        mongoOperations.save(tvProgram);
    }

    public void remove(TVProgram tvProgram) {
        mongoOperations.remove(tvProgram);
    }

    public void dropCollection() {
        mongoOperations.dropCollection(TVProgram.class);
    }

    public List<TVProgram> findProgramToShow(int day) {
        Date date = new Date(new Date().getTime() + day * ONE_DAY);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        Date afterDate = new Date(date.getTime() + ONE_DAY);
        Query query = new Query(Criteria.where("start_date").gt(date).lt(afterDate))
                .with(new Sort(Direction.ASC, "start_date"));
        return mongoOperations.find(query, TVProgram.class);
    }
}