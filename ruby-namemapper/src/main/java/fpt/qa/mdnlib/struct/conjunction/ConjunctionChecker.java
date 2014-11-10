/**
 * 
 */
package fpt.qa.mdnlib.struct.conjunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fpt.qa.mdnlib.util.string.StrUtil;

/**
 * @author pxhieu
 *
 */
public class ConjunctionChecker {
    private ConjunctionCollection conjunctionColl = null;
    private Words2Conjunctions words2Conjunctions = null;
    
    public ConjunctionChecker() {
        conjunctionColl = new ConjunctionCollection();
        words2Conjunctions = new Words2Conjunctions();
    }
    
    public void addConjunction(String str) {
        conjunctionColl.addConjunction(str);
        
        int idx = conjunctionColl.size() - 1;
        Conjunction conjunction = conjunctionColl.getConjunctionAt(idx);
        words2Conjunctions.addConjunctionToMap(idx, conjunction);
    }
    
    public void addConjunction(Conjunction conjunction) {
        conjunctionColl.addConjunction(conjunction);
        
        int idx = conjunctionColl.size() - 1;
        words2Conjunctions.addConjunctionToMap(idx, conjunction);
    }
    
    public int size() {
        return conjunctionColl.size();
    }
    
    public List<Conjunction> getAllConjunctions() {
        return conjunctionColl.getAllConjunctions();
    }
    
    public void print() {
        conjunctionColl.print();
    }
    
    public List<String> preprocessText(String text) {
        List<String> selectedTokens = new ArrayList<String>();
        
        List<String> tokens = StrUtil.tokenizeStr(text.toLowerCase());
        
        Set<String> tempSet = new HashSet<String>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (!tempSet.contains(token)) {
                selectedTokens.add(token);
                tempSet.add(token);
            }
        }
        
        return selectedTokens;
    }
    
    public List<String> preprocessText(String text, boolean isSet) {
    	
    	if (isSet == true)
    		return preprocessText(text);        
        List<String> tokens = StrUtil.tokenizeStr(text.toLowerCase());
        
        
        return tokens;
    }
    
    // Input: text
    // Tim xem trong conjucntion collection co conjunction nao nam trong text
    
    // Cho truoc 1 Conjunction Collection
    // Build map :cac word co trong cac conjunction
    // Moi Word -> tap chi so cac conjunction chua word do (Map<String, Set<Integer>>)
    public List<String> getRelevantConjunctions(String text) {
        List<String> relevantConjs = new ArrayList<String>();
        
        List<String> tokens = preprocessText(text);
        if (tokens.isEmpty()) {
            return relevantConjs;
        }
        
        
        // Map conj2Count
        // index : index 1 conjunction trong Collection
        // value : co bao nhieu tu trong Text ma co trong conjunction nay
        // neu value = conjunction.size() ===> tat cac cac word trong conjunction co trong Text
        // => conjunction nay nam trong Text
        Map<Integer, Integer> conj2Count = new HashMap<Integer, Integer>();
        
        for (int i = 0; i < tokens.size(); i++) {
            Set<Integer> conjIdxes = words2Conjunctions.getConjunctionIdxes(tokens.get(i));
            for (int idx : conjIdxes) {
                Integer count = conj2Count.get(idx);
                if (count == null) {
                    conj2Count.put(idx, 1);
                } else {
                    conj2Count.put(idx, count + 1);
                }
            }
        }
        
        for (Map.Entry<Integer, Integer> entry : conj2Count.entrySet()) {
            Conjunction conjunction = conjunctionColl.getConjunctionAt(entry.getKey());
            
            if (conjunction != null) {
                if (conjunction.size() == entry.getValue()) {
                    relevantConjs.add(conjunction.getStr());
                }
            }
        }
        
        return relevantConjs;
    }
    
 // Input: text
    // Tim xem trong conjucntion collection co conjunction nao nam trong text
    
    // Cho truoc 1 Conjunction Collection
    // Build map :cac word co trong cac conjunction
    // Moi Word -> tap cac conjunction chua word do (Map<String, Set<Integer>>)
    public List<String> getRelevantConjunctions(String text, boolean isOrder) {
    	
    	
    	if (isOrder == false)
    		return getRelevantConjunctions(text);
    	
        List<String> relevantConjs = new ArrayList<String>();
                
        List<String> selectedTokens = new ArrayList<String>();
        List<String> tokens = StrUtil.tokenizeStr(text.toLowerCase());
        //System.out.println("HL:" + tokens.toString());
        Set<String> tempSet = new HashSet<String>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (!tempSet.contains(token)) {
                selectedTokens.add(token);
                tempSet.add(token);
            }
        }
        
        if (tokens.isEmpty()) {
            return relevantConjs;
        }
        
        
       
        Map<Integer, Integer> conj2Count = new HashMap<Integer, Integer>();
        
        for (int i = 0; i < selectedTokens.size(); i++) {
            Set<Integer> conjIdxes = words2Conjunctions.getConjunctionIdxes(selectedTokens.get(i));
            for (int idx : conjIdxes) {
                Integer count = conj2Count.get(idx);
                if (count == null) {
                    conj2Count.put(idx, 1);
                } else {
                    conj2Count.put(idx, count + 1);
                }
            }
        }
        
        int startIndex = 0, len = tokens.size(); // tim trong text
        
        for (Map.Entry<Integer, Integer> entry : conj2Count.entrySet()) {
            Conjunction conjunction = conjunctionColl.getConjunctionAt(entry.getKey());
            
            if (conjunction != null) {
                if (conjunction.size() == entry.getValue()) {
                	
                	
                	// kiem tra thu tu
                	// tokens vs conjunction
                	
                	// danh sach cac tu trong conjunction
                	List<String> listWords = conjunction.getConjWords();
                	//System.out.println("S:" + listWords.toString());
                	startIndex = 0;
                	boolean notfound = true;
                	
                	// Duyet danh sach tu trong conjunction
                	for (String word : listWords) {
                		//System.out.print("Find : " + word);
                		notfound = true;
                		// Tim word trong text
                		for (int i = startIndex; i < len; i++) {
                			
                			// word nam trong tokens
                			if (word.equals(tokens.get(i))) {
                				startIndex = i + 1;
                				notfound = false;
                				break;
                			}
                		}
                		
                		//System.out.println(" Found:" + notfound);
                		if (notfound)
                			break;
                	}
                		
                    if (! notfound)
                    	relevantConjs.add(conjunction.getStr());
                    
                }
            }
        }
        
        return relevantConjs;
    }
}
