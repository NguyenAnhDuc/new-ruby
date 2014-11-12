package com.fpt.ruby.business.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fpt.ruby.business.config.SpringMongoConfig;
import com.fpt.ruby.business.constants.ProgramType;
import com.fpt.ruby.business.model.TVModifiers;
import com.fpt.ruby.business.model.TVProgram;



@Service
public class TVProgramService {
	private static long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;
	private static long ONE_DAY = 24 * 60 * 60 * 1000;
	private MongoOperations mongoOperations;

	public TVProgramService(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public TVProgramService() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		this.mongoOperations = (MongoOperations) ctx.getBean("mongoTemplate");
	}

	public void cleanOldData() {
		Date now = new Date(new Date().getTime() - ONE_WEEK);
		Query query = new Query(Criteria.where("start_date").lt(now));
		List<TVProgram> tvPrograms = mongoOperations.find(query,
				TVProgram.class);
		for (TVProgram tvProgram : tvPrograms) {
			System.out.println("TV Program: " + tvProgram.getChannel() + " | "
					+ tvProgram.getTitle() + " | " + tvProgram.getStart_date());
			mongoOperations.remove(tvProgram);
		}
	}

	/*
	 * Remove crawled data on specific day parameter: beforeToday 0 => today -1
	 * : yester day -i : i day before today 1 : tomorrow i : i day after today
	 */
	public void clearDataOnSpecificDay(int beforeToday) {
		Date cur = new Date(new Date().getTime() + beforeToday * ONE_DAY);
		cur.setSeconds(0);
		cur.setMinutes(0);
		cur.setHours(0);

		Date afterCur = new Date(cur.getTime() + ONE_DAY);
		Query query = new Query(Criteria.where("start_date").gte(cur)
				.lt(afterCur));

		System.out.println(query.toString());
		List<TVProgram> toRemove = mongoOperations.find(query, TVProgram.class);
		for (TVProgram prog : toRemove) {
			mongoOperations.remove(prog);
			System.out.println("TV Program: " + prog.getChannel() + " | "
					+ prog.getTitle() + " | " + prog.getStart_date());
		}
	}

	public List<TVProgram> getList(TVModifiers mod, String question) {

		String channel = mod.getChannel();
		String title = mod.getProg_title();
		Date start = mod.getStart();
		Date end = mod.getEnd();
		List<String> types = mod.getType();
		System.out.println("Type of question = " + types + " tvProTitle :"
				+ mod.getProg_title());

		List<TVProgram> result = new ArrayList<TVProgram>();

		if (mod.getStart() == null && mod.getEnd() == null) {
			if (question.contains("hết")
					|| question.contains("kết thúc")
					|| (question.contains("đến") && (question
							.contains("mấy giờ")
							|| question.contains("khi nào")
							|| question.contains("lúc nào") || question
								.contains("bao giờ")))) {
				return new ArrayList<TVProgram>();
			}
		}

		if (channel != null && title != null && start != null && end != null) {
			if (start.equals(end)) {
				result = findByTitleAtTimeAtChannel(title, start, channel);
				return filterByType(result, types);
			} else {
				result = findByTitleInPeriodAtChannel(title, start, end,
						channel);
				return filterByType(result, types);
			}
		}
		System.out.println("[TVProgram Service]-- channel: " + channel + " | proTitle: " + title);
		System.out.println("[TVProgram Service]-- start: " + start + " | end: " + end);
		if (channel == null) {
			if (title == null) {
				if (start == null) {
					return new ArrayList<TVProgram>();
				}
				if (start.equals(end)) {
					result = findAtTime(start);
					return filterByType(result, types);
				}
				result = findInPeriod(start, end);
				return filterByType(result, types);
			}
			if (start == null && end == null) {
				result = findByTitle(title);
				return filterByType(result, types);
			}
			if (start.equals(end)) {
				result = findByTitleAtTime(title, start);
				return filterByType(result, types);
			}
			result = findByTitleInPeriod(title, start, end);
			return filterByType(result, types);
		}
		if (title == null) {
			if (start == null && end == null) {
				result = findByChannel(channel);
				return filterByType(result, types);
			}
			if (start.equals(end)) {
				result = findAtTimeAtChannel(start, channel);
				return filterByType(result, types);
			}
			result = findInPeriodAtChannel(start, end, channel);
			return filterByType(result, types);
		}
		if (start == null && end == null) {
			result = findByTitleAndChannel(title, channel);
			return filterByType(result, types);
		}
		System.out.println("Nothing choice");
		return new ArrayList<TVProgram>();
	}

	public List<TVProgram> findAll() {
		return mongoOperations.findAll(TVProgram.class);
	}

	public List<TVProgram> findByTitle(String title) {
		System.out.println("FIND BY TITLE");
		Date today = new Date();
		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);
		Date oneWeekBefore = new Date(today.getTime() - ONE_WEEK);
		Date oneWeekLater = new Date(today.getTime() + ONE_WEEK);

