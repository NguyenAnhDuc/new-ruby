package fpt.qa.vnTime.vntime;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;
import fpt.qa.vnTime.utils.IConstants;
import fpt.qa.vnTime.utils.RangeParser;

public class VnTimeParser {
	StanfordCoreNLP coreNLP;

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
				+ "//vn.sutime.removeAccent.txt";
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

		Annotation annotation = new Annotation(textInput);
		annotation.set(CoreAnnotations.DocDateAnnotation.class, referenceDate);
		this.coreNLP.annotate(annotation);

		List<TimeRange> rangeList = new ArrayList<TimeRange>();
		List<CoreMap> timexAnnsAll = (List<CoreMap>) annotation
				.get(TimeAnnotations.TimexAnnotations.class);
		Integer id = Integer.valueOf(1);
		
		
//		if (timexAnnsAll.isEmpty()) {
//			return parser3("hôm nay", referenceDate);
//		}
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
		return rangeList;
	}
}