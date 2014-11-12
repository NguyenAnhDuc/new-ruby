package com.fpt.ruby.business.helper;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameMapperHelper {
    private static Set<String> prefix = new HashSet<String>();
    private static Set<Pattern> postfix = new HashSet<Pattern>();

    static {
        String[] prefixes1 = {"phim truyện", "phim truyền hình", "phim", "phim ngắn"};
        String[] prefixes2 = {"ngắn", "tài liệu", "mỹ", "hàn quốc", "trung quốc", "nước ngoài", "anh", "thái lan", "hoa kỳ", "hoạt hình", "việt nam", "thiếu nhi", "hoạt hình", ""};

        prefix.clear();
        for (String p : prefixes1) {
            for (String p2 : prefixes2) {
                String pr = (p + " " + p2).trim();
//                System.out.println(pr);
                prefix.add(pr);
                prefix.add(TypeMapperHelper.normalize(pr));
            }
        }

        String[] postFixPattern = {
                "(.+)(tập|phần|season|số|t|s|(\\sp))(\\s*)(\\d|\\\\|\\/)+",
                "(.+)\\((tập|phần|season|số|t|s|(\\sp))(\\s*)(\\d|\\\\|\\/)+\\)",
                "(.+)(\\d)+(\\s*)(tập|phần|season|số|t|s|(\\sp))",
                "(.+)\\((\\d)+(\\s*)(tập|phần|season|số|t|s|(\\sp))\\)"
        };

        postfix.clear();
        for (String p : postFixPattern) {
            postfix.add(Pattern.compile(p));
        }
    }

    public static String getRealName(String programTitle) {
        String prog = programTitle.toLowerCase().trim();

        // Prefix
        int max = 0;
        String rs = "";

        for (String p : prefix) {
            if (prog.startsWith(p)) {
                if (p.length() > max && p.length() < prog.length()) {
                    max = p.length();
                    rs = prog.substring(p.length());
                }
            }
        }

        if (max == 0) rs = prog;

        prog = rs;
        // Postfix
        String rs2 = prog;
        for (Pattern p : postfix) {
            Matcher m = p.matcher(rs);
            if (m.matches()) {
                String tmp = m.group(1);
                if (tmp.length() < rs2.length()) {
                    rs2 = tmp;
                }
            }
        }

        // remove special character -, _, ,
        String s = "-_,: ";
        StringBuilder b;
        int start, end;
        for (start = 0; start < rs2.length(); start++) {
            if (!s.contains(rs2.charAt(start) + "")) {
                break;
            }
        }

        for (end = rs2.length(); end > start; end--) {
            if (!s.contains(rs2.charAt(end - 1) + "")) {
                break;
            }
        }

        rs2 = rs2.substring(start, end);
        return rs2;
    }

    public static void main(String[] args) {
        String[] prog = {"Phim Việt Nam Hai phía chân trời - Tập 25", "Tanked (S4)", "Cánh chim cô đơn (62 tập)", "America The Wild - American Vampire S4 - 2", "Phim tài liệu Sự hình thành Âu lục - Tập 3", "Phim ngắn - Dọc đường đen trắng, tập 76/150", "FRIENDS S10", "Thành thật với tình yêu (59T)", "Đời sống chợ đêm - P2", "Bad Dog! (S3)", "HAWAII FIVE-0 (SEASON 4)", "Phim Lệnh truyền của thiên sứ", "Phim truyền hình mỹ Ranh giới - Tập 14", "Phim truyện : Tiếu ngạo giang hồ, tập 21/40", "Tình yêu và thù hận (150 tập)", "Duyên nợ miền tây - T12", "Phim truyện Việt Nam : Vòng vây hoa hồng"};
        for (String p: prog) {
            System.out.println(p + " ***** " + getRealName(p));
        }
    }


}
