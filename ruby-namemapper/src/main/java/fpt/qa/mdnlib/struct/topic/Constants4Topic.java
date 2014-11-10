/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.topic;

/**
 *
 * @author hieupx
 */
public class Constants4Topic {
    public static final int MaxNumWordsPerLDATopic = 300;
    
    public static final int MaxNumWordsPerHumanTopic = 500;
    // public static final int MaxNumWordsPerHumanTopic = 5000;
    
    public static final double MaxWordWeightforLDATopic = 
            Math.log10(MaxNumWordsPerLDATopic + 2);
    
    public static final double MaxWordWeightforHumanTopic = 
            Math.log10(MaxNumWordsPerHumanTopic + 2);
}
