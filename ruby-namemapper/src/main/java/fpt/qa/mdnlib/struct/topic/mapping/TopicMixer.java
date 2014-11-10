/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.topic.mapping;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 *
 * @author hieupx
 */
public class TopicMixer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        CmdOption option = new CmdOption();
        CmdLineParser parser = new CmdLineParser(option);

        if (args.length < 2) {
            showHelp(parser);
            return;
        }
        try {
            parser.parseArgument(args);
        } catch (CmdLineException ex) {
            Logger.getLogger(TopicMixer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TopicCollection topicColl = new TopicCollection();
        topicColl.loadTopics(option.topicFile);
        
        TopicMapCollection topicMapColl = new TopicMapCollection();
        topicMapColl.loadTopicMaps(option.topicMapFile);
        
        TopicMapping topicMapping = new TopicMapping(topicColl, topicMapColl);
        topicMapping.doMap();
    }
    
    public static void showHelp(CmdLineParser parser) {
        System.out.println("TopicMixer [options ...] [arguments ...]");
        parser.printUsage(System.out);
    }
}
