/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.gibbslda;

import java.util.List;

/**
 *
 * @author hieupx
 */
public class Document {
    public int[] words;
    public String rawStr;
    public int length;
    
    public Document() {
        words = null;
        rawStr = "";
        length = 0;
    }
    
    public Document(int length) {
        this.length = length;
        rawStr = "";
        words = new int[length];
    } 
    
    public Document(int length, int[] words) {
        this(length);
        System.arraycopy(words, 0, this.words, 0, length);
    }
    
    public Document(int length, int[] words, String rawStr) {
        this(length, words);
        this.rawStr = rawStr;
    }    
    
    public Document(List<Integer> doc) {
        this.length = doc.size();
        rawStr = "";        
        
        this.words = new int[length];        
        for (int i = 0; i < length; i++) {
            this.words[i] = doc.get(i);
        }
    }
    
    public Document(List<Integer> doc, String rawStr) {
        this(doc);
        this.rawStr = rawStr;
    }
    
    public void print() {
        if (rawStr != null && !rawStr.isEmpty()) {
            System.out.println(rawStr);
        }
    }
}
