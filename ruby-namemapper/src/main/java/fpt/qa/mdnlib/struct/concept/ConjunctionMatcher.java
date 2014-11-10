/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.concept;

import java.util.List;

import fpt.qa.mdnlib.struct.conjunction.ConjunctionChecker;
import fpt.qa.mdnlib.struct.pair.PairStrDouble;

/**
 *
 * @author hieupx
 */
public class ConjunctionMatcher {
    private ConceptCollection conceptColl = null;
    private ConjunctionChecker conjChecker = null;
    
    public ConjunctionMatcher() {
        conceptColl = null;
        conjChecker = new ConjunctionChecker();
    }
    
    public void init(ConceptCollection conceptColl) {
        this.conceptColl = conceptColl;
        genConjunctionChecker();
    }
    
    public void genConjunctionChecker() {
        System.out.println("Building ConjunctionChecker ...");
        
        int conceptCount = conceptColl.size();
        for (int i = 0; i < conceptCount; i++) {
            Concept concept = conceptColl.getConceptAt(i);
            process(concept);
        }
        
        System.out.println("Building ConjunctionChecker completed.");
    }
    
    public void process(Concept concept) {
        List<PairStrDouble> conceptWords = concept.getConceptWords();
        for (int j = 0; j < conceptWords.size(); j++) {
            PairStrDouble wordWeight = conceptWords.get(j);
            String str = wordWeight.first;
            
            if (str.contains("{") && str.contains("}")) {
                conjChecker.addConjunction(str);
            }
        }
    }
    
    List<String> getRelevantConjunctions(String text) {
        return conjChecker.getRelevantConjunctions(text);
    }
}
