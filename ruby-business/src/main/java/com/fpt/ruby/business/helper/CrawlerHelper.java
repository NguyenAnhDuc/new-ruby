package com.fpt.ruby.business.helper;

import com.fpt.ruby.business.model.TVProgram;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CrawlerHelper {
	private static List<String> ALLOWED_REQ_TYPE = new ArrayList<String>(Arrays.asList("GET", "POST"));
	private final static long ONE_DAY = 24 * 60 * 60 * 1000;

	public static String getResponse(String url, String urlParameters, String requestType) throws Exception {

		requestType = requestType.toUpperCase();
		if (!ALLOWED_REQ_TYPE.contains(requestType)) return "Unkonwn request type";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add request header
		con.setRequestMethod(requestType);
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Referer", url);

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();

	}

	public static List<TVProgram> calculateEndTime(List<TVProgram> tvPrograms) {
		if (tvPrograms == null || tvPrograms.size() == 0) return tvPrograms;

		List<TVProgram> results = new ArrayList<TVProgram>();
		for (int i = 0; i < tvPrograms.size() - 1; i++) {
			TVProgram tvProgram = tvPrograms.get(i);
			tvProgram.setEnd_date(tvPrograms.get(i + 1).getStart_date());
			results.add(tvProgram);
		}

		TVProgram lastProg = tvPrograms.get(tvPrograms.size() - 1);
		Date end = (Date) lastProg.getStart_date().clone();
		end.setHours(0); end.setMinutes(0); end.setSeconds(0);
		end = new Date(end.getTime() + ONE_DAY - 1);
		lastProg.setEnd_date(end);
		results.add(lastProg);

		return results;
	}

	public static String sendPost(String url, String urlParameters) throws Exception {
		return getResponse(url, urlParameters, "POST");
	}

	public static String sendGet(String url, String urlParameters) throws Exception {
		return getResponse(url, urlParameters, "GET");
	}
}
