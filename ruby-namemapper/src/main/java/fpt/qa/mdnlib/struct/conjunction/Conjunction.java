/**
 * 
 */
package fpt.qa.mdnlib.struct.conjunction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fpt.qa.mdnlib.util.string.StrUtil;

/**
 * @author pxhieu
 * 
 */
public class Conjunction {
	private String conjStr;
	private Set<String> conjWords = null;
	private ArrayList<String> listWords = null;

	public Conjunction() {
		conjStr = "";
		conjWords = new HashSet<String>();
		listWords = new ArrayList<String>();
	}

	public Conjunction(String str) {
		this();

		conjStr = str;
		parseStr();
	}

	public void setStr(String str) {
		conjStr = str;
		parseStr();
	}

	public final void parseStr() {
		conjWords.clear();

		List<String> tokens = StrUtil.tokenizeStr(
				StrUtil.normalizeStr(conjStr), "{} \t\r\n");
		for (int i = 0; i < tokens.size(); i++) {
			String str = tokens.get(i).toLowerCase();
			listWords.add(str);
			conjWords.add(str);
		}
	}

	public int size() {
		return conjWords.size();
	}

	public String getStr() {
		return conjStr;
	}

	public List<String> getConjWords() {
		return listWords;
	}

	public void print() {
		System.out.println(conjStr);

		List<String> words = getConjWords();
		for (int i = 0; i < words.size(); i++) {
			System.out.println("\t" + words.get(i));
		}
	}
}
