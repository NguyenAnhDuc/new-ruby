package com.fpt.ruby.commons.entity.modifiers;

import java.util.Date;
import java.util.List;

/**
 * Created by quang on 12/2/2014.
 */
public class TVModifiers extends RubyModifiers{
    private String tvTitle;
    private String tvChannel;
    private List<String> types;
    private Date start;
    private Date end;

    public String getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }

    public String getTvChannel() {
        return tvChannel;
    }

    public void setTvChannel(String tvChannel) {
        this.tvChannel = tvChannel;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
