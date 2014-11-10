package com.fpt.ruby.business.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fpt.ruby.config.SpringMongoConfig;
import com.fpt.ruby.model.Cinema;

@Service
public class CinemaService {
	private MongoOperations mongoOperations;
	public CinemaService(MongoOperations mongoOperations){
		this.mongoOperations = mongoOperations;
	}
	
	public CinemaService(){
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		this.mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
	}
	
	public List<Cinema> findAll(){
		return mongoOperations.findAll(Cinema.class);
	}
	
	public List<Cinema> findByName(String name){
		Query query = new Query(Criteria.where("name").regex("^" + name + "$","i"));
		return mongoOperations.find(query, Cinema.class);
	}
	
	public List<Cinema> findLogToShow(){
		List<Cinema> cinemas = mongoOperations.findAll(Cinema.class);
		return cinemas;
	}
	
	public void save(Cinema cinema){
		mongoOperations.save(cinema);
	}
	
	public void dropCollection(){
		mongoOperations.dropCollection(Cinema.class);
	}
	

}
