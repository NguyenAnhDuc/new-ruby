/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.topicmatching2;

import java.util.*;

import fpt.qa.mdnlib.struct.pair.PairStrDouble;

/**
 *
 * @author hieupx
 */
public class LDATopic {
    private int tId;
    private Map<String, Double> tWords;
    
    public LDATopic() {
        tId = -1;
        tWords = new HashMap();
    }
    
    public LDATopic(int id) {
        this();        
        tId = id;
    }
    
    public LDATopic(int id, List<PairStrDouble> words) {
        this(id);

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
        System.out.println("Topic: " + tId);
        
        List<PairStrDouble> words = getTopicWords();
        Collections.sort(words);
        
        for (int i = words.size() - 1; i >= 0; i--) {
            PairStrDouble pair = words.get(i);
            System.out.println("\t" + pair.first + "\t" + pair.second);
        }
    }
}
