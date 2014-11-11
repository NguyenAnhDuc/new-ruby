package fpt.qa;

import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.service.LogService;
import com.fpt.ruby.business.service.MovieTicketService;
import com.fpt.ruby.business.service.NameMapperService;
import com.fpt.ruby.business.service.TVProgramService;

import com.fpt.ruby.namemapper.conjunction.ConjunctionHelper;
import fpt.qa.crawler.CrawlerMyTV;
import fpt.qa.crawler.CrawlerVTVCab;
import fpt.qa.type_mapper.TypeMapper;

public class AutoCrawler {

	private MovieTicketService movieTicketService;
	private TVProgramService tvProgramService;
	private LogService logService;

	// private CinemaService cinemaService;

	private void init() {
//		movieTicketService = new MovieTicketService();
		tvProgramService = new TVProgramService();
		logService = new LogService();
//		cinemaService = new CinemaService();
	}

	private void cleanData() {
		try {
//			movieTicketService.cleanOldData();
		} catch (Exception e) {
			System.out.println("error clean movie ticket.");
		}

		try {
			tvProgramService.cleanOldData();
		} catch (Exception e) {
			System.out.println("error clean movie ticket.");
		}

		try {
//			movieTicketService.clearDataOnSpecificDay(0);
//			movieTicketService.clearDataOnSpecificDay(1);
			tvProgramService.clearDataOnSpecificDay(0);
			tvProgramService.clearDataOnSpecificDay(1);
		} catch (Exception e) {
			System.out.println("Error clear today data. " + e.getMessage());
		}
	}

	private void doCrawl(ConjunctionHelper conjunctionHelper) {
		CrawlerVTVCab crawvtvcab = new CrawlerVTVCab();
		CrawlerMyTV crawlmytv = new CrawlerMyTV();
//		CrawlPhimChieuRap crawlPhimChieuRap = new CrawlPhimChieuRap();
		NameMapperService nameMapperService = new NameMapperService();
		try {
			crawlmytv.crawlMyTV(tvProgramService);
		} catch (Exception ex) {
			System.out.println("Error crawling my tv!! Message = " + ex.getMessage());
		}

		try {
			crawvtvcab.doCrawl(tvProgramService, nameMapperService, conjunctionHelper);
		} catch (Exception ex) {
			System.out.println("Eror crawling vtvcab!! Message = " + ex.getMessage());
		}

		TypeMapper.clear(); // clear data

//		try {
//			MoveekCrawler.doCrawl(movieTicketService);
//		} catch (Exception ex) {
//			System.out.println("Eror crawling moveek!! Message = " + ex.getMessage());
//		}
	}

	public static void main(String[] args) {
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		NameMapperService nameMapperService = new NameMapperService();
		ConjunctionHelper conjunctionHelper = new ConjunctionHelper(dir, nameMapperService);
		System.out.println("Start!");
		AutoCrawler x = new AutoCrawler();
		x.init();
		x.cleanData();
		x.doCrawl(conjunctionHelper);
	}

}
