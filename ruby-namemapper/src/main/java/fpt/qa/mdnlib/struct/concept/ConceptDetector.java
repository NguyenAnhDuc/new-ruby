/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.concept;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.qa.mdnlib.math.general.MyMath;
import fpt.qa.mdnlib.struct.pair.PairIntDouble;
import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class ConceptDetector {
    private ConceptCollection conceptColl;
    private Words2Concepts words2Concepts;
    private ConjunctionMatcher conjTester;

    public ConceptDetector() {
        this.conceptColl = new ConceptCollection();
        this.words2Concepts = new Words2Concepts();
        this.conjTester = new ConjunctionMatcher();
    }
    
    public void init() {
        loadLocalConcepts();
        
        words2Concepts.init(conceptColl);
        conjTester.init(conceptColl);
    }
    
    public void loadLocalConcepts() {
        System.out.println("Loading concepts from local files...");
        conceptColl = LocalConceptsLoader.getConcepts();
        System.out.println("Loading concepts from local files completed.");
    }
    
    public String getConceptLabelById(Integer cId) {
        return conceptColl.getConceptLabelById(cId);
    }
    
    public void print() {
        if (conceptColl != null) {
            for (int i = 0; i < conceptColl.size(); i++) {
                conceptColl.getConceptAt(i).print();
            }
        }
    }
    
    public Map<Integer, Double> getConcepts(String text) {
        Map<Integer, Double> cIdWeightMap = new HashMap();

        if (text == null) {
            return logScaleAndNormalization(cIdWeightMap);
        }
        
        List<String> tokens = StrUtil.tokenizeStr(text);
        
        // get relevant conjunctions (if any)
        List<String> relevantConjs = conjTester.getRelevantConjunctions(text);
        for (int i = 0; i < relevantConjs.size(); i++) {
            String conjStr = relevantConjs.get(i);
            tokens.add(conjStr);
        }
        
        int count = tokens.size();
        for (int i = 0; i < count; i++) {
            String word = tokens.get(i);
            /*
            boolean isComplexWord = (word.indexOf("_") >= 0);
            String realWord = word.replace("_", " ");
            */
            List<PairIntDouble> cIdWeightList = words2Concepts.getConcepts(word);
            
            if (!cIdWeightList.isEmpty()) {
                for (int j = 0; j < cIdWeightList.size(); j++) {
                    PairIntDouble pair = cIdWeightList.get(j);
                    accumulateConceptWeight(pair.first, pair.second, cIdWeightMap);
                }
                
            } /* else { // for new concepts
                if (isComplexWord &&
                        word.length() >= MIN_CONCEPT_LENGTH &&
                        Regex.isInitCap(realWord) &&
                        !VnDict.contains(realWord) &&
                        !VnExtendedDict.contains(realWord) &&
                        !StopNames.contains(word) &&
                        VnBaomoiNamesDictForConceptLookup.contains(word)) {                    
                    
                    // new concept 
                    int newConceptId = ConceptCounter.getNewConceptId();
                    
                    Concept newConcept = new Concept(newConceptId, word);
                    newConcept.addConceptWord(word, 1.0); 
                    
                    conceptColl.addConcept(newConcept);
                    words2Concepts.addConceptToMap(newConcept);
                    
                    accumulateConceptWeight(newConceptId, 1.0, cIdWeightMap);
                }
            }    
            */
        }
        
        return logScaleAndNormalization(cIdWeightMap);
    }
    
    public Map<String, Double> getConceptsAndLabels(String text) {
        Map<String, Double> concepts = new HashMap();
        
        Map<Integer, Double> tempMap = getConcepts(text);
        for (Map.Entry<Integer, Double> entry : tempMap.entrySet()) {
            String conceptLabel = getConceptLabelById(entry.getKey());
            if (conceptLabel != null) {
                concepts.put(conceptLabel, entry.getValue());
            }
        }
        
        return concepts;
    }
    
    public void accumulateConceptWeight(Integer id, Double weight, 
            Map<Integer, Double> map) {
        
        Double value = map.get(id);
        if (value == null) {
            map.put(id, weight);
        } else {
            map.put(id, value + weight);
        }
    }
    
    public Map<Integer, Double> logScaleAndNormalization(Map<Integer, Double> map) {
        Map<Integer, Double> results = new HashMap();
        
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            double freq = entry.getValue();            
            double weight;
            
            if (freq <= 1) {
                weight = MyMath.log(Constants4Matching.LOG_BASE, 
                        freq + Constants4Matching.ADDITIONAL_VALUE_1);
            } else {
                weight = MyMath.log(Constants4Matching.LOG_BASE, 
                        freq - Constants4Matching.ADDITIONAL_VALUE_2);
            }
            
            if (weight > 1.0) {
                weight = 1.0;
            }

            results.put(entry.getKey(), weight);
        }

        return results;
    }
}
