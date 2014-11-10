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
import fpt.qa.mdnlib.struct.pair.PairStrInt;
import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class HumanTopicMatcher {
    private LDATopicCollection ldaTopicColl = null;
    private Words2LDATopics words2LDATopics = null;

    public HumanTopicMatcher() {
        ldaTopicColl = new LDATopicCollection(true);
        words2LDATopics = new Words2LDATopics();
    }
    
    public void init() {
        ldaTopicColl.init();
        words2LDATopics.init(ldaTopicColl);
    }
    
    public void addNewTopic(int id, List<PairStrDouble> words) {
        ldaTopicColl.addNewLDATopic(id, words);
    }
    
    public void loadLocalLDATopics(String filename) {
        ldaTopicColl.loadLocalLDATopics(filename);
    }
    
    public void print() {
        ldaTopicColl.print();
    }
    
    public List<PairStrInt> preprocessText(String text) {
        Map<String, Integer> map = new HashMap();
        
        List<String> tokens = StrUtil.tokenizeStr(text.toLowerCase());
        int count = tokens.size();
        for (int i = 0; i < count; i++) {
            String word = tokens.get(i);
            Integer freq = map.get(word);
            if (freq == null) {
                map.put(word, 1);
            } else {
                freq += 1;
                map.put(word, freq);
            }
        } 
        
        List<PairStrInt> tempList = new ArrayList();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            int freq = entry.getValue();
            for (int i = 0; i < freq; i++) {
                tempList.add(new PairStrInt(entry.getKey(), entry.getValue()));
            }
        }
        
        return tempList;
    }

    public List<PairStrInt> preprocessText2(String text) {
        List<PairStrInt> results = new ArrayList();

        List<String> tokens = StrUtil.tokenizeStr(text.toLowerCase());
        int count = tokens.size();
        for (int i = 0; i < count; i++) {
            String word = tokens.get(i);
            results.add(new PairStrInt(word, 1));
        } 
        
        return results;
    }
    
    public Map<Integer, PairIntDouble> getLDATopicDistribution(String text) {
        Map<Integer, PairIntDouble> results = new HashMap();
        
        List<PairStrInt> tokens = preprocessText(text);
        
        Map<Integer, List<PairStrDouble>> tIdWordWeightMap = new HashMap();
        
        int count = tokens.size();
        for (int i = 0; i < count; i++) {
            PairStrInt token = tokens.get(i);
            String word = token.first;
            
            List<PairIntDouble> tIdWeightList = words2LDATopics.getTopics(word);
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
    
    public List<PairStrDouble> getHumanTopicsByMatching(String text) {
        
        List<PairStrDouble> results = new ArrayList();
        
        Map<Integer, PairIntDouble> ldaTopics = getLDATopicDistribution(text);
        
        Map<String, Double> tempMap = new HashMap();
        
        for (Map.Entry<Integer, PairIntDouble> entry : ldaTopics.entrySet()) {
            Integer ldaTopic = entry.getKey();
            PairIntDouble pairKWeight = entry.getValue();
            
            String humanTopic = LDA2HumanTopicsMap.getHumanTopicByLDATopic(ldaTopic);
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
            
            int freq = LDA2HumanTopicsMap.getNumLDATopicsByHumanTopic(humanTopic);
            
            results.add(new PairStrDouble(humanTopic, weight / Math.log(freq + 1)));
        }
        
        return results;
    }
    
    public String classify(String text) {
        String classLabel = "";
        
        List<PairStrDouble> tempList = getHumanTopicsByMatching(text);
        
        double maxWeight = 0.0;
        for (int i = 0; i < tempList.size(); i++) {
            PairStrDouble pair = tempList.get(i);
            if (pair.second > maxWeight) {
                classLabel = pair.first;
                maxWeight = pair.second;
            }
        }
        
        return classLabel;
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
