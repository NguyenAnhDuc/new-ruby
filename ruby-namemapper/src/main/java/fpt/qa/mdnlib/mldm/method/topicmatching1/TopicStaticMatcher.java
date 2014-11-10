/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.topicmatching1;

import java.util.*;

import fpt.qa.mdnlib.mldm.method.selection.ClassSelector;
import fpt.qa.mdnlib.struct.pair.PairIntDouble;
import fpt.qa.mdnlib.struct.pair.PairStrDouble;
import fpt.qa.mdnlib.struct.pair.PairStrInt;
import fpt.qa.mdnlib.util.properties.Parameters;
import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class TopicStaticMatcher {
    private static TopicCollection topicColl = null;
    private static Words2Topics words2Topics = null;

    /*
     * initialization
     */
    static {
        init();
    }

    private static void init() {
        topicColl = new TopicCollection(true);
        words2Topics = new Words2Topics();
        
        topicColl.loadLocalTopics(Parameters.getMappedTopicsFile());
        words2Topics.init(topicColl);
    }
    
    public static void print() {
        topicColl.print();
    }
    
    public static List<PairStrInt> preprocessText(String text) {
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
            tempList.add(new PairStrInt(entry.getKey(), entry.getValue()));
        }
        
        return tempList;
    }
    
    public static Map<Integer, Double> getTopicIdsByMatching(String text) {
        Map<Integer, List<PairStrDouble>> tIdWordWeightMap = new HashMap();
        
        List<PairStrInt> tokens = preprocessText(text);
        
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
        
        ClassSelector distribution = new ClassSelector(tempList);
        distribution.normalize();
        List<PairIntDouble> selectedTopics = 
                distribution.select(Constants.MaxNumTopicsSelected, 
                Constants.CumulativeThreshold, Constants.Times);
        

        Map<Integer, Double> results = new HashMap();
        for (int i = 0; i < selectedTopics.size(); i++) {
            PairIntDouble pair = selectedTopics.get(i);
            results.put(pair.first, pair.second);
        }
        
        return results;
    }
    
    public static Map<String, Double> getTopicLabelsByMatching(String text) {
        Map<String, Double> map = new HashMap();
        
        Map<Integer, Double> tempMap = getTopicIdsByMatching(text);
        for (Map.Entry<Integer, Double> entry : tempMap.entrySet()) {
            map.put(topicColl.getTopicLabel(entry.getKey()), entry.getValue());
        }
        
        return map;
    }
    
    public static List<PairStrDouble> getTopicsLabels(String text) {
        List<PairStrDouble> tempList = new ArrayList();
        
        Map<String, Double> map = getTopicLabelsByMatching(text);
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            tempList.add(new PairStrDouble(entry.getKey(), entry.getValue()));
        }
        
        Collections.sort(tempList);
        
        List<PairStrDouble> results = new ArrayList();
        for (int i = tempList.size() - 1; i >= 0; i--) {
            results.add(tempList.get(i));
        }
        
        return results;
    }
    
    public static void accumulateTopicWeight(Integer id, String word, Double weight, 
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
