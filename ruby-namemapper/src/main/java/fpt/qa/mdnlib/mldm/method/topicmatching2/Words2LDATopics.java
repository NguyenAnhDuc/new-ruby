/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.topicmatching2;

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
public class Words2LDATopics {
    private LDATopicCollection ldaTopicColl;
    private Map<String, Map<Integer, Double>> words2LDATopics;
    
    public Words2LDATopics() {
        ldaTopicColl = null;
        words2LDATopics = new HashMap();
    }
    
    public void init(LDATopicCollection ldaTopicColl) {
        this.ldaTopicColl = ldaTopicColl;
        buildMap();
    }
    
    public final void buildMap() {
        System.out.println("Building Words2LDATopics map ...");
        
        int topicCount = ldaTopicColl.size();
        for (int i = 0; i < topicCount; i++) {
            LDATopic ldaTopic = ldaTopicColl.getLDATopicAt(i);
            addTopicToMap(ldaTopic);
        }
        
        System.out.println("Building Words2LDATopics map completed.");
    }
    
    public void addTopicToMap(LDATopic ldaTopic) {
        int ldaTopicId = ldaTopic.getTopicId();

        List<PairStrDouble> topicWords = ldaTopic.getTopicWords();
        for (int j = 0; j < topicWords.size(); j++) {
            PairStrDouble wordWeight = topicWords.get(j);            
            addWordToMap(ldaTopicId, wordWeight.first, wordWeight.second);
        }
    }
    
    public void addWordToMap(int topicId, String word, Double weight) {
        Map<Integer, Double> tIdWeightMap = words2LDATopics.get(word);
        if (tIdWeightMap == null) {
            tIdWeightMap = new HashMap();
            tIdWeightMap.put(topicId, weight);
            words2LDATopics.put(word, tIdWeightMap);
        } else {
            tIdWeightMap.put(topicId, weight);
        }
    }
    
    public List<PairIntDouble> getTopics(String word) {
        List<PairIntDouble> results = new ArrayList();
        
        Map<Integer, Double> tIdWeightMap = words2LDATopics.get(word);
        if (tIdWeightMap != null) {
            for (Map.Entry<Integer, Double> entry : tIdWeightMap.entrySet()) {
                results.add(new PairIntDouble(entry.getKey(), entry.getValue()));
            }
        }
        
        return results;
    }
}
