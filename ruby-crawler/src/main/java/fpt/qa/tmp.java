package fpt.qa;

import com.fpt.ruby.business.helper.RedisHelper;

/**
 * Created by timxad on 11/11/14.
 */
public class tmp {
    public static void main(String[] args) {
        String path = null;
        try {
            path = (Class.forName("HomeController")).getClass().getClassLoader().getResource("").getPath();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(path);
    }
}
