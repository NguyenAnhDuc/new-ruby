package fpt.qa.type_mapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fpt.ruby.business.constants.ProgramType;

public class FootballTypeRecognizer extends TypeRecognizer {
	public FootballTypeRecognizer() {
		SetType(ProgramType.FOOTBALL);

		String[] dedicatedChannels = new String[] { "vtvcab16", "bongdatv" };
		String[] typeKeywords = new String[] { "bong da", "vo dich quoc gia", "hang nhat phap", "hang nhat y", "cup c1", "world cup", "champion league", "champions league", "soccer", "cup lien doan", "bundesliga", "bundes liga", "laliga", "English Premier League"};

		super.loadConfig(dedicatedChannels, typeKeywords);
	}
	
	@Override
	public Boolean customCode(String channel, String program) {
//		if (channel.equals("k+pm")) {
//			System.out.println("Check football " + channel + " " + program);
//			String sport_regex = "(.*)-(.*)";
//			Pattern pat = Pattern.compile(sport_regex);
//			Matcher mat = pat.matcher(program);
//			if (mat.matches()) {
//				String first = mat.group(1);
//				String second = mat.group(2);
//				return first
//			}
//		}
		return super.customCode(channel, program);
	}
}
