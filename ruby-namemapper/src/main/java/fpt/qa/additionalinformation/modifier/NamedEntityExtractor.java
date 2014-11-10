package fpt.qa.additionalinformation.modifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;

public class NamedEntityExtractor {

    private static final String CONJUNCTION_FILE = "abc.txt";

    public NamedEntityExtractor() {
        File file = new File(CONJUNCTION_FILE);
        loadEntities(file);
    }

    private void loadEntities(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = null;
            while ((line = reader.readLine()) != null) {
                String type = line.split( "//w" )[ 0 ];
                String conjunction = line.substring( type.length(), line.length() );
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<AbstractMap.SimpleEntry<String, String>> getEntities(
            String sentence) {

        return null;
    }

    public static void main(String[] args) {
        NamedEntityExtractor extractor = new NamedEntityExtractor();
        System.out.println(extractor.getEntities("Phim Lucy?"));
    }

}
