/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.topic.mapping;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hieupx
 */
public class TopicCollection {
    private List<Topic> topics;
    
    public TopicCollection() {
        topics = new ArrayList();
    }
    
    public int size() {
        return topics.size();
    }
    
    public Topic getTopicAt(int idx) {
        return topics.get(idx);
    }
    
    public void addTopic(Topic tp) {
        topics.add(tp);
    }
    
    public void loadTopics(String filename) {
        BufferedReader fin;        
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(filename), "UTF8")); 
            
            List<String> lines = new ArrayList();
            int count = 0;
            String line;
            
            while ((line = fin.readLine()) != null) {
                if (line.startsWith("Topic") && line.endsWith(":")) {
                    if (lines.size() > 0) {
                        Topic tp = new Topic();
                        tp.setTopicId(count);
                        tp.parseTopic(lines);
                        addTopic(tp);
                        
                        lines.clear();
                        count++;
                    }
                    
                } else {
                    lines.add(line);
                }
            }
            
            // for the last topic
            if (lines.size() > 0) {
                Topic tp = new Topic();
                tp.setTopicId(count);
                tp.parseTopic(lines);
                addTopic(tp);
            }
            
            fin.close();
            
        } catch (IOException ex) {
            Logger.getLogger(TopicCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
