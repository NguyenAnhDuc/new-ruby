package fpt.qa.crawler;

import java.util.Calendar;
import java.util.Timer;

import static java.util.Calendar.*;

/**
 * Created by timxad on 11/24/14.
 */
public class X {

    public static void main(String[] args) {
        Timer timer = new Timer();
        Calendar date = Calendar.getInstance();
//        date.set(Calendar.HOUR, 1);
//        date.set(Calendar.MINUTE, 0);
//        date.set(Calendar.SECOND, 0);

        timer.schedule(new CrawlTask(),
                date.getTime(),
                10 * 60 * 1000);
    }
}
