package com.fpt.ruby.nlp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.fpt.ruby.business.model.MovieTicket;

import fpt.qa.mdnlib.util.string.StrUtil;

public class TicketAnswerMapperImpl implements TicketAnswerMapper {
	public String getTypeTicketAnswer(List<MovieTicket> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		String type = "";
		for (MovieTicket tick : ans){
			if (!type.contains(tick.getType())){
				type += tick.getType() + " và ";
			}
		}
		type = type.substring(0, type.length() - 4);
		String res = "Phim này có phiên bản " + type;
		if (type.indexOf(" và ") < 0){
			res = "Phim này chỉ có phiên bản " + type;
		}
		return res;
	}
	
	public String getTitleTicketAnswer(List<MovieTicket> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		HashSet<String> movies = new HashSet<String>();
		for (MovieTicket movieTicket : ans){
			movies.add(movieTicket.getMovie().toUpperCase());
		}
		List<String> movieNames = new ArrayList<String>();
		for (String movie : movies){
			String title = StrUtil.toInitCap(movie);
			int idx = title.indexOf("-");
			if (idx < 0){
				idx = title.length();
			}
			movieNames.add(title.substring(0, idx).trim());
		}
		String res = "";
		for (int i = 0; i < movieNames.size(); i++) {
			res += movieNames.get(i) + "</br>";
		}

		return res;
	}
	
	public String getCinemaTicketAnswer(List<MovieTicket> ans){
		if (ans.size() == 0){
			return "Xin lỗi, chúng tôi không tìm thấy rạp nào phù hợp";
		}
		HashSet<String> cinemas = new HashSet<String>();
		for (MovieTicket movieTicket : ans){
			cinemas.add(movieTicket.getCinema());
		}
		String res = "";
		for (String cinema : cinemas){
			res += cinema + "</br>";
		}
		return res.substring(0, res.length() - 2);
	}
	
	/**
	 * Chi tra loi gio chieu neu returned ticket cho 1 phim
	 * Tra loi ca ten phim + gio chieu, neu tickets cho nhieu hon 1 phim
	 * sap xep thoi gian theo chieu tang dan
	 */
	public String getDateTicketAnswer(List<MovieTicket> ans, MovieTicket matchMovieTicket, boolean haveTimeInfo){
		System.out.println("Date answer");
		if (ans.size() == 0){
			if (matchMovieTicket.getCinema() != null){
				if (matchMovieTicket.getMovie() != null){
					return "Phim này đang không được chiếu ở đó!";
				}
				return "Xin lỗi, chúng tôi không tìm thấy thông tin về lịch chiếu cho rạp này";
			}
			return "Xin lỗi, chúng tôi không tìm thấy dữ liệu cho câu trả lời";
		}
		// The key is the movie title or the cinema name, the value is the slots
		HashMap<String, List<Date>> movMap = new HashMap<String, List<Date>>();
		for (MovieTicket movieTicket : ans){
			String titile = movieTicket.getMovie();
			if (matchMovieTicket.getMovie() != null){
				titile = movieTicket.getCinema();
			}
			List<Date> val = movMap.get(titile);
			if (val == null){
				val = new ArrayList<Date>();
			}
			val.add(movieTicket.getDate());
			movMap.put(titile, val);
		}
		
		Object[] obs = movMap.keySet().toArray();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		if (!haveTimeInfo){
			sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm");
		}
		
		String res = "";
		if (obs.length == 1){
			res = "Giờ chiếu:</br>";
			List<Date> slots = movMap.get(obs[0]);
			Collections.sort(slots);
			for (Date date : slots){
				res += sdf.format(date) +  "</br>";
			}
			return res.substring(0, res.length() - 2);
		}
		
		for (Object obj : obs){
			res += StrUtil.getMovieName((String) obj) + ":</br>";
			List<Date> slots = movMap.get(obj);
			Collections.sort(slots);
			for (Date date : slots){
				res += sdf.format(date) +  "</br>";
			}
			res += "</br>";
		}
		
		return res.substring(0, res.length() - 2);
	}
}
