/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpt.ruby.intent.detection.qc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import jmdn.struct.sequence.SeqCollection;
import jmdn.struct.sequence.Sequence;
/*import jmdn.test.VnLcWordSegmenterTest;*/
import jmdn.util.string.StrUtil;

/**
 *
 * @author ngan
 */
public class GenTrainingData {
    
    public static void genTrainingData(String fileIn, String fileOut) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileOut));

            SeqCollection seqColl = new SeqCollection();
            seqColl.read(fileIn);

            for (int i = 0; i < seqColl.size(); i++) {
                Sequence seq = seqColl.getSequenceAt(i);
                List cpsList = FeaGenerator.scanFeaturesForTraining(seq);

                for (int j = 0; j < cpsList.size(); j++) {
                    writer.write(StrUtil.join((List<String>) cpsList.get(j)) + "\n");
                }

            }
            
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /*public static void main(String [] args){
        VnLcWordSegmenterTest.tokenizeFile("/home/ngan/Work/SuperApp/Sent_Categorization/models/wholeData.txt", 
                "/home/ngan/Work/SuperApp/Sent_Categorization/models/train.toks");
        genTrainingData("/home/ngan/Work/SuperApp/Sent_Categorization/models/train.toks",
                "/home/ngan/Work/SuperApp/Sent_Categorization/models/train.tagged");
        Trainer.doTrain("/home/ngan/Work/SuperApp/Sent_Categorization/models", true, false, false);
    }*/
}
