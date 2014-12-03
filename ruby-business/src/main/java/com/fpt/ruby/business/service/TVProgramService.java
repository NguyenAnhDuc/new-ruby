package com.fpt.ruby.business.service;

import com.fpt.ruby.business.config.SpringMongoConfig;
import com.fpt.ruby.business.model.TVModifiers;
import com.fpt.ruby.business.model.TVProgram;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
public class TVProgramService {
    private final static Comparator<TVProgram> START_CMP
            = (o1, o2) -> o1.getStart_date().compareTo(o2.getStart_date());
    private static long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
    private static long ONE_DAY = 24 * 60 * 60 * 1000;
    private static int ONE_HOUR = 60*60*1000;
    private static int ONE_MINUTE = 60*1000;
    private final String FIELD_CHANNEL = "channel";
    private final String FIELD_TITLE = "title";
    private final String FIELD_TYPES = "type";
    private final String FIELD_START = "start_date";
    private final String FIELD_END = "end_date";
    private MongoOperations mongoOperations;
    private static final Logger logger = Logger.getLogger(TVProgramService.class.getName());

    public TVProgramService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }
    private List<TVProgram> all;
    public TVProgramService() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(
                SpringMongoConfig.class);
        this.mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
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
        return filterByParamaters(mod);
        //return findByParamaters(mod);
    }

    public List<TVProgram> filterByParamaters(TVModifiers mods){
        System.out.println("Dinh Xuan Thuc la thang cho");
        long start = System.currentTimeMillis();
        List<TVProgram> tvPrograms = getAllTVPrograms();
        tvPrograms = tvPrograms.stream().filter(t->t.getStart_date() != null && t.getEnd_date() != null).collect(Collectors.toList());
        if (mods.getChannel() != null && !mods.getChannel().isEmpty())
            tvPrograms = tvPrograms.stream().filter(t->t.getChannel().equalsIgnoreCase(mods.getChannel())).collect(Collectors.toList());
        if (mods.getProg_title() != null && !mods.getProg_title().isEmpty())
            tvPrograms = tvPrograms.stream().filter(t->t.getTitle().equalsIgnoreCase(mods.getProg_title())).collect(Collectors.toList());
        if (mods.getProg_title() == null && mods.getType() != null && mods.getType().size() > 0)
            tvPrograms = tvPrograms.stream().filter(t->t.getType().matches(genRegex(mods.getType()))).collect(Collectors.toList());

        if (mods.getStart() != null && mods.getEnd() != null && mods.getStart() == mods.getEnd())
            tvPrograms = tvPrograms.stream().filter(t->
                    t.getStart_date().before(mods.getStart()) && t.getEnd_date().after(mods.getStart())).collect(Collectors.toList());
        else if (mods.getStart() != null && mods.getEnd() != null)
            tvPrograms = tvPrograms.stream().filter(t->
                        (t.getStart_date().after(mods.getStart()) && t.getStart_date().before(mods.getEnd())
                        || (t.getEnd_date().after(mods.getStart()) && t.getEnd_date().before(mods.getEnd()))))
                                .collect(Collectors.toList());
        else if (mods.getStart() != null)
            tvPrograms = tvPrograms.stream().filter(t->t.getEnd_date().after(mods.getStart())).collect(Collectors.toList());
        else if (mods.getEnd() != null)
            tvPrograms = tvPrograms.stream().filter(t->t.getStart_date().before(mods.getEnd())).collect(Collectors.toList());
        logger.info("Cache Query Time: " + (System.currentTimeMillis() - start));
        return tvPrograms;
    }

    public List<TVProgram> findByParamaters(TVModifiers mods) {
        long start = System.currentTimeMillis();
        Query query = new Query();
        Criteria criteria = new Criteria();

        if (mods.getChannel() != null && !mods.getChannel().isEmpty())
            query.addCriteria(Criteria.where(FIELD_CHANNEL).is(mods.getChannel().toLowerCase()));

        if (mods.getProg_title() != null && !mods.getProg_title().isEmpty())
            query.addCriteria(Criteria.where(FIELD_TITLE).is(mods.getProg_title().toLowerCase()));

        if (mods.getProg_title() == null && mods.getType() != null && mods.getType().size() > 0) {
            query.addCriteria(Criteria.where(FIELD_TYPES).regex(genRegex(mods.getType())));
        }

        if (mods.getStart() != null && mods.getEnd() != null && mods.getStart() == mods.getEnd())
            query.addCriteria(Criteria.where(FIELD_START).lte(mods.getStart()).and(FIELD_END).gte(mods.getStart()));
        else if (mods.getStart() != null && mods.getEnd() != null)
            query.addCriteria(Criteria.where(FIELD_START).gte(mods.getStart()).lte(mods.getEnd()));
        else if (mods.getStart() != null)
            query.addCriteria(Criteria.where(FIELD_START).gte(mods.getStart()).and(FIELD_END).gte(mods.getStart()));
        else if (mods.getEnd() != null)
            query.addCriteria(Criteria.where(FIELD_START).lte(mods.getEnd()).and(FIELD_END).lte(mods.getEnd()));

        System.out.println("[TVPROGRAMSERVICE]: Query" + query.toString());

        List<TVProgram> result = mongoOperations.find(query, TVProgram.class);
        // Fix bug film:action && film:usa
        result = filterListByFeatureType(result, mods.getType());

        result.sort(START_CMP);
        logger.info("Query DB TIME: " + (System.currentTimeMillis() - start));
        return result;
    }


    public static List<TVProgram> filterListByFeatureType(List<TVProgram> progs, List<String> types) {
        if (types == null || types.size() < 2) return progs;

        List<TVProgram> rs = new ArrayList<>();
        List<String> featured = new ArrayList<>();

        for (String type : types) {
            if (type.contains(":")) {
                featured.add(type);
            }
        }

        if (featured.size() >= 2) {
            for (TVProgram prog: progs) {
                boolean isOK = true;
                for (String f: featured) {
                    if (!prog.getType().contains(f)) {
                        isOK = false;
                        break;
                    }
                }
                if (isOK)   rs.add(prog);
            }
            return rs;
        }

        return progs;
    }

    public List<TVProgram> findAll() {
        logger.info("FIND ALL TV Programsss");
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

    private Supplier<List<TVProgram>> allTVPrograms = Suppliers.memoizeWithExpiration(
            new Supplier<List<TVProgram>>() {
                public List<TVProgram> get() {
                    return findAll();
                }
            }, ONE_HOUR, TimeUnit.MILLISECONDS);

    public List<TVProgram> getAllTVPrograms() {
        return allTVPrograms.get();
    }

    public void restartCached(){
        allTVPrograms = Suppliers.memoizeWithExpiration(
                new Supplier<List<TVProgram>>() {
                    public List<TVProgram> get() {
                        return findAll();
                    }
                }, 10, TimeUnit.MILLISECONDS);
    };
}