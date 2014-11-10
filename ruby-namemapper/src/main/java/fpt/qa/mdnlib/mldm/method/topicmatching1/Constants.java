/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.topicmatching1;

/**
 *
 * @author hieupx
 */
public class Constants {
    public static final int MaxNumWordsPerTopic = 500;
    public static final int MaxNumTopicsSelected = 2;
    public static final double CumulativeThreshold = 0.4;
    public static final double Times = 2.0;
    public static final double MaxWordWeight = Math.log10(MaxNumWordsPerTopic + 2);
    
    public static final int LOG_BASE_1 = 25;
    public static final int LOG_BASE_2 = 100;    
    public static final int LOG_BASE_3 = 200;
    public static final int LOG_BASE_4 = 300;
    public static final int LOG_BASE_5 = 500;
    public static final int LOG_BASE_6 = 1000;
    public static final int LOG_BASE_7 = 5000;
}
