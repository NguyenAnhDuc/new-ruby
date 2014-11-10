/**
 * 
 */
package fpt.qa.mdnlib.test;

import fpt.qa.mdnlib.struct.conjunction.ConjunctionChecker;

/**
 * @author pxhieu
 *
 */
public class ConjunctionCheckerTest {
    public static void main(String[] args) {
        // TODO code application logic here
        
        ConjunctionChecker conjChecker = new ConjunctionChecker();
        
        conjChecker.addConjunction("{ngan hang}");
        conjChecker.addConjunction("{lai suat}");
        conjChecker.addConjunction("{My Tam hat}");
        conjChecker.addConjunction("{bai hat viet}");
        conjChecker.addConjunction("{gia vang}");
        conjChecker.addConjunction("{gia vang sjc}");
        conjChecker.addConjunction("{thanh khoan}");
        conjChecker.addConjunction("{tieng hat}");
        conjChecker.addConjunction("{bai hat}");
        conjChecker.addConjunction("{ngan hang nha nuoc}");
        
        conjChecker.print();
        
        String text = "hien nay Ngan hang Nha nuoc dang xuc viec ha lai suat cho vay.";
        System.out.println(text);
        System.out.println(conjChecker.getRelevantConjunctions(text).toString());
    }
}
