package com.fpt.ruby.business.helper;

import java.util.List;

import com.fpt.ruby.model.BingSearchAnswer;

public class DisplayAnswerHelper {
	public static String display(List<BingSearchAnswer> bingAnswers){
		StringBuilder result = new StringBuilder();
		for (BingSearchAnswer bingSearchAnswer : bingAnswers){
			result.append("<a href=\"" + bingSearchAnswer.getUrl() + "\">" );
			result.append(bingSearchAnswer.getTitle());
			result.append("</a>");
			result.append("</br>");
			
			String website = bingSearchAnswer.getDisplayUrl();
			int end = website.indexOf("/");
			if (end != -1) {
				website = website.substring(0, end);
			}
			result.append("<strong>" + website + "</strong>").append("</br>");
			result.append(bingSearchAnswer.getDescription()).append("</br>");
			result.append("</br>");
		}
		return result.toString();
	}
}
