package fpt.qa.typeclassifier;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		/*List<String> list = fileReader("questions_tv");
		ProgramTypeExtractor extractor = new ProgramTypeExtractor();
		// System.out.println(list);
		for (String s : list) {
			System.out
					.println("====================================================================================================");
			System.out.println("question : " + s);
			System.out.println("type     : " + extractor.getType(s));
		}*/
	}

	public static List<String> fileReader(String path) {
		List<String> list = new ArrayList<String>();
		try {
			FileInputStream is = new FileInputStream(path);
			Scanner input = new Scanner(is, "UTF-8");
			while (input.hasNextLine()) {
				String line = input.nextLine();
				if (line.equalsIgnoreCase(""))
					;
				list.add(line.trim());
			}
			is.close();
			input.close();

		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
}
