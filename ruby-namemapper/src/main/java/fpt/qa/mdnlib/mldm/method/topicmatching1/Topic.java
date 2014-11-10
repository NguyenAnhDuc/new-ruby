/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.topicmatching1;

import java.util.*;

import fpt.qa.mdnlib.struct.pair.PairStrDouble;

/**
 *
 * @author hieupx
 */
public class Topic {
    private int tId;
    private String tLabel;
    private Map<String, Double> tWords;
    
    public Topic() {
        tId = -1;
        tLabel = "";
        tWords = new HashMap();
    }
    
    public Topic(int id, String label) {
        this();
        
        tId = id;
        tLabel = label;
    }
    
    public Topic(int id, String label, List<PairStrDouble> words) {
        this(id, label);

        for (int i = 0; i < words.size(); i++) {
            PairStrDouble word = words.get(i);
            tWords.put(word.first, word.second);
        }
    }
    
    public int getTopicId() {
        return tId;
    }
    
    public void setTopicId(int id) {
        tId = id;
    }
    
    public String getTopicLabel() {
        return tLabel;
    }
    
    public void setTopicLabel(String label) {
        tLabel = label;
    }
    
    public List<PairStrDouble> getTopicWords() {
        List<PairStrDouble> words = new ArrayList();
        
        for (Map.Entry<String, Double> entry : tWords.entrySet()) {
            words.add(new PairStrDouble(entry.getKey(), entry.getValue()));
        }
        
        return words;
    }
    
    public Map<String, Double> getTopicWordMap() {
        return tWords;
    }
    
    public void setTopicWords(List<PairStrDouble> words) {
        tWords.clear();
        
        for (int i = 0; i < words.size(); i++) {
            PairStrDouble word = words.get(i);
            tWords.put(word.first, word.second);
        }
    }
    
    public void addTopicWord(String word, double weight) {
        tWords.put(word, weight);
    }
    
    public void print() {
        System.out.println("Topic: " + tId + ", Label: " + tLabel);
        
        List<PairStrDouble> words = getTopicWords();
        Collections.sort(words);
        
        for (int i = words.size() - 1; i >= 0; i--) {
            PairStrDouble pair = words.get(i);
            System.out.println("\t" + pair.first + "\t" + pair.second);
        }
    }
}
