package com.fpt.ruby.business.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {

    @Override
    public String getDatabaseName() {
        return "testdb";
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
<<<<<<< HEAD
        return new MongoClient("10.3.9.236");
//        return new MongoClient("localhost");
//        return new MongoClient("10.3.9.81");
=======
         return new MongoClient("10.3.9.236");
        //return new MongoClient("localhost");
//  return new MongoClient("10.3.9.81");
>>>>>>> 0ebaa0fb823f894fcda5ee3e0d704228d4ba0412
    }
}