package com.fpt.ruby.commons.helper;

import com.fpt.ruby.commons.entity.objects.BingSearchAnswer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class DisplayAnswerHelper {
	public static String display(List<BingSearchAnswer> bingAnswers){
		StringBuilder result = new StringBuilder();
		for (BingSearchAnswer bingSearchAnswer : bingAnswers){
			result.append("<a href=\"" + bingSearchAnswer.getUrl() + "\">" );
			result.append(bingSearchAnswer.getTitle());
			result.append("</a>");
			result.append("</br>");

			String website = extractHost(bingSearchAnswer.getUrl());
			result.append("<strong>" + website + "</strong>").append("</br>");
			result.append(bingSearchAnswer.getDescription()).append("</br>");
			result.append("</br>");
		}
		return result.toString();
	}

	public static String extractHost(String url) {
		String result = url;
		try {
			URL u = new URL(url);
			result = u.getHost();
			if (result.contains("www.")) {
				result = result.substring(result.indexOf("www.") + 4);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
