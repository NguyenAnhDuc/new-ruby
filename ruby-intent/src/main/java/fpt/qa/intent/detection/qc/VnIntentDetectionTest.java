/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.intent.detection.qc;

/**
 *
 * @author ngan
 */
public class VnIntentDetectionTest {
    static final int FOLDS = 5;
    /*public static void main(String[] args){
        System.out.println("Question classification for intent detection test now begin...");
        SplitDataByTags.splitFile("/home/ngan/Work/AHongPhuong/Intent_detection/data/qc/train.txt",
                "/home/ngan/Work/AHongPhuong/Intent_detection/data/qc", FOLDS);
//        
        System.out.println("Finish five folds generation");
        String dir = "/home/ngan/Work/AHongPhuong/Intent_detection/data/qc/";
        
        // init conj list
        FreqConjDict.loadConjList("/home/ngan/Work/AHongPhuong/Intent_detection/data/dicts/conj.txt");
        
        String trnIn = "/train.txt";
        String trnOut = "/train.toks";
        String trnData = "/train.tagged";
        
        String tstIn = "/test.txt";
        String tstOut = "/test.toks";
        String tstData = "/test.tagged";
        
        for (int i = 0; i < FOLDS; i++){
            System.out.println("Processing the " + i + "th fold");
            // generate train.tagged and test.tagged file
            VnLcWordSegmenterTest.tokenizeFile(dir + i + trnIn, dir + i + trnOut);
            VnLcWordSegmenterTest.tokenizeFile(dir + i + tstIn, dir + i + tstOut);
            GenTrainingData.genTrainingData(dir + i + trnOut, dir + i + trnData);
            GenTrainingData.genTrainingData(dir + i + tstOut, dir + i + tstData);
            Trainer.doTrain(dir + i, false, false, true);
//            Trainer.doTrain(dir + i, true, false, false);
//            Trainer.doTrain(dir + i, false, true, false);
        }
    }*/
}
