package com.fpt.ruby.intent.detection;

import com.fpt.ruby.commons.constants.IntentConstants;
import com.fpt.ruby.intent.detection.qc.VnIntentDetection;

public class NonDiacriticMovieIntentDetection {

    static VnIntentDetection classifier;

    public static void init(String qcDir, String dictDir) {
        FreqConjDict.loadConjList(dictDir + "/movie_conjunction.txt");
        classifier = new VnIntentDetection(qcDir);
        classifier.init();

        StopWords.loadList(dictDir + "/stopwords.txt");
    }

    public static String getTunedSent(String sent) {
        String cleanSent = StopWords.removeAllStopWords(" " + sent + " ");
        if (cleanSent.indexOf("co ") == 0) {
            cleanSent = cleanSent.substring(3);
        }
        if (cleanSent.lastIndexOf(" co") == cleanSent.length() - 3) {
            cleanSent = cleanSent.substring(0, cleanSent.length() - 3);
        }
        if (cleanSent.length() > 6 && cleanSent.lastIndexOf(" khong") == cleanSent.length() - 6) {
            cleanSent = cleanSent.substring(0, cleanSent.length() - 6);
        }
        return classifier.classify(sent.trim()) + "\t" + cleanSent;
    }

    public static String getIntent(String sent) {
        String tunedSent = getTunedSent(sent);
        System.out.println("Tuned sent: " + tunedSent);
        if (tunedSent.contains("nuoc nao") && !tunedSent.contains("tieng nuoc nao") || 
                tunedSent.contains("quoc gia nao")) {
            return IntentConstants.MOV_COUNTRY;
        }

        if (tunedSent.contains("cua dao dien nao")) {
            return IntentConstants.MOV_DIRECTOR;
        }

        if (tunedSent.contains("san xuat nam nao") || tunedSent.contains("san xuat nam bao nhieu") ||
        		tunedSent.contains("san xuat nam")) {
            return IntentConstants.MOV_YEAR;
        }

        if (tunedSent.contains("khoi chieu") || tunedSent.contains("cong chieu")
                || tunedSent.contains("ra mat") || tunedSent.contains("bat dau chieu")) {
            return IntentConstants.MOV_RELEASE;
        }

        if (tunedSent.contains("giai thuong nao")) {
            return IntentConstants.MOV_AWARD;
        }

        if (tunedSent.contains("ngon ngu nao") || tunedSent.contains("ngon ngu gi")
                || tunedSent.contains("ngon ngu duoc dung trong phim")
                || tunedSent.contains("ngon ngu dung trong phim") ||
                tunedSent.contains("tieng gi") || tunedSent.contains("tieng nuoc nao")) {
            return IntentConstants.MOV_LANG;
        }

        if (tunedSent.startsWith("PRI\t")) {
//            return "(rap, how much)";
            return IntentConstants.TICKET_PRICE;
        }
        if (tunedSent.startsWith("CAL\t")) {
//            return "(phim, when)";
            return IntentConstants.MOV_DATE;
        }
        if (tunedSent.indexOf("DES\t") == 0 && (tunedSent.contains("noi dung")
                || tunedSent.contains("ve cai gi"))) {
            return IntentConstants.MOV_PLOT;
        }

        String[] happenWord = {"may gio", "khi nao", "luc nao"};
        if (tunedSent.indexOf("DTI\t") == 0) {
            if (tunedSent.contains("phim") || tunedSent.contains("suat chieu") || 
                    tunedSent.contains("xuat chieu")) {
                return IntentConstants.MOV_DATE;
            }

            if (tunedSent.contains(" chieu ")) {
                for (String w: happenWord) {
                    if (tunedSent.contains(w)) {
                        return IntentConstants.MOV_DATE;
                    }
                }
            }

            return IntentConstants.CIN_DATE;
        }

        if (tunedSent.indexOf("DIS\t") == 0) {
            return IntentConstants.CIN_DIS;
        }

        if (tunedSent.indexOf("ADD\t") == 0) {
            return IntentConstants.CIN_ADD;
        }

        if (tunedSent.indexOf("DUR\t") == 0) {
            if (tunedSent.contains("keo dai") || tunedSent.contains("thoi luong")) {
                return IntentConstants.MOV_RUNTIME;
            }
            if (tunedSent.contains("cach day") || tunedSent.contains("mat bao lau") ||
                    tunedSent.contains("mat bao nhieu phut")){
                return IntentConstants.UNDEF;
            }
            return IntentConstants.CIN_SERVICETIME;
        }
        
        if (tunedSent.indexOf("HUM\t") == 0){
            if (tunedSent.contains("dao dien")){
                return IntentConstants.MOV_DIRECTOR;
            }
            if (tunedSent.contains("dien vien") || tunedSent.contains("sao nao") || tunedSent.contains("dan sao")){
                return IntentConstants.MOV_ACTOR;
            }
            return IntentConstants.MOV_AUDIENCE;
        }

        if (tunedSent.indexOf("NAM\t") == 0 && tunedSent.contains("thuoc the loai")) {
            return IntentConstants.MOV_GENRE;
        }
        if (tunedSent.indexOf("NAM\t") == 0 && (tunedSent.contains("phim gi") || tunedSent.contains("phim nao"))) {
            return IntentConstants.MOV_TITLE;
        }
        if (tunedSent.indexOf("NAM\t") == 0 && (tunedSent.contains("rap nao") || tunedSent.contains("rap gi"))) {
            return IntentConstants.CIN_NAME;
        }
        if (tunedSent.indexOf("NAM\t") == 0 && tunedSent.contains("dien vien nao")) {
            return IntentConstants.MOV_ACTOR;
        }
        if (tunedSent.indexOf("NAM\t") == 0 && tunedSent.contains("nao")) {
            int idx = tunedSent.indexOf("nao");
            int idx1 = Math.abs(idx - tunedSent.indexOf("phim"));
            int idx2 = Math.abs(idx - tunedSent.indexOf("rap"));
            int idx3 = Math.abs(idx - tunedSent.indexOf("dien vien"));
            int idx4 = Math.abs(idx - tunedSent.indexOf("sao"));

            if (idx3 > idx4) {
                idx3 = idx4;
            }
            if (idx1 < idx2 && idx1 < idx3) {
                return IntentConstants.MOV_TITLE;
            }
            if (idx2 < idx1 && idx2 < idx3) {
                return IntentConstants.CIN_NAME;
            }
            return IntentConstants.MOV_ACTOR;
        }

        if (tunedSent.indexOf("NAM\t") == 0) {
            int idx1 = tunedSent.indexOf("phim");
            int idx2 = tunedSent.indexOf("rap");
            if (idx2 > 0 && idx1 < 0) {
                return IntentConstants.CIN_NAME;
            }
            int idx3 = tunedSent.indexOf("dien vien");
            if (idx3 < 0) {
                idx3 = tunedSent.indexOf("sao");
            }
            if (tunedSent.indexOf("dien vien nao") > 0 || idx3 > 0 && idx3 < idx1) {
                return IntentConstants.MOV_ACTOR;
            }
            idx3 = tunedSent.indexOf("dao dien");

            if (tunedSent.indexOf("dao dien nao") > 0 || idx3 > 0 && (idx3 < idx1
                    || tunedSent.contains("ai la") || tunedSent.contains("la ai"))) {
                return IntentConstants.MOV_DIRECTOR;
            }
            if (idx2 > 0 && idx2 < idx1) {
                return IntentConstants.CIN_NAME;
            }
            return IntentConstants.MOV_TITLE;
        }

        if (tunedSent.indexOf("DES\t") == 0) {
            if (tunedSent.indexOf("duong den") == 4 || tunedSent.indexOf("chi duong") == 4) {
                return IntentConstants.CIN_MAP;
            }
            return IntentConstants.MOV_IMDBRATING;
        }

        if (tunedSent.indexOf("SEL\t") == 0) {
            if (tunedSent.contains("phim") && (tunedSent.contains("2D")||tunedSent.contains("3D") )) {
                return IntentConstants.MOV_TYPE;
            }
            
            if (tunedSent.contains("phim")) {
                return IntentConstants.MOV_GENRE;
            }
            return IntentConstants.UNDEF;
        }
        if (tunedSent.indexOf("NUM\t") == 0) {
            if (tunedSent.contains("imdb")) {
                return IntentConstants.MOV_IMDBRATING;
            }
            return IntentConstants.UNDEF;
        }

        if (tunedSent.indexOf("POL\t") == 0) {
        	// handle wrong question classification result such as in the sentence:
        	// rap vincom bà triệu tối nay chieu the maze runner may gio
        	if (tunedSent.contains("may gio")){
        		return IntentConstants.MOV_DATE;
        	}
        	
            if (tunedSent.contains("dat ve") || tunedSent.contains("con")) {
                return IntentConstants.TICKET_STATUS;
            }

            if (tunedSent.contains("ghe")) {
                return IntentConstants.UNDEF;
            }
            if (tunedSent.contains("phim") && (tunedSent.contains(" 2D") || tunedSent.contains(" 3D"))) {
                return IntentConstants.MOV_TYPE;
            }
            return IntentConstants.UNDEF;
        }

        return IntentConstants.MOV_TITLE;
    }

}
