package com.fpt.ruby.business.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class QuestionStructure {
	@Id
	private String key;
	private String head;
	private List<String> modifiers;
	
	public QuestionStructure() {
		key = "";
		head = "";
		modifiers = new ArrayList<String>();
	}
	
	public QuestionStructure(String key, String head, List<String> modifiers) {
		this.key = key;
		this.head = head;
		this.modifiers = modifiers;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String text) {
		this.key = text;
	}

	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public List<String> getModifiers() {
		return modifiers;
	}
	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

}
