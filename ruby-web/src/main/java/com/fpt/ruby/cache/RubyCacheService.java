package com.fpt.ruby.cache;

/**
 * Created by quang on 12/2/2014.
 */
public interface RubyCacheService {
    public void cache(RubyCache rubyCache);
    public RubyCache getCache(String key);
    public void saveToDb(RubyCache rubyCache);
    public void loadFromDbToCache();
}
