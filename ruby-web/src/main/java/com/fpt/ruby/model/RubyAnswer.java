package com.fpt.ruby.model;

import java.util.Date;

import com.fpt.ruby.business.model.MovieTicket;
import com.fpt.ruby.business.model.QueryParamater;
import com.fpt.ruby.business.model.QuestionStructure;

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
	private String questionType;
	private String movieTitle;
	private MovieTicket movieTicket;
	private QuestionStructure questionStructure;
	private Date beginTime;
	private Date  endTime;
	private boolean successful = false;
	
	
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
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getMovieTitle() {
		return movieTitle;
	}
	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}
	public MovieTicket getMovieTicket() {
		return movieTicket;
	}
	public void setMovieTicket(MovieTicket movieTicket) {
		this.movieTicket = movieTicket;
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
	public QuestionStructure getQuestionStructure() {
		return questionStructure;
	}
	public void setQuestionStructure(QuestionStructure questionStructure) {
		this.questionStructure = questionStructure;
	}
	
	public void setQueryParamater(QueryParamater queryParamater) {
		// TODO Auto-generated method stub
		this.queryParamater = queryParamater;
		
	}
	
}
