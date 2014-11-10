package com.fpt.ruby.nlp;

import java.util.List;

import com.fpt.ruby.business.model.MovieTicket;

public interface TicketAnswerMapper {
	String getTypeTicketAnswer(List<MovieTicket> ans);
	String getTitleTicketAnswer(List<MovieTicket> ans);
	String getCinemaTicketAnswer(List<MovieTicket> ans);
	String getDateTicketAnswer(List<MovieTicket> ans, MovieTicket matchMovieTicket, boolean haveTimeInfo);
}
