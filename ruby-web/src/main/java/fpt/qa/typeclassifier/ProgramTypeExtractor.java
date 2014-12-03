package fpt.qa.typeclassifier;

import java.util.ArrayList;
import java.util.List;

import com.fpt.ruby.business.constants.ProgramType;
import com.fpt.ruby.business.helper.RedisHelper;

import fpt.qa.mdnlib.struct.pair.Pair;

public class ProgramTypeExtractor {

	public static ConjType conjType;

	public ProgramTypeExtractor() {
		if (conjType == null) {
			this.conjType = new ConjType((new RedisHelper()).getClass()
					.getClassLoader().getResource("").getPath());
		}
	}

	public ProgramTypeExtractor(String resourcePath) {
		this.conjType = new ConjType(resourcePath);
	}

	public List<Pair<ArrayList<String>, String>> getNER(String text) {
		return this.conjType.getListRelevantConjunctionsWithType(text);
	}

	public String tagText(String text) {
		text = lowecase(text);
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

	public ProgramType getType(String text) {
		text = lowecase(text);
		List<Pair<ArrayList<String>, String>> listNer = getNER(text);
		for (Pair<ArrayList<String>, String> p : listNer) {
			if (p.second.equalsIgnoreCase("TYPE")) {
				for (String s : p.first) {
					ProgramType type = ProgramType.getType(s.trim());
					if (!type.equals(ProgramType.OTHER)) {
						return type;
					}
				}
			}
		}
		return ProgramType.OTHER;
	}

	public List<ProgramType> getTypes(String text) {
		text = lowecase(text);
		List<ProgramType> listType = new ArrayList<>();
		List<Pair<ArrayList<String>, String>> listNer = getNER(text);
		for (Pair<ArrayList<String>, String> p : listNer) {
			if (p.second.trim().equalsIgnoreCase("TYPE")) {
				for (String s : p.first) {
					ProgramType type = ProgramType.getType(s);
					if (!type.equals(ProgramType.OTHER)) {
						listType.add(type);
						break;
					}
				}
			}
		}
		if (listType.isEmpty()) {
			return null;
		}

		if(listType.size()>1 && listType.contains(ProgramType.ENTERTAINMENT)) {
			listType.remove(ProgramType.ENTERTAINMENT);
		}

		if (listType.contains(ProgramType.ENTERTAINMENT)) {
			listType.add(ProgramType.CARTOON);
			listType.add(ProgramType.MUSIC);
			listType.add(ProgramType.FILM);
			listType.add(ProgramType.FASHION);
			listType.add(ProgramType.GAMESHOW);
		}

		if (listType.contains(ProgramType.SPORT)) {
			listType.add(ProgramType.FOOTBALL);
			listType.add(ProgramType.GOLF);
			listType.add(ProgramType.TENNIS);
		}

		return listType;
	}

	public static void main(String[] args) {
		String question = "tối nay chiếu phim hoạt hình";
		System.out.println(new ProgramTypeExtractor().getTypes(question));
		System.out.println(new ProgramTypeExtractor().tagText(question));
	}

	public String makeTag(String type, String obj) {
		return "<" + type + ">" + obj + "</" + type + ">";
	}

	private String lowecase(String text) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			sb.append(Character.toLowerCase(text.charAt(i)));
		}
		return sb.toString();
	}

	public String typeString(String text) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		List<ProgramType> listPro = getTypes(text);
		if (listPro==null) return "{}";
		for(int i = 0 ; i < listPro.size()-1;++i) {
			builder.append(listPro.get(i).toString()+",");
		}
		builder.append(listPro.get(listPro.size()-1).toString()+"}");
		return builder.toString();
	}
}
