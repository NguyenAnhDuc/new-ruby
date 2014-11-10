/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.intent.detection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import fpt.qa.intent.detection.qc.VnIntentDetection;

/**
 *
 * @author ngan
 */
public class StopWordsTest {

    public static void cleanFile(String fileIn, String fileOut) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileIn));
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileOut));
            
            VnIntentDetection classifier = new VnIntentDetection("/home/ngan/Work/AHongPhuong/Intent_detection/data/qc/2");
            classifier.init();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()){
                    continue;
                }
                line = " " + line + " ";
//                System.out.println("line**" + line + "**");
                String cleanLine = StopWords.removeAllStopWords(line);
                if (cleanLine.indexOf("có ") == 0){
                    cleanLine = cleanLine.substring(3);
                }
                if (cleanLine.lastIndexOf(" có") == cleanLine.length() - 3){
                    cleanLine = cleanLine.substring(0, cleanLine.length() - 3);
                }
                if (cleanLine.length() > 6 && cleanLine.lastIndexOf(" không") == cleanLine.length() - 6){
                    cleanLine = cleanLine.substring(0, cleanLine.length() - 6);
                }
                        
                writer.write(classifier.classify(line.trim()) + "\t" + cleanLine + "\n");
            }

            writer.close();
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(StopWordsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        StopWords.loadList("/home/ngan/Work/AHongPhuong/Intent_detection/data/dicts/stopwords.txt");
        cleanFile("/home/ngan/Work/AHongPhuong/Intent_detection/data/whole.txt", 
                "/home/ngan/Work/AHongPhuong/Intent_detection/data/August19th/intent_data_step1.txt");
    }

}
