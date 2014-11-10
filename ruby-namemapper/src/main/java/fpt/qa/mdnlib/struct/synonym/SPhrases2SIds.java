package fpt.qa.mdnlib.struct.synonym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SPhrases2SIds {
	private SynonymCollection synonymColl = null;
	private Map<String, List> sPhrases2SIds = null;

	public SPhrases2SIds() {
		synonymColl = null;
		sPhrases2SIds = new HashMap();
	}
	
	public void init(SynonymCollection synonymColl) {
		this.synonymColl = synonymColl;
		genMap();
	}
	
	public void genMap() {
		int synonymCount = synonymColl.size();
		for (int i = 0; i < synonymCount; i++) {
			Synonym synonym = synonymColl.getSynonymAt(i);

			int sId = synonym.getSynonymId();
			List sPhrases = synonym.getSynonymPhrases();
			for (int j = 0; j < sPhrases.size(); j++) {
				String phrase = ((String)sPhrases.get(j)).toLowerCase();
				List sIdList = sPhrases2SIds.get(phrase);
				if (sIdList == null) {
					List tempSIdList = new ArrayList();
					tempSIdList.add(sId);
					sPhrases2SIds.put(phrase, tempSIdList);
				} else {
					sIdList.add(sId);
				}
			}
		}
	}
	
	public List<Integer> getSIdsOfSPhrase(String sPhrase) {
		return sPhrases2SIds.get(sPhrase);
	}
}
