package fpt.qa.genreclassifier;

import com.fpt.ruby.business.helper.RedisHelper;
import fpt.qa.mdnlib.struct.pair.Pair;

import java.util.ArrayList;
import java.util.List;

public class GenreExtractor {

	public static ConjGenre conjType;

	public GenreExtractor() {
		if (conjType == null) {
			this.conjType = new ConjGenre((new RedisHelper()).getClass()
					.getClassLoader().getResource("").getPath());
		}
	}

	public GenreExtractor(String resourcePath) {
		this.conjType = new ConjGenre(resourcePath);
	}

	public List<Pair<ArrayList<String>, String>> getNER(String text) {
		return this.conjType.getListRelevantConjunctionsWithType(text);
	}

	public String tagText(String text) {
		text = text.toLowerCase();
		List<Pair<ArrayList<String>, String>> listNer = getNER(text);
		for (Pair<ArrayList<String>, String> p : listNer) {
			for (String s : p.first) {
				if (text.contains(s)) {
					String tag = makeTag(p.second, s);
					text = text.replaceAll(s, tag);
					break;
				}
			}
		}
		return text;
	}

	public List<String> getGenre(String text) {
		text = text.toLowerCase();
		List<String> listType = new ArrayList<>();
		List<Pair<ArrayList<String>, String>> listNer = getNER(text);
		for (Pair<ArrayList<String>, String> p : listNer) {
			if (p.second.trim().equalsIgnoreCase("GENRE")) {
				for (String s : p.first) {
					String genre = conjType.getNameMapper().getFinalName("GENRE",s);
					if (genre != s) {
						listType.add("film:" + genre.toLowerCase());
						break;
					}
				}
			}
		}
		if (listType.isEmpty()) {
			return null;
		}

		return listType;
	}

	public static void main(String[] args) {
		String question = "tối nay chiếu phim hanh dong gì";
		System.out.println(new GenreExtractor().getGenre(question));
		System.out.println(new GenreExtractor().tagText(question));
	}

	public String makeTag(String type, String obj) {
		return "<" + type + ">" + obj + "</" + type + ">";
	}


	public String typeString(String text) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		List<String> listPro = getGenre(text);
		if (listPro==null) return "{}";
		for(int i = 0 ; i < listPro.size()-1;++i) {
			builder.append(listPro.get(i).toString()+",");
		}
		builder.append(listPro.get(listPro.size()-1).toString()+"}");
		return builder.toString();
	}
}
