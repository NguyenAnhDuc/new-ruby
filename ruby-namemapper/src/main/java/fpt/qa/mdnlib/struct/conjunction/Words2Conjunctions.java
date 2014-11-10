/**
 * 
 */
package fpt.qa.mdnlib.struct.conjunction;

import java.util.*;

/**
 * @author pxhieu
 *
 */
public class Words2Conjunctions {
    private Map<String, Set<Integer>> words2Conjunctions;
    
    public Words2Conjunctions() {
        words2Conjunctions = new HashMap<String, Set<Integer>>();
    }
    
    public void buildMap(ConjunctionCollection conjunctionColl) {
        System.out.println("Building Words2Conjunctions map ...");
        
        if (conjunctionColl == null) {
            System.out.println("No conjunction collection is provided!");
            return;
        }
        
        int conjCount = conjunctionColl.size();
        for (int i = 0; i < conjCount; i++) {
            Conjunction conjunction = conjunctionColl.getConjunctionAt(i);
            addConjunctionToMap(i, conjunction);
        }
        
        System.out.println("Building Words2Conjunctions map completed.");
    }
    
    public void addConjunctionToMap(int idx, Conjunction conjunction) {
        List<String> words = conjunction.getConjWords();
        for (int i = 0; i < words.size(); i++) {
            addWordToMap(idx, words.get(i));
        }
    }
    
    public void addWordToMap(int idx, String word) {
        Set<Integer> conjIdxes = words2Conjunctions.get(word);
        
        if (conjIdxes == null) {
            conjIdxes = new HashSet<Integer>();
            conjIdxes.add(idx);
            words2Conjunctions.put(word, conjIdxes);
        } else {
            conjIdxes.add(idx);            
        }
    }
    
    public Set<Integer> getConjunctionIdxes(String word) {
        Set<Integer> conjIdxes = words2Conjunctions.get(word);

        if (conjIdxes == null) {
            conjIdxes = new HashSet<Integer>();
        }
        
        return conjIdxes;
    }
}
