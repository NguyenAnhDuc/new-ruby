package com.fpt.ruby.cache;

import org.springframework.data.annotation.Id;

/**
 * Created by quang on 12/2/2014.
 */
public class RubyCache {
    @Id
    private String id;
    private String question;
    private String domain;
    private String intent;
    private String timeString;
    private TVModifiers tvModifiers;
    private MovieModifiers movieModifiers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public TVModifiers getTvModifiers() {
        return tvModifiers;
    }

    public void setTvModifiers(TVModifiers tvModifiers) {
        this.tvModifiers = tvModifiers;
    }

    public MovieModifiers getMovieModifiers() {
        return movieModifiers;
    }

    public void setMovieModifiers(MovieModifiers movieModifiers) {
        this.movieModifiers = movieModifiers;
    }
}