		return findByTitleInPeriod(title, oneWeekBefore, oneWeekLater);
	}

	public List<TVProgram> findByChannel(String channel) {
		Date today = new Date();
		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);
		Date oneWeekBefore = new Date(today.getTime() - ONE_WEEK);
		Date oneWeekLater = new Date(today.getTime() + ONE_WEEK);

		return findInPeriodAtChannel(today, oneWeekLater, channel);
	}

	public List<TVProgram> findByTitleAndChannel(String title, String channel) {
		Date today = new Date();
		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);
		Date oneWeekLater = new Date(today.getTime() + ONE_WEEK);
		Date oneWeekBefore = new Date(today.getTime() - ONE_WEEK);

		return findByTitleInPeriodAtChannel(title, oneWeekBefore, oneWeekLater,
				channel);
	}

	public List<TVProgram> findInPeriod(Date start, Date end) {
		Query query = new Query(Criteria.where("start_date").gt(start)
				.and("end_date").lt(end));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findByTitleInPeriod(String title, Date start,
			Date end) {
		Query query = new Query(Criteria.where("title")
				.regex("^.*" + title + ".*", "i").and("start_date").gt(start)
				.and("end_date").lt(end));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findInPeriodAtChannel(Date start, Date end,
			String channel) {
		String chn = channel.replaceAll("\\+", "\\\\+");

		Query query = new Query(Criteria.where("channel")
				.regex("^" + chn + "$", "i").and("start_date").gte(start)
				.lte(end));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findByTitleInPeriodAtChannel(String title,
			Date start, Date end, String channel) {
		String channel2 = channel.replace("\\+", "\\\\+");

		Query query = new Query(Criteria.where("channel")
				.regex("^" + channel2 + "$", "i").and("title")
				.regex("^.*" + title + ".*", "i").and("start_date").gt(start)
				.and("end_date").lt(end));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findAtTime(Date startDate) {
		Query query = new Query(Criteria.where("start_date").lt(startDate)
				.and("end_date").gt(startDate));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findByTitleAtTime(String title, Date date) {
		Query query = new Query(Criteria.where("title")
				.regex("^.*" + title + ".*", "i").and("start_date").lt(date)
				.and("end_date").gt(date));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findAtTimeAtChannel(Date date, String channel) {
		String chn = channel.replaceAll("\\+", "\\\\+");
		Query query = new Query(Criteria.where("channel")
				.regex("^" + chn + "$", "i").and("start_date").lt(date)
				.and("end_date").gt(date));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findByTitleAtTimeAtChannel(String title, Date date,
			String channel) {
		String chn = channel.replaceAll("\\+", "\\\\+");
		Query query = new Query(Criteria.where("channel")
				.regex("^" + chn + "$", "i").and("title")
				.regex("^.*" + title + ".*", "i").and("start_date").lt(date)
				.and("end_date").gt(date));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findAfter(Date date) {
		Query query = new Query(Criteria.where("start_date").gt(date))
				.with(new Sort(Direction.ASC, "start_date"));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findAfterByTitle(String title, Date date) {
		Query query = new Query(Criteria.where("title")
				.regex("^.*" + title + ".*", "i").and("start_date").gt(date))
				.with(new Sort(Direction.ASC, "start_date"));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findAfterAtChannel(Date date, String channel) {
		String chn = channel.replaceAll("\\+", "\\\\+");
		Query query = new Query(Criteria.where("channel")
				.regex("^" + chn + "$", "i").and("start_date").gt(date))
				.with(new Sort(Direction.ASC, "start_date"));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findAfterByTitleAtChannel(String title, Date date,
			String channel) {
		String chn = channel.replaceAll("\\+", "\\\\+");

		Query query = new Query(Criteria.where("channel")
				.regex("^" + chn + "$", "i").and("title")
				.regex("^.*" + title + ".*", "i").and("start_date").gt(date))
				.with(new Sort(Direction.ASC, "start_date"));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findProgramToShow(int day) {
		Date date = new Date(new Date().getTime() + day * ONE_DAY);
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		Query query = new Query(Criteria.where("start_date").gt(date))
				.with(new Sort(Direction.ASC, "start_date"));
		return mongoOperations.find(query, TVProgram.class);
	}

	public List<TVProgram> findTaggedProgram(Date start, Date end) {
		Query query = new Query(Criteria.where("start_date").gte(start)
				.lte(end).and("types").exists(true));
		List<TVProgram> progs = mongoOperations.find(query, TVProgram.class);
		List<TVProgram> rs = new ArrayList<TVProgram>();
		for (TVProgram prog : progs) {
			if (!prog.getType().equals(ProgramType.OTHER.toString())) {
				rs.add(prog);
			}
		}
		return rs;
	}

	public void save(TVProgram tvProgram) {
		mongoOperations.save(tvProgram);
	}

	public void dropCollection() {
		mongoOperations.dropCollection(TVProgram.class);
	}

	private List<TVProgram> filterByType(List<TVProgram> lst, List<String> type) {

		if (type == null || type.isEmpty()) {
			return lst;
		}
		long start = System.currentTimeMillis();
		List<TVProgram> res = new ArrayList<TVProgram>();
		for (TVProgram prog : lst) {
			boolean check = true;
			String curType = prog.getType();
			if (curType == null)
				continue;
			for (String t : type) {
				if (curType.contains(t)) {
					res.add(prog);
					break;
				}
			}
		}
		return res;
	}
	public List<TVProgram> findByTimeRange(List<TVProgram> list, Date start, Date end) {
		if(start == null || end == null) {
			return list;
		}
		List<TVProgram> rtList = new ArrayList<TVProgram>();
		for(TVProgram program : list) {
			if((program.getStart_date().after(start)) && (program.getEnd_date().before(end))) {
				rtList.add(program);
			}
		}
		return rtList;
	}
	public static void main(String[] args) throws Exception {
		TVProgramService tvService = new TVProgramService();
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				SpringMongoConfig.class);
		MongoOperations mongoOperations = (MongoOperations) ctx
				.getBean("mongoTemplate");
		mongoOperations.findAll(TVProgram.class);
		System.out.println("DONE");

	}

	public void show(List<TVProgram> program) {
		for (TVProgram program2 : program) {
			System.out.println("program : [" + program2.getChannel() + " , "
					+ program2.getTitle() + " , " + program2.getType() + " , ");
		}

	}

}