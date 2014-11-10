package com.fpt.ruby.helper;

import java.util.ArrayList;
import java.util.List;

import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebQuery;
import net.billylieurance.azuresearch.AzureSearchWebResult;

import org.springframework.stereotype.Service;

import com.fpt.ruby.business.helper.DisplayAnswerHelper;
import com.fpt.ruby.business.model.BingSearchAnswer;
@Service
public class BingSearchHelper {
private String appId;
	
	public BingSearchHelper() {
		this.appId = "sPqkjRn2CLELygmcDJ7kIAC0YOm0nihpzVJ3RLVu3XI=";
	}
	
	public List<BingSearchAnswer> getDocuments(String question, int limit) {
		List<BingSearchAnswer> docs = new ArrayList<BingSearchAnswer>();
		AzureSearchWebQuery query = new AzureSearchWebQuery();
		query.setAppid(appId);
		query.setQuery(question);
		query.doQuery();
		query.setMarket("Vietnam");
		AzureSearchResultSet<AzureSearchWebResult> results = query.getQueryResult();
		int count = 0;
		for (AzureSearchWebResult r : results) {
			BingSearchAnswer bingAnswer = new BingSearchAnswer();
			bingAnswer.setUrl(r.getUrl());
			bingAnswer.setDescription(r.getDescription());
			bingAnswer.setDisplayUrl(r.getDisplayUrl());
			bingAnswer.setTitle(r.getTitle());;
			docs.add(bingAnswer);
			count ++;
			if (count>=limit) break;
		}
		return docs;
	}
	
	public static void main(String[] args){
		BingSearchHelper bingWebSearchProvider = new BingSearchHelper();
		List<BingSearchAnswer> docs = bingWebSearchProvider.getDocuments("có món gì ngon",5);
		System.out.println( DisplayAnswerHelper.display(docs));
		System.out.println("DONE!");
	}
}
