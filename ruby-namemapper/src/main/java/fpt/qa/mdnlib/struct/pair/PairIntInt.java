/**
 * 
 */
package fpt.qa.mdnlib.struct.pair;

/**
 * @author pxhieu
 *
 */
public class PairIntInt implements Comparable<Object> {
    public Integer first;
    public Integer second;
    
    public PairIntInt(int f, int s) {
        this.first = f;
        this.second = s;
    }
    
    @Override
    public int compareTo(Object o) {
        return this.second - ((PairIntInt)o).second;
    }
}
