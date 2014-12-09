package fpt.qa.additionalinformation.name_mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fpt.ruby.commons.service.NameMapperService;

import fpt.qa.mdnlib.struct.pair.Pair;

public class NameMapperEngine {
	private Map<String, NameMapper> mappers;
	private String resourcePath;

	public NameMapperEngine(String directory) {
		mappers = new HashMap<String, NameMapper>();
		resourcePath = directory;
	}

	public void loadDomainMapper(String domainName, String dataFileName) {
		NameMapper mapper = new NameMapper(resourcePath + "/data-name-mapper/"
				+ dataFileName);
		//System.out.println(mapper.getAllNames().size());
		mappers.put(domainName, mapper);
	}
	
	public void loadDomainMapper(NameMapperService nameMapperService, String domain){
		NameMapper mapper = new NameMapper(nameMapperService, domain);
		mappers.put(domain, mapper);
	}

	public String getFinalName(String type, String name) {
		for (String domainName : mappers.keySet()) {
			String finalName = mappers.get(domainName).getFinalName(type, name);
			if (finalName != "") {
				return finalName;
			}
		}

		return name;
	}

	public String getFinalName(String domain, String type, String name) {
		String finalName;
		if (mappers.containsKey(domain)
				&& (finalName = mappers.get(domain).getFinalName(type, name)) != "") {
			return finalName;
		}
		return null;
	}

	public Set<String> getVariationNames(String type, String name) {
		for (String domainName : mappers.keySet()) {
			Set<String> varNames = mappers.get(domainName).getVariationNames(
					type, name);
			if (varNames != null) {
				return varNames;
			}
		}

		Set<String> set = new HashSet<String>();
		set.add(name);
		return set;
	}

	public List<Pair<String, String>> getAllNames() {
		List<Pair<String, String>> allNames = new ArrayList<Pair<String, String>>();

		for (String domainName : mappers.keySet()) {
			//System.out.println(domainName + " __ " + mappers.get(domainName).getAllNames().size());
			allNames.addAll(mappers.get(domainName).getAllNames());
		}
		//System.out.println(allNames.size());
		return allNames;
	}

}
