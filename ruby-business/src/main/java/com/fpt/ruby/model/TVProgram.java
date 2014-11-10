package com.fpt.ruby.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class TVProgram {
	@Id
	private String id;
	private String title = "";
	private String description = "";
	private String type = "";
	//private List<String> types = new ArrayList<String>();
	private Date start_date;
	private Date end_date;
	private String channel = "";

	/*public List<String> getTypes() {
		return types;
	}
	public void setTypes(List<String> types) {
		this.types = types;
	}*/

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	/*public String showTypes() {
		if (types.equals(null) || types.isEmpty())
			return "{}";
		return "{" + String.join(", ", types) + "}";
	}
	@Override
	public String toString() {
		return String.format("id = %s; title = %s; type = %s; start_date = %s; end_date = %s; channel= %s", id, title,
				showTypes(), start_date, end_date, channel);
	}*/

}