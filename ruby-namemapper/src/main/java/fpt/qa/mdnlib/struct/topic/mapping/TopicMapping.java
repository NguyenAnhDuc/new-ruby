/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.topic.mapping;

import java.util.*;

import fpt.qa.mdnlib.struct.pair.PairStrDouble;

/**
 *
 * @author hieupx
 */
public class TopicMapping {
    private TopicCollection topicColl;
    private TopicMapCollection topicMapColl;
    
    public TopicMapping(TopicCollection tColl, TopicMapCollection tmColl) {
        topicColl = tColl;
        topicMapColl = tmColl;
    }
    
    public void doMap() {
        int nTopics = topicColl.size();
        int nTopicMaps = topicMapColl.size();
        
        for (int i = 0; i < nTopicMaps; i++) {
            TopicMap tm = topicMapColl.getTopicMapAt(i);
            
            Topic topic = doMap(tm, i);
            
            topic.print();
        }
    }
    
    public Topic doMap(TopicMap tm, int id) {
        Topic topic = new Topic();        
        topic.setTopicId(id);
        topic.setTopicName(tm.getTopicName());
        
        List<Integer> tids = tm.getTopicIds();
        
        List<PairStrDouble> tempList = new ArrayList();
        for (int i = 0; i < tids.size(); i++) {
            int tid = tids.get(i).intValue();
            Topic tp = topicColl.getTopicAt(tid);
            List<PairStrDouble> words = tp.getWords();
            
            for (int j = 0; j < words.size(); j++) {
                tempList.add(words.get(j));
            }
        }
        
        // System.out.println(tm.getTopicName() + " Temp list size: " + tempList.size());
        
        Collections.sort(tempList);
        
        Set<String> strSet = new HashSet();
        for (int i = tempList.size() - 1; i >= 0; i--) {
            PairStrDouble pair = tempList.get(i);
            if (strSet.contains(pair.first)) {
                continue;
            } else {
                topic.addWord(pair.first, pair.second);
                strSet.add(pair.first);
            }
        }
        
        return topic;
    }
}
