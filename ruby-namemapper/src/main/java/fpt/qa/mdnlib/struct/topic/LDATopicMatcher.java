/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.qa.mdnlib.math.general.MyMath;
import fpt.qa.mdnlib.mldm.method.selection.ClassSelector;
import fpt.qa.mdnlib.struct.pair.PairIntDouble;
import fpt.qa.mdnlib.struct.pair.PairStrDouble;
import fpt.qa.mdnlib.struct.pair.PairStrInt;

/**
 *
 * @author pxhieu
 */
public class LDATopicMatcher {
    private TopicMap topicMap = null;
    private LDATopicColl ldaTopicColl = null;
    private Words2Topics words2Topics = null;
    
    public LDATopicMatcher(String topicMapFile, String ldaTopicWordsFile) {
        topicMap = new TopicMap(topicMapFile);
        ldaTopicColl = new LDATopicColl(topicMap, true, ldaTopicWordsFile);
        words2Topics = new Words2Topics(ldaTopicColl.getLDATopics());
    }
    
    public Map<Integer, PairIntDouble> getLDATopicDistribution(String text) {
        Map<Integer, PairIntDouble> results = new HashMap();
        
        List<PairStrInt> tokens = TextWebPrepro.preprocessText(text);
        
        Map<Integer, List<PairStrDouble>> tIdWordWeightMap = new HashMap();
        
        int count = tokens.size();
        for (int i = 0; i < count; i++) {
            PairStrInt token = tokens.get(i);
            String word = token.first;
            
            List<PairIntDouble> tIdWeightList = words2Topics.getTopics(word);
            if (!tIdWeightList.isEmpty()) {
                for (int j = 0; j < tIdWeightList.size(); j++) {
                    PairIntDouble pair = tIdWeightList.get(j);

                    double calculatedWeight = pair.second * Math.log(token.second + 0.2);
                    accumulateTopicWeight(pair.first, word, calculatedWeight, tIdWordWeightMap);
                }
            }
        }
        
        for (Map.Entry<Integer, List<PairStrDouble>> entry : tIdWordWeightMap.entrySet()) {
            Integer topicId = entry.getKey();
            List<PairStrDouble> words = entry.getValue();
            
            double topicWeightSum = 0.0;
            int K = words.size();            
            for (int i = 0; i < K; i++) {
                topicWeightSum += words.get(i).second;
            }
            
            results.put(topicId, new PairIntDouble(K, topicWeightSum));
        }
        
        return results;
    }
    
    public List<PairStrDouble> getHumanTopicLabelsByMatching(String text) {
        List<PairStrDouble> results = new ArrayList();
        
        Map<Integer, PairIntDouble> ldaTopics = getLDATopicDistribution(text);
        
        Map<String, Double> tempMap = new HashMap();
        
        for (Map.Entry<Integer, PairIntDouble> entry : ldaTopics.entrySet()) {
            Integer ldaTopic = entry.getKey();
            PairIntDouble pairKWeight = entry.getValue();
            
            String humanTopic = topicMap.getHumanTopicByLDATopic(ldaTopic);
            if (humanTopic != null) {
                Double weight = tempMap.get(humanTopic);
                
                if (weight == null) {
                    weight = pairKWeight.first * pairKWeight.second;
                    tempMap.put(humanTopic, weight);
                } else {
                    weight += pairKWeight.first * pairKWeight.second;
                    tempMap.put(humanTopic, weight);
                }
            }
        }
        
        for (Map.Entry<String, Double> entry : tempMap.entrySet()) {
            String humanTopic = entry.getKey();
            Double weight = entry.getValue();
            
            int count = topicMap.getNumLDATopicsByHumanTopic(humanTopic);
            
            results.add(new PairStrDouble(humanTopic, weight / Math.log(count + 1)));
        }
        
        return results;
    }

    public List<PairIntDouble> getHumanTopicIdsByMatching(String text) {
        List<PairIntDouble> results = new ArrayList();
        
        List<PairStrDouble> tempList = getHumanTopicLabelsByMatching(text);
        for (int i = 0; i < tempList.size(); i++) {
            PairStrDouble pair = tempList.get(i);
            results.add(new PairIntDouble(topicMap.getHumanTopicIdByItsLabel(pair.first), pair.second));
        }
        
        return results;
    }
    
    public Map<Integer, Double> getTopicIdsByMatching(String text, int maxNumTopicsSelected, 
            double cumulativeThreshold, int times) {
        List<PairIntDouble> tempList = getHumanTopicIdsByMatching(text);
        
        ClassSelector classSelector = new ClassSelector(tempList);
        List<PairIntDouble> selectedTopics = classSelector.select(maxNumTopicsSelected, cumulativeThreshold, times);

        Map<Integer, Double> results = new HashMap();
        for (int i = 0; i < selectedTopics.size(); i++) {
            PairIntDouble pair = selectedTopics.get(i);
            
            double newWeight = MyMath.log(Constants4Matching.LOG_BASE_12, pair.second + 2);
            
            /*
            if (newWeight > 0.7) {
                newWeight = MyMath.log(Constants4Matching.LOG_BASE_2, pair.second + 2);

                if (newWeight > 0.7) {
                    newWeight = MyMath.log(Constants4Matching.LOG_BASE_3, pair.second + 2);
                }
                
                if (newWeight > 0.7) {
                    newWeight = MyMath.log(Constants4Matching.LOG_BASE_4, pair.second + 2);
                }
                
                if (newWeight > 0.7) {
                    newWeight = MyMath.log(Constants4Matching.LOG_BASE_5, pair.second + 2);
                }

                if (newWeight > 0.7) {
                    newWeight = MyMath.log(Constants4Matching.LOG_BASE_6, pair.second + 2);
                }

                if (newWeight > 0.7) {
                    newWeight = MyMath.log(Constants4Matching.LOG_BASE_7, pair.second + 2);
                }
                
                if (newWeight > 0.7) {
                    newWeight = 0.7;
                }
            }
            */
            
            if (newWeight > 1.0) {
                newWeight = 1.0;
            }
            
            results.put(pair.first, newWeight);
        }
        
        return results;
    }
    
    public Map<String, Double> getTopicLabelsByMatching(String text, int maxNumTopicsSelected, 
            double cumulativeThreshold, int times) {
        Map<String, Double> map = new HashMap();
        
        Map<Integer, Double> tempMap = getTopicIdsByMatching(text, maxNumTopicsSelected, cumulativeThreshold, times);

        for (Map.Entry<Integer, Double> entry : tempMap.entrySet()) {
            map.put(topicMap.getHumanTopicLabelByItsId(entry.getKey()), entry.getValue());
        }
        
        return map;
    }
    
    public String classify(String text) {
        Map<Integer, Double> tempMap = getTopicIdsByMatching(text, 1, 0.4, 2);
        
        String label = "";
        double maxWeight = 0.0;
        
        for (Map.Entry<Integer, Double> entry : tempMap.entrySet()) {
            double weight = entry.getValue();
            if (maxWeight < weight) {
                maxWeight = weight;
                label = topicMap.getHumanTopicLabelByItsId(entry.getKey());
            }
        }
        
        return label;
    }
    
    public void accumulateTopicWeight(Integer id, String word, Double weight, 
            Map<Integer, List<PairStrDouble>> map) {
        
        List<PairStrDouble> words = map.get(id);
        if (words == null) {
            words = new ArrayList();
            words.add(new PairStrDouble(word, weight));
            map.put(id, words);
                    
        } else {
            words.add(new PairStrDouble(word, weight));
        }
    }
    
}
