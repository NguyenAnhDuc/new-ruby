/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.gibbslda;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class Dictionary {
    private Map<String, Integer> word2id;
    private Map<Integer, String> id2word;
    
    public Dictionary() {
        word2id = new HashMap();
        id2word = new HashMap();
    }
    
    public int size() {
        return word2id.size();
    }

    public boolean isEmpty() {
        return (size() == 0);
    }
    
    public String getWord(int id) {
        return id2word.get(id);
    }
    
    public Integer getId(String word) {
        return word2id.get(word);
    }
    
    public boolean contains(String word) {
        return word2id.containsKey(word);
    }
    
    public boolean contains(int id) {
        return id2word.containsKey(id);
    }

    public int addWord(String word) {
        if (!contains(word)) {
            int id = word2id.size();
            
            word2id.put(word, id);
            id2word.put(id, word);
            
            return id;
            
        } else {
            return word2id.get(word);
        }
    }
    
    public boolean readWordMap(String wordMapFile) {
        BufferedReader fin;
        
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(wordMapFile), "UTF8"));

            String line;
            
            /* read number of words */
            line = fin.readLine();
            int nWords = Integer.parseInt(line);
            
            if (nWords <= 0) {
                fin.close();
                return false;
            }
            
            /* read word2id map */
            for (int i = 0; i < nWords; i++) {
                line = fin.readLine();
                
                List<String> tokens = StrUtil.tokenizeStr(line);
                if (tokens.size() != 2) {
                    continue;
                }
                
                String word = tokens.get(0);
                int id = Integer.parseInt(tokens.get(1));
                
                word2id.put(word, id);
                id2word.put(id, word);
            }
            
            fin.close();
            
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.toString());
            return false;
        } catch (IOException ex) {
            System.err.println(ex.toString());
            return false;
        } 
        
        return true;
    }
    
    public boolean writeWordMap(String wordMapFile) {
        BufferedWriter out;
        
        try {
            out = new BufferedWriter(
                         new OutputStreamWriter(
                         new FileOutputStream(wordMapFile), "UTF8"));

            /* write number of words */
            out.write(word2id.size() + "\n");
            
            /* write word2id map */
            for (Map.Entry<String, Integer> entry : word2id.entrySet()) {
                out.write(entry.getKey() + " " + entry.getValue() + "\n");
            }
            
            out.close();
            
            return true;
            
        } catch (IOException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        }
        
        return false;
    }
}
