package com.fpt.ruby.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Log {
	@Id private String id;
	private String inputType;
	private String userAgent;
	private String question;
	private String answer;
	private String intent;
	private String domain;
	private QueryParamater queryParamater;
	private Date date;
	
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public QueryParamater getQueryParamater() {
		return queryParamater;
	}
	public void setQueryParamater(QueryParamater queryParamater) {
		this.queryParamater = queryParamater;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	
}
