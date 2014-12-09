package fpt.qa.vnTime.vntime;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;
import fpt.qa.vnTime.utils.RangeParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class VnTimeParser {
	StanfordCoreNLP coreNLP;
	String temporal = " hôm nay ";

	public String getTemporal() {
		return temporal;
	}

	public VnTimeParser() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, sutime");
		props.setProperty("customAnnotatorClass.sutime",
				"edu.stanford.nlp.time.TimeAnnotator");
		props.setProperty(
				"sutime.rules",
				"resources/vnsutime/defs.sutime.txt,resources/vnsutime/vn.sutime.removeAccent.txt, resources/vnsutime/vn.sutime.txt, resources/vnsutime/vietnamese.holidays.sutime.txt");
		this.coreNLP = new StanfordCoreNLP(props);
	}

	// public VnTimeParser(String propertyFile) throws FileNotFoundException,
	// IOException {
	// Properties props = new Properties();
	// props.load(new FileInputStream(propertyFile));
	// this.coreNLP = new StanfordCoreNLP(props);
	// }

	public VnTimeParser(String modelDir) {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, sutime");
		props.setProperty("customAnnotatorClass.sutime",
				"edu.stanford.nlp.time.TimeAnnotator");
		String tmp = modelDir + "//defs.sutime.txt," + modelDir
				+ "//vn.sutime.txt," + modelDir
				+ "//vietnamese.holidays.sutime.txt," + modelDir
				+ "//vn.sutime.removeAccent.txt," + modelDir
				+ "//vietnamese.holidays.sutime.removeAccent.txt";
		props.setProperty("sutime.rules", tmp);
		this.coreNLP = new StanfordCoreNLP(props);
	}

	public VnTimeObjectList parser(String textInput, String referenceDate) {
		VnTimeObjectList sutimeObjectList = new VnTimeObjectList();

		Annotation annotation = new Annotation(textInput);

		annotation.set(CoreAnnotations.DocDateAnnotation.class, referenceDate);

		this.coreNLP.annotate(annotation);

		List<CoreMap> timexAnnsAll = (List<CoreMap>) annotation
				.get(TimeAnnotations.TimexAnnotations.class);
		Integer id = Integer.valueOf(1);
		for (CoreMap cm : timexAnnsAll) {
			List<CoreLabel> tokens = (List<CoreLabel>) cm
					.get(CoreAnnotations.TokensAnnotation.class);
			Integer

			tmp98_96 = id;
			id = Integer.valueOf(tmp98_96.intValue() + 1);
			VnTimeObject sutimeObject = new VnTimeObject(
					tmp98_96,
					cm.toString(),
					(Integer) ((ArrayCoreMap) tokens.get(0))
							.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class),
					(Integer) ((ArrayCoreMap) tokens.get(tokens.size() - 1))
							.get(CoreAnnotations.CharacterOffsetEndAnnotation.class),
					((TimeExpression) cm.get(TimeExpression.Annotation.class))
							.getTemporal().toString());

			try {
				String range = ((TimeExpression) cm
						.get(TimeExpression.Annotation.class)).getTemporal()
						.getRange().toString();
				// //System.out.println(range);
			} catch (Exception exception) {
				//
			}
			sutimeObjectList.add(sutimeObject);
		}
		return sutimeObjectList;
	}

	public List<String> parser2(String textInput, String referenceDate) {
		Annotation annotation = new Annotation(textInput);
		annotation.set(CoreAnnotations.DocDateAnnotation.class, referenceDate);
		this.coreNLP.annotate(annotation);
		List<String> rangeList = new ArrayList<String>();
		List<CoreMap> timexAnnsAll = (List<CoreMap>) annotation
				.get(TimeAnnotations.TimexAnnotations.class);
		Integer id = Integer.valueOf(1);
		for (CoreMap cm : timexAnnsAll) {
			Integer tmp98_96 = id;
			String range = "";
			id = Integer.valueOf(tmp98_96.intValue() + 1);
			range = ((TimeExpression) cm.get(TimeExpression.Annotation.class))
					.getTemporal().toString();
			if (!range.contains("IN")) {
				try {
					range = ((TimeExpression) cm
							.get(TimeExpression.Annotation.class))
							.getTemporal().getRange().toString();
				} catch (Exception exception) {
					range = ((TimeExpression) cm
							.get(TimeExpression.Annotation.class))
							.getTemporal().toString();
					// exception.printStackTrace();
				}
			} else {
				// String rangeTmp = range.substring(range.indexOf("IN"));
				// Pattern pattern = Pattern.compile(IConstants.TIME_REGEX);
				// Matcher matcher = pattern.matcher(rangeTmp);
				// List<String> times = new
				// while(matcher.find()) {
				//
				// }
			}

			rangeList.add(range);
		}
		return rangeList;
	}

	public List<TimeRange> parser3(String textInput, String referenceDate)
			throws ParseException {

		if (checkSpe(textInput)) {
			return parser3("ngày mai", referenceDate);
		}

		Annotation annotation = new Annotation(textInput);
		annotation.set(CoreAnnotations.DocDateAnnotation.class, referenceDate);
		this.coreNLP.annotate(annotation);

		List<TimeRange> rangeList = new ArrayList<TimeRange>();
		List<CoreMap> timexAnnsAll = (List<CoreMap>) annotation
				.get(TimeAnnotations.TimexAnnotations.class);
		Integer id = Integer.valueOf(1);

		// if (timexAnnsAll.isEmpty()) {
		// return parser3("hôm nay", referenceDate);
		// }

		for (CoreMap cm : timexAnnsAll) {
			Integer tmp98_96 = id;
			String range = "";
			id = Integer.valueOf(tmp98_96.intValue() + 1);
			String cmString = cm.toString();
			if (cm.toString().equalsIgnoreCase("chieu")) {
				continue;
			}
			// System.out.println("cm.toString: " + cmString);
			if (cmString.equals("giờ") || cmString.equals("chiều")) {
				continue;
			}
			if (cmString.endsWith("giờ") || cmString.endsWith("phút")
					|| cmString.endsWith("trưa") || cmString.endsWith("chiều")
					|| cmString.endsWith("tối")) {
				temporal = "Hôm nay";
				return parser3(
						textInput.replace(cmString, cmString + " hôm nay"),
						referenceDate);
			}

			if (cmString.contains("giờ") && cmString.contains("trưa")) {
				return parser3(textInput.replace("trưa", "chiều"),
						referenceDate);
			}
			range = ((TimeExpression) cm.get(TimeExpression.Annotation.class))
					.getTemporal().toString();

			// xử lý các trường hợp: 9 giờ tối, 9 giờ 30 phút tối nay, 1 giờ
			// chiều nay, 2 giờ trưa nay
			int itmp = range.lastIndexOf("T");
			if (itmp > 0) {

				try {
					int hour = Integer.parseInt(range.substring(
							range.lastIndexOf("T") + 1, itmp + 3));
					if ((cmString.contains("tối") || cmString.contains("chiều") || cmString
							.contains("trưa")) && hour < 12) {
						range += "pm";
					}

					// //System.out.println("range after change: " + range);

					hour = Integer.parseInt(range.substring(
							range.lastIndexOf("T") + 1, itmp + 3));
					if ((cmString.contains("tối") || cmString.contains("chiều") || cmString
							.contains("trưa")) && hour < 12) {
						range += "pm";
					}
				} catch (Exception ex) {

				}
			}
			if (itmp > 0
					&& Character.isLetter(range.charAt(range.length() - 1))
					&& !range.contains("IN")) {
				try {
					range = ((TimeExpression) cm
							.get(TimeExpression.Annotation.class))
							.getTemporal().getRange().toString();

				} catch (Exception exception) {
					range = ((TimeExpression) cm
							.get(TimeExpression.Annotation.class))
							.getTemporal().toString();
					// System.out.println("Range = "+range);
					// exception.printStackTrace();
				}
			} else {
				try {
					range = ((TimeExpression) cm
							.get(TimeExpression.Annotation.class))
							.getTemporal().getRange().toString();
					// //System.out.println("range 2: " + range);
				} catch (Exception exception) {
					range = ((TimeExpression) cm
							.get(TimeExpression.Annotation.class))
							.getTemporal().toString();
				}
			}
			// System.out.println("Range = "+range);
			TimeRange timeRange = RangeParser.parser(range);
			timeRange.setExpression(cm.toString());

			rangeList.add(timeRange);
		}
		if (!timexAnnsAll.isEmpty() || !rangeList.isEmpty()) {
			temporal = timexAnnsAll.get(0).toString();
		}

		return rangeList;
	}

	public static String getTimeRange(String question) {
		VnTimeParser timeParser = new VnTimeParser((new VnTimeParser())
				.getClass().getClassLoader().getResource("").getPath()
				+ "vnsutime/");
		if (question.contains("đang") || question.contains("dang")
				|| question.contains("bây giờ") || question.contains("bay gio")
				|| question.contains("lúc này") || question.contains("luc nay")
				|| question.contains("hiện tại")
				|| question.contains("hien tai")) {
			return " này";
		}
		try {

			timeParser.parser3(question, new SimpleDateFormat(
					"yyyy-MM-dd HH:mm").format(new Date()));
			System.out.println();
			return timeParser.getTemporal();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return " hôm nay ";
		}

		// try {
		// TimeRange range = new VnTimeParser((new RedisHelper()).getClass()
		// .getClassLoader().getResource("").getPath()).parser3(
		// question, IConstants.CURRENT_DATE).get(0);
		// if (range == null) {
		// return "";
		// } else {
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM hh:mm:ss a");
		// String fDate = sdf.format(range.getfDate()).toString();
		// String sDate = sdf.format(range.getsDate()).toString();
		// if (range.getfDate().equals(range.getsDate())) {
		// return "Lúc " + fDate + " ";
		// } else {
		// return "Từ "+ fDate + " đến "+sDate +" " ;
		// }
		//
		// }
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// // e.printStackTrace();
		// return "";
		// }
	}

	private boolean checkSpe(String textInput) {
		String[] tokens = textInput.split(" ");
		String temp = " sáng sang ngay chieu toi ngày mai mãi chiều tối sau ban";
		int index = -1;
		for (int i = 0; i < tokens.length; ++i) {
			if (tokens[i].equalsIgnoreCase("mai")) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			if ((index - 1 >= 0 && temp.contains(tokens[index - 1])) || ( index + 1 < tokens.length && temp.contains(tokens[index + 1]))) {
				return false ;
			}
			if (index - 1 >= 0 && !temp.contains(tokens[index - 1])) {
				return true;
			}
			if (index + 1 < tokens.length && !temp.contains(tokens[index + 1])) {
				return true;
			}
		}
		return false;

	}
}