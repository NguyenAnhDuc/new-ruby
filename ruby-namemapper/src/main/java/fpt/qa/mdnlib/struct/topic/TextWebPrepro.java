/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpt.qa.mdnlib.struct.pair.PairStrInt;
import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author pxhieu
 */
public class TextWebPrepro {
    public static List<PairStrInt> preprocessText(String text) {
        Map<String, Integer> map = new HashMap();
        
        List<String> tokens = StrUtil.tokenizeStr(text.toLowerCase());
        int count = tokens.size();
        for (int i = 0; i < count; i++) {
            String word = tokens.get(i);
            Integer freq = map.get(word);
            if (freq == null) {
                map.put(word, 1);
            } else {
                freq += 1;
                map.put(word, freq);
            }
        } 
        
        List<PairStrInt> tempList = new ArrayList();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            tempList.add(new PairStrInt(entry.getKey(), entry.getValue()));
        }
        
        return tempList;
    }
    
    public static List<PairStrInt> preprocessText2(String text) {
        List<PairStrInt> results = new ArrayList();

        List<String> tokens = StrUtil.tokenizeStr(text.toLowerCase());
        int count = tokens.size();
        for (int i = 0; i < count; i++) {
            String word = tokens.get(i);
            results.add(new PairStrInt(word, 1));
        } 
        
        return results;
    }
}
