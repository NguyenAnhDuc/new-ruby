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
import java.util.stream.Collectors;


@Service
public class TVProgramService {
    private static long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
    private static long ONE_DAY = 24 * 60 * 60 * 1000;
    private MongoOperations mongoOperations;
    private final String FIELD_CHANNEL = "channel";
    private final String FIELD_TITLE = "title";
    private final String FIELD_TYPES = "type";
    private final String FIELD_START = "start_date";
    private final String FIELD_END = "end_date";

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
        return findByPatamates(mod);

    }

    public List<TVProgram> findByPatamates(TVModifiers mods){
        Query query = new Query();
        Criteria criteria = new Criteria();
        if (mods.getChannel() != null && !mods.getChannel().isEmpty())
            query.addCriteria(Criteria.where(FIELD_CHANNEL).is(mods.getChannel().toLowerCase()));
        if (mods.getProg_title() != null && !mods.getProg_title().isEmpty())
            query.addCriteria(Criteria.where(FIELD_TITLE).is(mods.getProg_title().toLowerCase()));
        if (mods.getProg_title() == null && mods.getType() != null && mods.getType().size() > 0)
            query.addCriteria(Criteria.where(FIELD_TYPES).regex(genRegex(mods.getType())));
        if (mods.getStart() != null && mods.getEnd() != null && mods.getStart() == mods.getEnd())
            query.addCriteria(Criteria.where(FIELD_START).lte(mods.getStart()).and(FIELD_END).gte(mods.getStart()));
        else if (mods.getStart() != null && mods.getEnd() != null)
            query.addCriteria(Criteria.where(FIELD_START).gte(mods.getStart()).lte(mods.getEnd()));
        else if (mods.getStart() != null)
            query.addCriteria(Criteria.where(FIELD_START).gte(mods.getStart()).and(FIELD_END).gte(mods.getStart()));
        else if (mods.getEnd() != null)
            query.addCriteria(Criteria.where(FIELD_START).lte(mods.getEnd()).and(FIELD_END).lte(mods.getEnd()));
        System.out.println("[TVPROGRAMSERVICE]: Query"+ query.toString());
        return mongoOperations.find(query,TVProgram.class);
    }

    public List<TVProgram> findAll() {
        return mongoOperations.findAll(TVProgram.class);
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