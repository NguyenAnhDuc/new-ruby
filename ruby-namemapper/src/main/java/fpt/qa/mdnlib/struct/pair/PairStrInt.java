/**
 * 
 */
package fpt.qa.mdnlib.struct.pair;

import fpt.qa.mdnlib.util.string.StrUtil;

/**
 * @author pxhieu
 *
 */
public class PairStrInt implements Comparable<Object> {
    public String first;
    public Integer second;
    
    public PairStrInt(String f, int s) {
        this.first = f;
        this.second = s;
    }
    
    @Override
    public int compareTo(Object o) {
        return this.second - ((PairStrInt)o).second;
    }
    
    public void print() {
        System.out.println(this.first + "\t\t\t" + this.second);
    }
    
    @Override
    public String toString() {
        return StrUtil.normalizeStr(this.first);
    }
}
