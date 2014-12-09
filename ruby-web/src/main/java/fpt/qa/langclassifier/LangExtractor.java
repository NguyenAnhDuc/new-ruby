package fpt.qa.langclassifier;

import fpt.qa.mdnlib.struct.pair.Pair;

import java.util.ArrayList;
import java.util.List;

public class LangExtractor {

	public static ConjLang conjType;

	public LangExtractor() {
		if (conjType == null) {
			this.conjType = new ConjLang((new LangExtractor()).getClass()
					.getClassLoader().getResource("").getPath());
		}
	}

	public LangExtractor(String resourcePath) {
		this.conjType = new ConjLang(resourcePath);
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

	public List<String> getLanguage(String text) {
		text = text.toLowerCase();
		List<String> listType = new ArrayList<>();
		List<Pair<ArrayList<String>, String>> listNer = getNER(text);
		for (Pair<ArrayList<String>, String> p : listNer) {
			if (p.second.trim().equalsIgnoreCase("LANG")) {
				for (String s : p.first) {
					String lang = conjType.getNameMapper().getFinalName("LANG",s);
					if (lang != s) {
						listType.add("film:" + lang.toLowerCase());
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
		String question = "tối nay chiếu phim mỹ gì";
		System.out.println(new LangExtractor().getLanguage(question));
		System.out.println(new LangExtractor().tagText(question));
	}

	public String makeTag(String type, String obj) {
		return "<" + type + ">" + obj + "</" + type + ">";
	}


	public String typeString(String text) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		List<String> listPro = getLanguage(text);
		if (listPro==null) return "{}";
		for(int i = 0 ; i < listPro.size()-1;++i) {
			builder.append(listPro.get(i).toString()+",");
		}
		builder.append(listPro.get(listPro.size()-1).toString()+"}");
		return builder.toString();
	}
}
