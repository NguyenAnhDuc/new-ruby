/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.topic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import fpt.qa.mdnlib.struct.pair.PairStrDouble;
import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class TopicMap {
    private Map<Integer, String> l2HTopicMap = null;
    private Map<String, List<Integer>> h2LTopicsMap = null;
    private Map<String, List<PairStrDouble>> relatedHTopicsMap = null;
    
    private Map<Integer, String> hLabelInt2StrMap = null;
    private Map<String, Integer> hLabelStr2IntMap = null;

    public TopicMap() {
        l2HTopicMap = new HashMap();
        h2LTopicsMap = new HashMap();
        relatedHTopicsMap = new HashMap();
        
        hLabelInt2StrMap = new HashMap();
        hLabelStr2IntMap = new HashMap();
    }
    
    public TopicMap(String topicMapFile) {
        this();
        
        loadTopicMap(topicMapFile);
    }
    
    private void loadTopicMap(String filename) {
        BufferedReader fin;   
        
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(filename), "UTF8")); 

            String topicLabel = "";

            String line;            
            while ((line = fin.readLine()) != null) {
                line = StrUtil.normalizeStr(line);
                
                if (line.startsWith("Topic:")) {
                    List<String> tokens = StrUtil.tokenizeStr(line, ": \t\r\n");

                    if (tokens.size() < 3) {
                        topicLabel = "";
                        continue;
                    }
                    
                    topicLabel = tokens.get(1);
                    
                    List<Integer> ldaIntTopics = new ArrayList();

                    for (int i = 2; i < tokens.size(); i++) {
                        int ldaTopic = Integer.parseInt(tokens.get(i));
                        
                        l2HTopicMap.put(ldaTopic, topicLabel);
                        ldaIntTopics.add(ldaTopic);
                    }
                    
                    h2LTopicsMap.put(topicLabel, ldaIntTopics);
                    
                    int hLabelInt = hLabelStr2IntMap.size();
                    hLabelStr2IntMap.put(topicLabel, hLabelInt);
                    hLabelInt2StrMap.put(hLabelInt, topicLabel);
                }
                
                if (line.startsWith("%")) {
                    List<String> tokens = StrUtil.tokenizeStr(line);
                    if (tokens.size() != 3) {
                        continue;
                    }
                    
                    PairStrDouble pair = new PairStrDouble(tokens.get(1), 
                            Double.parseDouble(tokens.get(2)));
                    
                    if (!topicLabel.isEmpty()) {
                        List<PairStrDouble> relatedTopics = 
                                relatedHTopicsMap.get(topicLabel);
                        
                        if (relatedTopics != null) {
                            relatedTopics.add(pair);
                        } else {
                            relatedTopics = new ArrayList();
                            relatedTopics.add(pair);
                            relatedHTopicsMap.put(topicLabel, relatedTopics);
                        }
                    }
                }
            }
            
            fin.close();
            
        } catch (IOException ex) {
            Logger.getLogger(LDATopicColl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean hasLDATopic(int ldaTopic) {
        return l2HTopicMap.containsKey(ldaTopic);
    }
    
    public Set<String> getHumanTopics() {
        return h2LTopicsMap.keySet();
    }
    
    public String getHumanTopicByLDATopic(int ldaTopic) {
        return l2HTopicMap.get(ldaTopic);
    }
    
    public List<Integer> getLDATopicsByHumanTopic(String humanTopic) {
        return h2LTopicsMap.get(humanTopic);
    }
    
    public int getNumLDATopicsByHumanTopic(String humanTopic) {
        List<Integer> ldaTopics = h2LTopicsMap.get(humanTopic);
        if (ldaTopics == null || ldaTopics.isEmpty()) {
            return 0;
        } else {
            return ldaTopics.size();
        }
    }
    
    public List<PairStrDouble> getRelatedHumanTopics(String humanTopic) {
        return relatedHTopicsMap.get(humanTopic);
    }
    
    public String getHumanTopicLabelByItsId(int id) {
        return hLabelInt2StrMap.get(id);
    }
    
    public Integer getHumanTopicIdByItsLabel(String label) {
        return hLabelStr2IntMap.get(label);
    }
    
    public void print() {
        for (Map.Entry<String, List<Integer>> entry : h2LTopicsMap.entrySet()) {
            System.out.println("Topic: " + entry.getKey() + ": " + entry.getValue().toString());
            
            List<PairStrDouble> relatedTopics = 
                    relatedHTopicsMap.get(entry.getKey());
            if (relatedTopics != null) {
                for (int i = 0; i < relatedTopics.size(); i++) {
                    PairStrDouble pair = relatedTopics.get(i);
                    System.out.println("% " + pair.first + " " + pair.second);
                }
            }
            
            System.out.println();
        }
    }
}
