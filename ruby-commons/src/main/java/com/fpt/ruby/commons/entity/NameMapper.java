package com.fpt.ruby.commons.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Set;

@Document
public class NameMapper {
	@Id
	String id;
	String domain;
	String type;
	String name;
	Set<String> variants;
	Boolean isDiacritic;
	Date enteredDate;
	Date lastMention;
	Boolean isReviewed;

	
	public NameMapper() {
		domain = type = name = null;
		variants = null;
		isDiacritic = null;
		enteredDate = lastMention = null;
		isReviewed = false;
	}

	public Boolean getIsReviewed() {
		return isReviewed;
	}

	public void setIsReviewed(Boolean isReviewed) {
		this.isReviewed = isReviewed;
	}

	public Boolean getIsDiacritic() {
		return isDiacritic;
	}

	public void setIsDiacritic(Boolean isDiacritic) {
		this.isDiacritic = isDiacritic;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getVariants() {
		return variants;
	}

	public void setVariant(Set<String> variants) {
		this.variants = variants;
	}

	public Date getEnteredDate() {
		return enteredDate;
	}

	public void setEnteredDate(Date enteredDate) {
		this.enteredDate = enteredDate;
	}

	public Date getLastMention() {
		return lastMention;
	}

	public void setLastMention(Date lastMention) {
		this.lastMention = lastMention;
	}
	
	/*public String getFormattedVariants() {
		StringBuilder result = new StringBuilder();
		if (variants.size() == 0) return "";
		
		result.append(variants.);
		for (int i = 1; i < variants.size(); ++i) {
			result.append(", " + variants.get(i).trim());
		}
		
		return result.toString().trim();
	}*/
}
