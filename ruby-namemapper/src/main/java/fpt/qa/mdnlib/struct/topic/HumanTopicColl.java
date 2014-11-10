/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.topic;

import java.util.*;

import fpt.qa.mdnlib.struct.pair.PairStrDouble;

/**
 *
 * @author hieupx
 */
public class HumanTopicColl {
    private boolean positionBasedWeight = false;
    private TopicMap topicMap = null;
    private LDATopicColl ldaTopicColl = null;
    private List<Topic> humanTopics = null;
    private Map<Integer, Integer> humanTopicsDict = null;
    
    public HumanTopicColl() {
        positionBasedWeight = false;
        topicMap = null;
        ldaTopicColl = null;
        humanTopics = new ArrayList();
        humanTopicsDict = new HashMap();
    }
    
    public HumanTopicColl(TopicMap topicMap, LDATopicColl ldaTopicColl, boolean positionBasedWeight) {
        this();
        
        this.topicMap = topicMap;
        this.ldaTopicColl = ldaTopicColl;
        this.positionBasedWeight = positionBasedWeight;
        
        generateHumanTopics();
        buildHumanTopicsDict();
    }
    
    public Topic getHumanTopicById(int tId) {
        Integer idx = humanTopicsDict.get(tId);
        
        if (idx != null) {
            return humanTopics.get(idx);
        } 
        
        return null;
    }
    
    public List<Topic> getHumanTopics() {
        return humanTopics;
    }
    
    private void generateHumanTopics() {
        humanTopics.clear();
        
        int humanTopicId = 0;
        for (String humanTopicLabel : topicMap.getHumanTopics()) {
            Topic humanTopic = new Topic(humanTopicId, humanTopicLabel);

            List<Integer> ldaTopics = topicMap.getLDATopicsByHumanTopic(humanTopicLabel);
            List<PairStrDouble> tempList = new ArrayList();
            
            for (int i = 0; i < ldaTopics.size(); i++) {
                int ldaTopicId = ldaTopics.get(i);
                Topic ldaTopic = ldaTopicColl.getLDATopicByTopicId(ldaTopicId);
                
                List<PairStrDouble> words = ldaTopic.getTopicWords();
                for (int j = 0; j < words.size(); j++) {
                    tempList.add(words.get(j));
                }
            }

            Collections.sort(tempList);
            
            Set<String> wordSet = new HashSet();
            int count = 0;
            int i = tempList.size() - 1;
            while (i >= 0) {
                if (count >= Constants4Topic.MaxNumWordsPerHumanTopic) {
                    break;
                }
                
                PairStrDouble pair = tempList.get(i);
                
                if (!wordSet.contains(pair.first)) {
                    double weight = pair.second;
                    if (positionBasedWeight) {
                        weight = Constants4Topic.MaxNumWordsPerHumanTopic - Math.log10(count + 2);
                    }
                    
                    humanTopic.addTopicWord(pair.first, weight);
                    wordSet.add(pair.first);
                    
                    count++;
                }
                
                i--;
            }
            
            humanTopics.add(humanTopic);
            
            humanTopicId++;
        }
    }
    
    private void buildHumanTopicsDict() {
        for (int i = 0; i < humanTopics.size(); i++) {
            Topic humanTopic = humanTopics.get(i);
            humanTopicsDict.put(humanTopic.getTopicId(), i);
        }
    }
    
    public void print() {
        for (int i = 0; i < humanTopics.size(); i++) {
            humanTopics.get(i).print();
        }
    }
}
