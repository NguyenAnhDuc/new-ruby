/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.selection;

import java.util.*;

import fpt.qa.mdnlib.struct.pair.PairIntDouble;

/**
 *
 * @author pxhieu
 */
public class ClassSelector {
    private List<PairIntDouble> probs = null;
    private Map<Integer, Double> originalValueMap = null;
    
    public ClassSelector() {
        probs = new ArrayList();
        originalValueMap = new HashMap();
    }
    
    public ClassSelector(List<PairIntDouble> ps) {
        this();
        for (int i = 0; i < ps.size(); i++) {
            PairIntDouble pair = ps.get(i);
            probs.add(pair);
            originalValueMap.put(pair.first, pair.second);
        }
    }
    
    public ClassSelector(Map<Integer, Double> ps) {
        this();
        for (Map.Entry<Integer, Double> entry : ps.entrySet()) {
            probs.add(new PairIntDouble(entry.getKey(), entry.getValue()));
        }
    }
    
    public int size() {
        return probs.size();
    }
    
    public void normalize() {
        int count = size();

        double min = probs.get(0).second;
        for (int i = 1; i < count; i++) {
            double temp = probs.get(i).second;
            if (min > temp) {
                min = temp;
            }
        }
        
        if (min < 0) {
            min = Math.abs(min);
            for (int i = 0; i < count; i++) {
                PairIntDouble pair = probs.get(i);
                pair.second += min;
                probs.set(i, pair);
            }
        }
        
        double sum = 0.0;
        for (int i = 0; i < count; i++) {
            sum += probs.get(i).second;
        }
        
        if (sum <= 0) {
            return;
        }
        
        for (int i = 0; i < count; i++) {
            PairIntDouble pair = probs.get(i);
            pair.second /= sum;
            probs.set(i, pair);
        }
    }

    public void sort() {
        Collections.sort(probs);
    }
    
    public List<PairIntDouble> select(int numSelected, double cumulativeThreshold,
            double times) {
        List<PairIntDouble> results = new ArrayList();

        normalize();
        sort();
        
        int count = size();
        if (numSelected > count) {
            numSelected = count;
        }
        
        double cumulativeValue = 0.0;
        double lastValue = 0.0;
        
        for (int i = count - 1; i >= count - numSelected; i--) {
            PairIntDouble pair = probs.get(i);
            
            if (cumulativeValue > cumulativeThreshold) {
                break;
            }
            
            double currentValue = pair.second;
            if (currentValue > 0 && (lastValue / currentValue) > times) {
                break;
            }
            
            PairIntDouble newPair = new PairIntDouble(pair.first, originalValueMap.get(pair.first));
            results.add(newPair);
            
            cumulativeValue += pair.second;
            lastValue = currentValue;
        } 
        
        return results;
    }
    
    public List<PairIntDouble> selectOne() {
        List<PairIntDouble> results = new ArrayList();
        
        PairIntDouble pairMax = new PairIntDouble(-1, 0.0);
        for (int i = 0; i < probs.size(); i++) {
            PairIntDouble pairTemp = probs.get(i);
            if (pairTemp.second > pairMax.second) {
                pairMax = pairTemp;
            }
        }
        
        if (pairMax.second > 0.0) {
            results.add(pairMax);
        }
        
        return results;
    }
}
