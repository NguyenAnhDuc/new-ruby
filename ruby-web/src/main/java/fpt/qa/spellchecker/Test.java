package fpt.qa.spellchecker;

import fpt.qa.spellchecker.io.UTF8FileUtility;

import java.io.IOException;

/**
 * Created by Bui Thien on 12/4/2014.
 */
public class Test {

	public static void main(String[] args) throws IOException {
		// String TEXT1 = "hom nay tren vtv3 co ai laf trieu phu";
		String[] questions = UTF8FileUtility.getLines("data/data-correct-spellchecker/sample/test");
		SpellCheckAndCorrector spellCheckAnCorrector = new SpellCheckAndCorrector("data/");
		for (String question : questions) {
			long st = System.currentTimeMillis();
			String ner = spellCheckAnCorrector.completed(question);
			System.out.println("Question  = " + question);
			System.out.println("Corrected = " + ner + "\n -" + "Time :"
					+ (System.currentTimeMillis() - st) + "-");
			System.out.println("_________________________________________________________________");
			
		}
	}
}
