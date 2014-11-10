package com.fpt.ruby.business.model;


import java.util.Date;
import java.util.List;


public class TVModifiers {
	private static final String CHANNEL = "chanel_title";
	private static final String PROGRAM = "program_title";
	private static long ONE_HOUR = 60 * 60 * 1000;
	private static long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;



	private String prog_title;
	private String channel = null;
	private Date start = null;
	private Date end = null;
	private List<String> type = null;

	public String getProg_title() {
		return prog_title;
	}

	public void setProg_title(String prog_title) {
		this.prog_title = prog_title;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public List<String> getType() {
		return type;
	}

	public void setType(List<String> type) {
		this.type = type;
	}

	public String toString() {
		return channel + " : " + prog_title + " : " + start + " : " + end;
	}

}
