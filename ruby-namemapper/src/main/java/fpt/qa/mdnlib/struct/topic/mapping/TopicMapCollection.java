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

import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class TopicMapCollection {
    private List<TopicMap> topicMaps;
    
    public TopicMapCollection() {
        topicMaps = new ArrayList();
    }
    
    public int size() {
        return topicMaps.size();
    }
    
    public void addTopicMap(TopicMap tm) {
        topicMaps.add(tm);
    }
    
    public TopicMap getTopicMapAt(int idx) {
        return topicMaps.get(idx);
    }
    
    public void loadTopicMaps(String filename) {
        BufferedReader fin;        
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(filename), "UTF8")); 
            
            List<String> lines = new ArrayList();
            String line;
            
            while ((line = fin.readLine()) != null) {
                line = StrUtil.normalizeStr(line);
                
                if (line.startsWith("#") || line.length() <= 0) {
                    continue;
                }
                
                TopicMap tm = new TopicMap();
                tm.parseTopicMap(line);
                
                addTopicMap(tm);
            }
            
            fin.close();
            
        } catch (IOException ex) {
            Logger.getLogger(TopicMapCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
