package com.fpt.ruby.helper;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class ExtractHelper {
	/*public Set<Genre> extractGenre(JSONArray jsonArray, GenreService genreService){
		Set<Genre> genres = new HashSet<Genre>();
		for (int i=0;i<jsonArray.length();i++){
			int id = jsonArray.getJSONObject(i).getInt("id");
			Genre genre = genreService.findGenreById(id);
			if (genre == null) {
				genre = new Genre();
				genre.setId(id);
				genre.setName(jsonArray.getJSONObject(i).getString("name"));
			}
			genres.add(genre);
		}
		return genres;
	}
	
	public Set<ProductionCompany> extractProductionCompany(JSONArray jsonArray, ProductionCompanyService productionCompanyService){
		Set<ProductionCompany> productionCompanies = new HashSet<ProductionCompany>();
		for (int i=0;i<jsonArray.length();i++){
			int id = jsonArray.getJSONObject(i).getInt("id");
			ProductionCompany productionCompany = productionCompanyService.findProductionCompanyById(id);
			if (productionCompany == null){
				productionCompany = new ProductionCompany();
				productionCompany.setId(id);
				productionCompany.setName(jsonArray.getJSONObject(i).getString("name"));
			}
			productionCompanies.add(productionCompany);
		}
		return productionCompanies;
	}
	
	public Set<ProductionCountry> extractProductionCountry(JSONArray jsonArray, ProductionCountryService productionCountryService){
		Set<ProductionCountry> productionCountries = new HashSet<ProductionCountry>();
		for (int i=0;i<jsonArray.length();i++){
			String iso_3166_1 = jsonArray.getJSONObject(i).getString("iso_3166_1");
			ProductionCountry productionCountry = productionCountryService.findProductionCountryByIso_3166_1(iso_3166_1); 
			if (productionCountry == null){
				productionCountry = new ProductionCountry();
				productionCountry.setIso_3166_1(iso_3166_1);
				productionCountry.setName(jsonArray.getJSONObject(i).getString("name"));
			}
			productionCountries.add(productionCountry);
		}
		return productionCountries;
	}
	
	public Set<SpokenLanguage> extractSpokenLanguage(JSONArray jsonArray, SpokenLanguageService spokenLanguageService){
		Set<SpokenLanguage> spokenLanguages = new HashSet<SpokenLanguage>();
		for (int i=0;i<jsonArray.length();i++){
			String iso_639_1 = jsonArray.getJSONObject(i).getString("iso_639_1");
			SpokenLanguage spokenLanguage = spokenLanguageService.findSpokenLanguageByIso_639_1(iso_639_1);
			if (spokenLanguage == null){
				spokenLanguage = new SpokenLanguage();
				spokenLanguage.setIso_639_1(iso_639_1);
				spokenLanguage.setName(jsonArray.getJSONObject(i).getString("name"));
			}
			spokenLanguages.add(spokenLanguage);
		}
		return spokenLanguages;
	}*/
	
	public Date extractDate(String jsonString) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = sdf.parse(jsonString);
		Date sqlDate = new Date(date.getTime());
		return sqlDate;
	}
	
	public static void main(String[] args) throws ParseException{
		ExtractHelper extractHelper = new ExtractHelper();
		String dateString = "2001-03-09";
		System.out.println(extractHelper.extractDate(dateString));
	}
}
