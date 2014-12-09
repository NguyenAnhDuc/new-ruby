package com.fpt.ruby.cache;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * Created by quang on 12/2/2014.
 */
@Service
public class RubyCacheServiceImpl implements RubyCacheService{
    private final String REDIS_HOST = "10.3.9.236";
    private MongoOperations mongoOperations;
    private Jedis jedis;
    private CacheHelper cacheHelper;

    public RubyCacheServiceImpl(){
       /* ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
        jedis = new Jedis(REDIS_HOST);
        cacheHelper = new CacheHelper();*/
    }

    public void save (RubyCache rubyCache){
        mongoOperations.save(rubyCache);
    }

    public void cache(RubyCache rubyCache){
        String key = cacheHelper.normalizeQuestion(rubyCache.getQuestion());
        String value = cacheHelper.toJsonString(rubyCache);
        jedis.set(key,value);

    }
    public RubyCache getCache(String key){
        RubyCache rubyCache = new RubyCache();
        return rubyCache;
    }

    @Override
    public void saveToDb(RubyCache rubyCache) {
        mongoOperations.save(rubyCache);
    }

    @Override
    public void loadFromDbToCache() {
        mongoOperations.findAll(RubyCache.class).stream().forEach( rbc ->{
                    String key = cacheHelper.normalizeQuestion(rbc.getQuestion());
                    String value = cacheHelper.toJsonString(rbc);
                    jedis.set(key,value);
                }

        );
    }
}
