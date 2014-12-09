package com.fpt.ruby.nlp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MovieModifiers {
	
	public static Map<String, String> genreMap = new HashMap<String, String>();
	public static Map<String, String> countryMap = new HashMap<String, String>();
	public static Map<String, String> actorMap = new HashMap<String, String>();
	public static Map<String, String> directorMap = new HashMap<String, String>();
	public static Map<String, String> langMap = new HashMap<String, String>();
	
	static {
		String dir = (new MovieModifiers()).getClass().getClassLoader().getResource("").getPath() + "/dicts/";
		initGenreMap(dir);
		initCountryMap(dir);
		initActorsMap(dir);
		initDirectorsMap(dir);
		initLangMap(dir);
		
		dir = dir + "non-diacritic/";
		initGenreMap(dir);
		initCountryMap(dir);
		initActorsMap(dir);
		initDirectorsMap(dir);
		initLangMap(dir);
	}
	
	private String title;
	private List<String> genre;
	private String actor;
	private String director;
	private String country;
	private String lang;
	private String audience;
	private String award;
	private String cin_name;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getGenre() {
		return genre;
	}
	public void setGenre(List<String> genre) {
		this.genre = genre;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getAudience() {
		return audience;
	}
	public void setAudience(String audience) {
		this.audience = audience;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public String getCin_name() {
		return cin_name;
	}
	public void setCin_name(String cin_name) {
		this.cin_name = cin_name;
	}
	
	public boolean atLeastOneOtherFeatureNotNull(){
		if (!genre.isEmpty() || actor != null || director != null || lang != null){
			return true;
		}
		
		if (country != null || award != null || audience != null){
			return true;
		}
		
		return false;
	}
	

	public static MovieModifiers getModifiers(String question){
		MovieModifiers res = new MovieModifiers();
		
		List<String> ngrams = getNGrams(question);
		res.setActor(getActor(ngrams));
		res.setDirector(getDirector(ngrams));
		res.setCountry(getCountry(ngrams));
		res.setGenre(getGenreList(ngrams));
		
		return res;
	}
	
	
	private static List<String> getGenreList(List<String> ngrams){
		List<String> genList  = new ArrayList<String>();
		
		for (String ngram : ngrams){
			String enGenre = genreMap.get(ngram);
			if (enGenre != null){
				genList.add(enGenre);
			}
		}
		
		return genList;
	}
	
	private static String getCountry(List<String> ngrams){
		for (String ngram : ngrams){
			String enCountry = countryMap.get(ngram);
			if (enCountry != null){
				return enCountry;
			}
		}
		return null;
	}
	
	
	private static String getActor(List<String> ngrams){
		for (String ngram : ngrams){
			String actor = actorMap.get(ngram);
			if (actor != null){
				return actor;
			}
		}
		return null;
	}
	
	private static String getDirector(List<String> ngrams){
		for (String ngram : ngrams){
			String director = directorMap.get(ngram);
			if (director != null){
				return director;
			}
		}
		return null;
	}

	private static void initGenreMap(String dir){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir + "genreMap.txt"));
			String line;
			while((line = reader.readLine()) != null){
				int idx = line.indexOf("\t");
				if (line.isEmpty() || idx < 0){
					continue;
				}
				String key = line.substring(0, idx).toLowerCase();
				String val = line.substring(idx + 1);
				genreMap.put(key, val);
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void initCountryMap(String dir){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir + "countryMap.txt"));
			String line;
			while((line = reader.readLine()) != null){
				int idx = line.indexOf("\t");
				if (line.isEmpty() || idx < 0){
					continue;
				}
				String key = line.substring(0, idx).toLowerCase();
				String val = line.substring(idx + 1);
				countryMap.put(key, val);
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void initActorsMap(String dir){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir + "actors.txt"));
			String line;
			while((line = reader.readLine()) != null){
				if (line.isEmpty()){
					continue;
				}
				actorMap.put(line.toLowerCase(), line);
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void initDirectorsMap(String dir){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir + "directors.txt"));
			String line;
			while((line = reader.readLine()) != null){
				if (line.isEmpty()){
					continue;
				}
				directorMap.put(line.toLowerCase(), line);
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void initLangMap(String dir){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dir + "languageMap.txt"));
			String line;
			while((line = reader.readLine()) != null){
				if (line.isEmpty()){
					continue;
				}
				langMap.put(line.toLowerCase(), line);
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// get 1-4grams from the input question
	private static List<String> getNGrams(String question){
		List<String> res = new ArrayList<String>();
		String lowercaseQuestion = question.toLowerCase();
		res.addAll(getUnigrams(lowercaseQuestion));
		res.addAll(getBigrams(lowercaseQuestion));
		res.addAll(getTrigrams(lowercaseQuestion));
		res.addAll(get4Grams(lowercaseQuestion));
		return res;
	}
	
	private static List<String> getUnigrams(String question){
		List<String> res = new ArrayList<String>();
		String[] toks = question.split("\\s");
		for (String tok : toks){
			res.add(tok);
		}
		return res;
	}
	
	private static List<String> getBigrams(String question){
		List<String> res = new ArrayList<String>();
		String[] toks = question.split("\\s");
		if (toks.length < 2){
			return res;
		}
		
		for (int i = 0; i < toks.length - 1; i++){
			for (int j = i+1; j < toks.length; j++){
				res.add(toks[i] + " " + toks[j]);
			}
		}
		return res;
	}
	
	private static List<String> getTrigrams(String question){
		List<String> res = new ArrayList<String>();
		String[] toks = question.split("\\s");
		if (toks.length < 3){
			return res;
		}
		
		for (int i = 0; i < toks.length - 2; i++){
			for (int j = i+1; j < toks.length - 1; j++){
				for (int k = j+1; k < toks.length; k++){
					res.add(toks[i] + " " + toks[j] + " " + toks[k]);
				}
			}
		}
		return res;
	}
	
	private static List<String> get4Grams(String question){
		List<String> res = new ArrayList<String>();
		String[] toks = question.split("\\s");
		if (toks.length < 4){
			return res;
		}
		
		for (int i = 0; i < toks.length - 3; i++){
			for (int j = i+1; j < toks.length - 2; j++){
				for (int k = j+1; k < toks.length - 1; k++){
					for (int l = k+1; l < toks.length; l++){
						res.add(toks[i] + " " + toks[j] + " " + toks[k] + " "
								+ toks[l]);
					}
				}
			}
		}
		return res;
	}
}