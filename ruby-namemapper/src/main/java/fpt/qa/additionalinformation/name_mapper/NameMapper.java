package fpt.qa.additionalinformation.name_mapper;

import com.fpt.ruby.business.service.NameMapperService;
import fpt.qa.mdnlib.struct.pair.Pair;
import vn.hus.nlp.utils.UTF8FileUtility;

import java.util.*;

public class NameMapper {

    private HashMap<NamedEntity, Set<String>> nameMap;

    public NameMapper(NameMapperService nameMapperService, String domain) {
        if (nameMap != null && nameMap.size() == 0) return;
        load(nameMapperService, domain);
    }

    public NameMapper(String namedFile) {
        nameMap = new HashMap<>();
        String[] lines = UTF8FileUtility.getLines(namedFile);
        for (String line : lines) {
            if (line == "" | line.indexOf('#') == 0) {
                continue;
            }
            int i = line.indexOf('\t');
            String type = line.substring(0, i).trim();
            int j = line.indexOf(';');
            String finalName;
            if (j == -1) {
                j = line.length() - 1;
                finalName = line.substring(i + 1, j + 1).trim();
            } else {
                finalName = line.substring(i + 1, j).trim();

            }
            NamedEntity namedEntity = new NamedEntity(type, finalName);

            Set<String> varNames = new HashSet<String>();
            varNames.add(finalName); // add the word itself
            StringTokenizer tokenizer = new StringTokenizer(line.substring(j + 1), ",");
            while (tokenizer.hasMoreTokens()) {
                varNames.add(tokenizer.nextToken().trim());
            }
            nameMap.put(namedEntity, varNames);
        }

    }

    public void load(NameMapperService nameMapperService, String domain) {
        nameMap = new HashMap<>();
        List<com.fpt.ruby.business.model.NameMapper> nameMappers = nameMapperService.findByDomain(domain);
        for (com.fpt.ruby.business.model.NameMapper nameMapper : nameMappers) {
            NamedEntity namedEntity = new NamedEntity(nameMapper.getType(), nameMapper.getName());
            nameMap.put(namedEntity, nameMapper.getVariants());
        }
        System.out.println(domain + " -> " + nameMap.size());
    }

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String args[]) {
        NameMapper map = new NameMapper("resources/data/movieNames.txt");
        map.print();
    }

    /**
     * Gets final names of a given type and name.
     *
     * @param word
     * @return final name of the type and name.
     */

    public String getFinalName(String type, String name) {
        name = name.trim();
        type = type.trim();
        for (NamedEntity entity : nameMap.keySet()) {
            if (entity.getType().equalsIgnoreCase(type) && nameMap.get(entity).contains(name.trim())) {
                return entity.getFinalName();
            }
        }
        return "";
    }

    /**
     * Gets a list of variable names of a given type and name.
     *
     * @param word
     * @return a list of variable names of the type and name.
     */
    public Set<String> getVariationNames(String type, String name) {
        name = name.trim();
        type = type.trim();
        for (NamedEntity entity : nameMap.keySet()) {
            if (entity.getType().equalsIgnoreCase(type) && nameMap.get(entity).contains(name.trim())) {
                return nameMap.get(entity);
            }
        }
        return null;
    }

    /**
     * Display datas in map
     *
     * @param
     * @return
     */
    void print() {
        System.out.println("size = " + nameMap.size());
        for (NamedEntity word : nameMap.keySet()) {
            System.out.print(word + "\t");
            Set<String> syns = nameMap.get(word);
            System.out.println(syns.toString());
        }
    }

    public List<Pair<String, String>> getAllNames() {
        List<Pair<String, String>> allNames = new ArrayList<Pair<String, String>>();

        for (NamedEntity word : nameMap.keySet()) {
            String type = word.getType();
            for (String name : nameMap.get(word)) {
                allNames.add(new Pair<String, String>(name, type));
            }
        }

        return allNames;
    }
}
