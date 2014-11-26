package fpt.qa.crawler;

import com.fpt.ruby.business.model.NameMapper;
import com.fpt.ruby.business.service.NameMapperService;

import java.io.*;
import java.util.List;

/**
 * Created by timxad on 11/25/14.
 */
public class Y {
    public static void main(String[] args) {
        NameMapperService nms = new NameMapperService();
        List<NameMapper> all = nms.getAll();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("out.txt"));
            for (NameMapper n: all) {
                bw.write(n.getDomain() +"\t" + n.getType() + "\t" + n.getName());
                for (String s : n.getVariants()) {
                    bw.write("\t" + s);
                }
                bw.write("\n");
            }
            bw.close();
        } catch (Exception e) {

        }
    }
}
