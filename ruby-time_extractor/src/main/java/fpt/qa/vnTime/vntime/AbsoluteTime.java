
package fpt.qa.vnTime.vntime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AbsoluteTime{
	private Date currentDate;
	private VnTimeParser timeParser;

	public AbsoluteTime() {
		setCurrentDate( updateCurrentDate() );
		setTimeParser( new VnTimeParser() );
	}

	public AbsoluteTime( String resourcePath ) {
		setCurrentDate( updateCurrentDate() );
		// TODO Uncomment when time lib ready
		setTimeParser( new VnTimeParser( resourcePath ) );
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate( Date currentDate ) {
		this.currentDate = currentDate;
	}

	public VnTimeParser getTimeParser() {
		return timeParser;
	}

	public void setTimeParser( VnTimeParser timeParser ) {
		this.timeParser = timeParser;
	}

	public Date updateCurrentDate() {
		return new Date();
	}

	public TimeResult getAbsoluteTime( String sent ) {
		String text = sent;
		if (sent.contains("rạp tháng tám") || sent.contains("rạp tháng 8") || sent.contains("rap thang tam") || sent.contains("rap thang 8")){
			text = sent.replace("rạp tháng tám", "").replace("rạp tháng 8", "").replace("rap thang tam", "").replace("rap thang 8", "");
		}
		TimeResult timeResult = new TimeResult();

		DateFormat dateFormatHour = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );

		List<TimeRange> results;
		
		setCurrentDate( updateCurrentDate() );
		
		try {
			results = getTimeParser().parser3( text, dateFormatHour.format( new Date() ) );
			
			//System.out.println("curentdate :"+dateFormatHour.format( new Date() ));
			if(results.isEmpty()) {
				results.add(new TimeRange());
				return timeResult;
			}
			timeResult.setBeginTime(results.get(0).getfDate());
			timeResult.setEndTime(results.get(0).getsDate());
			//System.out.println( timeResult.toString() );
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeResult;
	}

	public Date addDays( Date date, int days ) {
		Calendar cal = Calendar.getInstance();
		cal.setTime( date );
		cal.add( Calendar.DATE, days ); // minus number would decrement the days
		return cal.getTime();
	}
	
	public Date addMonths( Date date, int months ) {
		Calendar cal = Calendar.getInstance();
		cal.setTime( date );
		cal.add( Calendar.MONTH, months ); // minus number would decrement the days
		return cal.getTime();
	}

	public class TimeResult{

		private Date beginTime;
		private Date endTime;

		public Date getBeginTime() {
			return beginTime;
		}

		public void setBeginTime( Date beginTime ) {
			this.beginTime = beginTime;
		}

		public Date getEndTime() {
			return endTime;
		}

		public void setEndTime( Date endTime ) {
			this.endTime = endTime;
		}
		
		@Override
		public String toString() {
			return "<<"+ this.beginTime + " , " + this.endTime+">>";
		}
	}
}