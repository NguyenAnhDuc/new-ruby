/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpt.ruby.intent.detection;

import com.fpt.ruby.intent.detection.qc.VnIntentDetection;

/**
 *
 * @author ngan
 */
public class TVTypeDetection extends TypeDetection {

    private static VnIntentDetection classifier;

    public void init(String qcDir, String dictDir) {
        FreqConjDict.loadConjList(dictDir + "/tv_conjunctions.txt");
        classifier = new VnIntentDetection(qcDir);
        classifier.init();
    }

    public static String getIntent(String sent) {
        return "tv_" + classifier.classify(sent).toLowerCase();
    }
}
