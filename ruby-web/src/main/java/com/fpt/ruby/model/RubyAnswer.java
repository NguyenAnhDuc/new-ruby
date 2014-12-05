package com.fpt.ruby.model;

import com.fpt.ruby.business.model.QueryParamater;

public class RubyAnswer {
	private String domain;
	private QueryParamater queryParamater;
	
	public QueryParamater getQueryParamater() {
		return queryParamater;
	}

	private String question;
	private String answer;
	private boolean isInCache;
	private String intent;
	private boolean successful = false;
	private String query;
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public boolean isSuccessful() {
		return successful;
	}
	public void setSuccessful( boolean successful ) {
		this.successful = successful;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
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
	public boolean isInCache() {
		return isInCache;
	}
	public void setInCache(boolean isInCache) {
		this.isInCache = isInCache;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setQueryParamater(QueryParamater queryParamater) {
		this.queryParamater = queryParamater;
		
	}
	
}
