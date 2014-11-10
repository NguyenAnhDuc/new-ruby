package fpt.qa.mdnlib.struct.synonym;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fpt.qa.mdnlib.struct.conjunction.ConjunctionChecker;

public class SynonymMatcher {
	private SPhrases2SIds sPhrases2SIds = null;
	private ConjunctionChecker conjChecker = null;
	
	public SynonymMatcher() {
		sPhrases2SIds = new SPhrases2SIds();
		conjChecker = new ConjunctionChecker();
	}
	
	public void init(SynonymCollection synonymColl) {
		sPhrases2SIds.init(synonymColl);
		
		int synonymCount = synonymColl.size();
		for (int i = 0; i < synonymCount; i++) {
			List<String> sPhrases = synonymColl.getSynonymAt(i).getSynonymPhrases();
			for (int j = 0; j < sPhrases.size(); j++) {
				conjChecker.addConjunction(sPhrases.get(j).toLowerCase());
			}
		}
	}
	
	public Set<Integer> getRelevantSynonyms(String text) {
		Set<Integer> sIds = new HashSet<Integer>();
		
		List<String> relevantConjs = conjChecker.getRelevantConjunctions(text);
		
		for (int i = 0; i < relevantConjs.size(); i++) {
			List<Integer> sIdsOfPhrase = sPhrases2SIds.getSIdsOfSPhrase(relevantConjs.get(i));
			if (sIdsOfPhrase != null) {
				for (int j = 0; j < sIdsOfPhrase.size(); j++) {
					sIds.add(sIdsOfPhrase.get(j));
				}
			}
		}
		
		return sIds;
	}
	
	public List<Integer> getMatchedSynonyms(String text1, String text2) {
		List<Integer> matchedSIds = new ArrayList();
		
		Set<Integer> sIds1 = getRelevantSynonyms(text1);
		Set<Integer> sIds2 = getRelevantSynonyms(text2);
		sIds1.retainAll(sIds2);
		
		for (Integer sId : sIds1) {
			matchedSIds.add(sId);
		}
		
		return matchedSIds;
	}
}
