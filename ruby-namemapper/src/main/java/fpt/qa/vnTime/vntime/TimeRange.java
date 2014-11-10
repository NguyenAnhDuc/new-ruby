package fpt.qa.vnTime.vntime;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeRange implements Serializable {

	static final long HAFL_DAY = 12 * 60 * 60 * 1000;
	static final long ONE_DAY = 2 * HAFL_DAY;

	private String expression;
	private Date fDate;
	private Date sDate;

	public TimeRange(String expression, String fDate, String sDate)
			throws ParseException {
		super();
		this.expression = expression;
		this.fDate = parsefDate(fDate);
		this.sDate = parsesDate(sDate);
	}

	public TimeRange() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	private Date parsefDate(String date) throws ParseException {
		// TODO Auto-generated method stub
		long bonusTime = 0;
		String dateString = date;
		if (dateString.endsWith("pm")) {
			bonusTime = HAFL_DAY;
			dateString = date.substring(0, date.length() - 2);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date utilDate = null;

		try {
			utilDate = format.parse(dateString);
		} catch (Exception ex1) {
			try {
				utilDate = new SimpleDateFormat("yyyy-MM-dd HH")
						.parse(dateString);
			} catch (ParseException e) {
				// e.printStackTrace();
				utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
				utilDate = new Date(utilDate.getTime());
				try {
					utilDate = new SimpleDateFormat("yyyy-MM-dd")
							.parse(dateString);
					utilDate = new Date(utilDate.getTime());
				} catch (ParseException ex) {
					e.printStackTrace();
					utilDate = new SimpleDateFormat("yyyy-MM")
							.parse(dateString);
					utilDate.setDate(30);
					dateString = new SimpleDateFormat("yyyy-MM-dd")
							.format(utilDate);
					utilDate = parsefDate(dateString);
				}
			}
		}
		if (bonusTime > 0) {
			return new Date(utilDate.getTime() + bonusTime);
		}
		return utilDate;
	}

	@SuppressWarnings("deprecation")
	private Date parsesDate(String date) throws ParseException {
		long bonusTime = 0;
		String dateString = date;
		if (dateString.endsWith("pm")) {
			bonusTime = HAFL_DAY;
			dateString = date.substring(0, date.length() - 2);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date utilDate = null;
		try {
			utilDate = format.parse(dateString);
		} catch (ParseException e) {
			try {
				utilDate = new SimpleDateFormat("yyyy-MM-dd HH")
						.parse(dateString);
				utilDate = new Date(utilDate.getTime() + 60 * 1000 * 59);
			} catch (Exception ex1) {
				try {
					utilDate = new SimpleDateFormat("yyyy-MM-dd")
							.parse(dateString);
					utilDate = new Date(utilDate.getTime() + 60 * 60 * 1000
							* 23 + 60 * 1000 * 59 + 59 * 1000);
				} catch (ParseException ex2) {
					utilDate = new SimpleDateFormat("yyyy-MM")
							.parse(dateString);
					utilDate.setDate(30);
					dateString = new SimpleDateFormat("yyyy-MM-dd")
							.format(utilDate);
					utilDate = parsesDate(dateString);
				}
			}
		}
		if (bonusTime > 0) {
			return new Date(utilDate.getTime() + bonusTime);
		}
		return utilDate;
	}

	public static void main(String[] args) throws ParseException {
		String date = "2014-9-21";
		// System.out.println( "Date = " + new TimeRange().parsefDate( date ) );
		// System.out.println( "Date = " + new TimeRange().parsesDate( date ) );
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Date getfDate() {
		return fDate;
	}

	public void setfDate(String fDate, boolean plusOneDay)
			throws ParseException {
		Date parsedDate = parsefDate(fDate);
		if (parsedDate != null && plusOneDay) {
			parsedDate = new Date(parsedDate.getTime() + ONE_DAY);
		}
		this.fDate = parsedDate;
	}

	public Date getsDate() {
		return sDate;
	}

	public void setsDate(String sDate, boolean plusOneDay)
			throws ParseException {
		Date parsedDate = parsesDate(sDate);
		if (parsedDate != null && plusOneDay) {
			parsedDate = new Date(parsedDate.getTime() + ONE_DAY);
		}
		this.sDate = parsedDate;
	}

	@Override
	public String toString() {
		return "Chuỗi thời gian :" + expression + "\n Cận trên : " + this.fDate
				+ "\n Cận dưới :" + sDate;
	}
}