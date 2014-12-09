package com.fpt.ruby.commons.service;

import com.fpt.ruby.commons.entity.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LogService {
	@Autowired
	private MongoTemplate mongoOperations;

	public List<Log> findAll(){
		return mongoOperations.findAll(Log.class);
	}
	

	public List<Log> findLogGtTime(Date date){
		Query query = new Query(Criteria.where("date").gt(date));
		return mongoOperations.find(query, Log.class);
	}
	
	public List<Log> findLogToShow(){
		List<Log> logs = mongoOperations.findAll(Log.class);
		/*List<Log> results = new ArrayList<Log>();
		Date date = new Date();
		date.setHours(0);date.setMinutes(0);date.setSeconds(0);
		for (Log Log : Logs){
			if (Log.getDate() != null && 
				(Log.getDate().getDate() == date.getDate() && Log.getDate().getMonth() == date.getMonth() 
				 && Log.getDate().getYear() == date.getYear()))
				 results.add(Log);
		}*/
		return logs;
	}
	
	public void save(Log Log){
		mongoOperations.save(Log);
	}
	
	public void dropCollection(){
		mongoOperations.dropCollection(Log.class);
	}
	
	
	
	public static void main(String[] args) throws Exception {
	}

}
