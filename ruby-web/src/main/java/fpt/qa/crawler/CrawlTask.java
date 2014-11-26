package fpt.qa.crawler;

import fpt.qa.AutoCrawler;

import java.util.TimerTask;

public class CrawlTask extends TimerTask {
    Thread crawler;

    public CrawlTask() {
        crawler = new Thread() {
            @Override
            public void run() {
                (new AutoCrawler()).main(new String[]{});
            }
        };
    }

    @Override
    public void run() {
        crawler.start();
    }
}
