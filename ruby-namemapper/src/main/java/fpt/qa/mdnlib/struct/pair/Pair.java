/**
 * 
 */
package fpt.qa.mdnlib.struct.pair;

/**
 * @author pxhieu
 *
 */
public class Pair<F, S> {
    public F first;
    public S second;
    
    public Pair(F f, S s) {
        this.first = f;
        this.second = s;
    }
    
    public String toString(){
    	return "[" + first + ", " + second + "]";
    }
}
