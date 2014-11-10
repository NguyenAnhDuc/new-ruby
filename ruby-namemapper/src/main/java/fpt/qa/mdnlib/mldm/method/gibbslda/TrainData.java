/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.gibbslda;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class TrainData {
    public List<Document> docs;
    public Dictionary dict;
    
    public TrainData() {
        docs = new ArrayList();
        dict = new Dictionary();
    }
    
    public TrainData(Dictionary dict) {
        this();
        
        if (dict != null) {
            this.dict = dict;
        } else {
            this.dict = new Dictionary();
        }
    }
    
    public int size() {
        return docs.size();
    }
    
    public Dictionary getDict() {
        return dict;
    }
    
    public int dictSize() {
        return dict.size();
    }
    
    public void addDoc(Document doc) {
        docs.add(doc);
    }
    
    public boolean readData(String dataFile) {
        BufferedReader fin;
        
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(dataFile), "UTF8"));

            String line;
            
            /* read number of docs */
            line = fin.readLine();
            int nDocs = Integer.parseInt(line);
            
            if (nDocs <= 0) {
                System.out.println("You must specify the number of documents at the top of data file");
                fin.close();
                return false;
            }
            
            /* read documents */
            while ((line = fin.readLine()) != null) {
                List<String> tokens = StrUtil.tokenizeStr(line);
                
                if (tokens.isEmpty()) {
                    continue;
                }
                
                int docLen = tokens.size();
                Document doc = new Document(docLen);
                
                for (int i = 0; i < docLen; i++) {
                    doc.words[i] = dict.addWord(tokens.get(i));
                }
                
                docs.add(doc);
            }
            
            fin.close();
            
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.toString());
            return false;
        } catch (IOException ex) {
            System.err.println(ex.toString());
            return false;
        } 
        
        return (docs.size() > 0);
    }
}
