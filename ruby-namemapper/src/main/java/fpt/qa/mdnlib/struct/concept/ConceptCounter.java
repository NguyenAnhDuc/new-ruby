/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.concept;

/**
 *
 * @author hieupx
 */
public class ConceptCounter {
    private static int counter = 0;

    public static int getNewConceptId() {
        int id = counter;
        counter++;
        
        return id;
    }
    
    public static void setConceptCounter(int newCounter) {
        counter = newCounter;
    }
}
