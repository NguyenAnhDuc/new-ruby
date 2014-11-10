package fpt.qa.mdnlib.test;

import java.util.ArrayList;
import java.util.List;

import fpt.qa.mdnlib.struct.synonym.Synonym;
import fpt.qa.mdnlib.struct.synonym.SynonymCollection;
import fpt.qa.mdnlib.struct.synonym.SynonymMatcher;

public class SynonymMatcherTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SynonymMatcher synonymMatcher = new SynonymMatcher();
		SynonymCollection synonymColl = new SynonymCollection();

		List<String> sPhrases = new ArrayList();

		sPhrases.add("{fpt software}");
		sPhrases.add("{fsoftware}");
		sPhrases.add("{FSoft}");
		synonymColl.addSynonym(new Synonym(1, "FSoft", sPhrases));

		sPhrases.clear();
		sPhrases.add("{fpt university}");
		sPhrases.add("{fu}");
		sPhrases.add("{Dai hoc FPT}");
		synonymColl.addSynonym(new Synonym(2, "FPT University", sPhrases));
		
		synonymColl.print();
		
		synonymMatcher.init(synonymColl);
		
		System.out.println(synonymMatcher.getMatchedSynonyms("Truong dai hoc fpt co hoc sinh thuc tap o FSoft", "FPT University is one of the top , fsoftware").toString());
	}

}
