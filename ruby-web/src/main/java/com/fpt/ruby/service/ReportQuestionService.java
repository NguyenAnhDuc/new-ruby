package com.fpt.ruby.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.fpt.ruby.model.ReportQuestion;

@Service
public class ReportQuestionService {
	@Autowired
	private MongoTemplate db;


	public ReportQuestionService() {
		//ApplicationContext context = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		//this.db = (MongoOperations) context.getBean("mongoTemplate");
	}

	public List<ReportQuestion> findAll() {
		return db.findAll(ReportQuestion.class);
	}

	
	
	public void save(ReportQuestion inst) {
		db.save(inst);
	}
	
	
}
