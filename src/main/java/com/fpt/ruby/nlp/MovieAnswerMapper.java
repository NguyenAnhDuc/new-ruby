package com.fpt.ruby.nlp;

import java.util.List;

import com.fpt.ruby.business.model.Cinema;
import com.fpt.ruby.business.model.MovieFly;

public interface MovieAnswerMapper{
	String getTitleMovieAnswer(List<MovieFly> ans);
	String getGenreMovieAnswer(List<MovieFly> ans);
	String getActorMovieAnswer(List<MovieFly> ans);
	String getDirectorMovieAnswer(List<MovieFly> ans);
	String getLangMovieAnswer(List<MovieFly> ans);
	String getCountryMovieAnswer(List<MovieFly> ans);
	String getAwardMovieAnswer(List<MovieFly> ans);
	String getPlotMovieAnswer(List<MovieFly> ans);
	String getYearMovieAnswer(List<MovieFly> ans);
	String getRuntimeMovieAnswer(List<MovieFly> ans);
	String getAudienceMovieAnswer(List<MovieFly> ans);
	String getReleaseMovieAnswer(List<MovieFly> ans);
	String getImdbRatingMovieAnswer(List<MovieFly> ans);
	String getCinemaAddressAnswer(List<Cinema> ans);
}