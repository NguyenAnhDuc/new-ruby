package com.fpt.ruby.template;

import com.fpt.ruby.business.model.TimeExtract;
import org.springframework.data.annotation.Id;

/**
 * Created by quang on 12/2/2014.
 */
public class RubyAnswer {
    @Id
    private String id;
    private String question;
    private String domain;
    private String intent;
    private TimeExtract timeExtract;
    private RubyModifiers rubyModifiers;
    private String answer;

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

    public TimeExtract getTimeExtract() {
        return timeExtract;
    }

    public void setTimeExtract(TimeExtract timeExtract) {
        this.timeExtract = timeExtract;
    }

    public void setRubyModifiers(RubyModifiers rubyModifiers) {
        this.rubyModifiers = rubyModifiers;
    }

    public RubyModifiers getRubyModifiers() {
        return rubyModifiers;
    }

    public void RubyModifiers(RubyModifiers rubyModifiers) {
        this.rubyModifiers = rubyModifiers;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
