/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.concept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.qa.mdnlib.struct.pair.PairStrDouble;

/**
 *
 * @author hieupx
 */
public class Concept {
    private int cId;
    private String cLabel;
    private Map<String, Double> cWords;
    
    public Concept() {
        cId = -1;
        cLabel = "";
        cWords = new HashMap<String, Double>();
    }
    
    public Concept(int id, String label) {
        this();
        
        cId = id;
        cLabel = label;
    }
    
    public Concept(int id, String label, List<PairStrDouble> words) {
        this();
        
        cId = id;
        cLabel = label;
        
        for (int i = 0; i < words.size(); i++) {
            PairStrDouble word = words.get(i);
            cWords.put(word.first, word.second);
        }
    }
    
    public int getConceptId() {
        return cId;
    }
    
    public void setConceptId(int id) {
        cId = id;
    }
    
    public String getConceptLabel() {
        return cLabel;
    }
    
    public void setConceptLabel(String label) {
        cLabel = label;
    }
    
    public List<PairStrDouble> getConceptWords() {
        List<PairStrDouble> words = new ArrayList();
        
        for (Map.Entry<String, Double> entry : cWords.entrySet()) {
            words.add(new PairStrDouble(entry.getKey(), entry.getValue()));
        }
        
        return words;
    }
    
    public Map<String, Double> getConceptWordMap() {
        return cWords;
    }
    
    public void setConceptWords(List<PairStrDouble> words) {
        cWords.clear();
        
        for (int i = 0; i < words.size(); i++) {
            PairStrDouble word = words.get(i);
            cWords.put(word.first, word.second);
        }
    }
    
    public void addConceptWord(String word, double weight) {
        cWords.put(word, weight);
    }
    
    public void print() {
        System.out.println(cLabel + " (" + cId + "):");
        for (Map.Entry<String, Double> entry : cWords.entrySet()) {
            System.out.println("\t" + entry.getKey() + "\t" + entry.getValue());
        }
    }
    
    public void printForNLPService() {
        System.out.println(cLabel + ":");
        for (Map.Entry<String, Double> entry : cWords.entrySet()) {
            System.out.println("\t" + entry.getKey());
        }
    }
}
