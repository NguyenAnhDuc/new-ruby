/*
Copyright (C) 2006 by

Xuan-Hieu Phan

Email:	hieuxuan@ecei.tohoku.ac.jp
pxhieu@gmail.com
URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan

Graduate School of Information Sciences,
Tohoku University
 */
package com.fpt.ruby.intent.detection.qc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Trainer {

    public static void main(String[] args) throws IOException {

        String modelDir = "/home/ngan/Work/SuperApp/Sent_Categorization/models";
        boolean isAll = false;
        boolean isTrn = true;
        boolean isTst = false;

        // create option object
        Option option = new Option(modelDir);
        option.readOptions();

        Data data;
        Dictionary dict;
        FeatGen feaGen;
        Train train;
        Inference inference;
        Evaluation evaluation;
        Model model;

        PrintWriter foutModel;
        BufferedReader finModel;

        if (isAll) {
            // both training and testing

            PrintWriter flog = option.openTrainLogFile();
            if (flog == null) {
                System.out.println("Couldn't create training log file");
                return;
            }

            foutModel = option.createModelFile();
            if (foutModel == null) {
                System.out.println("Couldn't create model file");
                return;
            }

            data = new Data(option);
            data.readTrnData(option.modelDir + File.separator + option.trainDataFile);
            data.readTstData(option.modelDir + File.separator + option.testDataFile);

            dict = new Dictionary(option, data);
            dict.generateDict();

            feaGen = new FeatGen(option, data, dict);
            feaGen.generateFeatures();

            data.writeCpMaps(dict, foutModel);
            data.writeLbMaps(foutModel);

            train = new Train();
            inference = new Inference();
            evaluation = new Evaluation();

            model = new Model(option, data, dict, feaGen, train, inference, evaluation);
            model.doTrain(flog);

            model.doInference(model.data.tstData);
            model.evaluation.evaluate(flog);

            dict.writeDict(foutModel);
            feaGen.writeFeatures(foutModel);

            foutModel.close();
        }

        if (isTrn) {
            // training only

            PrintWriter flog = option.openTrainLogFile();
            if (flog == null) {
                System.out.println("Couldn't create training log file");
                return;
            }

            foutModel = option.createModelFile();
            if (foutModel == null) {
                System.out.println("Couldn't create model file");
                return;
            }

            data = new Data(option);
            data.readTrnData(option.modelDir + File.separator + option.trainDataFile);

            dict = new Dictionary(option, data);
            dict.generateDict();

            feaGen = new FeatGen(option, data, dict);
            feaGen.generateFeatures();

            data.writeCpMaps(dict, foutModel);
            data.writeLbMaps(foutModel);

            train = new Train();

            model = new Model(option, data, dict, feaGen, train, null, null);
            model.doTrain(flog);

            dict.writeDict(foutModel);
            feaGen.writeFeatures(foutModel);

            foutModel.close();
        }

        if (isTst) {
            // testing only

            finModel = option.openModelFile();
            if (finModel == null) {
                System.out.println("Couldn't open model file");
                return;
            }

            data = new Data(option);
            data.readCpMaps(finModel);
            data.readLbMaps(finModel);
            data.readTstData(option.modelDir + File.separator + option.testDataFile);

            dict = new Dictionary(option, data);
            dict.readDict(finModel);

            feaGen = new FeatGen(option, data, dict);
            feaGen.readFeatures(finModel);

            inference = new Inference();
            evaluation = new Evaluation();

            model = new Model(option, data, dict, feaGen, null, inference, evaluation);

            model.doInference(model.data.tstData);
            model.evaluation.evaluate(null);

            finModel.close();
        }

    } // end of the main method

    public static void doTrain(String modelDir, boolean isTrn, boolean isTst, boolean isAll) {

        // create option object
        Option option = new Option(modelDir);
        option.readOptions();

        Data data;
        Dictionary dict;
        FeatGen feaGen;
        Train train;
        Inference inference;
        Evaluation evaluation;
        Model model;

        PrintWriter foutModel;
        BufferedReader finModel;
        try {

            if (isTrn) {
                // training only

                PrintWriter flog = option.openTrainLogFile();
                if (flog == null) {
                    System.out.println("Couldn't create training log file");
                    return;
                }

                foutModel = option.createModelFile();
                if (foutModel == null) {
                    System.out.println("Couldn't create model file");
                    return;
                }

                data = new Data(option);
                data.readTrnData(option.modelDir + File.separator + option.trainDataFile);

                dict = new Dictionary(option, data);
                dict.generateDict();

                feaGen = new FeatGen(option, data, dict);
                feaGen.generateFeatures();

                data.writeCpMaps(dict, foutModel);
                data.writeLbMaps(foutModel);

                train = new Train();

                model = new Model(option, data, dict, feaGen, train, null, null);
                model.doTrain(flog);

                dict.writeDict(foutModel);
                feaGen.writeFeatures(foutModel);

                foutModel.close();
            }

            if (isTst) {
                // testing only

                finModel = option.openModelFile();
                if (finModel == null) {
                    System.out.println("Couldn't open model file");
                    return;
                }

                data = new Data(option);
                data.readCpMaps(finModel);
                data.readLbMaps(finModel);
                data.readTstData(option.modelDir + File.separator + option.testDataFile);

                dict = new Dictionary(option, data);
                dict.readDict(finModel);

                feaGen = new FeatGen(option, data, dict);
                feaGen.readFeatures(finModel);

                inference = new Inference();
                evaluation = new Evaluation();

                model = new Model(option, data, dict, feaGen, null, inference, evaluation);

                model.doInference(model.data.tstData);
                model.evaluation.evaluate(null);

                finModel.close();
            }
            if (isAll) {
                // both training and testing

                PrintWriter flog = option.openTrainLogFile();
                if (flog == null) {
                    System.out.println("Couldn't create training log file");
                    return;
                }

                foutModel = option.createModelFile();
                if (foutModel == null) {
                    System.out.println("Couldn't create model file");
                    return;
                }

                data = new Data(option);
                data.readTrnData(option.modelDir + File.separator + option.trainDataFile);
                data.readTstData(option.modelDir + File.separator + option.testDataFile);

                dict = new Dictionary(option, data);
                dict.generateDict();

                feaGen = new FeatGen(option, data, dict);
                feaGen.generateFeatures();

                data.writeCpMaps(dict, foutModel);
                data.writeLbMaps(foutModel);

                train = new Train();
                inference = new Inference();
                evaluation = new Evaluation();

                model = new Model(option, data, dict, feaGen, train, inference, evaluation);
                model.doTrain(flog);

                model.doInference(model.data.tstData);
                model.evaluation.evaluate(flog);

                dict.writeDict(foutModel);
                feaGen.writeFeatures(foutModel);

                foutModel.close();
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static boolean checkArgs(String[] args) {
        if (args.length < 3) {
            return false;
        }

        if (!(args[0].compareToIgnoreCase("-all") == 0
                || args[0].compareToIgnoreCase("-trn") == 0
                || args[0].compareToIgnoreCase("-tst") == 0)) {
            return false;
        }

        if (args[1].compareToIgnoreCase("-d") != 0) {
            return false;
        }

        return true;
    }

    public static void displayHelp() {
        System.out.println("Usage:");
        System.out.println("\tTrainer -all/-trn/-tst -d <model directory>");
    }
}
