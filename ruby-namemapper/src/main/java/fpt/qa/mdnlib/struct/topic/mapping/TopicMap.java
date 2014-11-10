/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.topic.mapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class TopicMap {
    private String tName;
    private Set<Integer> tids;
    
    public TopicMap() {
        tName = "";
        tids = new HashSet();
    } 
    
    public TopicMap(String name, List ids) {
        tName = name;
        for (int i = 0; i < ids.size(); i++) {
            tids.add((Integer)ids.get(i));
        }
    }
    
    public String getTopicName() {
        return tName;
    }
    
    public void setTopicName(String name) {
        tName = name;
    }
    
    public List<Integer> getTopicIds() {
        List<Integer> results = new ArrayList();
        for (Integer i : tids) {
            results.add(i);
        }
        return results;
    }
    
    public void addTopicId(int tid) {
        tids.add(tid);
    }
    
    public void parseTopicMap(String line) {
        List<String> tokens = StrUtil.tokenizeStr(line, "\t\r\n :");
        
        if (tokens.size() < 2) {
            return;
        }
        
        setTopicName(tokens.get(0));
        for (int i = 1; i < tokens.size(); i++) {
            addTopicId(Integer.parseInt(tokens.get(i)));
        }
    }
}
