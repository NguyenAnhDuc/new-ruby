package fpt.qa.mdnlib.struct.synonym;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Synonym {
	private int sId;
	private String sLabel;
	private Set<String> sPhrases;

	public Synonym() {
		sId = -1;
		sLabel = "";
		sPhrases = new HashSet<String>();
	}

	public Synonym(int id, String label) {
		this();
		
		sId = id;
		sLabel = label;
	}

	public Synonym(int id, String label, List<String> phrases) {
		this(id, label);
		
		for (int i = 0; i < phrases.size(); i++) {
			sPhrases.add(phrases.get(i));
		}
	}

	public int getSynonymId() {
		return sId;
	}
	
	public void setSynonymId(int id) {
		sId = id;
	}
	
	public String getSynonymLabel() {
		return sLabel;
	}

	public void setSynonymLabel(String label) {
		sLabel = label;
	}
	
	public List<String> getSynonymPhrases() {
		List<String> phrases = new ArrayList<String>();
		
		for (String phrase : sPhrases) {
			phrases.add(phrase);
		}
		
		return phrases;
	}

	public void setSynonymPhrases(List<String> phrases) {
		sPhrases.clear();

		for (int i = 0; i < phrases.size(); i++) {
			sPhrases.add(phrases.get(i));
		}
	}
	
	public void addSynonymPhrase(String phrase) {
		sPhrases.add(phrase);
	}
	
	public void print() {
		System.out.println(sLabel + " (" + sId + "):");
		
		for (String phrase : sPhrases) {
			System.out.println("\t" + phrase);
		}
	}
}
