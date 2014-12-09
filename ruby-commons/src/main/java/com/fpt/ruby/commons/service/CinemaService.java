package com.fpt.ruby.commons.service;

import com.fpt.ruby.commons.entity.movie.Cinema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CinemaService {
	@Autowired
	private MongoOperations mongoOperations;
	public CinemaService(MongoOperations mongoOperations){
		this.mongoOperations = mongoOperations;
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
