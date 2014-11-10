/**
 * 
 */
package fpt.qa.mdnlib.struct.conjunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pxhieu
 *
 */
public class ConjunctionCollection {
    private List<Conjunction> conjunctions;
    
    public ConjunctionCollection() {
        conjunctions = new ArrayList<Conjunction>();
    }
    
    public int size() {
        return conjunctions.size();
    }
    
    public void clear() {
        conjunctions.clear();
    }
    
    public Conjunction getConjunctionAt(int idx) {
        if (idx < 0 || idx >= conjunctions.size()) {
            return null;
        }
        
        return conjunctions.get(idx);
    }
    
    public void addConjunction(String str) {
        Conjunction conjunction = new Conjunction(str);
        addConjunction(conjunction);
    }
    
    public void addConjunction(Conjunction conjunction) {
        conjunctions.add(conjunction);
    }
    
    public List<Conjunction> getAllConjunctions() {
        return conjunctions;
    }
    
    public void print() {
        for (int i = 0; i < conjunctions.size(); i++) {
            conjunctions.get(i).print();
            System.out.println();
        }
    }
}
