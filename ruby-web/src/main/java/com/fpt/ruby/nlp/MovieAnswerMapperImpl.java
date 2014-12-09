package com.fpt.ruby.nlp;

import com.fpt.ruby.commons.entity.movie.Cinema;
import com.fpt.ruby.commons.entity.movie.MovieFly;
import jmdn.util.string.StrUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieAnswerMapperImpl implements MovieAnswerMapper {
	static Map<String, String> genreMap = new HashMap<String, String>();
	static Map<String, String> langMap = new HashMap<String, String>();
	static Map<String, String> countryMap = new HashMap<String, String>();
	static {
		String dir = (new MovieAnswerMapperImpl()).getClass().getClassLoader().getResource("").getPath();
		initGenreMap(dir + "/dicts/genreMap.txt");
		initLangMap(dir + "/dicts/languageMap.txt");
		initCountryMap(dir + "/dicts/countryMap.txt");
	}
	
	private static void initGenreMap(String map){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(map));
			String line;
			while((line = reader.readLine()) != null){
				int idx = line.indexOf("\t");
				if (line.isEmpty() || idx < 0){
					continue;
				}
				genreMap.put(line.substring(idx+1), line.substring(0, idx).trim());
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private static void initCountryMap(String map){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(map));
			String line;
			while((line = reader.readLine()) != null){
				int idx = line.indexOf("\t");
				if (line.isEmpty() || idx < 0){
					continue;
				}
				countryMap.put(line.substring(idx+1).toLowerCase(), line.substring(0, idx).trim());
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void initLangMap(String map){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(map));
			String line;
			while((line = reader.readLine()) != null){
				int idx = line.indexOf("\t");
				if (line.isEmpty() || idx < 0){
					continue;
				}
				langMap.put(line.substring(idx+1).toLowerCase(), line.substring(0, idx).trim());
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getTitleMovieAnswer(List<MovieFly> ans) {
		String res = "";
		res = "Phim ";
		if (ans.size() == 0) {
			return "Xin lỗi, chúng tôi không tìm được kết quả thích hợp";
		}
		for (int i = 0; i < ans.size(); i++) {
			String title = StrUtil.toInitCap(ans.get(i).getTitle());
			int idx = title.indexOf("-");
			if (i > 0 && i == ans.size() - 1) {
				res += "và " + title.substring(0, idx) + " đang được chiếu";
				break;
			} else if (i == ans.size() - 1) {
				res += title.substring(0, idx) + "</br>";
				break;
			}
			res += title.substring(0, idx);
			if (ans.size() > 1) {
				res += ", ";
			} else {
				res += " ";
			}
		}

		return res;
	}

	public String getGenreMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " thuộc thể loại " + getVnMovieGenre(mov.getGenre());
		return res;
	}
	
	private String getVnMovieGenre(String enGenre){
		String[] tokens = enGenre.split(",");
		String res = "";
		for (String token : tokens){
			String key = token.toLowerCase().trim();
			if (genreMap.containsKey(key)){
				res += genreMap.get(key) + ", ";
				continue;
			}
			res += key + ", ";
		}
		return res.substring(0, res.length() - 2);
	}

	public String getActorMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " có sự góp mặt của diễn viên " + mov.getActor();
		return res;
	}

	public String getDirectorMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Tôi không biết nhưng hẳn phải là 1 đạo diễn nổi tiếng đấy";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " của đạo diễn " + mov.getDirector();
		return res;
	}

	public String getLangMovieAnswer(List<MovieFly> ans){
		MovieFly mov = ans.get(0);
		if (ans.size() == 0){
			return "Tôi không biết nhưng theo tôi " + mov.getTitle() + " là một bộ phim tiếng Anh";
		}
		String res = "Phim " + mov.getTitle() + " sử dụng tiếng " + getVnMovieLang(mov.getLanguage());
		return res;
	}
	
	private String getVnMovieLang(String enLang){
		String lang = enLang.toLowerCase();
		if (langMap.containsKey(lang)){
			return langMap.get(lang);
		}
		return enLang;
	}

	public String getCountryMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " là phim " + getVnMovieCountry(mov.getCountry());
		return res;
	}
	private String getVnMovieCountry(String enCountry){
		String c = enCountry.toLowerCase();
		if (countryMap.containsKey(c)){
			return countryMap.get(c);
		}
		return enCountry;
	}

	public String getAwardMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		if (mov.getAwards().equals("N/A")){
			return "Phim " + mov.getTitle() + " chưa nhận được giải thưởng nào";
		}
		String res = "Phim " + mov.getTitle() + " đã nhận được giải thưởng " + mov.getAwards();
		return res;
	}

	public String getPlotMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = mov.getPlot();
		return res;
	}

	public String getYearMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " được sản xuất năm " + mov.getYear();
		return res;
	}

	public String getRuntimeMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " kéo dài " + mov.getRuntime();
		return res;
	}

	public String getAudienceMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " không phù hợp với trẻ em";
		return res;
	}

	public String getReleaseMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " được công chiếu ngày " + mov.getReleased();
		return res;
	}

	public String getImdbRatingMovieAnswer(List<MovieFly> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		MovieFly mov = ans.get(0);
		String res = "Phim " + mov.getTitle() + " có imdb rating khoảng " + mov.getImdbRating();
		return res;
	}

	public String getCinemaAddressAnswer(List<Cinema> ans) {
		// TODO Auto-generated method stub
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		Cinema cin = ans.get(0);
		String res =  cin.getName() + " ở địa chỉ: " + cin.getAddress();
		return res;
	}

	/*public static void main(String[] args) throws UnsupportedEncodingException {
		App app = new App();
		MovieAnswerMapper map = new MovieAnswerMapperImpl();
		String title = "Lucy";
		initGenreMap("/home/ngan/Work/AHongPhuong/RubyWeb/rubyweb/src/main/resources/dicts/genreMap.txt");
		initLangMap("/home/ngan/Work/AHongPhuong/RubyWeb/rubyweb/src/main/resources/dicts/languageMap.txt");
		initCountryMap("/home/ngan/Work/AHongPhuong/RubyWeb/rubyweb/src/main/resources/dicts/countryMap.txt");
		List<MovieFly> mov = app.getMovieFlyService().searchOnImdb(title);
		
		System.out.println(map.getActorMovieAnswer(mov));
		System.out.println(map.getAudienceMovieAnswer(mov));
		System.out.println(map.getAwardMovieAnswer(mov));
		System.out.println(map.getCountryMovieAnswer(mov));
		System.out.println(map.getDirectorMovieAnswer(mov));
		System.out.println(map.getGenreMovieAnswer(mov));
		System.out.println(map.getImdbRatingMovieAnswer(mov));
		System.out.println(map.getLangMovieAnswer(mov));
		System.out.println(map.getPlotMovieAnswer(mov));
		System.out.println(map.getReleaseMovieAnswer(mov));
		System.out.println(map.getRuntimeMovieAnswer(mov));
		System.out.println(map.getTitleMovieAnswer(mov));
		System.out.println(map.getYearMovieAnswer(mov));
	}*/
}
