/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fpt.ruby.intent.detection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ngan
 */
public class StopWords {
    private static List<String> stopwords = new ArrayList<String>();
    
    public static void loadList(String fileIn){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileIn));
            
            String line;
            while((line = reader.readLine()) != null){
                stopwords.add(line);
            }
            
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(StopWords.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String removeAllStopWords(String sent){
        String res = sent;
        for (String w : stopwords){
            res = res.replaceAll(w, " ");
        }
        return res.replaceAll("\\s+", " ").trim();
    }
    
}
