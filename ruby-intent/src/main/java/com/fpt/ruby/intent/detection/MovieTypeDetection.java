/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpt.ruby.intent.detection;

import com.fpt.ruby.intent.detection.constants.IntentConstants;
import com.fpt.ruby.intent.detection.qc.VnIntentDetection;
import jmdn.nlp.diacritic.DiacriticConverter;

/**
 *
 * @author ngan
 */
public class MovieTypeDetection {

    static VnIntentDetection classifier;
    public static void init(String qcDir, String dictDir) {
        FreqConjDict.loadConjList(dictDir + "/movie_conjunction.txt");
        classifier = new VnIntentDetection(qcDir);
        classifier.init();

        StopWords.loadList(dictDir + "/stopwords.txt");
    }

    private static String getTunedSent(String sent) {
        String cleanSent = StopWords.removeAllStopWords(" " + sent + " ");
        if (cleanSent.indexOf("có ") == 0) {
            cleanSent = cleanSent.substring(3);
        }
        if (cleanSent.lastIndexOf(" có") == cleanSent.length() - 3) {
            cleanSent = cleanSent.substring(0, cleanSent.length() - 3);
        }
        if (cleanSent.length() > 6 && cleanSent.lastIndexOf(" không") == cleanSent.length() - 6) {
            cleanSent = cleanSent.substring(0, cleanSent.length() - 6);
        }
        return classifier.classify(sent.trim()) + "\t" + cleanSent;
    }

