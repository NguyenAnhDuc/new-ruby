package com.fpt.ruby.commons.entity.modifiers;

/**
 * Created by quang on 12/2/2014.
 */
public class MovieModifiers extends RubyModifiers {
    private String movieTitle;
    private String cinName;
    public MovieModifiers(){
        movieTitle = null;
        cinName = null;
    }
    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getCinName() {
        return cinName;
    }

    public void setCinName(String cinName) {
        this.cinName = cinName;
    }
}
