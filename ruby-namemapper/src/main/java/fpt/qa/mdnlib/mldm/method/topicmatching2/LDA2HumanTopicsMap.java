/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.topicmatching2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import fpt.qa.mdnlib.struct.pair.PairStrDouble;
import fpt.qa.mdnlib.util.properties.Parameters;
import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class LDA2HumanTopicsMap {
    private static Map<Integer, String> lda2HumanTopicsMap = null;
    private static Map<String, List<Integer>> human2LDATopicsMap = null;
    private static Map<String, List<PairStrDouble>> relatedHumanTopicsMap = null;
    
    /*
     * initialization
     */
    static {
        init();
    }

    private static void init() {
        lda2HumanTopicsMap = new HashMap();
        human2LDATopicsMap = new HashMap();
        relatedHumanTopicsMap = new HashMap();
        
        loadTopicsMap(Parameters.getHumanNewsTopicsMapFile());
    }
    
    private static void loadTopicsMap(String filename) {
        BufferedReader fin;   
        
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(filename), "UTF8")); 

            String topicLabel = "";

            String line;            
            while ((line = fin.readLine()) != null) {
                line = StrUtil.normalizeStr(line);
                
                if (line.startsWith("Topic:")) {
                    List<String> tokens = StrUtil.tokenizeStr(line, ": \t\r\n");

                    if (tokens.size() < 3) {
                        topicLabel = "";
                        continue;
                    }
                    
                    topicLabel = tokens.get(1);
                    
                    List<Integer> ldaIntTopics = new ArrayList();

                    for (int i = 2; i < tokens.size(); i++) {
                        int ldaTopic = Integer.parseInt(tokens.get(i));
                        
                        lda2HumanTopicsMap.put(ldaTopic, topicLabel);
                        ldaIntTopics.add(ldaTopic);
                    }
                    
                    human2LDATopicsMap.put(topicLabel, ldaIntTopics);
                }
                
                if (line.startsWith("%")) {
                    List<String> tokens = StrUtil.tokenizeStr(line);
                    if (tokens.size() != 3) {
                        continue;
                    }
                    
                    PairStrDouble pair = new PairStrDouble(tokens.get(1), 
                            Double.parseDouble(tokens.get(2)));
                    
                    if (!topicLabel.isEmpty()) {
                        List<PairStrDouble> relatedTopics = 
                                relatedHumanTopicsMap.get(topicLabel);
                        
                        if (relatedTopics != null) {
                            relatedTopics.add(pair);
                        } else {
                            relatedTopics = new ArrayList();
                            relatedTopics.add(pair);
                            relatedHumanTopicsMap.put(topicLabel, relatedTopics);
                        }
                    }
                }
            }
            
            fin.close();
            
        } catch (IOException ex) {
            Logger.getLogger(LDATopicCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    public static boolean hasLDATopic(int ldaTopic) {
        return lda2HumanTopicsMap.containsKey(ldaTopic);
    }
    
    public static String getHumanTopicByLDATopic(int ldaTopic) {
        return lda2HumanTopicsMap.get(ldaTopic);
    }
    
    public static List<Integer> getLDATopicsByHumanTopic(String humanTopic) {
        return human2LDATopicsMap.get(humanTopic);
    }
    
    public static int getNumLDATopicsByHumanTopic(String humanTopic) {
        List<Integer> ldaTopics = human2LDATopicsMap.get(humanTopic);
        if (ldaTopics == null || ldaTopics.isEmpty()) {
            return 1;
        } else {
            return ldaTopics.size();
        }
    }
    
    public static List<PairStrDouble> getRelatedHumanTopics(String humanTopic) {
        return relatedHumanTopicsMap.get(humanTopic);
    }
    
    public static void print() {
        for (Map.Entry<String, List<Integer>> entry : human2LDATopicsMap.entrySet()) {
            System.out.println("Topic: " + entry.getKey() + ": " + entry.getValue().toString());
            
            List<PairStrDouble> relatedTopics = 
                    relatedHumanTopicsMap.get(entry.getKey());
            if (relatedTopics != null) {
                for (int i = 0; i < relatedTopics.size(); i++) {
                    PairStrDouble pair = relatedTopics.get(i);
                    System.out.println("% " + pair.first + " " + pair.second);
                }
            }
            
            System.out.println();
        }
    }
}
