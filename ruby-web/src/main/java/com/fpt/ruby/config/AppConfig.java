package com.fpt.ruby.config;

import fpt.qa.configs.SpringMongoConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Created by quang on 12/9/2014.
 */
@Configuration
public class AppConfig {
    @Bean
    MongoOperations mongoOperations(){
        ApplicationContext context = new AnnotationConfigApplicationContext(
                SpringMongoConfig.class);
        return  (MongoOperations) context.getBean("mongoTemplate");
    }
}
