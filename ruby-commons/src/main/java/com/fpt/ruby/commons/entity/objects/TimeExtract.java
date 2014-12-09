package com.fpt.ruby.commons.entity.objects;

import java.util.Date;

public class TimeExtract {
	private Date beforeDate;
	private Date afterDate;
	public TimeExtract(){
		this.beforeDate = null;
		this.afterDate = null;
	}
	public Date getBeforeDate() {
		return beforeDate;
	}
	public void setBeforeDate(Date beforeDate) {
		this.beforeDate = beforeDate;
	}
	public Date getAfterDate() {
		return afterDate;
	}
	public void setAfterDate(Date afterDate) {
		this.afterDate = afterDate;
	}
	
}
