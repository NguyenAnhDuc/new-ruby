
package fpt.qa.mdnlib.mldm.method.gibbslda;

import org.kohsuke.args4j.Option;

public class Options {
    @Option(name="-est", usage="Specify whether we will perform com.fpt.ruby.business.model estimation from scratch")
    public boolean est = false;
    
    @Option(name="-estc", usage="Specify whether we will continue to perform com.fpt.ruby.business.model estimation from a previously estimated com.fpt.ruby.business.model")
    public boolean estc = false;

    @Option(name="-inf", usage="Specify whether we will perform topic inference for new data")
    public boolean inf = false;

    @Option(name="-infs", usage="Specify whether we will perform topic inference for new data in server/service mode")
    public boolean infs = false;
    
    @Option(name="-dir", usage="Specify the directory where the com.fpt.ruby.business.model was saved")
    public String modelDir = "./";
	
    @Option(name="-dfile", usage="Specify the data file")
    public String dataFile = "trndocs.dat";
	
    @Option(name="-com.fpt.ruby.business.model", usage="Specify the com.fpt.ruby.business.model name")
    public String modelName = "com.fpt.ruby.business.model-final";
	
    @Option(name="-alpha", usage="Specify alpha value")
    public double alpha = 0.5;
	
    @Option(name="-beta", usage="Specify beta value")
    public double beta = 0.1;
	
    @Option(name="-ntopics", usage="Specify the number of LDA topics")
    public int K = 100;
	
    @Option(name="-niters", usage="Specify the number of Gibbs sampling iterations for com.fpt.ruby.business.model estimation")
    public int nEstIters = 1000;

    @Option(name="-niters-inf", usage="Specify the number of Gibbs sampling iterations for topic inference (for new data)")
    public int nInfIters = 200;
    
    @Option(name="-savestep", usage="Specify the number of steps to save the com.fpt.ruby.business.model since the last save")
    public int saveStep = 200;
	
    @Option(name="-twords", usage="Specify the number of most likely (top) words to be printed for each topic")
    public int tWords = 500;
	
    public boolean validOptions() {
        int count = 0;
        
        if (est) {
            count++;
        }
        if (estc) {
            count++;
        }
        if (inf) {
            count++;
        }
        if (infs) {
            count++;
        }
        
        if (count == 0) {
            System.out.println("You must specify com.fpt.ruby.business.model status!");
            return false;
        }
        
        if (count > 1) {
            System.out.println("You must specify only one status for com.fpt.ruby.business.model!");
            return false;
        }
        
        return true;
    }
}