    public static String getIntent(String sent) {
        String tunedSent = getTunedSent(sent);
        System.out.println("Tuned sent: " + tunedSent);
        if (tunedSent.contains("nước nào") && !tunedSent.contains("tiếng nước nào") ||
                tunedSent.contains("quốc gia nào")) {
            return IntentConstants.MOV_COUNTRY;
        }

        if (tunedSent.contains("của đạo diễn nào") || tunedSent.contains("cua dao dien nao")) {
            return IntentConstants.MOV_DIRECTOR;
        }

        if (tunedSent.contains("sản xuất năm nào") || tunedSent.contains("sản xuất năm bao nhiêu") ||
                tunedSent.contains("sản xuất năm") || tunedSent.contains("năm sản xuất")) {
            return IntentConstants.MOV_YEAR;
        }

        if (tunedSent.contains("khởi chiếu") || tunedSent.contains("công chiếu")
                || tunedSent.contains("ra mắt") || tunedSent.contains("bắt đầu chiếu")) {
            return IntentConstants.MOV_RELEASE;
        }

        if (tunedSent.contains("giải thưởng nào")) {
            return IntentConstants.MOV_AWARD;
        }

        if (tunedSent.contains("ngôn ngữ nào") || tunedSent.contains("ngôn ngữ gì")
                || tunedSent.contains("ngôn ngữ được dùng trong phim")
                || tunedSent.contains("ngôn ngữ dùng trong phim") ||
                tunedSent.contains("tiếng gì") || tunedSent.contains("tiếng nước nào")) {
            return IntentConstants.MOV_LANG;
        }


        if (tunedSent.startsWith("PRI\t")) {
//            return "(rạp, how much)";
            return IntentConstants.TICKET_PRICE;
        }
        if (tunedSent.startsWith("CAL\t")) {
//            return "(phim, when)";
            return IntentConstants.MOV_DATE;
        }
        if (tunedSent.indexOf("DES\t") == 0 && (tunedSent.contains("nội dung")
                || tunedSent.contains("với cái gì"))) {
            return IntentConstants.MOV_PLOT;
        }


        // TODO: pre-init
        /* START: MOV_PLOT hard code */
        //1. Phim XYZ co noi dung j & noi dung cua XYZ la gi
        String[] PRE_PLOT = new String[] {"nói về", "kể về", "nội dung", "câu chuyện", "content", "cốt chuyện", "cốt truyện", "cốt phim", "cốt film", "about"};
        String[] AFTER_PLOT = new String[] {"j", "cái gì", "cái j", "như thế nào", "ntn", "là j", "là gì", "là cái gì", "là như thế nào", "là ntn"};

        for (String a : AFTER_PLOT) {
            if (tunedSent.endsWith(a) || tunedSent.endsWith(DiacriticConverter.removeDiacritics(a))) {
                for (String f: PRE_PLOT) {
                    if (tunedSent.contains(f) || tunedSent.contains(DiacriticConverter.removeDiacritics(f)))
                        return IntentConstants.MOV_PLOT;
                }
            }
        }
        /* END : MOV_PLOT hard code */

        String[] happenWord = {"mấy giờ", "khi nào", "lúc nào"};
        if (tunedSent.indexOf("DTI\t") == 0) {
            if (tunedSent.contains("phim") || tunedSent.contains("suất chiếu") ||
                    tunedSent.contains("xuất chiếu")) {
                return IntentConstants.MOV_DATE;
            }

            if (tunedSent.contains(" chiếu ")) {
                for (String w : happenWord) {
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
            if (tunedSent.contains("kéo dài") || tunedSent.contains("thời lượng")) {
                return IntentConstants.MOV_RUNTIME;
            }
            if (tunedSent.contains("cách đây") || tunedSent.contains("mất bao lâu") ||
                    tunedSent.contains("mất bao nhiêu phút")){
                return IntentConstants.UNDEF;
            }
            return IntentConstants.CIN_SERVICETIME;
        }

        if (tunedSent.indexOf("HUM\t") == 0){
            if (tunedSent.contains("đạo diễn") || tunedSent.contains("dao dien")){
                return IntentConstants.MOV_DIRECTOR;
            }
            if (tunedSent.contains("diễn viên") || tunedSent.contains("sao nào") || tunedSent.contains("dàn sao") ||
                    tunedSent.contains("dien vien") || tunedSent.contains("sao nao") || tunedSent.contains("dan sao")){
                return IntentConstants.MOV_ACTOR;
            }
            return IntentConstants.MOV_AUDIENCE;
        }

        if (tunedSent.indexOf("NAM\t") == 0 && (tunedSent.contains("thuộc thể loại") || tunedSent.contains("thuoc the loai"))) {
            return IntentConstants.MOV_GENRE;
        }
        if (tunedSent.indexOf("NAM\t") == 0 && (tunedSent.contains("rạp nào") || tunedSent.contains("rạp gì") ||
                tunedSent.contains("rap nao") || tunedSent.contains("rap gi"))) {
            return IntentConstants.CIN_NAME;
        }

        if (tunedSent.indexOf("NAM\t") == 0 && (tunedSent.contains("phim gì") || tunedSent.contains("phim nào") ||
                tunedSent.contains("phim nao"))) {
            return IntentConstants.MOV_TITLE;
        }

        if (tunedSent.indexOf("NAM\t") == 0 && (tunedSent.contains("diễn viên nào") || tunedSent.contains("dien vien nao")
                || tunedSent.contains("dien vien nào"))) {
            return IntentConstants.MOV_ACTOR;
        }
        if (tunedSent.indexOf("NAM\t") == 0 &&
                (tunedSent.contains("nào") || tunedSent.contains("nao") || tunedSent.contains("gì") || tunedSent.contains("gi"))) {
            int idx = tunedSent.indexOf("nào");
            if (idx < 0){
                idx = tunedSent.indexOf("gì");
            }
            //System.out.println("tune : " + tunedSent);
            int temp = tunedSent.indexOf("chiếu") > 0 ? tunedSent.indexOf("chiếu") : tunedSent.indexOf("chieu");
            int temp2 = tunedSent.indexOf("có gì") > 0 ? tunedSent.indexOf("có gì") : tunedSent.indexOf("co gi");
            int idx1 = Math.abs(idx - (tunedSent.indexOf("phim") > 0 ? tunedSent.indexOf("phim") : temp > 0 ? temp :temp2));
            int idx2 = Math.abs(idx - (tunedSent.indexOf("rạp") > 0 ? tunedSent.indexOf("rạp") : tunedSent.indexOf("rap")));
            int idx3 = Math.abs(idx - (tunedSent.indexOf("diễn viên") > 0 ? tunedSent.indexOf("diễn viên") : tunedSent.indexOf("dien vien")));
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
            int idx2 = tunedSent.indexOf("rạp");
            if (idx2 < 0){
                idx2 = tunedSent.indexOf("rap");
            }
            if (idx2 > 0 && idx1 < 0) {
                if (tunedSent.contains( "rạp" ) && tunedSent.contains("có gì"))
                    return IntentConstants.MOV_TITLE;
                return IntentConstants.CIN_NAME;
            }
            int idx3 = tunedSent.indexOf("diễn viên");
            if (idx3 < 0) {
                idx3 = tunedSent.indexOf("sao");
            }
            if (idx3 < 0) {
                idx3 = tunedSent.indexOf("dien vien");
            }
            if (tunedSent.indexOf("diễn viên nào") > 0 || tunedSent.indexOf("dien vien nao") > 0 || idx3 > 0 && idx3 < idx1) {
                return IntentConstants.MOV_ACTOR;
            }
            idx3 = tunedSent.indexOf("đạo diễn");

            if (tunedSent.indexOf("đạo diễn nào") > 0 || tunedSent.indexOf("dao dien nao") > 0 || idx3 > 0 && (idx3 < idx1
                    || tunedSent.contains("ai là") || tunedSent.contains("là ai") ||
                    tunedSent.contains("ai la") || tunedSent.contains("la ai"))) {
                return IntentConstants.MOV_DIRECTOR;
            }
            if (idx2 > 0 && idx2 < idx1) {
                return IntentConstants.CIN_NAME;
            }
            return IntentConstants.MOV_TITLE;
        }

        if (tunedSent.indexOf("DES\t") == 0) {
            if (tunedSent.indexOf("đường đến") == 4 || tunedSent.indexOf("chỉ đường") == 4 ||
                    tunedSent.indexOf("duong den") == 4 || tunedSent.indexOf("chi duong") == 4) {
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
            // rạp vincom bà triệu tối nay chiếu the maze runner mấy gi�?
            if (tunedSent.contains("mấy giờ") || tunedSent.contains("may gio")){
                return IntentConstants.MOV_DATE;
            }

            if (tunedSent.contains("đặt vé") || tunedSent.contains("còn") ||
                    tunedSent.contains("dat ve")  || tunedSent.contains("đặt ve")|| tunedSent.contains("con")
                    || tunedSent.contains("đat vé") || tunedSent.contains("dat vé")) {
                return IntentConstants.TICKET_STATUS;
            }

            if (tunedSent.contains("ghế") || tunedSent.contains("ghe")) {
                return IntentConstants.UNDEF;
            }
            if (tunedSent.contains("phim") && (tunedSent.contains(" 2D") || tunedSent.contains(" 3D"))) {
                return IntentConstants.MOV_TYPE;
            }
            return IntentConstants.UNDEF;
        }

        return IntentConstants.MOV_TITLE;
    }

    public static void main(String[] args) {
        init("/home/ngan/Work/AHongPhuong/RubyWeb/rubyweb/src/main/resources/qc/movie",
//        		"/home/ngan/Work/AHongPhuong/Intent_detection/data/qc/1", 
                "/home/ngan/Work/AHongPhuong/RubyWeb/rubyweb/src/main/resources/dicts");

//        try {
//            BufferedReader reader = new BufferedReader(new FileReader("/home/ngan/Work/AHongPhuong/Intent_detection/data/whole.txt"));
//            BufferedWriter writer = new BufferedWriter(new FileWriter("/home/ngan/Work/AHongPhuong/Intent_detection/data/test.result"));
//            
//            String line;
//            while((line = reader.readLine()) != null){
//                if (line.isEmpty()){
//                    continue;
//                }
//                writer.write(getIntent(line) + "\t" + classifier.classify(line.trim()) + "\t" + line + "\n");
//            }
//            
//            System.out.println(getTunedSent("lucy được bao nhiêu điểm"));
//            writer.close();
//            reader.close();
//        } catch (IOException ex) {
//            Logger.getLogger(MovieIntentDetection.class.getName()).log(Level.SEVERE, null, ex);
//        }

        String sent1 = "ngoài rạp có phim chiến tranh gì không";
        String sent2 = "phim tâm lý kinh dị nào đang chiếu rạp";
        String sent3 = "phim tâm lý tình cảm nào đang chiếu rạp";
        String sent4 = "rạp vincom bà triệu tối nay chiếu the maze runner lúc mấy gi�??";
        String sent5 = "rạp vincom bà triệu tối nay chiếu the maze runner mấy gi�??";

        System.out.println(getTunedSent(sent1));
//        System.out.println(getIntent(sent1));

//        System.out.println(getTunedSent(sent2));
//        System.out.println(getIntent(sent2));
//        
//        System.out.println(getTunedSent(sent3));
//        System.out.println(getIntent(sent3));
//        
//        System.out.println(getTunedSent(sent4));
//        System.out.println(getIntent(sent4));
//        
//        System.out.println(getTunedSent(sent5));
//        System.out.println(getIntent(sent5));
//        
//        System.out.println(getTunedSent( "có phim hài nào hay" ));
//        System.out.println(getIntent( "có phim hài nào hay" ));
    }
}