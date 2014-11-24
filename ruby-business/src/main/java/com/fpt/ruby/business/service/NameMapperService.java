package com.fpt.ruby.business.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fpt.ruby.business.helper.NameMapperHelper;
import com.fpt.ruby.business.model.MovieTicket;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fpt.ruby.business.config.SpringMongoConfig;
import com.fpt.ruby.business.helper.TypeMapperHelper;
import com.fpt.ruby.business.model.NameMapper;
import com.fpt.ruby.business.model.TVProgram;

@Service
public class NameMapperService {
	static Set<String> x = new HashSet<String>();
	
	private static MongoOperations db;
	
	static {
		ApplicationContext context = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		db = (MongoOperations) context.getBean("mongoTemplate");
		List<NameMapper> names = findAll();
		x.clear();
		
		for (NameMapper n: names) {
			x.add(n.getName().toLowerCase() + n.getType() + n.getDomain());
			Set<String> ss = n.getVariants();
			for (String s: ss) {
				x.add(s.toLowerCase() + n.getType() + n.getDomain());
			}
		}
	}
	
	public NameMapperService(MongoOperations db) {
		this.db = db;
	}
	
	public NameMapperService() {
		ApplicationContext context = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		this.db = (MongoOperations) context.getBean("mongoTemplate");
		List<NameMapper> names = findAll();
		x.clear();
		
		for (NameMapper n: names) {
			x.add(n.getName().toLowerCase() + n.getType() + n.getDomain());
			Set<String> ss = n.getVariants();
			for (String s: ss) {
				x.add(s.toLowerCase() + n.getType() + n.getDomain());
			}
		}
	}
	
	public static List<NameMapper> findAll() {
		return db.findAll(NameMapper.class);
	}
	public List<NameMapper> getAll() {
		return db.findAll(NameMapper.class);
	}

	public static List<NameMapper> findByDomain(String domain){
		Query query = new Query(Criteria.where("domain").is(domain));
		return db.find(query, NameMapper.class);
	}
	
	public NameMapper findByName(String name){
		Query query = new Query(Criteria.where("name").regex(name,"i"));
		return db.findOne(query, NameMapper.class);
	}
	public void save(List<NameMapper> nameMappers) {
		for (NameMapper name : nameMappers) {
			db.save(name);
		}
	}
	
	public static void save(NameMapper inst) {
		if (!x.contains(inst.getName().toLowerCase() + inst.getType() + inst.getDomain())) {
			db.save(inst);
			x.add(inst.getName().toLowerCase() + inst.getType() + inst.getDomain());
			Set<String> vars = inst.getVariants();
			for (String var: vars) {
				x.add(var.toLowerCase() + inst.getType() + inst.getDomain());
			}
		}
	}
	
	public static void save(TVProgram prog) {
		NameMapper n = new NameMapper();
		
		String title = NameMapperHelper.getRealName(prog.getTitle()).toLowerCase();
		Set<String> variants = new HashSet<String>();
		variants.add(title);
		variants.add(TypeMapperHelper.normalize(title));
		n.setVariant(variants);

		n.setName(title);
		n.setDomain("tv");
		n.setType("program_title");
		n.setEnteredDate(new Date());
		n.setLastMention(new Date());
		
		save(n);
	}

	public void remove(NameMapper nameMapper){
		db.remove(nameMapper);
	}

	public static void save(MovieTicket mt) {
		NameMapper n = new NameMapper();

		String title = mt.getMovie();
		Set<String> variants = new HashSet<String>();
		variants.add(title);
		variants.add(TypeMapperHelper.normalize(title));
		variants.add(mt.getAlias());
		variants.add(TypeMapperHelper.normalize(mt.getAlias()));

		n.setVariant(variants);

		n.setName(title);
		n.setDomain("movie");
		n.setType("mov_title");
		n.setEnteredDate(new Date());
		n.setLastMention(new Date());

		save(n);
	}

	
	public static void main(String[] args) {
		NameMapperService nameMapperService = new NameMapperService();
		List<NameMapper> nameMappers =  nameMapperService.findAll();
		System.out.println(nameMappers.size());
		for (NameMapper nameMapper : nameMappers){
			if (nameMapper.getName().toLowerCase().equals("bóng đá") || nameMapper.getName().toLowerCase().equals("bong da")
					|| nameMapper.getName().toLowerCase().equals("phim") || nameMapper.getName().toLowerCase().equals("phim")){
				nameMappers.remove(nameMapper);
			}
		}
		System.out.println("DONE");
	}
}