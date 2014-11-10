/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.topic.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fpt.qa.mdnlib.struct.pair.PairStrDouble;
import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class Topic {
    private int tid;
    private String tName;
    private List<PairStrDouble> words;
    
    public Topic() {
        tid = -1;
        tName = "";
        words = new ArrayList();
    }
    
    public Topic(int id, String name, List<PairStrDouble> ws) {
        this();
        
        tid = id;
        tName = name;
        
        for (int i = 0; i < ws.size(); i++) {
            words.add(ws.get(i));
        }
    }
    
    public void parseTopic(List<String> lines) {
        words.clear();
        
        // System.out.println(lines.toString());
        
        for (int i = 0; i < lines.size(); i++) {
            List<String> tokens = StrUtil.tokenizeStr(lines.get(i));
            
            if (tokens.size() != 2) {
                continue;
            }
            
            String word = tokens.get(0);
            double weight = Double.parseDouble(tokens.get(1));
            
            words.add(new PairStrDouble(word, weight));
        }
    }

    public int getTopicId() {
        return tid;
    }
    
    public void setTopicId(int id) {
        tid = id;
    }
    
    public String getTopicName() {
        return tName;
    }
    
    public void setTopicName(String name) {
        tName = name;
    }
    
    public int size() {
        return words.size();
    }
    
    public List<PairStrDouble> getWords() {
        return words;
    }
    
    public PairStrDouble getWordAt(int idx) {
        return words.get(idx);
    }
    
    public void addWord(String word, double weight) {
        words.add(new PairStrDouble(word, weight));
    }
    
    public void addWord(PairStrDouble pair) {
        words.add(pair);
    }
    
    public void sort() {
        List<PairStrDouble> tempList = new ArrayList();
        for (int i = 0; i < words.size(); i++) {
            tempList.add(words.get(i));
        }
        
        Collections.sort(tempList);
        
        words.clear();
        for (int i = 0; i < tempList.size(); i++) {
            words.add(tempList.get(i));
        }
    }
    
    public void print() {
        System.out.println("Topic: " + tid + ", Name: " + tName);
        for (int i = 0; i < words.size(); i++) {
            PairStrDouble pair = words.get(i);
            System.out.println("\t" + pair.first + "\t" + pair.second);
        }
        System.out.println();
    }
}
