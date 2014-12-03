package fpt.qa.type_mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.fpt.ruby.business.constants.ProgramType;
import com.fpt.ruby.business.helper.RedisHelper;
import com.fpt.ruby.business.helper.TypeMapperHelper;
import com.fpt.ruby.business.model.TVProgram;
import com.fpt.ruby.business.service.TVProgramService;

import fpt.qa.mdnlib.struct.pair.Pair;

public class TypeMapper {
	static List<TypeRecognizer> types;
	private static Map<ProgramType, Set<String>> typeMapper;
	private static List<Pair<String, List<ProgramType>>> tagged = new ArrayList<>();
	private static Map<ProgramType, ProgramType> rootType;
	private static TVProgramService tvs;
	private static Boolean useTagged = false;
	private static String PATH = File.separator + "type_mapper" + File.separator + "type_mapper.txt";
	private static long ONE_WEEK = 7 * 24 * 60 * 60;

	static {
		tvs = new TVProgramService();
		types = new ArrayList<>();
		types.add(new FilmTypeRecognizer());
		types.add(new GameShowTypeRecognizer());
		types.add(new MusicTypeRecognizer());
		types.add(new SportTypeRecognizer());
		types.add(new CartoonTypeRecognizer());
		types.add(new NewsTypeRecognizer());
		types.add(new FootballTypeRecognizer());
		types.add(new EntertainmentTypeRecognizer());
		types.add(new TenisTypeRecognizer());
		types.add(new FashionTypeRecognizer());
		types.add(new ReportTypeRecognizer());
		types.add(new DiscoveryTypeRecognizer());
		types.add(new ScienceTypeRecognizer());
		types.add(new StyleTypeRecognizer());
		types.add(new GolfTypeRecognizer());
		types.add(new RacingTypeRecognizer());

		setRootType();

		for (TypeRecognizer tp : types) {
			tp.show();
			System.out.println("\n________________");
		}
		try {
			loadData((new RedisHelper()).getClass().getClassLoader().getResource("").getPath() + PATH);
		} catch (Exception ex) {
			System.out.println("ERROR " + ex.getMessage());
			ex.printStackTrace();
			loadData("./classes" + PATH);
		}
//		if (useTagged)
//		retrieveTaggedProgram();
	}

	public TypeMapper() {
	}

	private static void setRootType() {
		rootType = new TreeMap<>();
		
		rootType.put(ProgramType.FOOTBALL, ProgramType.SPORT);
		rootType.put(ProgramType.TENNIS, ProgramType.SPORT);
		rootType.put(ProgramType.GOLF, ProgramType.SPORT);
		rootType.put(ProgramType.RACING, ProgramType.SPORT);

		rootType.put(ProgramType.CARTOON, ProgramType.FILM);
	}

	public static void clear() {
		typeMapper.clear();
		tagged.clear();
	}

	private static void reload(String dir) {

	}

	public static void init() {
		try {
			loadData((new RedisHelper()).getClass().getClassLoader().getResource("").getPath() + PATH);
		} catch (Exception ex) {
			System.out.println("ERROR " + ex.getMessage());
			ex.printStackTrace();
			loadData("./classes" + PATH);

		}
		if (useTagged)
			retrieveTaggedProgram();
	}

	private static void retrieveTaggedProgram() {
		System.out.println("Retrieve tagged program");
		tagged = new ArrayList<>();
		// get from db
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
		Date start = new Date(cal.getTime().getTime() - ONE_WEEK);
		Date end = new Date();

		List<TVProgram> progs = tvs.findTaggedProgram(start, end);
		System.out.println(progs.size() + " progs found in last week!");

		for (TVProgram prog : progs) {
			if (prog.getTitle().trim().isEmpty())
				continue;
			String[] tstr = prog.getType().split(",");
			List<ProgramType> types = new ArrayList<>();
			for (String str : tstr) {
				types.add(ProgramType.getType(str.toLowerCase()));
			}
			tagged.add(new Pair<>(TypeMapperHelper.getTitle(prog.getTitle()), types));
		}

		// get from file
		System.out.println("Show type mapper: ");
		for (Map.Entry<ProgramType, Set<String>> e : typeMapper.entrySet()) {

			for (String str : e.getValue()) {
				List<ProgramType> type = new ArrayList<>(Arrays.asList(e.getKey()));
				str = TypeMapperHelper.getTitle(str);
				Pair<String, List<ProgramType>> t = new Pair<>(str, type);
				// System.out.println("add " + str + " " + type.get(0));
				tagged.add(t);
			}
		}

	}

