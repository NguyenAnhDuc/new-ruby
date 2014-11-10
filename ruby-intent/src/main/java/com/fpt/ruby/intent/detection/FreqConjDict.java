/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fpt.ruby.intent.detection;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import jmdn.md.method.conjunction.Conjunction;
import jmdn.md.method.conjunction.ConjunctionMatcher;

/**
 *
 * @author ngan
 */
public class FreqConjDict {
    private static ConjunctionMatcher conjMatcher = new ConjunctionMatcher();
    
    /*
     * initialization
     */
    static {
        conjMatcher = new ConjunctionMatcher();
    }
    
    public static void loadConjList(String fileIn) {
        BufferedReader fin;
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(fileIn), "UTF8"));

            String line;
            while ((line = fin.readLine()) != null) {
                int idx = line.indexOf(":");
                if (idx < 0){
                    continue;
                }
                String word = line.substring(idx + 1);
                Conjunction conj = new Conjunction(word);
                conj.setConjName(line);
                conjMatcher.addConjunction(conj);
            }
            
            fin.close();
            
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        } catch (IOException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        }        
    }
    
    public static void matches(List<String> cps, String sent) {
        List<Conjunction> mtedConjs = conjMatcher.getMatchedConjunctionsWithOrder(sent);
        for (Conjunction c : mtedConjs){
            cps.add(c.getConjName());
        }
    }
}
