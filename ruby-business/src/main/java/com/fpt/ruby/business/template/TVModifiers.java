package com.fpt.ruby.business.template;

import java.util.List;

/**
 * Created by quang on 12/2/2014.
 */
public class TVModifiers extends RubyModifiers{
    private String tvTitle;
    private String tvChannel;
    private List<String> types;
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
}
