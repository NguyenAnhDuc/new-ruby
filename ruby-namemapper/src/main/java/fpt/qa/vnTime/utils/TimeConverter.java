package fpt.qa.vnTime.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TimeConverter {
	
	private Date date ;
	private String dayOfWeek;
	private int day ;
	private int moth ;
	private int year ;
	private String time ;
	
	public static final Map<String, String> map = new HashMap<String, String>();
	static {
		init();
	}
	public TimeConverter(Date date) {
		this.date = date;
		String[] tokens = date.toString().split(" ");
		this.dayOfWeek = map.get(tokens[0]);
		this.moth = Integer.parseInt(map.get(tokens[1].trim()));
		this.day = Integer.parseInt(tokens[2]);
		this.time = makeTime();
		this.year = Integer.parseInt(tokens[5].trim());
	}
	
	
	
	public TimeConverter(String time) {
		Date date ;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			//date
		} catch(Exception ex) {
			System.err.println(ex.toString());
		}
	}
	
	public String makeTime() {
		int hour, min;
		String [] timeTokens = new SimpleDateFormat("hh:mm a").format(this.date).split(":| ");
		//for(String s : timeTokens) {
		hour = Integer.parseInt(timeTokens[0].trim());
		min = Integer.parseInt(timeTokens[1].trim());
		if(timeTokens[2].equalsIgnoreCase("AM")) { timeTokens[2] = "SA";} else { timeTokens[2] = "CH";}
		return hour + "h" + min + "'" + timeTokens[2];
	}
	
	// Time - Date String format
	public String toTDString() {
		return time + " " + dayOfWeek +  " ngày " + day + " tháng " + moth + " năm " + year ;
	}
	
	// Time - Date String format
	public String toDTString() {
		return dayOfWeek +  ", ngày " + day + " tháng " + moth + " năm " + year + ", " + time;
	}
	
	// Time - Date String format
	public String toSimpleString() {
		return dayOfWeek + ","  + day + "/" + moth + "/" + year +", " + time ;
	}
	
	
	
	private static void init() {
		map.put("Mon", "T.hai");map.put("Tue", "T.ba");
		map.put("Wed", "T.tư");map.put("Thu", "T.năm");
		map.put("Fri", "T.sáu");map.put("Sat", "T.bảy");
		map.put("Sun", "Chủ nhật");
		map.put("Jan", "1");map.put("Feb", "2");
		map.put("Mar", "3");map.put("Apr", "4");
		map.put("May", "5");map.put("Jun", "6");
		map.put("Jul", "7");map.put("Aug", "8");
		map.put("Sep", "9");map.put("Oct", "10");
		map.put("Nov", "11");map.put("Dec", "12");
	}


	public static void main(String[] args) {
		@SuppressWarnings("deprecation")
		TimeConverter converter = new TimeConverter(new Date());
		System.err.println(converter.toDTString());
		System.err.println(converter.toTDString());
		System.err.println(converter.toSimpleString());
		converter.makeTime();
	}
}
