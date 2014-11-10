package fpt.qa.mdnlib.struct.synonym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynonymCollection {
	private List<Synonym> synonyms = null;
	private Map<Integer, String> synonymsIds2Labels = null;
	
	public SynonymCollection() {
		synonyms = new ArrayList<Synonym>();
		synonymsIds2Labels = new HashMap<Integer, String>();
	}
	
	public int size() {
		return synonyms.size();
	}

	public void clear() {
		synonyms.clear();
	}

	public Synonym getSynonymAt(int idx) {
		return synonyms.get(idx);
	}

	public void addSynonym(Synonym synonym) {
		synonyms.add(synonym);
		synonymsIds2Labels.put(synonym.getSynonymId(), synonym.getSynonymLabel());
	}
	
	public String getSynonymLabelById(Integer sId) {
		return synonymsIds2Labels.get(sId);
	}

	public List<Synonym> getAllSynonyms() {
		return synonyms;
	}
	
    public void print() {
        for (int i = 0; i < synonyms.size(); i++) {
            synonyms.get(i).print();
            System.out.println();
        }
    }
}
