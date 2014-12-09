package fpt.qa.vnTime.utils;

import fpt.qa.vnTime.vntime.VnTimeParser;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface IConstants {
	public static final VnTimeParser VN_TIME_PARSER = new VnTimeParser("src/main/resources/vnsutime");
	public static final String DTIME_REGEX = "\\d{4}-\\d{1,2}(-\\d{1,2})*(T\\d{1,2}(:\\d{2})*)*(pm|am)*";
	public static final String DATE_REGEX = "\\d{4}-\\d{1,2}-\\d{1,2}";
	public static final String CURRENT_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
	public static final String TIME_REGEX = "\\d{1,2}(:\\d{2})?(:\\d{2})?";
}