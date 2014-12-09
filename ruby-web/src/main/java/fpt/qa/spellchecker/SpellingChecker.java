package fpt.qa.spellchecker;

import fpt.qa.spellchecker.algorithm.DynamicProgramming;
import fpt.qa.spellchecker.io.StopWordsFilter;
import fpt.qa.spellchecker.io.UTF8FileUtility;
import fpt.qa.spellchecker.util.IConstands;
import fpt.qa.spellchecker.util.Language;
import fpt.qa.spellchecker.util.LanguageClassifier;

import java.io.IOException;
import java.util.*;

public class SpellingChecker {

	private String ALPHABET;
	private final HashMap<String, Integer> nWordsEng = new HashMap<String, Integer>();
	private final HashMap<String, Integer> nWordsViet = new HashMap<String, Integer>();
	private final HashMap<String, Integer> nTokens = new HashMap<String, Integer>();
	public HashMap<String, Integer> getnWords() {
		return nWordsEng;
	}

	public HashMap<String, Integer> getnToken() {
		return nTokens;
	}

	public SpellingChecker() {
	}

	public SpellingChecker(String resourcePath) {
		System.err.println("Loading...");
		training(resourcePath+"/data-correct-spellchecker/train/big.txt");

		System.err.println("Loading " + nWordsEng.size()
				+ " features of English\n\t" + " and " + nWordsViet.size()
				+ " features of Vietnamese\n\nLoading completed!");
		/*
		 * try { Thread.sleep(2000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		//in.close();
	}

	public void training(String trainFile, String stopwordsFile) {
		if (stopwordsFile == "" || stopwordsFile == null) {
			training(trainFile);
		} else {
			@SuppressWarnings("unused")
			StopWordsFilter stopWordsFilter = new StopWordsFilter(stopwordsFile);
		}
	}

	public void training(String trainFile) {
		String[] features = UTF8FileUtility.getLines(trainFile);
		for (String s : features) {
			if (s.length() < 4) {
				continue;
			}
			String temp = s.toLowerCase().replaceAll("\\s+", " ").trim();
			if (temp.equalsIgnoreCase(""))
				continue;
			Language lang = LanguageClassifier.getLanguage(temp);
			if (lang.equals(Language.EN)) {
				nWordsEng.put(temp,
						nWordsEng.containsKey(temp) ? nWordsEng.get(temp) + 1
								: 1);
			} else {
				nWordsViet.put(temp,
						nWordsViet.containsKey(temp) ? nWordsViet.get(temp) + 1
								: 1);
			}
			
			String[] tokens = temp.split(" ");
			for(String token : tokens) {
				token = token.trim();
				nTokens.put(token, nTokens.containsKey(token) ? nTokens.remove(token)+1 : 1);
			}
		}
	}

	private final ArrayList<String> edits(String word) {
		if (word.length() < 3) {
			// System.err.println(">>>>>");
			return new ArrayList<String>();
		}
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < word.length(); ++i) {
			result.add((word.substring(0, i) + word.substring(i + 1)).trim());
		}

		for (int i = 0; i < word.length() - 1; ++i) {
			result.add((word.substring(0, i) + word.substring(i + 1, i + 2)
					+ word.substring(i, i + 1) + word.substring(i + 2)).trim());
		}
		for (int i = 0; i < word.length(); ++i) {
			for (int j = 0; j < ALPHABET.length(); j++) {
				if (word.charAt(i) != ' ') {
					result.add((word.substring(0, i)
							+ String.valueOf(ALPHABET.charAt(j)) + word
							.substring(i + 1)).trim());
				}
			}
		}
		for (int i = 0; i <= word.length(); ++i) {
			for (int j = 0; j < ALPHABET.length(); j++) {
				result.add((word.substring(0, i)
						+ String.valueOf(ALPHABET.charAt(j)) + word
						.substring(i)).trim());
			}
		}

		// clear duplicates
		return new ArrayList<String>(new HashSet<String>(result));
	}

	public final String correct(String phrase) {
		phrase = removeDuplicateCharacter(phrase);
		if (nWordsEng.containsKey(phrase) || nWordsViet.containsKey(phrase)) {
			return phrase;
		}

		HashMap<String, Integer> nWords = new HashMap<String, Integer>();
		Language language = LanguageClassifier.getLanguage(phrase);
		if (language.equals(Language.VI)) {
			// token = RemoveAccent.removeAccent(token);
			ALPHABET = IConstands.VIET_CHARS;
			nWords = nWordsViet;
		} else {
			ALPHABET = IConstands.EN_CHARS;
			nWords = nWordsEng;
		}
		ArrayList<String> list = edits(phrase);
		HashMap<Integer, String> candidates = new HashMap<Integer, String>();
		for (String s : list) {
			if (nWords.containsKey(s))
				candidates.put(nWords.get(s), s);
		}
		printMap(candidates);
		if (candidates.size() > 0) {
			// System.out.println("" + (System.currentTimeMillis() -
			// start)+" ms");
			return candidates.get(Collections.max(candidates.keySet()));
		}

		// heuristic algorithm
		double max = 0.8;
		String strMax = null;
		for (String key : nWords.keySet()) {
			// ignore
			if ((double) key.length() / phrase.length() < 0.7
					|| (double) phrase.length() / key.length() < 0.7) {
				continue;
			}

			double score = DynamicProgramming.SWSPassageScore(key, phrase);
				if (max < score) {
					max = score;
					strMax = key;
				} else if (max == score) {
					// strMax = nWords.get(key) > nWords.get(strMax) ? key :
					// strMax ;
					strMax = DynamicProgramming.SWSNonStopPassageScore(key,
							phrase) > DynamicProgramming
							.SWSNonStopPassageScore(strMax, phrase) ? key
							: strMax;
				}
			}
		// System.out.println("" + (System.currentTimeMillis() - start)+" ms");
		return strMax;
	}

	public void printMap(HashMap<Integer, String> candidates) {
		for (Integer word : candidates.keySet()) {
			System.err.println(word + "\t" + candidates.get(word));
		}
	}

	public static void print(HashMap<String, Integer> map) {
		System.out.println("size = " + map.size());
		for (String word : map.keySet()) {
			System.err.println(word + "\t" + map.get(word));
		}
	}

	public String removeDuplicateCharacter(String token) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(token.charAt(0));
		for (int i = 0; i < token.length() - 1; i++) {
			if (token.charAt(i) == token.charAt(i + 1)) {
				// stringBuilder.append(token.charAt(i+1));
				continue;
			} else {
				stringBuilder.append(token.charAt(i + 1));
				// i--;
			}

		}
		return stringBuilder.toString();
	}

	public static void main(String args[]) throws IOException {
		String[] testList = UTF8FileUtility.getLines("data/sample/test.txt");
		SpellingChecker spellchecker = new SpellingChecker("data/train/big.txt");
		for (String s : testList) {
			String word = s.split(";")[0];
			// String result = s.split(";")[1];
			String candidate = spellchecker.correct(word);
			System.out.println("word    = " + word);
			System.out.println("result  = " + candidate);
			System.out
					.println("__________________________________________________");
		}

	}

}
