/**
 * 
 */
package fpt.qa.mdnlib.math.random;

import java.util.Random;

/**
 * @author pxhieu
 *
 */
public class RandomNumber {
    private Random randomGenerator = null;
    
    /**
     * RandomNumber default constructor
     */
    public RandomNumber() {
        randomGenerator = new Random();
    }
    
    /**
     * This method generates the next random integer in [0..MAX-1], MAX depends on platform, machine architecture
     * @return the random integer
     */
    public int genNextInt() {
        return randomGenerator.nextInt();
    }
    
    /**
     * This method generates the next random integer in [0..max]
     * @param max
     * @return the random integer
     */
    public int genNextInt(int max) {
        return randomGenerator.nextInt(max);
    }
}
