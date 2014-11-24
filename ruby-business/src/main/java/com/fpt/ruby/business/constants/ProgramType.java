package com.fpt.ruby.business.constants;

public enum ProgramType {
	FILM, CARTOON, SPORT, FOOTBALL, TENNIS, ENTERTAINMENT, MUSIC, GAMESHOW, NEWS, FASHION, STYLE, REPORT, OTHER, DISCOVERY, SCIENCE, GOLF, RACING;

	public static ProgramType getType(String typeName) {
		typeName = typeName.toLowerCase().trim();

		switch (typeName) {
		case "golf":
			return GOLF;
		case "film":
		case "phim":
		case "phim truyen":
		case "phim truyện":
			return FILM;
		case "cartoon":
			return CARTOON;
		case "sport":
		case "the thao":
		case "thể thao":
			return SPORT;
		case "football":
		case "bong da":
		case "bóng đá":
		case "soccer":
			return FOOTBALL;
		case "tenis": 
		case "tennis":
		case "quan vot":
		case "quần vợt":
		case "ten nis":
		case "ten nit":
			return TENNIS;
		case "music":
		case "am nhac":
		case "âm nhạc":
		case "ca nhac":
		case "ca nhạc":
			return MUSIC;
		case "gameshow":
		case "game show":
			return GAMESHOW;
		case "news":
		case "tin tuc":
		case "tin tức":
		case "thoi su":
		case "thời sự":
			return NEWS;
		case "fashion":
		case "thời trang":
		case "thoi trang":
			return FASHION;
		case "entertainment":
		case "giai tri":
		case "ent":
			return ENTERTAINMENT;
		case "style":
		case "phong cach":
		case "phong cách":
			return STYLE;
		case "reportage":
		case "report":
		case "phong su":
		case "phóng sự":
			return REPORT;
		case "tong hop":
		case "tổng hợp":
		case "khong ro":
		case "không rõ":
			return OTHER;
		case "kham pha":
		case "khám phá":
		case "discovery":
			return DISCOVERY;
		case "science":
		case "khoa học":
		case "khoa hoc":
			return SCIENCE;
		default:
			return OTHER;
		}
	}
}