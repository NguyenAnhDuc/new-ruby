package com.fpt.ruby.model;

import java.util.Date;

public class QueryParamater {
	private String movieTitle;
	private MovieTicket movieTicket;
	private Date beginTime;
	private Date endTime;
	private String cinName;
	private String tvChannel;
	private String tvProTitle;
	public String types;

	public QueryParamater() {
		movieTitle = null;
		movieTicket = null;
		beginTime = null;
		endTime = null;
		types = null;
	}

	public Date getStartTime() {
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

	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public MovieTicket getMovieTicket() {
		return movieTicket;
	}

	public String getCinName() {
		return cinName;
	}

	public void setCinName(String cinName) {
		this.cinName = cinName;
	}

	public String getTvChannel() {
		return tvChannel;
	}

	public void setTvChannel(String tvChannel) {
		this.tvChannel = tvChannel;
	}

	public String getTvProTitle() {
		return tvProTitle;
	}

	public void setTvProTitle(String tvProTitle) {
		this.tvProTitle = tvProTitle;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setMovieTicket(MovieTicket movieTicket) {
		this.movieTicket = movieTicket;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String toShow() {
		StringBuilder result = new StringBuilder().append("{");
		if (movieTitle != null)
			result.append("Movie Title: " + movieTitle + " | ");
		if (movieTicket != null)
			result.append("Movie Ticket: " + movieTicket.toShow() + " | ");
		if (beginTime != null)
			result.append("Begin Time: " + beginTime.toLocaleString() + " | ");
		if (endTime != null)
			result.append("End Time: " + endTime.toLocaleString() + " | ");
		if (tvChannel != null)
			result.append("TVChannel: " + tvChannel + " | ");
		if (tvProTitle != null)
			result.append("TVProgram: " + tvProTitle + " | ");
		result.append("Question type: " + types + " | ");
		return result.append("}").toString();
	}
}
