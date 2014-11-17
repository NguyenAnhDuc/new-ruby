package com.fpt.ruby.business.model;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Document
public class MovieTicket {
    @Id
    String id;
    private String cinema;
    private String movie;
    private String anotherName;
    private String type;
    private Date date;
    private String city;
    public MovieTicket() {
        cinema = null;
        movie = null;
        type = null;
        date = null;
        city = null;
        anotherName = null;
    }

    public String getAnotherName() {
        return anotherName;
    }

    public void setAnotherName(String anotherName) {
        this.anotherName = anotherName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCinema() {
        return cinema;
    }

    public void setCinema(String cinema) {
        this.cinema = cinema;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toShow() {
        StringBuilder result = new StringBuilder().append("{");
        if (cinema != null) result.append("Cinema: " + cinema + ",");
        if (movie != null) result.append("Movie: " + movie + ",");
        result.append("}");
        return result.toString();
    }
}
