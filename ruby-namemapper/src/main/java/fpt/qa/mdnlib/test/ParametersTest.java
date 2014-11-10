/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpt.qa.mdnlib.test;

import fpt.qa.mdnlib.util.properties.Parameters;

/**
 *
 * @author pxhieu
 */
public class ParametersTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        System.out.println(Parameters.getNlpVnSentSegmenterDataDir());
        System.out.println(Parameters.getNlpVnWordSegmenterDataDir());
        System.out.println(Parameters.getNlpVnPOSTaggerDataDir());
        System.out.println(Parameters.getEnDictDir());
        System.out.println(Parameters.getLexiconsDictDir());
        System.out.println(Parameters.getLexiconsEnUnitsFile());
        System.out.println(Parameters.getLexiconsVnUnitsFile());
        System.out.println(Parameters.getStopWordsFile());
        System.out.println(Parameters.getNamesDictDir());
        System.out.println(Parameters.getNamesLocationsFile());
        System.out.println(Parameters.getNamesOrganizationsFile());
        System.out.println(Parameters.getNamesPersonsFile());
        System.out.println(Parameters.getNamesProductsFile());
        System.out.println(Parameters.getNlpEnChunkerModelDir());
        System.out.println(Parameters.getNlpEnPOSTaggerModelDir());
        System.out.println(Parameters.getNlpEnSentSegmenterModelDir());
        System.out.println(Parameters.getNlpVnChunkerModelDir());
        System.out.println(Parameters.getNlpVnNERModelDir());
        System.out.println(Parameters.getNlpVnPOSTaggerModelDir());
        System.out.println(Parameters.getNlpVnSentSegmenterModelDir());
        System.out.println(Parameters.getNlpVnTokenizerModelDir());
        System.out.println(Parameters.getNlpVnWordSegmenterModelDir());
        System.out.println(Parameters.getRegexDir());
        System.out.println(Parameters.getHumanTopicMapDir());
        System.out.println(Parameters.getRegexFile());
        System.out.println(Parameters.getRegexPunctuationMarksFile());
        System.out.println(Parameters.getSpecialcharsDictDir());
        System.out.println(Parameters.getSpecialcharsFile());
        System.out.println(Parameters.getNonAlphaCharsFile());
        System.out.println(Parameters.getVnDictDir());
        System.out.println(Parameters.getVnDictFile());
        System.out.println(Parameters.getVnExtendedDictFile());
        System.out.println(Parameters.getVnFirstWordsDictFile());
        System.out.println(Parameters.getAbbreviationsDir());
        System.out.println(Parameters.getVnStopAbbrsFile());
        System.out.println(Parameters.getEnStopAbbrsFile());
        System.out.println(Parameters.getHonotificTitlesFile());
        System.out.println(Parameters.getPrefixTitlesFile());
        System.out.println(Parameters.getVnIdiomsDictFile());
        System.out.println(Parameters.getVnFamilyNamesDictFile());
        System.out.println(Parameters.getVnLocationsDictFile());
        System.out.println(Parameters.getVnMiddleLastNamesDictFile());
        System.out.println(Parameters.getVnBaomoiWordListFile());
        System.out.println(Parameters.getVnBaomoiNameListForConceptLookupFile());
        System.out.println(Parameters.getHumanTopicsMapFile());
        System.out.println(Parameters.getMappedTopicsFile());
    }
}
