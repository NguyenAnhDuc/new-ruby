package com.fpt.ruby.commons.helper;

import com.fpt.ruby.commons.basic.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeMapperHelper {
	static List<Pair<Pattern, Integer>> regexes = new ArrayList<Pair<Pattern, Integer>>();
	static Map<Character, Character> convert = new HashMap<>();
	static Integer SAME = 20;
	static Integer MAX_LEN = 40;

	private static String[] to_hop_co_dau = new String[]{"à","á","ạ","ả","ã","â","ầ","ấ","ậ","ẩ","ẫ","ă",
			"ằ","ắ","ặ","ẳ","ẵ","è","é","ẹ","ẻ","ẽ","ê","ề",
			"ế","ệ","ể","ễ",
			"ì","í","ị","ỉ","ĩ",
			"ò","ó","ọ","ỏ","õ","ô","ồ","ố","ộ","ổ","ỗ","ơ",
			"ờ","ớ","ợ","ở","ỡ",
			"ù","ú","ụ","ủ","ũ","ư","ừ","ứ","ự","ử","ữ",
			"ỳ","ý","ỵ","ỷ","ỹ",
			"đ",
			"à","á","ạ","ả","ã","Â","ầ","ấ","ậ","ẩ","ẫ","Ă",
			"ằ","ắ","ặ","ẳ","ẵ",
			"è","é","ẹ","ẻ","ẽ","Ê","ề","ế","ệ","ể","ễ",
			"ì","í","ị","ỉ","ĩ",
			"ò","ó","ọ","ỏ","õ","Ô","ồ","ố","ộ","ổ","ỗ","Ơ","ờ","ớ","ợ","ở","ỡ",
			"ù","ú","ụ","ủ","ũ","Ư","ừ","ứ","ự","ử","ữ",
			"ỳ","ý","ỵ","ỷ","ỹ",
			"Đ"," "
	};

	private static String[] dung_sang_co_dau = new String[]{
			"à", "á", "ạ", "ả", "ã", "â", "ầ", "ấ", "ậ", "ẩ", "ẫ", "ă",
			"ằ", "ắ", "ặ", "ẳ", "ẵ", "è", "é", "ẹ", "ẻ", "ẽ", "ê", "ề",
			"ế", "ệ", "ể", "ễ",
			"ì", "í", "ị", "ỉ", "ĩ",
			"ò", "ó", "ọ", "ỏ", "õ", "ô", "ồ", "ố", "ộ", "ổ", "ỗ", "ơ",
			"ờ", "ớ", "ợ", "ở", "ỡ",
			"ù", "ú", "ụ", "ủ", "ũ", "ư", "ừ", "ứ", "ự", "ử", "ữ",
			"ỳ", "ý", "ỵ", "ỷ", "ỹ",
			"đ",
			"À", "Á", "Ạ", "Ả", "Ã", "Â", "Ầ", "Ấ", "Ậ", "Ẩ", "Ẫ", "Ă",
			"Ằ", "Ắ", "Ặ", "Ẳ", "Ẵ",
			"È", "É", "Ẹ", "Ẻ", "Ẽ", "Ê", "Ề", "Ế", "Ệ", "Ể", "Ễ",
			"Ì", "Í", "Ị", "Ỉ", "Ĩ",
			"Ò", "Ó", "Ọ", "Ỏ", "Õ", "Ô", "Ồ", "Ố", "Ộ", "Ổ", "Ỗ", "Ơ", "Ờ", "Ớ", "Ợ", "Ở", "Ỡ",
			"Ù", "Ú", "Ụ", "Ủ", "Ũ", "Ư", "Ừ", "Ứ", "Ự", "Ử", "Ữ",
			"Ỳ", "Ý", "Ỵ", "Ỷ", "Ỹ",
			"Đ", " "};

	private static String[] khong_dau = new String[]{"a", "a", "a", "a", "a",
			"a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "e",
			"e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "i", "i", "i",
			"i", "i", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o",
			"o", "o", "o", "o", "o", "o", "u", "u", "u", "u", "u", "u", "u",
			"u", "u", "u", "u", "y", "y", "y", "y", "y", "d", "A", "A", "A",
			"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
			"A", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "I",
			"I", "I", "I", "I", "O", "O", "O", "O", "O", "O", "O", "O", "O",
			"O", "O", "O", "O", "O", "O", "O", "O", "U", "U", "U", "U", "U",
			"U", "U", "U", "U", "U", "U", "Y", "Y", "Y", "Y", "Y", "D", " "};

	static {
		regexes.add(new Pair<>(Pattern.compile("((\\w|\\s)+)(tap|phan|episode|ep|eps|t|s|season|so)(\\s*)(\\d+)(((\\s*)(/|\\\\|:)(\\s*)\\d+)*)"), 1));
		regexes.add(new Pair<>(Pattern.compile("((\\w|\\s)+)((\\W|\\s|\\w)+)(tap|phan|episode|ep|eps|t|s|season|so)(\\s*)(\\d+)"), 1));
		regexes.add(new Pair<>(Pattern.compile("((\\w|\\s)+)(\\s*)\\((\\s*)(tap|phan|episode|ep|eps|t|s|season|so)(\\s*)(\\d+)(\\s*)\\)"), 1));
		regexes.add(new Pair<>(Pattern.compile("((\\w|\\s)+)(\\s*)\\((\\s*)(\\d+)(\\s*)(tap|phan|episode|ep|eps|t|s|season|so)(\\s*)\\)"), 1));
		regexes.add(new Pair<>(Pattern.compile("((\\w|\\s)+)(\\s)((\\d+)(\\s*)(/|\\\\|:|-)(\\s*)(\\d+))"), 1));

		for (int i = 0; i < khong_dau.length; ++i) {
			convert.put(dung_sang_co_dau[i].charAt(0), khong_dau[i].charAt(0));
			convert.put(to_hop_co_dau[i].charAt(0), khong_dau[i].charAt(0));
		}
	}

	public static String normalize(String name) {
		name = name.toLowerCase().trim();

		String[] parts = name.split("\\s+");
		name = String.join(" ", parts);
		StringBuilder norm = new StringBuilder();
		for (int i = 0; i < name.length(); ++i) {
			Character n = (convert.containsKey(name.charAt(i)) ? convert.get(name.charAt(i)) : name.charAt(i));
			norm.append(n);
		}

		return norm.toString().toLowerCase();
	}

	public static String getTitle(String mess) {
		if (mess.length() >= MAX_LEN) mess = mess.substring(0, MAX_LEN - 1);

		mess = normalize(mess);
		String title = mess;

		for (Pair<Pattern, Integer> pat : regexes) {
			Matcher m = pat.first.matcher(mess);
			Boolean b = m.matches();
			if (b) {
				String curTitle = m.group(pat.second);
				if (curTitle.isEmpty()) continue;
				if (title.length() > curTitle.length()) {
					title = curTitle;
				}
			}
		}

		return title;
	}

	public static boolean isSame(String origin, String variant) {
		if (contains(origin, variant)) return true;
		variant = variant.substring(0, Math.min(variant.length(), SAME));
		origin = origin.substring(0, Math.min(origin.length(), SAME));
		return variant.equals(origin);
	}

	// Return true if a contains b or b contains a
	public static boolean contains(String a, String b) {
		if (a.length() <= b.length()) return b.contains(a);
		return a.contains(b);
	}

}
