package com.fpt.ruby.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by quang on 12/2/2014.
 */
public class CacheHelper {
    private Gson gson;

    public CacheHelper(){
        gson = new GsonBuilder().create();
    }

    public String normalizeQuestion(String ques){
        String question = ques.toLowerCase();
        if(question.isEmpty()){
            return "";
        }
        int j = question.length() - 1;
        // remove question mark or special character
        for (; j >= 0; j--){
            char c = question.charAt(j);
            if (Character.isLetter(c) || Character.isDigit(c)){
                break;
            }
        }
        return question.toLowerCase().substring(0,j+1);
    }

    public String toJsonString(RubyCache rubyCache){
        return gson.toJson(rubyCache).toString();
    }

    public RubyCache getRubyCache(String jsonString){
        return gson.fromJson(jsonString,RubyCache.class);
    }

    public static void main(String[] args){
        RubyCache rubyCache = new RubyCache();
        rubyCache.setQuestion("test question");
        rubyCache.setDomain("test Domain");
        rubyCache.setIntent("test Intent");
        MovieModifiers movieModifiers = new MovieModifiers();
        movieModifiers.setCinName("test Cinema");
        movieModifiers.setMovieTitle("test movie title");
        rubyCache.setMovieModifiers(movieModifiers);

        CacheHelper cacheHelper = new CacheHelper();

        String jsonString = cacheHelper.toJsonString(rubyCache);
        System.out.println("json string: " + jsonString);
        RubyCache rubyCache1 = cacheHelper.getRubyCache(jsonString);
        System.out.println(rubyCache1.getDomain());
        //System.out.println(toJsonString(rubyCache));
    }
}
