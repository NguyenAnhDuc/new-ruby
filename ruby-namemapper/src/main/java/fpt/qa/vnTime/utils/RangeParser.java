package fpt.qa.vnTime.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;

import fpt.qa.vnTime.vntime.TimeRange;

public class RangeParser {

	@SuppressWarnings("unused")
	// private static final String DTIME_REGEX =
	// "\\d{4}-\\d{1,2}-\\d{1,2}(T\\d{1,2}:\\d{2})*";
	public RangeParser() {
		// /
	}

	public static TimeRange parser(String timeString) throws ParseException {
		TimeRange timeRange = new TimeRange();
		boolean plusOneDay = false;

		// Handle case like 9 giờ ngày mai, 9 giờ tối mai
		if (timeString.contains("NEXT") && timeString.contains("INTERSECT")) {
			plusOneDay = true;
		} else {
			if (timeString.contains("IN")) {
				Pattern pattern = Pattern.compile(IConstants.DATE_REGEX);
				Matcher matcher = pattern.matcher(timeString);
				String date = "";
				while (matcher.find()) {
					date = matcher.group();
				}

				String time = timeString.substring(timeString.indexOf("IN"));
				pattern = Pattern.compile(IConstants.TIME_REGEX);
				matcher = pattern.matcher(time);
				List<String> listTime = new ArrayList<String>();

				while (matcher.find()) {
					listTime.add(matcher.group());
				}
				if (listTime.isEmpty())
					;
				else if (listTime.size() == 2) {

				}
				timeString = date + "T" + listTime.get(0) + "," + date + "T"
						+ listTime.get(1);
			}
		}

		try {
			List<String> list = new ArrayList<String>();
			Pattern pattern = Pattern.compile(IConstants.DTIME_REGEX);
			Matcher matcher = pattern.matcher(timeString);
			while (matcher.find()) {
				String timeStr = matcher.group();
				if (timeStr.indexOf("T") > 0 && timeStr.indexOf(":") < 0) {
					timeStr += ":00";
				}

				list.add(timeStr.replace("T", " ").replace("pm:00", ":00pm"));
			}
			if (list.isEmpty()) {
				return timeRange;
			}

			//System.out.println("list size: " + list.size());
			//System.out.println("!!!!!!" + list.get(0));
			if (list.size() >= 2) {
				timeRange.setfDate(list.get(0), plusOneDay);
				timeRange.setsDate(list.get(1), plusOneDay);
			} else if (list.size() == 1) {
				timeRange.setfDate(list.get(0), plusOneDay);
				timeRange.setsDate(list.get(0), plusOneDay);
			} else {

			}

		} catch (Exception e) {
			// //System.err.println( e.toString() );
			e.printStackTrace();
		}
		return timeRange;
	}

}