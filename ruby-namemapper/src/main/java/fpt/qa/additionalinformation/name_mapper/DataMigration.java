/*
package fpt.qa.additionalinformation.name_mapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import vn.hus.nlp.utils.UTF8FileUtility;

import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.service.NameMapperService;


public class DataMigration {
	private static NameMapperService ns = new NameMapperService();
//	private static final String resource = "C:/Users/ducna/workspace/ruby/target/test-classes/";
	private static final String resource = "/home/timxad/ws/proj/rubyweb/src/main/resources/";
//	private static final String resource = "E:\\Workspace\\Tutorial\\ruby-web\\src\\main\\resources";

	private static void loadDomainMapper(String domain, String data,
			Boolean isDiacritic, List<String> setOfNames) {
		String fullPath = resource + File.separator
				+ (!isDiacritic ? ("non-diacritic" + File.separator) : "")
				+ "data-name-mapper" + File.separator + data;
		String[] lines = UTF8FileUtility.getLines(fullPath);
		int count = 0;
		for (String line : lines) {
			line = line.trim();
			if (line.equals("") || line.startsWith("#"))
				continue;

			String[] parts = line.split("\t");
			String type = parts[0];
			String name = "";
			String allVar = "";
			Set<String> vars = new HashSet<String>();

			if (parts.length < 2)
				continue;
			int sep = parts[1].indexOf(';');

			if (sep > -1) {
				name = parts[1].substring(0, sep);
				allVar = parts[1].substring(sep + 1);
			} else {
				name = parts[1];
			}
			
			vars.add(name);
			StringTokenizer tokenizer = new StringTokenizer(allVar, ",");
			while (tokenizer.hasMoreTokens()) {
				vars.add(tokenizer.nextToken().trim());
			}
			
			if (name.toLowerCase().equals("tt chiếu phim quốc gia"))
			{
				System.out.println("Name: " + name);
				System.out.println("Vars: ");
				for (String var : vars){
					System.out.println(var);
				}
			}
			
			//if (setOfNames.contains(name.toLowerCase())) continue;
			//setOfNames.add(name.trim().toLowerCase());
			
			if (setOfNames.contains(name.toLowerCase())){
				NameMapper n = ns.findByName(name);
				if (n != null){
					//System.out.println(name);
					Set<String> varss = n.getVariants();
					varss.addAll(vars);
					n.setVariant(varss);
					ns.save(n);
					
					count ++ ;
					continue;
				}
				
			}
			
			setOfNames.add(name.trim().toLowerCase());
			
			
			
			
			NameMapper n = new NameMapper();
			n.setName(name);
			n.setDomain(domain);
			n.setIsDiacritic(isDiacritic);
			n.setType(type);
			n.setVariant(vars);
			n.setEnteredDate(new Date());
			n.setLastMention(new Date());

			ns.save(n);
		}

	}
	
	public static void db2file(String file) throws IOException {
		NameMapperService nms = new NameMapperService();
		List<NameMapper> names = nms.findAll();
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file)));
		
		for (NameMapper n: names) {
//			String line = String.format("%s\t", n.getName());
//			bw.write("%s, %s, %s, %s"
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Start................");
		String dir = (new RedisHelper()).getClass().getClassLoader().getResource("").getPath();
		System.out.println(dir);
		List<String> setOfNames = new ArrayList<String>();
		for (int i = 0; i <= 1; ++i) {
			loadDomainMapper("tv",  "tv_domain.txt", i != 0,setOfNames);
			//loadDomainMapper("food", dir + "foodNames.txt", i != 0);
			loadDomainMapper("movie", "movieNames.txt", i != 0,setOfNames);
		}	 
		
		//add name mapper
		NameMapper nameMapper = new NameMapper();
		nameMapper.setDomain("tv");
		nameMapper.setName("vtv1");
		ns.save(nameMapper);
		//test
		NameMapper nameMapper = ns.findByName("vtv1");
		nameMapper.setDomain("movie");
		ns.save(nameMapper);
		
		System.out.println("DONE!");
	}
*/
