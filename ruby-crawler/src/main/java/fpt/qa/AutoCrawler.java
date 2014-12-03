package fpt.qa;

import com.fpt.ruby.business.service.MovieTicketService;
import com.fpt.ruby.business.service.NameMapperService;
import com.fpt.ruby.business.service.TVProgramService;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import fpt.qa.crawler.CrawlerMyTV;
import fpt.qa.crawler.CrawlerVTVCab;
import fpt.qa.crawler.moveek.MoveekCrawler;
import fpt.qa.type_mapper.TypeMapper;

public class AutoCrawler {

    //	private LogService logService;
    private static Integer FUTURE_DAY = 3;
    private MovieTicketService movieTicketService;
    private TVProgramService tvProgramService;
    // private CinemaService cinemaService;

    public static void main(String[] args) {
        String dir = "/home/timxad/ws/proj/ruby/new-ruby/ruby-web/src/main/resources/";
        int numday = 3;

        if (args.length >= 1) {
            dir = args[0];
            if (args.length >= 2) {
                numday = Integer.parseInt(args[1]);
            }
        }

        System.out.println("Start!");
        AutoCrawler x = new AutoCrawler();
        x.doCrawl(dir, numday);
    }

    private void doCrawl(String dir, int numday) {
        movieTicketService = new MovieTicketService();
        tvProgramService = new TVProgramService();
        CrawlerVTVCab vtvcab = new CrawlerVTVCab();
        CrawlerMyTV mytv = new CrawlerMyTV();

        // Clean data
        try {
            movieTicketService.cleanOldData();
        } catch (Exception e) {
            System.out.println("error clean movie ticket.");
        }

        try {
            tvProgramService.cleanOldData();
        } catch (Exception e) {
            System.out.println("error clean movie ticket.");
        }

        try {
            for (int i = 0; i <= numday; ++i) {
                movieTicketService.clearDataOnSpecificDay(i);
                tvProgramService.clearDataOnSpecificDay(i);
            }
        } catch (Exception e) {
            System.out.println("Error clear on specific data. Message = " + e.getMessage());
        }

        // Start crawling
        NameMapperService nms = new NameMapperService();
        ConjunctionHelper conjunctionHelper = new ConjunctionHelper(dir, nms);

        try {
            mytv.doCrawl(tvProgramService, conjunctionHelper, FUTURE_DAY);
        } catch (Exception ex) {
            System.out.println("Error crawling my tv!! Message = " + ex.getMessage());
        }

        try {
            vtvcab.doCrawl(tvProgramService, conjunctionHelper, FUTURE_DAY);
        } catch (Exception ex) {
            System.out.println("Eror crawling vtvcab!! Message = " + ex.getMessage());
        }

        TypeMapper.clear(); // clear data

        try {
            MoveekCrawler.doCrawl(movieTicketService);
        } catch (Exception ex) {
            System.out.println("Eror crawling moveek!! Message = " + ex.getMessage());
        }
    }

}