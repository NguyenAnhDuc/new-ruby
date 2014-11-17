package com.fpt.ruby.business.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {
    private static String DEV_DB = "testdb";
    private static String PRO_DB = "yourdb";
    private static Boolean isDevelop = false;

    @Override
    public String getDatabaseName() {
        return isDevelop ? DEV_DB : PRO_DB;
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient("10.3.9.236");
        //return new MongoClient("localhost");
    }
}