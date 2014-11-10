/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.topic.mapping;

import org.kohsuke.args4j.Option;

/**
 *
 * @author hieupx
 */
public class CmdOption {
    @Option(name="-topicfile", usage="input topic file")
    public String topicFile = "";

    @Option(name="-topicmapfile", usage="input topic map file")
    public String topicMapFile = "";
}
