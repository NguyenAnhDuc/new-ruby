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
public class HumanTopicMatcher {
    private TopicMap topicMap = null;
    private LDATopicColl ldaTopicColl = null;
    private HumanTopicColl humanTopicColl = null;
    private Words2Topics words2Topics = null;
    
    public HumanTopicMatcher(String topicMapFile, String ldaTopicWordsFile) {
        topicMap = new TopicMap(topicMapFile);
        ldaTopicColl = new LDATopicColl(topicMap, false, ldaTopicWordsFile);
        humanTopicColl = new HumanTopicColl(topicMap, ldaTopicColl, true);
        words2Topics = new Words2Topics(humanTopicColl.getHumanTopics());
    }

    public Map<Integer, Double> getTopicIdsByMatching(String text, int maxNumTopicsSelected, 
            double cumulativeThreshold, int times) {
        Map<Integer, List<PairStrDouble>> tIdWordWeightMap = new HashMap();
        
        List<PairStrInt> tokens = TextWebPrepro.preprocessText(text);
        
        int count = tokens.size();
        for (int i = 0; i < count; i++) {
            PairStrInt token = tokens.get(i);
            String word = token.first;
            
            List<PairIntDouble> tIdWeightList = words2Topics.getTopics(word);
            if (!tIdWeightList.isEmpty()) {
                for (int j = 0; j < tIdWeightList.size(); j++) {
                    PairIntDouble pair = tIdWeightList.get(j);

                    double calculatedWeight = pair.second * Math.log(token.second + Constants4Matching.a);
                    accumulateTopicWeight(pair.first, word, calculatedWeight, tIdWordWeightMap);
                }
            }
        }
        
        List<PairIntDouble> tempList = new ArrayList();
        
        for (Map.Entry<Integer, List<PairStrDouble>> entry : tIdWordWeightMap.entrySet()) {
            Integer topicId = entry.getKey();
            List<PairStrDouble> words = entry.getValue();
            
            double topicWeightSum = 0.0;
            int K = words.size();
            for (int i = 0; i < K; i++) {
                topicWeightSum += words.get(i).second;
            }
            
            topicWeightSum *= K;
            
            tempList.add(new PairIntDouble(topicId, topicWeightSum));
        }
        
        ClassSelector classSelector = new ClassSelector(tempList);
        
        List<PairIntDouble> selectedTopics = classSelector.select(maxNumTopicsSelected, cumulativeThreshold, times);

        Map<Integer, Double> results = new HashMap();
        for (int i = 0; i < selectedTopics.size(); i++) {
            PairIntDouble pair = selectedTopics.get(i);
            
            List<PairStrDouble> relatedWords = tIdWordWeightMap.get(pair.first);
            if (relatedWords != null && relatedWords.size() > 0) {
                pair.second /= relatedWords.size();
            }
            
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
                    newWeight = MyMath.log(Constants4Matching.LOG_BASE_8, pair.second + 2);
                }

                if (newWeight > 0.7) {
                    newWeight = MyMath.log(Constants4Matching.LOG_BASE_9, pair.second + 2);
                }

                if (newWeight > 0.7) {
                    newWeight = MyMath.log(Constants4Matching.LOG_BASE_10, pair.second + 2);
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
            map.put(humanTopicColl.getHumanTopicById(entry.getKey()).getTopicLabel(), entry.getValue());
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
                label = humanTopicColl.getHumanTopicById(entry.getKey()).getTopicLabel();
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
