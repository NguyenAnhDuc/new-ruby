package fpt.qa.spellchecker.io;

import java.util.HashSet;
import java.util.Set;

public class StopWordsFilter  {

    private Set<String> stopWords;

    public Set<String> getStopWords() {
        return stopWords;
    }
    /**
     * Default constructor.
     */
    public StopWordsFilter() {
        stopWords = new HashSet<String>();
    }

    /**
     * Creates a filter given a set of stop words.
     *
     * @param stopWords
     *            a set of stop words.
     */
    public StopWordsFilter(Set<String> stopWords) {
        this.stopWords = stopWords;
    }

    /**
     * Creates a filter, stop words are loaded from a file.
     *
     * @param fileName
     *            a text file containing stop words, each on a line.
     */
    public StopWordsFilter(String fileName) {
        this();
        String[] words = UTF8FileUtility.getLines(fileName);
        for (String word : words) {
            stopWords.add(word.toLowerCase());
        }
    }

    public boolean accept(String token) {
        if (stopWords.contains(token))
            return false;
        return true;
    }
}
