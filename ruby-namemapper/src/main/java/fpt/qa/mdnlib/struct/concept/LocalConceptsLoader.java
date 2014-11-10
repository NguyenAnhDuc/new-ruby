/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.struct.concept;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import fpt.qa.mdnlib.util.filesystem.DirFileUtil;
import fpt.qa.mdnlib.util.properties.Parameters;
import fpt.qa.mdnlib.util.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class LocalConceptsLoader {
    private static ConceptCollection conceptColl = null;
    private static Set<String> conceptLabels = null;

    /*
     * initialization
     */
    static {
        conceptColl = new ConceptCollection();
        conceptLabels = new HashSet();
        loadLocalConcepts(Parameters.getConceptsFilesDir());
    }
    
    private static void loadLocalConcepts(String dir) {
        List<String> filenames = DirFileUtil.listDir(dir, ".txt");
        
        BufferedReader fin;
        List<String> tempList = new ArrayList();
        String tempLabel = "";
        
        for (int i = 0; i < filenames.size(); i++) {
            String filename = filenames.get(i);

            String topic = filename;
            if (topic.endsWith(".txt")) {
                topic = topic.substring(0, topic.length() - 4);
            }
            
            filename = DirFileUtil.getFullFilename(dir, filename);
            
            try {
                fin = new BufferedReader(
                        new InputStreamReader(
                        new FileInputStream(filename), "UTF8"));

                String line;
                while ((line = fin.readLine()) != null) {
                    if (line.startsWith("#") || line.length() <= 0) {
                        continue;
                    }

                    String text = StrUtil.normalizeStr(line);
                    
                    if ((line.charAt(0) != ' ' && line.charAt(0) != '\t') ||
                            text.endsWith(":")) {
                        
                        if (!tempList.isEmpty()) {
                            if (!tempLabel.isEmpty()) {
                                addNewConcept(tempLabel, topic, tempList);
                                tempList.clear();
                                tempLabel = "";
                            }
                        }
                        
                        if (text.length() > 1) {
                            tempLabel = text.substring(0, text.length() - 1);
                        }
                        
                    } else if (text.length() > 0) {
                        tempList.add(text);
                    }
                }
                
                if (!tempList.isEmpty()) {
                    if (!tempLabel.isEmpty()) {
                        addNewConcept(tempLabel, topic, tempList);
                        tempList.clear();
                        tempLabel = "";
                    }
                }
                
                fin.close();
                
            } catch (IOException ex) {
                Logger.getLogger(LocalConceptsLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void addNewConcept(String label, String topic, List<String> words) {
        String loweredLabel = label.toLowerCase();
        
        if (!conceptLabels.contains(loweredLabel)) {
            conceptLabels.add(loweredLabel);
            
            Concept concept = new Concept(ConceptCounter.getNewConceptId(), label + "|" + topic);
            
            Set<String> tempSet = new HashSet();
            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i);
                if (!tempSet.contains(word)) {
                    tempSet.add(word);
                    
                    concept.addConceptWord(word, 1.0);
                }
            }
            
            conceptColl.addConcept(concept);
        }
    }
    
    public static void print() {
        for (int i = 0; i < conceptColl.size(); i++) {
            Concept concept = conceptColl.getConceptAt(i);
            concept.print();
        }
    }
    
    public static void printForNLPService() {
        for (int i = 0; i < conceptColl.size(); i++) {
            Concept concept = conceptColl.getConceptAt(i);
            concept.printForNLPService();
        }
    }
    
    public static ConceptCollection getConcepts() {
        return conceptColl;
    }
}
