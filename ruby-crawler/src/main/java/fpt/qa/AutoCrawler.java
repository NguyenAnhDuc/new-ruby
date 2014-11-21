package fpt.qa;

import com.fpt.ruby.business.service.LogService;
import com.fpt.ruby.business.service.MovieTicketService;
import com.fpt.ruby.business.service.NameMapperService;
import com.fpt.ruby.business.service.TVProgramService;
import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import fpt.qa.crawler.CrawlerMyTV;
import fpt.qa.crawler.CrawlerVTVCab;
import fpt.qa.crawler.moveek.MoveekCrawler;
import fpt.qa.type_mapper.TypeMapper;

public class AutoCrawler {

	private MovieTicketService movieTicketService;
	private TVProgramService tvProgramService;
	private LogService logService;

	// private CinemaService cinemaService;

	private void init() {
		movieTicketService = new MovieTicketService();
		tvProgramService = new TVProgramService();
		logService = new LogService();
//		cinemaService = new CinemaService();
	}

	private void cleanData() {
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
			for (int i = 0; i <= 6; ++i) {
				movieTicketService.clearDataOnSpecificDay(i);
				tvProgramService.clearDataOnSpecificDay(i);
			}
		} catch (Exception e) {
			System.out.println("Error clear today data. " + e.getMessage());
		}
	}

	private void doCrawl(ConjunctionHelper conjunctionHelper) {
		CrawlerVTVCab crawvtvcab = new CrawlerVTVCab();
		CrawlerMyTV crawlmytv = new CrawlerMyTV();
		NameMapperService nameMapperService = new NameMapperService();

		try {
			crawlmytv.doCrawl(tvProgramService, conjunctionHelper);
		} catch (Exception ex) {
			System.out.println("Error crawling my tv!! Message = " + ex.getMessage());
		}

		try {
			crawvtvcab.doCrawl(tvProgramService, nameMapperService, conjunctionHelper);
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

	public static void main(String[] args) {
//		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		String dir = "/home/timxad/ws/proj/ruby/new-ruby/ruby-web/src/main/resources/";
//		String dir = "C:\\Users\\quang\\workspace\\new\\new-ruby\\ruby-web\\src\\main\\resources/";
		NameMapperService nameMapperService = new NameMapperService();
		ConjunctionHelper conjunctionHelper = new ConjunctionHelper(dir, nameMapperService);

		System.out.println("Start!");
		AutoCrawler x = new AutoCrawler();

		x.init();
		x.cleanData();
		x.doCrawl(conjunctionHelper);
	}

}
