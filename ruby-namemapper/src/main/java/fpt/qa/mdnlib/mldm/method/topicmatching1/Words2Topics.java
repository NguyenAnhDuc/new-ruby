/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.topicmatching1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.qa.mdnlib.struct.pair.PairIntDouble;
import fpt.qa.mdnlib.struct.pair.PairStrDouble;

/**
 *
 * @author hieupx
 */
public class Words2Topics {
    private TopicCollection topicColl;
    private Map<String, Map<Integer, Double>> words2Topics;
    
    public Words2Topics() {
        topicColl = null;
        words2Topics = new HashMap();
    }
    
    public void init(TopicCollection topicColl) {
        this.topicColl = topicColl;
        buildMap();
    }
    
    public final void buildMap() {
        System.out.println("Building Words2Topics map ...");
        
        int topicCount = topicColl.size();
        for (int i = 0; i < topicCount; i++) {
            Topic topic = topicColl.getTopicAt(i);
            addTopicToMap(topic);
        }
        
        System.out.println("Building Words2Topics map completed.");
    }
    
    public void addTopicToMap(Topic topic) {
        int topicId = topic.getTopicId();

        List<PairStrDouble> topicWords = topic.getTopicWords();
        for (int j = 0; j < topicWords.size(); j++) {
            PairStrDouble wordWeight = topicWords.get(j);            
            addWordToMap(topicId, wordWeight.first, wordWeight.second);
        }
    }
    
    public void addWordToMap(int topicId, String word, Double weight) {
        Map<Integer, Double> tIdWeightMap = words2Topics.get(word);
        if (tIdWeightMap == null) {
            tIdWeightMap = new HashMap();
            tIdWeightMap.put(topicId, weight);
            words2Topics.put(word, tIdWeightMap);
        } else {
            tIdWeightMap.put(topicId, weight);
        }
    }
    
    public List<PairIntDouble> getTopics(String word) {
        List<PairIntDouble> results = new ArrayList();
        
        Map<Integer, Double> tIdWeightMap = words2Topics.get(word);
        if (tIdWeightMap != null) {
            for (Map.Entry<Integer, Double> entry : tIdWeightMap.entrySet()) {
                results.add(new PairIntDouble(entry.getKey(), entry.getValue()));
            }
        }
        
        return results;
    }
}
