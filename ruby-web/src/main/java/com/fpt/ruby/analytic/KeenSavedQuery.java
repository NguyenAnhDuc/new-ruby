package com.fpt.ruby.analytic;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class KeenSavedQuery {
	private static void saveQuery(String name, String  json) throws Exception{
		URL url = new URL(
				"https://api.keen.io/3.0/projects/5446262e07271972f4861489/saved_queries/"
				+	name +"?api_key=559F95824E2A4112F4DDF722B6E8EABC");
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("PUT");
		httpCon.setRequestProperty("Content-Type", "application/json");
		httpCon.setRequestProperty("Accept", "application/json");
		OutputStreamWriter out = new OutputStreamWriter(
				httpCon.getOutputStream());
				
		//String json = "{\"analysis_type\": \"count_unique\",\"event_collection\": \"userActivity\", \"interval\": \"hourly\",\"timeframe\": \"this_month\"}";
		out.write(json);
		out.close();
		System.out.println(httpCon.getResponseCode());
	}
	
	public static void main(String[] args) throws Exception {
		String  json = "";
		// Count unique user 
		//json = "{\"analysis_type\": \"count_unique\",\"event_collection\": \"userActivity\", \"target_property\":\"userID\",\"timezone\":\"25200\"}";
		//saveQuery("countUniqueUser", json);
		
		// Count unique user per day
		//json = "{\"analysis_type\": \"count_unique\",\"event_collection\": \"userActivity\", \"target_property\":\"userID\",\"interval\": \"daily\",\"timeframe\": \"this_month\",\"timezone\":\"25200\" }";
		//saveQuery("uniqueUserPerDay", json);
		
		// %domainQuestion 
		//json = "{\"analysis_type\": \"count\",\"event_collection\": \"userActivity\", \"group_by\":\"domain\",\"timezone\":\"25200\"}";
		//saveQuery("percentDomain", json);
		
		// domainQuestionPerday
		//json = "{\"analysis_type\": \"count\",\"event_collection\": \"userActivity\", \"group_by\":\"domain\",\"timezone\":\"25200\",\"interval\": \"daily\",\"timeframe\":\"this_month\"}";
		//saveQuery("DomainPerday", json);
		
		// %intentQuestion 
		//json = "{\"analysis_type\": \"count\",\"event_collection\": \"userActivity\", \"group_by\":\"intent\",\"timezone\":\"25200\"}";
		//saveQuery("intentQuestion", json);
				
		// domainQuestionPerday
		json = "{\"analysis_type\": \"count\",\"event_collection\": \"userActivity\", \"group_by\":\"userID\",\"timezone\":\"25200\",\"interval\": \"hourly\",\"timeframe\":\"this_day\"}";
		saveQuery("count_GROUPBY_userid_THISDAY_HOURLY", json);
	}
}
