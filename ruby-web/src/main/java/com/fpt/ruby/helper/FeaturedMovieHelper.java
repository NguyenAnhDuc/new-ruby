package com.fpt.ruby.helper;

import java.util.ArrayList;
import java.util.List;

import com.fpt.ruby.business.model.MovieFly;
import com.fpt.ruby.nlp.AnswerMapper;

public class FeaturedMovieHelper {
	
	
	public static String filterByDirector(String director, List<MovieFly> movieFlies){
		System.out.println("Filter by Director: " + director);
		String movTitles = "";
		for (MovieFly mf : movieFlies){
			if (mf.getDirector() != null && mf.getDirector().contains(director)){
				movTitles += mf.getTitle() + ", ";
			}
		}
		
		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return movTitles.substring(0, movTitles.length() - 2);
	}
	
	public static String filterByActor(String actor, List<MovieFly> movieFlies){
		List<String> movieNames = new ArrayList<String>();
		System.out.println("Filter by Actor: " + actor);
		String movTitles = "";
		for (MovieFly mf : movieFlies){
			if (mf.getActor().contains(actor)){
				if (!movieNames.contains(mf.getTitle())){
					movTitles += mf.getTitle() + "</br>";
					movieNames.add(mf.getTitle());
				}
			}
		}

		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return movTitles.substring(0, movTitles.length() - 2);
	}
	
	public static String filterByImdb(List<MovieFly> movieFlies){
		System.out.println("Filter by ImDB");
		float highest = -1;
		String title = "";
		for (MovieFly mf : movieFlies){
			if (mf.getImdbRating() > highest){
				highest = mf.getImdbRating();
				title = mf.getTitle();
			}
		}

		if (title.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return title + " có imdb rating cao nhất";
	}
	
	public static String filterByCountry(String country, List<MovieFly> movieFlies){
		List<String> movieNames = new ArrayList<String>();
		System.out.println("Filter by Country: " + country);
		String movTitles = "";
		for (MovieFly mf : movieFlies){
			if (mf.getCountry() != null && mf.getCountry().equals(country)){
				if (!movieNames.contains(mf.getTitle())){
					movTitles += mf.getTitle() + "</br>";
					movieNames.add(mf.getTitle());
				}
			}
		}

		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		return movTitles.substring(0, movTitles.length() - 2);
	}
	
	public static String filterByLang(String lang, List<MovieFly> movieFlies){
		System.out.println("Filter by Lang: " + lang);
		String movTitles = "";
		for (MovieFly mf : movieFlies){
			if (mf.getLanguage() != null && mf.getLanguage().equals(lang)){
				movTitles += mf.getTitle() + ", ";
			}
		}
		
		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return movTitles.substring(0, movTitles.length() - 2);
	}
	
	public static String filterByGenre(List<String> genre, List<MovieFly> movieFlies){
		System.out.println("Filter by Genre: " + genre);
		System.out.println(movieFlies.size() + " " + genre.size());
		List<String> movieNames = new ArrayList<String>();
		String movTitles = "";
		for (MovieFly mf : movieFlies){
			String movGen = mf.getGenre() != null ? mf.getGenre().toLowerCase() : null;
			boolean satisfied = true;
			for (String gen : genre){
				if (movGen != null && !movGen.contains(gen)){
					satisfied = false;
					break;
				}
			}
			if (satisfied){
				if (!movieNames.contains(mf.getTitle())){
					movTitles += mf.getTitle() + "</br>";
					movieNames.add(mf.getTitle());
				}

			}
		}

		if (movTitles.isEmpty()){
			return "Xin lỗi, chúng tôi không tìm thấy phim như thế trong cơ sở dữ liệu";
		}

		return movTitles.substring(0, movTitles.length() - 2);
	}
	
	public static String filterByImdbAndGenre(List<String> genre, List<MovieFly> movieFlies){
		System.out.println("Filter by Genre and Imdb: " + genre);
		String movTitles = "";
		float highestImdb = -1;
		
		for (MovieFly mf : movieFlies){
			String movGen = mf.getGenre() != null ? mf.getGenre().toLowerCase() : null;
			boolean satisfied = true;
			for (String gen : genre){
				if (movGen != null && !movGen.contains(gen)){
					satisfied = false;
					break;
				}
			}
			if (satisfied){
				if (mf.getImdbRating() > highestImdb){
					highestImdb = mf.getImdbRating();
					movTitles = mf.getTitle();
				}
			}
		}
		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return movTitles;
	}
	
	public static String filterByImdbAndCountry(String country, List<MovieFly> movieFlies){
		System.out.println("Filter by Country and Imdb: " + country);
		String movTitles = "";
		float highestImdb = -1;
		
		for (MovieFly mf : movieFlies){
			if (mf.getCountry() != null && mf.getCountry().equals(country)){
				if (mf.getImdbRating() > highestImdb){
					highestImdb = mf.getImdbRating();
					movTitles = mf.getTitle();
				}
			}
		}
		
		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return movTitles;
	}
	
	public static String filterByAward(String award, List<MovieFly> movieFlies){
		String movTitles = "";
		for (MovieFly mf : movieFlies){
			if (mf.getAwards() != null){
				movTitles += mf.getTitle() + ", ";
			}
		}

		if (movTitles.isEmpty()){
			return AnswerMapper.Default_Answer;
		}
		
		return movTitles.substring(0, movTitles.length() - 2) + " đã nhận được giải thưởng";
	}
	
}
