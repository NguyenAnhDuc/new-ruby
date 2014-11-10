/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.topicmatching1;

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
public class TopicCollection {
    private boolean positionBasedWeight;
    private List<Topic> topics;
    private Map<Integer, String> topicIds2Labels;
    
    public TopicCollection() {
        positionBasedWeight = false;
        topics = new ArrayList();
        topicIds2Labels = new HashMap();
    }
    
    public TopicCollection(boolean positionBasedWeight) {
        this();
        this.positionBasedWeight = positionBasedWeight;
    }
    
    public int size() {
        return topics.size();
    }
    
    public void clear() {
        topics.clear();
    }
    
    public Topic getTopicAt(int idx) {
        return topics.get(idx);
    }
    
    public void addTopic(Topic topic) {
        topics.add(topic);
    }
    
    public List<Topic> getAllTopics() {
        return topics;
    }
    
    public String getTopicLabel(int id) {
        String label = topicIds2Labels.get(id);
        if (label != null) {
            return label;
        } else {
            return "";
        }
    }
    
    public void addNewTopic(int id, String label, List<PairStrDouble> words) {
        Topic topic = new Topic(id, label);

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
            
            topic.addTopicWord(pair.first, weight);
        }
        
        topics.add(topic);
        topicIds2Labels.put(id, label);
    }
    
    public void loadLocalTopics(String filename) {
        BufferedReader fin;   
        
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(filename), "UTF8")); 
        
            int topicId = 0;
            String topicLabel = "";
            List<PairStrDouble> words = new ArrayList();

            String line;            
            while ((line = fin.readLine()) != null) {
                line = StrUtil.normalizeStr(line);
                
                if (line.startsWith("Topic") && line.indexOf(":") >= 0) {
                    if (!words.isEmpty()) {
                        addNewTopic(topicId, topicLabel, words);
                        
                        topicId++;
                        words.clear();
                    }
                    
                    List<String> tokens = StrUtil.tokenizeStr(line, ":, \t\r\n");
                    if (tokens.size() >= 4) {
                        topicLabel = tokens.get(3);
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
                addNewTopic(topicId, topicLabel, words);
                
                topicId++;
                words.clear();
            }
            
            fin.close();
            
        } catch (IOException ex) {
            Logger.getLogger(TopicCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void print() {
        for (int i = 0; i < topics.size(); i++) {
            topics.get(i).print();
            System.out.println();
        }
    }
}