	private static void loadData(String fileName) {
		try {
			System.out.println("Load data: " + fileName);
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			typeMapper = new TreeMap<>();
			String line;

			while ((line = br.readLine()) != null) {
				try {
//				System.out.println("LINE: " + line);
				String[] part = line.split("\t");
				if (part.length < 2 || part[1].trim().isEmpty())
					continue;

				ProgramType type = ProgramType.getType(part[0]);
				Set<String> s = new HashSet<>();
				String[] progs = part[1].split(",");
				for (String prog : progs) {
					prog = TypeMapperHelper.normalize(prog);
					s.add(prog);
				}
				typeMapper.put(type, s);
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		} catch (Exception ex) {
			System.out.println("ERROR loadData: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public static void showData() {
		System.out.println("Show type mapper:. ");
		for (Map.Entry<ProgramType, Set<String>> e : typeMapper.entrySet()) {
			System.out.println(e.getKey().toString());
			for (String str : e.getValue()) {
				System.out.print(str + " - ");
			}
			System.out.println();
		}
	}
	
	public static List<ProgramType> getType(String channel, String program, String description) {
		return getType(channel, program + " " + description);
	}
	
	public static List<ProgramType> getType(String channel, String program) {
//		System.out.println("Get type for: " + channel + " " + program);
		channel = TypeMapperHelper.normalize(channel);
		program = TypeMapperHelper.normalize(program);

		Set<ProgramType> rs = new HashSet<>();
		
		for (TypeRecognizer tc : types) {
			Boolean isOK = false;
			ProgramType type = tc.getType();

			// fixed program
			if (typeMapper.containsKey(type)) {
				Set<String> progs = typeMapper.get(type);
				for (String prog : progs) {
					if (TypeMapperHelper.contains(prog, program)) {
						isOK = true;
						break;
					}
				}
			}

			if (!isOK) {
				isOK = tc.contains(channel, program);
			}
			if (isOK) {
				rs.add(type);
				if (rootType.containsKey(type)) {
					rs.add(rootType.get(type)); // add rootType
				}
			}
		}

		if (rs.size() == 0 && tagged.size() > 0) {
			// query from tagged.
			int totalProg = tagged.size();
			System.out.println(totalProg);
			program = TypeMapperHelper.getTitle(program);
			for (int i = 0; i < totalProg; ++i) {
				Pair<String, List<ProgramType>> progWithType = tagged.get(i);
				if (TypeMapperHelper.isSame(progWithType.first, program)) {
					rs.addAll(progWithType.second);
				}
			}

			for (ProgramType t : rs) {
				if (rootType.containsKey(t)) {
					rs.add(rootType.get(t)); // add rootType
				}
			}
		}

		// Remove duplicate
		if(rs.contains(ProgramType.FOOTBALL) && (rs.contains(ProgramType.GOLF) || rs.contains(ProgramType.RACING)
				|| rs.contains(ProgramType.TENNIS))) {
			rs.remove(ProgramType.FOOTBALL);
		}

		if (rs.contains(ProgramType.FILM) && rs.contains(ProgramType.REPORT)) {
			rs.remove(ProgramType.FILM);
		}

		if (rs.contains(ProgramType.CARTOON) && rs.contains(ProgramType.REPORT)) {
			rs.remove(ProgramType.CARTOON);
		}

		// Add root type
		ArrayList<ProgramType> allTypes = new ArrayList<>();
		for (ProgramType t: rs) {
			allTypes.add(t);
			if (rootType.containsKey(t) && !allTypes.contains(t)) {
				allTypes.add(rootType.get(t));
			}
		}
		
		return allTypes;
	}

	public static void main(String[] args) {
		System.out.println("NEW CODE2");
		TypeRecognizer f = new FootballTypeRecognizer();
		String chn = "cartoon";
		String prog = "animation cartoon hehe";
		System.out.println(f.contains(chn, prog));
//	FootballTypeRecognizer
		System.out.println("RESULTX: " + TypeMapper.getType(chn, prog));
	}

}
