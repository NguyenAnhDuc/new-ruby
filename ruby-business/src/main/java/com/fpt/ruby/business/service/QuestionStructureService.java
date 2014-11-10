package com.fpt.ruby.business.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.fpt.ruby.business.config.SpringMongoConfig;
import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.model.QuestionStructure;

@Service
public class QuestionStructureService {
	private final String REDIS_HOST = "10.3.9.236";
	private MongoOperations mongoOperations;
	private Jedis jedis;
	public QuestionStructureService(){
		//ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
		jedis = new Jedis(REDIS_HOST);
	}
	public void save (QuestionStructure questionStructure){
		mongoOperations.save(questionStructure);
	}
	
	public List<QuestionStructure> allQuestionStructures(){
		return mongoOperations.findAll(QuestionStructure.class);
	}
	
	public void cached(QuestionStructure questionStructure) {
		String key  = questionStructure.getKey();
		String redisString = RedisHelper.toRedisString(questionStructure);
		jedis.set(key, redisString);
	}
	
	public boolean isInCache(String key) {
		if (jedis.get(key) == null) return false;
		return true;
	}
	
	public QuestionStructure getInCache(String key){
		return  RedisHelper.toQuestionStructure(key, jedis.get(key));
	}
	
	public void loadFromDBToCache(){
		List<QuestionStructure> questionStructures = mongoOperations.findAll(QuestionStructure.class);
		for (QuestionStructure questionStructure : questionStructures) {
			String key  = questionStructure.getKey();
			jedis.set(key, RedisHelper.toRedisString(questionStructure));
		}
	}
	
}
