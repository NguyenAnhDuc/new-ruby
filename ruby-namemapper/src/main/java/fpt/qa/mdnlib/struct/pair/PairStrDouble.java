/**
 * 
 */
package fpt.qa.mdnlib.struct.pair;

import fpt.qa.mdnlib.util.string.StrUtil;

/**
 * @author pxhieu
 *
 */
public class PairStrDouble implements Comparable<Object> {
    public String first;
    public Double second;
    
    public PairStrDouble(String f, double s) {
        this.first = f;
        this.second = s;
    }
    
    @Override
    public int compareTo(Object o) {
        double val1 = second.doubleValue();
        double val2 = ((PairStrDouble)o).second.doubleValue();
        
        if (val1 > val2) {
            return 1;
        } else if (val1 == val2) {
            return 0;
        } else {
            return -1;
        }
    }
    
    public void print() {
        System.out.println(this.first + "\t\t\t" + this.second);
    }
    
    @Override
    public String toString() {
        return StrUtil.normalizeStr(this.first);
    }
}
