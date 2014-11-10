/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.concept;

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
public class Words2Concepts {
    private ConceptCollection conceptColl;
    private Map<String, Map<Integer, Double>> words2Concepts;
    
    public Words2Concepts() {
        conceptColl = null;
        words2Concepts = new HashMap();
    }
    
    public void init(ConceptCollection conceptColl) {
        this.conceptColl = conceptColl;
        buildMap();
    }
    
    public final void buildMap() {
        System.out.println("Building Words2Concepts map ...");
        
        int conceptCount = conceptColl.size();
        for (int i = 0; i < conceptCount; i++) {
            Concept concept = conceptColl.getConceptAt(i);
            addConceptToMap(concept);
        }
        
        System.out.println("Building Words2Concepts map completed.");
    }
    
    public void addConceptToMap(Concept concept) {
        int conceptId = concept.getConceptId();
        List<PairStrDouble> conceptWords = concept.getConceptWords();
        for (int j = 0; j < conceptWords.size(); j++) {
            PairStrDouble wordWeight = conceptWords.get(j);
            
            addWordToMap(conceptId, wordWeight.first, wordWeight.second);
            
            String upperedCaseWord = wordWeight.first.toUpperCase();
            if (!upperedCaseWord.equals(wordWeight.first)) {
                addWordToMap(conceptId, wordWeight.first.toUpperCase(), wordWeight.second);
            }
        }
    }
    
    public void addWordToMap(int conceptId, String word, Double weight) {
        Map<Integer, Double> cIdWeightMap = words2Concepts.get(word);
        if (cIdWeightMap == null) {
            cIdWeightMap = new HashMap();
            cIdWeightMap.put(conceptId, weight);
            words2Concepts.put(word, cIdWeightMap);
        } else {
            cIdWeightMap.put(conceptId, weight);
        }
    }
    
    public List<PairIntDouble> getConcepts(String word) {
        List<PairIntDouble> results = new ArrayList();
        
        Map<Integer, Double> cIdWeightMap = words2Concepts.get(word);
        if (cIdWeightMap != null) {
            for (Map.Entry<Integer, Double> entry : cIdWeightMap.entrySet()) {
                results.add(new PairIntDouble(entry.getKey(), entry.getValue()));
            }
        }
        
        return results;
    }
}
