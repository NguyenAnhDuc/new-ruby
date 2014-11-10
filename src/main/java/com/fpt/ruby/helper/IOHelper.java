package com.fpt.ruby.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IOHelper {
	public static String readFile(String fileName) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		StringBuilder everything = new StringBuilder();

		String line = "";
		try {

			line = br.readLine();
			while (line != null) {
				everything.append(line + "\n");
				line = br.readLine();
			}
		} catch (Exception ex) {
			System.out.println("Line Error: " + line);
			ex.printStackTrace();
		} finally {
			br.close();
		}
		return everything.toString();
	}

	public static void writeFile(String filename, String content)
			throws IOException {
		File file = new File(filename);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
	}

}