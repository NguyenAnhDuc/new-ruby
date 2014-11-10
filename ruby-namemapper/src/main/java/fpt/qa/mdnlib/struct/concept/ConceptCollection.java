/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.concept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hieupx
 */
public class ConceptCollection {
    private List<Concept> concepts = null;
    private Map<Integer, String> conceptsIds2Labels = null;
    
    public ConceptCollection() {
        concepts = new ArrayList();
        conceptsIds2Labels = new HashMap();
    }
    
    public int size() {
        return concepts.size();
    }
    
    public void clear() {
        concepts.clear();
    }
    
    public Concept getConceptAt(int idx) {
        return concepts.get(idx);
    }
    
    public void addConcept(Concept concept) {
        concepts.add(concept);
        conceptsIds2Labels.put(concept.getConceptId(), concept.getConceptLabel());
    }
    
    public String getConceptLabelById(Integer cId) {
        return conceptsIds2Labels.get(cId);
    }
    
    public List<Concept> getAllConcepts() {
        return concepts;
    }
}
