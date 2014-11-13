package com.fpt.ruby.business.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

public class HttpHelper {
	public static String sendGet(String url) throws Exception {
		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpGet request = new HttpGet(url);
		// add request header
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent(),"UTF-8"));
 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}

	private static void test(){
		String question = "hôm nay có phim gì hay không";
		
		try{
			String url = "http://sandbox.api.simsimi.com/request.p?key=9cac0c6c-1810-447b-ad5c-c1c76b2aadeb&lc=vn&text=" 
					+ URLEncoder.encode(question,"UTF-8") ;
			String jsonString = HttpHelper.sendGet(url);
			System.out.println("response: " + jsonString);
			JSONObject json = new JSONObject(jsonString);
			String answer = json.getString("response");
			
		}
		catch (Exception ex){
			System.out.println("Exception");
		}
	}
	
	public static void main(String[] args) throws Exception{
		test();
		
		/*String jsonString = sendGet("http://localhost:8080/rubyweb/movie_ticket/allCinema");
		JSONObject json = new JSONObject(jsonString);
		List<String> movies = new ArrayList<String>();
		List<String> cinemas = new ArrayList<String>();
		JSONArray movieArray = json.getJSONArray("movies");
		JSONArray cinemaArray = json.getJSONArray("cinemas");
		for (int i=0;i < movieArray.length(); i++) {
			movies.add(movieArray.getString(i).trim());
			System.out.println(movieArray.getString(i).trim());
		}
		for (int i=0;i < cinemaArray.length(); i++) {
			movies.add(cinemaArray.getString(i).trim());
		}*/
		
	}
}
