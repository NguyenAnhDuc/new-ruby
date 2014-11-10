/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.topic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import fpt.qa.mdnlib.struct.pair.PairStrDouble;
import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public final class LDATopicColl {
    private TopicMap topicMap = null;
    private boolean positionBasedWeight;
    private List<Topic> ldaTopics;
    private Map<Integer, Integer> ldaTopicsDict = null;
    
    public LDATopicColl() {
        topicMap = null;
        positionBasedWeight = false;        
        ldaTopics = new ArrayList();
        ldaTopicsDict = new HashMap();
    }
    
    public LDATopicColl(TopicMap topicMap, boolean positionBasedWeight) {
        this();
        
        this.topicMap = topicMap;
        this.positionBasedWeight = positionBasedWeight;
    }
    
    public LDATopicColl(TopicMap topicMap, boolean positionBasedWeight,
            String ldaTopicWordsFile) {
        this(topicMap, positionBasedWeight);
        
        loadLocalLDATopics(ldaTopicWordsFile);
        buildLDATopicsDict();
    }
    
    public int size() {
        return ldaTopics.size();
    }
    
    public void clear() {
        ldaTopics.clear();
    }
    
    public Topic getLDATopicAt(int idx) {
        return ldaTopics.get(idx);
    }
    
    public Topic getLDATopicByTopicId(int tId) {
        Integer idx = ldaTopicsDict.get(tId);
        
        if (idx != null) {
            return ldaTopics.get(idx);
        }
        
        return null;
    }
    
    public void addLDATopic(Topic ldaTopic) {
        ldaTopics.add(ldaTopic);
    }
    
    public List<Topic> getLDATopics() {
        return ldaTopics;
    }
    
    public void addNewLDATopic(int id, List<PairStrDouble> words) {
        Topic ldaTopic = new Topic(id);

        int count = words.size();
        if (count > Constants4Topic.MaxNumWordsPerLDATopic) {
            count = Constants4Topic.MaxNumWordsPerLDATopic;
        }
        
        for (int i = 0; i < count; i++) {
            PairStrDouble pair = words.get(i);
            
            double weight = pair.second;
            if (positionBasedWeight) {
                weight = Constants4Topic.MaxWordWeightforLDATopic - Math.log10(i + 2);
                weight = Math.log(weight + 2);
            }
            
            ldaTopic.addTopicWord(pair.first, weight);
        }
        
        ldaTopics.add(ldaTopic);
    }
    
    public void loadLocalLDATopics(String filename) {
        ldaTopics.clear();
        
        BufferedReader fin;   
        
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(filename), "UTF8")); 
        
            int topicId = 0;
            List<PairStrDouble> words = new ArrayList();

            String line;            
            while ((line = fin.readLine()) != null) {
                line = StrUtil.normalizeStr(line);
                
                if (line.startsWith("Topic") && line.indexOf(":") >= 0) {
                    
                    if (!words.isEmpty()) {
                            if (topicMap != null && topicMap.hasLDATopic(topicId)) {
                                addNewLDATopic(topicId, words);
                            }

                        words.clear();
                    }

                    List<String> tokens = StrUtil.tokenizeStr(line, "Topic th:\t\r\n");
                    
                    if (tokens.size() == 1) {
                        topicId = Integer.parseInt(tokens.get(0));
                    }
                    
                } else {
                    List<String> tokens = StrUtil.tokenizeStr(line);
                    
                    if (tokens.size() != 2) {
                        continue;
                    }
                    
                    words.add(new PairStrDouble(tokens.get(0), Double.parseDouble(tokens.get(1))));
                }
            }
            
            if (!words.isEmpty()) {
                if (topicMap != null && topicMap.hasLDATopic(topicId)) {
                    addNewLDATopic(topicId, words);
                }

                words.clear();
            }
            
            fin.close();
            
        } catch (IOException ex) {
            Logger.getLogger(LDATopicColl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void buildLDATopicsDict() {
        for (int i = 0; i < ldaTopics.size(); i++) {
            Topic ldaTopic = ldaTopics.get(i);
            ldaTopicsDict.put(ldaTopic.getTopicId(), i);
        }
    }
    
    public void print() {
        for (int i = 0; i < ldaTopics.size(); i++) {
            ldaTopics.get(i).print();
        }
    }
}
