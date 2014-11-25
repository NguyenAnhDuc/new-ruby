/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpt.ruby.intent.detection.qc;

import jmdn.nlp.vnsentsegmenter.SentSegPrepro;
import jmdn.util.string.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hieupx
 */
public class VnIntentDetection {
    private Classification classifier = null;

    public VnIntentDetection(String vnIntentDetectionModelDir) {
        this.classifier = new Classification(vnIntentDetectionModelDir);
    }

    public void init() {
        if (!this.classifier.isInitialized()) {
            this.classifier.init();
        }
    }

    public String classify(String text) {
        if (text.indexOf("địa chỉ") >= 0) {
            return "ADD";
        }


        List tokens = StrUtil.tokenizeStr(SentSegPrepro.preprocessText(text));

        List data = new ArrayList();

        // generate features
        data = FeaGenerator.scanFeatures(tokens);
        String sent = "";
        List obj = (List) data.get(0);
        for (Object ob : obj.toArray()) {
            sent += (String) ob + " ";
        }
        sent = sent.trim();
        // classify
        String label = this.classifier.classify(sent);
        return label;
    }

}
