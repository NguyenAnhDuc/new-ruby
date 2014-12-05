package fpt.qa.spellchecker;

import fpt.qa.spellchecker.io.StopWordsFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellCheckAndCorrector {

    private Map<String, String> conjunctionMap = new HashMap<String, String>();
    private SpellingChecker spellingChecker = new SpellingChecker();
    private StopWordsFilter stopWordsFilter = new StopWordsFilter();

    public SpellCheckAndCorrector(String resourcePath) {
        System.err.println("stopword = " + resourcePath + "/data-correct-spellchecker/stopwords/stopwords.txt");
        stopWordsFilter = new StopWordsFilter(resourcePath + "/data-correct-spellchecker/stopwords/stopwords.txt");

        spellingChecker = new SpellingChecker(resourcePath);
        //spellingChecker.training("data/train/big.txt");
        conjunctionMap = new HashMap<String, String>();
    }


    public Map<String, String> getConjunctionMap() {
        return this.conjunctionMap;
    }

    public List<String> getTokens(String text) {
        List<String> result = new ArrayList<String>();
        for (String s : stopWordsFilter.getStopWords()) {
            if (text.contains(s)) {
                text = text.replaceAll(s, "");
            }
        }
        String[] tokens = text.split(" |\\?");
        for (String token : tokens) {
            if (!token.equalsIgnoreCase("")) {
                result.add(token);
            }
        }
        // System.out.println();
        return result;
    }

    /*
     *
     */
    void print() {
        for (String s : conjunctionMap.keySet()) {
            System.out.println("{" + s + " ---> "
                    + conjunctionMap.get(s) + " }");
        }
    }

    public void makeAll(List<String> tokens) {
        if (tokens.isEmpty()
                || isAccept(listToString(tokens, 0, tokens.size() - 1))) {
            return;
        }
        int pv = 0;
//		System.err.println("tokens : " + tokens);
        for (int i = 0; i < tokens.size(); ++i) {
            String str = listToString(tokens, 0, i);
            //System.err.println("word = " + str);
            if (isAccept(str)) {
                pv = i;
                //System.err.println(listToString(tokens, 0, i+1));
                if (isAccept(listToString(tokens, 0, i + 1))) {
                    conjunctionMap.remove(str);
                    pv = i + 1;
                    break;
                }
            }
        }
        List<String> removes = new ArrayList<String>();
        for (int k = 0; k <= pv; ++k) {
            removes.add(tokens.get(k));
        }
        tokens.removeAll(removes);
        makeAll(tokens);
    }

    public String completed(String text) {
        List<String> tokens = getTokens(text);
        if (isFine(tokens)) {
            System.out.println("Everything OK ! ");
            return text;
        }
        makeAll(tokens);
        for (String s : conjunctionMap.keySet()) {
            text = text.replaceAll(s, conjunctionMap.get(s));
        }
        print();
        conjunctionMap.clear();
        System.out.println("Complete to:  " + text);
        return text;
    }

    private boolean isAccept(String word) {
        String word2 = spellingChecker.removeDuplicateCharacter(word);
        String cand = spellingChecker.correct(word2);
        if (cand != null) {
            //System.err.println(word + " ---> " + cand);
            conjunctionMap.put(word, cand);
            return true;
        }
        return false;
    }

    private boolean isFine(List<String> tokens) {
        for (String token : tokens) {
            if (!spellingChecker.getnToken().containsKey(token)) {
                return false;
            }
        }

        return true;
    }

    private String listToString(List<String> list, int start, int end) {
        if (end == 0) {
            return list.get(0);
        }

        return String.join(" ", list.subList(start, Math.min(end + 1, list.size())));
    }

}
