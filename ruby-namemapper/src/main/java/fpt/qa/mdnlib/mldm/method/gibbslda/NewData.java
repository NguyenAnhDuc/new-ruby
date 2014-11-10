/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.gibbslda;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class NewData {
    public List<Document> docs; /* new data */
    public List<Document> idocs; /* for inference */
    public Map<Integer, Integer> iid2id; /* for inference */
    public Map<Integer, Integer> id2iid; /* for inference */
    
    public NewData() {
        docs = new ArrayList();
        idocs = new ArrayList();
        iid2id = new HashMap();
        id2iid = new HashMap();
    }
    
    public void reset() {
        docs.clear();
        idocs.clear();
        iid2id.clear();
        id2iid.clear();
    }
    
    public int size() {
        return docs.size();
    }
    
    public Map<Integer, Integer> getIId2IdMap() {
        return iid2id;
    }
    
    public Map<Integer, Integer> getId2IIdMap() {
        return id2iid;
    }
    
    public int dictSize() {
        return id2iid.size();
    }
    
    public boolean readNewData(List<String> documents, Dictionary dict) {
        if (docs.isEmpty()) {
            System.out.println("No documents provided!");
            return false;
        }
        
        reset(); /* reset variables for storing new data */
        
        for (int i = 0; i < docs.size(); i++) {
            List<String> tokens = StrUtil.tokenizeStr(documents.get(i));
            
            if (tokens.isEmpty()) {
                continue;
            }
            
            List<Integer> tempList1 = new ArrayList();
            List<Integer> tempList2 = new ArrayList();
            
            int len = tokens.size();
            for (int j = 0; j < len; j++) {
                Integer wordInt = dict.getId(tokens.get(j));
                
                if (wordInt != null) {
                    Integer iId = id2iid.get(wordInt);
                    
                    if (iId == null) {
                        iId = id2iid.size();
                        id2iid.put(wordInt, iId);
                        iid2id.put(iId, wordInt);
                    } 
                    
                    tempList1.add(wordInt);
                    tempList2.add(iId);
                }
            }
            
            if (tempList1.isEmpty()) {
                continue;
            }
            
            Document doc = new Document(tempList1);
            Document idoc = new Document(tempList2);
            
            docs.add(doc);
            idocs.add(idoc);
        }
        
        return true;
    }
    
    public boolean readNewData(String dataFile, Dictionary dict) {
        List<String> documents = new ArrayList();            
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

            /* read new documents */
            while ((line = fin.readLine()) != null) {
                documents.add(line);
            }
            
            fin.close();
            
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.toString());
            return false;
        } catch (IOException ex) {
            System.err.println(ex.toString());
            return false;
        } 
     
        return readNewData(documents, dict);
    }
}
