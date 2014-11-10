/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.mldm.method.gibbslda;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fpt.qa.mdnlib.struct.pair.PairIntDouble;
import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class Model {
    public String modelDir; /* the directory where the model files are stored */    
    public String dataFile; /* the data file */
    
    public String wordMapFile; /* the word map file */
    public String trainLogFile; /* the training log file */
    public String tAssignSuffix; /* the suffix for topic assignment file */
    public String thetaSuffix; /* the suffix for theta file */
    public String phiSuffix; /* the suffix for phi file */
    public String othersSuffix; /* the suffix for file containing other model parameters */
    public String tWordsSuffix; /* the suffix for file containing words-per-topics */
    
    public String modelName; /* the model name */
    public String modelStatus; /* the model status, can be oen of the following: */
                             /* MODEL_STATUS_UNKNOWN */
                             /* MODEL_STATUS_EST */
                             /* MODEL_STATUS_ESTC */
                             /* MODEL_STATUS_INF */
                             /* MODEL_STATUS_INFS */
    
    public int K; /* the number of LDA topics */
    public double alpha, beta; /* LDA hyperparameters */
    public int nEstIters; /* the number of Gibbs sampling iterations for model estimation */
    public int nInfIters; /* the number of Gibbs sampling iterations for inference mode */
    public int saveStep; /* model saving period */
    public int tWords; /* print out tWords top words per each topic */
    public int M; /* the number of documents (training data) */
    public int V; /* the number of words (vocabulary size of training data) */
    public int lastIter; /* the iteration at which the model was saved */
    
    public TrainData data; /* training data */
    public Dictionary dict; /* dictionary */
    
    /* variables for sampling and model estimation */
    public double[] p; /* temporary variable for sampling */
    public int[][] z; /* topic assignments for words, size M x doc.size() */
    public int[][] nw; /* nw[i][j]: the number of instances of word/term i assigned to topic j, size V x K */
    public int[][] nd; /* nd[i][j]: the number of words in document i assigned to topic j, size M x K */
    public int[] nwsum; /* nwsum[j]: the total number of words assigned to topic j, size K */
    public int[] ndsum; /* ndsum[i]: the total number of words in document i, size M */
    
    public double[][] theta; /* document-topic distributions, size M x K */
    public double[][] phi; /* topic-word distributions, size K x V */
    
    public Model() {
        setDefaultValues();
    }
    
    private void setDefaultValues() {
        modelDir = "./";        
        dataFile = "trndocs.dat";
        
        wordMapFile = "wordmap.txt";
        trainLogFile = "trainlog.txt";
        tAssignSuffix = ".tassign";
        thetaSuffix = ".theta";
        phiSuffix = ".phi";
        othersSuffix = ".others";
        tWordsSuffix = ".twords";
        
        modelName = "model-final";
        modelStatus = Constants.MODEL_STATUS_UNKNOWN;
        
        K = 100;
        alpha = 50.0 / K;
        beta = 0.1;
        nEstIters = 2000;
        nInfIters = 200;
        saveStep = 200;
        tWords = 500;
        
        M = 0;
        V = 0;
        lastIter = 0;
        
        data = null;
        dict = null;
        
        p = null;
        z = null;
        nw = null;
        nd = null;
        nwsum = null;
        ndsum = null;
        
        theta = null;
        phi = null;
    }
    
    public void readOptions(Options options) {
        modelDir = options.modelDir;
        if (!modelDir.endsWith(File.separator)) {
            modelDir += File.separator;
        }
        
        dataFile = options.dataFile;
        
        modelName = options.modelName;
        
        if (options.est) {
            modelStatus = Constants.MODEL_STATUS_EST;
        }
        
        if (options.estc) {
            modelStatus = Constants.MODEL_STATUS_ESTC;
        }

        if (options.inf) {
            modelStatus = Constants.MODEL_STATUS_INF;
        }
        
        if (options.infs) {
            modelStatus = Constants.MODEL_STATUS_INFS;
        }
        
        alpha = options.alpha;
        beta = options.beta;
        nEstIters = options.nEstIters;
        nInfIters = options.nInfIters;
        K = options.K;
        saveStep = options.saveStep;
        tWords = options.tWords;
    }
    
    public boolean loadOptions() {
        String filename = modelDir + modelName + othersSuffix;
        
        BufferedReader fin;
        
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(filename), "UTF8"));

            String line;
            
            /* read documents */
            while ((line = fin.readLine()) != null) {
                List<String> tokens = StrUtil.tokenizeStr(line, "= \t\r\n");
                
                if (tokens.isEmpty() || tokens.size() != 2) {
                    continue;
                }
                
                String optStr = tokens.get(0);
                String optVal = tokens.get(1);
                
                if (optStr.equals("alpha")) {
                    alpha = Double.parseDouble(optVal);
                }
                
                if (optStr.equals("beta")) {
                    beta = Double.parseDouble(optVal);
                }
                
                if (optStr.equals("ntopics")) {
                    K = Integer.parseInt(optVal);
                }
                
                if (optStr.equals("ndocs")) {
                    M = Integer.parseInt(optVal);
                }
                
                if (optStr.equals("nwords")) {
                    V = Integer.parseInt(optVal);
                }
                
                if (optStr.equals("liter")) {
                    lastIter = Integer.parseInt(optVal);
                }
            }
            
            fin.close();
            
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.toString());
            return false;
        } catch (IOException ex) {
            System.err.println(ex.toString());
            return false;
        } 
        
        return true;
    }
    
    public boolean init(Options options) {
        readOptions(options);
        
        if (modelStatus.equals(Constants.MODEL_STATUS_EST)) {
            return initForEst();
            
        } else if (modelStatus.equals(Constants.MODEL_STATUS_ESTC)) {
            loadOptions();
            return initForEstC();
            
        } else if (modelStatus.equals(Constants.MODEL_STATUS_INF)) {
            loadOptions();
            return initForInf();
            
        } else if (modelStatus.equals(Constants.MODEL_STATUS_INFS)) {
            loadOptions();
            return initForInf();
            
        } else {
            System.out.println("No model initialization!");
            return false;
        }
    }
    
    public boolean initForEst() {
        System.out.println("Model initialization for estimation from scratch...");
        
        data = new TrainData();

        /* read training data */
        if (!data.readData(dataFile)) {
            System.out.println("Reading data failed or no documents read!");
            return false;
        }
        
        /* get dictionary */
        dict = data.getDict();
        
        M = data.size();
        V = dict.size();

        p = new double[K];
        
        nw = new int[V][K];
        for (int w = 0; w < V; w++) {
            for (int k = 0; k < K; k++) {
                nw[w][k] = 0;
            }
        }
        
        nd = new int[M][K];
        for (int m = 0; m < M; m++) {
            for (int k = 0; k < K; k++) {
                nd[m][k] = 0;
            }
        }
        
        nwsum = new int[K];
        for (int k = 0; k < K; k++) {
            nwsum[k] = 0;
        }
        
        ndsum = new int[M];
        for (int m = 0; m < M; m++) {
            ndsum[m] = 0;
        }
        
        z = new int[M][];
        for (int m = 0; m < M; m++) {
            Document doc = data.docs.get(m);
            
            int N = doc.length;
            z[m] = new int[N];
            
            /* initialization for z */
            for (int n = 0; n < N; n++) {
                int topic = (int)Math.floor(Math.random() * K);
                
                z[m][n] = topic;
                
                /* number of instances of word n assigned to topic "topic" */
                nw[doc.words[n]][topic] += 1;
                
                /* number of words in document m assigned to topic "topic" */
                nd[m][topic] += 1;
                
                /* total number of words assigned to topic "topic" */
                nwsum[topic] += 1;
            }
            
            /* total number of words in document m */
            ndsum[m] = N;
        }

        theta = new double[M][K];
        phi = new double[K][V];
        
        System.out.println("Model initialization for estimation from scratch: completed!");

        return true;
    }
    
    public boolean initForEstC() {
        System.out.println("Model initialization to continue estimation...");
        
        data = new TrainData();
        data.dict.readWordMap(modelDir + wordMapFile);
        
        /* load LDA model */
        if (!loadModelForEstC()) {
            System.out.println("Failed to load LDA model!");
            return false;
        }
        
        p = new double[K];
        
        nw = new int[V][K];
        for (int w = 0; w < V; w++) {
            for (int k = 0; k < K; k++) {
                nw[w][k] = 0;
            }
        }
        
        nd = new int[M][K];
        for (int m = 0; m < M; m++) {
            for (int k = 0; k < K; k++) {
                nd[m][k] = 0;
            }
        }
        
        nwsum = new int[K];
        for (int k = 0; k < K; k++) {
            nwsum[k] = 0;
        }
        
        ndsum = new int[M];
        for (int m = 0; m < M; m++) {
            ndsum[m] = 0;
        }
        
        for (int m = 0; m < M; m++) {
            Document doc = data.docs.get(m);
            int N = doc.length;
            
            /* initialization for z */
            for (int n = 0; n < N; n++) {
                int topic = z[m][n];
                
                /* number of instances of word n assigned to topic "topic" */
                nw[doc.words[n]][topic] += 1;
                
                /* number of words in document m assigned to topic "topic" */
                nd[m][topic] += 1;
                
                /* total number of words assigned to topic "topic" */
                nwsum[topic] += 1;
            }
            
            /* total number of words in document m */
            ndsum[m] = N;
        }

        theta = new double[M][K];
        phi = new double[K][V];

        /* ??? compute theta and phi */
        
        System.out.println("Model initialization to continue estimation: completed!");
        
        return true;
    }
    
    public boolean initForInf() {
        System.out.println("Model initialization for inference...");
        
        data = new TrainData();
        data.dict.readWordMap(modelDir + wordMapFile);
        
        int[][] tempZ = loadModelForInf();
        if (tempZ == null) {
            System.out.println("Failed to load LDA model!");
            return false;
        }
        
        p = new double[K];
        
        nw = new int[V][K];
        for (int w = 0; w < V; w++) {
            for (int k = 0; k < K; k++) {
                nw[w][k] = 0;
            }
        }
        
        nd = new int[M][K];
        for (int m = 0; m < M; m++) {
            for (int k = 0; k < K; k++) {
                nd[m][k] = 0;
            }
        }
        
        nwsum = new int[K];
        for (int k = 0; k < K; k++) {
            nwsum[k] = 0;
        }
        
        ndsum = new int[M];
        for (int m = 0; m < M; m++) {
            ndsum[m] = 0;
        }
        
        for (int m = 0; m < M; m++) {
            Document doc = data.docs.get(m);
            int N = doc.length;
            
            /* initialization for z */
            for (int n = 0; n < N; n++) {
                int topic = tempZ[m][n];
                
                /* number of instances of word n assigned to topic "topic" */
                nw[doc.words[n]][topic] += 1;
                
                /* number of words in document m assigned to topic "topic" */
                nd[m][topic] += 1;
                
                /* total number of words assigned to topic "topic" */
                nwsum[topic] += 1;
            }
            
            /* total number of words in document m */
            ndsum[m] = N;
        }

        theta = new double[M][K];
        phi = new double[K][V];

        computeTheta();
        computePhi();
        
        System.out.println("Model initialization for inference: completed!");

        return true;
    }
    
    public boolean loadModelForEstC() {
        System.out.println("Loading LDA model to continue estimation...");
        
        String filename = modelDir + modelName + tAssignSuffix;
        
        z = new int[M][];
        
        BufferedReader fin;        
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(filename), "UTF8"));

            String line;
            
            /* read documents */
            for (int m = 0; m < M; m++) {
                line = fin.readLine();
                
                List<String> tokens = StrUtil.tokenizeStr(line);
                
                if (tokens.isEmpty()) {
                    continue;
                }
                
                int N = tokens.size();
                List<Integer> words = new ArrayList();
                List<Integer> topics = new ArrayList();
                
                for (int n = 0; n < N; n++) {
                    List<String> wt = StrUtil.tokenizeStr(tokens.get(n), ":");
                    
                    if (wt.size() != 2) {
                        System.out.println("Invalid word-topic assignment line!");
                        return false;
                    }
                    
                    words.add(Integer.parseInt(wt.get(0)));
                    topics.add(Integer.parseInt(wt.get(1)));
                }
                
                /* add document to data set */
                Document doc = new Document(words);
                data.addDoc(doc);
                
                /* assign values for z */
                z[m] = new int[N];
                for (int n = 0; n < N; n++) {
                    z[m][n] = topics.get(n);
                }
            }
            
            fin.close();
            
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.toString());
            return false;
        } catch (IOException ex) {
            System.err.println(ex.toString());
            return false;
        } 
        
        System.out.println("Loading LDA model to continue estimation: completed!");
        
        return true;
    }
    
    public int[][] loadModelForInf() {
        System.out.println("Loading LDA model for inference...");
        
        String filename = modelDir + modelName + tAssignSuffix;
        
        int[][] tempZ = new int[M][];
        
        BufferedReader fin;        
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(filename), "UTF8"));

            String line;
            
            /* read documents */
            for (int m = 0; m < M; m++) {
                line = fin.readLine();
                
                List<String> tokens = StrUtil.tokenizeStr(line);
                
                if (tokens.isEmpty()) {
                    continue;
                }
                
                int N = tokens.size();
                List<Integer> topics = new ArrayList();
                
                for (int n = 0; n < N; n++) {
                    List<String> wt = StrUtil.tokenizeStr(tokens.get(n), ":");
                    
                    if (wt.size() != 2) {
                        System.out.println("Invalid word-topic assignment line!");
                        return null;
                    }
                    
                    topics.add(Integer.parseInt(wt.get(1)));
                }
                
                /* assign values for z */
                tempZ[m] = new int[N];
                for (int n = 0; n < N; n++) {
                    tempZ[m][n] = topics.get(n);
                }
            }
            
            fin.close();
            
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.toString());
            return null;
        } catch (IOException ex) {
            System.err.println(ex.toString());
            return null;
        } 
        
        System.out.println("Loading LDA model for inference: completed!");
        
        return tempZ;
    }
    
    public void computeTheta() {
        for (int m = 0; m < M; m++) {
            for (int k = 0; k < K; k++) {
                theta[m][k] = (nd[m][k] + alpha) / (ndsum[m] + K * alpha);
            }
        }
    }
    
    public void computePhi() {
        for (int k = 0; k < K; k++) {
            for (int w = 0; w < V; w++) {
                phi[k][w] = (nw[w][k] + beta) / (nwsum[k] + V * beta);
            }
        }
    }
    
    public boolean saveModel(String modelName) {
        if (!saveModelTAssign(modelDir + modelName + tAssignSuffix)) {
            return false;
        }

        if (!saveModelOthers(modelDir + modelName + othersSuffix)) {
            return false;
        }

        if (!saveModelTheta(modelDir + modelName + thetaSuffix)) {
            return false;
        }

        if (!saveModelPhi(modelDir + modelName + phiSuffix)) {
            return false;
        }
        
        if (!saveModelTWords(modelDir + modelName + tWordsSuffix)) {
            return false;
        }
        
        return true;
    }
    
    public boolean saveModelTAssign(String filename) {
        BufferedWriter out;
        
        try {
            out = new BufferedWriter(
                         new OutputStreamWriter(
                         new FileOutputStream(filename), "UTF8"));
            
            for (int m = 0; m < M; m++) {
                Document doc = data.docs.get(m);
                int N = doc.length;
                for (int n = 0; n < N; n++) {
                    out.write(doc.words[n] + ":" + z[m][n] + " ");
                }
                out.write("\n");
            }

            out.close();
            
        } catch (IOException ex) {
            System.err.println(ex.toString());
            return false;
        }
        
        return true;
    }
        
    public boolean saveModelOthers(String filename) {
        BufferedWriter out;
        
        try {
            out = new BufferedWriter(
                         new OutputStreamWriter(
                         new FileOutputStream(filename), "UTF8"));
            
            out.write("alpha=" + alpha + "\n");
            out.write("beta=" + beta + "\n");
            out.write("ntopics=" + K + "\n");
            out.write("ndocs=" + M + "\n");
            out.write("nwords=" + V + "\n");
            out.write("liter=" + lastIter + "\n");

            out.close();
            
        } catch (IOException ex) {
            System.err.println(ex.toString());
            return false;
        }
        
        return true;
    }
       
    public boolean saveModelTheta(String filename) {
        BufferedWriter out;
        
        try {
            out = new BufferedWriter(
                         new OutputStreamWriter(
                         new FileOutputStream(filename), "UTF8"));

            for (int m = 0; m < M; m++) {
                for (int k = 0; k < K; k++) {
                    out.write(theta[m][k] + " ");
                }
                out.write("\n");
            }

            out.close();
            
        } catch (IOException ex) {
            System.err.println(ex.toString());
            return false;
        }
        
        return true;
    }
    
    public boolean saveModelPhi(String filename) {
        BufferedWriter out;
        
        try {
            out = new BufferedWriter(
                         new OutputStreamWriter(
                         new FileOutputStream(filename), "UTF8"));

            for (int k = 0; k < K; k++) {
                for (int w = 0; w < V; w++) {
                    out.write(phi[k][w] + " ");
                }
                out.write("\n");
            }

            out.close();
            
        } catch (IOException ex) {
            System.err.println(ex.toString());
            return false;
        }
        
        return true;
    }
    
    public boolean saveModelTWords(String filename) {
        BufferedWriter out;
        
        try {
            out = new BufferedWriter(
                         new OutputStreamWriter(
                         new FileOutputStream(filename), "UTF8"));
   
            int tempTWords = tWords;
            if (tempTWords > V) {
                tempTWords = V;
            }
            
            for (int k = 0; k < K; k++) {
                List<PairIntDouble> wordsProbs = new ArrayList();
                
                for (int w = 0; w < V; w++) {
                    wordsProbs.add(new PairIntDouble(w, phi[k][w]));
                }
                
                Collections.sort(wordsProbs);
                
                out.write("Topic: " + k + "th:\n");
                for (int i = 0; i < tempTWords; i++) {
                    PairIntDouble wordProb = wordsProbs.get(V - i - 1);
                    String wordStr = dict.getWord(wordProb.first);
                    if (wordStr != null) {
                        out.write("\t" + wordStr + "   " + wordProb.second + "\n");
                    }
                }
            }

            out.close();
            
        } catch (IOException ex) {
            System.err.println(ex.toString());
            return false;
        }
        
        return true;
    }
}
