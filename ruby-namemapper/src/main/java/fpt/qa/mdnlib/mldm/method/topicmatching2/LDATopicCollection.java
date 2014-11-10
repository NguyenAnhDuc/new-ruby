/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.topicmatching2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fpt.qa.mdnlib.struct.pair.PairStrDouble;
import fpt.qa.mdnlib.util.properties.Parameters;
import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class LDATopicCollection {
    private boolean positionBasedWeight;
    private List<LDATopic> ldaTopics;
    
    public LDATopicCollection() {
        positionBasedWeight = false;
        ldaTopics = new ArrayList();
    }
    
    public LDATopicCollection(boolean positionBasedWeight) {
        this();
        this.positionBasedWeight = positionBasedWeight;
    }
    
    public void init() {
        loadLocalLDATopics(Parameters.getLDATWordsFile());
    }
    
    public int size() {
        return ldaTopics.size();
    }
    
    public void clear() {
        ldaTopics.clear();
    }
    
    public LDATopic getLDATopicAt(int idx) {
        return ldaTopics.get(idx);
    }
    
    public void addLDATopic(LDATopic ldaTopic) {
        ldaTopics.add(ldaTopic);
    }
    
    public List<LDATopic> getAllLDATopics() {
        return ldaTopics;
    }
    
    public void addNewLDATopic(int id, List<PairStrDouble> words) {
        LDATopic ldaTopic = new LDATopic(id);

        int count = words.size();
        if (count > Constants.MaxNumWordsPerTopic) {
            count = Constants.MaxNumWordsPerTopic;
        }
        
        for (int i = 0; i < count; i++) {
            PairStrDouble pair = words.get(i);
            
            double weight = pair.second;
            if (positionBasedWeight) {
                weight = Constants.MaxWordWeight - Math.log10(i + 2);
                weight = Math.log(weight + 2);
            }
            
            ldaTopic.addTopicWord(pair.first, weight);
        }
        
        ldaTopics.add(ldaTopic);
    }
    
    public void loadLocalLDATopics(String filename) {
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
                        if (LDA2HumanTopicsMap.hasLDATopic(topicId)) {
                            addNewLDATopic(topicId, words);
                        }
                        
                        topicId++;
                        words.clear();
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
                if (LDA2HumanTopicsMap.hasLDATopic(topicId)) {
                    addNewLDATopic(topicId, words);
                }
                
                topicId++;
                words.clear();
            }
            
            fin.close();
            
        } catch (IOException ex) {
            Logger.getLogger(LDATopicCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void print() {
        for (int i = 0; i < ldaTopics.size(); i++) {
            ldaTopics.get(i).print();
            System.out.println();
        }
    }
}
