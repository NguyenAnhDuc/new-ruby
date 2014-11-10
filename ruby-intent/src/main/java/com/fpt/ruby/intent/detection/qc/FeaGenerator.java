/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpt.ruby.intent.detection.qc;

import java.util.ArrayList;
import java.util.List;

import com.fpt.ruby.intent.detection.FreqConjDict;

import jmdn.struct.sequence.Sequence;

/**
 *
 * @author ngandong
 */
public class FeaGenerator {
//    static VietnameseMaxentTagger tagger = new VietnameseMaxentTagger();

    static String wsent;

    /**
     * Scan features for word segmentation
     *
     * @param sent
     * @return
     */
    public static List scanFeatures(List<String> sent) {
        List seq = new ArrayList();

        List<String> lcSent = new ArrayList();
        for (String sent1 : sent) {
            lcSent.add(sent1.toLowerCase());
        }

        seq.add(scanAllCPs(lcSent));

        return seq;
    }

    /**
     * Scan features for training the word segmentation model
     *
     * @param sent
     * @return
     */
    public static List scanFeaturesForTraining(Sequence sent) {
        List seq = new ArrayList();

        List<String> tokens = new ArrayList();
        List tags = new ArrayList();

        int len = sent.size();
        for (int i = 0; i < len; i++) {
            List texts = sent.getTextAt(i);
            tokens.addAll(texts);
            tags.add(sent.getTagAt(i));
        }

        List cps = scanAllCPs(tokens);
        cps.add((String) tags.get(0));
        seq.add(cps);

        return seq;
    }

    /**
     * Scan all context predicates at a specific position of a sentence
     *
     * @param sent
     * @return
     */
    public static List<String> scanAllCPs(List<String> sent) {
        List<String> cps = new ArrayList();

        /*
         * -2 -1 0 +1 +2
         * for normal word lookup
         */
        scanWordsRelatedCPs(cps, sent);

        // Add conjution features
        FreqConjDict.matches(cps, wsent);

        return cps;
    }

    public static void scanWordsRelatedCPs(List<String> cps, List<String> sent) {
        int j;
        int len = sent.size();
        String first = sent.get(0).toLowerCase();
        String last = sent.get(len - 1).toLowerCase();

        // first and last word
        cps.add("sw:" + first);
        cps.add("ew:" + last);
        // combination of first and last word
        cps.add("fl:" + first + " " + last);

        if (len > 1) {
            String sec = sent.get(1).toLowerCase();
            String esec = sent.get(len - 2).toLowerCase();
            String sbi = first + " " + sec;
            String ebi = esec + " " + last;

            // first and last pair of words
            cps.add("sbi:" + sbi);
            cps.add("ebi:" + ebi);

            // combination of first and last pairs of words
            cps.add("bfl:" + sbi + " " + ebi);
        }

        /*
         * individual tokens
         */
        StringBuilder wholeSent = new StringBuilder();// whole sentence feature
        StringBuilder wcp = new StringBuilder("ws");
        wsent = "";// the whole sentence in one string

        for (int i = 0; i < len; i++) {
            wcp.append(":").append(i);
            wholeSent.append(":").append(sent.get(i));
            wsent += sent.get(i) + " ";

            for (j = -2; j <= 2; j++) {
                if (i + j >= 0 && i + j < len) {
                    String tk = sent.get(i + j);
                    String idx = Integer.toString(j);

                    String cp = "t:";
                    cp += idx + ":" + tk;
                    cps.add(cp.toLowerCase());
                }
            }

            /*
             * combination of two consecutive tokens
             */
            for (j = -2; j <= 1; j++) {
                if (i + j >= 0 && i + j + 1 < len) {
                    String tk1 = sent.get(i + j).toLowerCase();
                    String tk2 = sent.get(i + j + 1).toLowerCase();
                    String tk12 = tk1 + " " + tk2;
                    String idx1 = Integer.toString(j);
                    String idx2 = Integer.toString(j + 1);

                    String cp = "t:";
                    cp += idx1 + ":" + idx2 + ":" + tk1 + ":" + tk2;
                    cps.add(cp.toLowerCase());

                }

            }

            // add in the whole sentence information 
            if (len < 10) {
                cps.add(wcp.toString() + wholeSent.toString().toLowerCase());
            }
            // add in the sentence length
            cps.add("sl:" + len);;
        }
    }

}
