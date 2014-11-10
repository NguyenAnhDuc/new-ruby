package com.fpt.ruby.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.fpt.ruby.config.SpringMongoConfig;
import com.fpt.ruby.model.ReportQuestion;

@Service
public class ReportQuestionService {

	private MongoOperations db;

	public ReportQuestionService(MongoOperations db) {
		this.db = db;
	}
	
	public ReportQuestionService() {
		ApplicationContext context = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		this.db = (MongoOperations) context.getBean("mongoTemplate");
	}

	public List<ReportQuestion> findAll() {
		return db.findAll(ReportQuestion.class);
	}

	
	
	public void save(ReportQuestion inst) {
		db.save(inst);
	}
	
	
}
