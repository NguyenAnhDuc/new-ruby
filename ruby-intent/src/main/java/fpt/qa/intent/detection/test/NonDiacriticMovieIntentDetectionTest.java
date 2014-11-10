package fpt.qa.intent.detection.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import fpt.qa.intent.detection.NonDiacriticMovieIntentDetection;

public class NonDiacriticMovieIntentDetectionTest {
	
	public static void test(String fileIn, String fileOut){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileOut));
			BufferedReader reader = new BufferedReader(new FileReader(fileIn));
			
			String line;
			while((line = reader.readLine()) != null){
				writer.write(line + "\n");
				writer.write(NonDiacriticMovieIntentDetection.getTunedSent(line) + "\n");
				writer.write(NonDiacriticMovieIntentDetection.getIntent(line) + "\n\n\n");
			}
			
			reader.close();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NonDiacriticMovieIntentDetection.init("/home/ngan/Work/AHongPhuong/RubyWeb/rubyweb/src/main/resources/qc/movie/non-diacritic",
//        		"/home/ngan/Work/AHongPhuong/Intent_detection/data/qc/1", 
                "/home/ngan/Work/AHongPhuong/RubyWeb/rubyweb/src/main/resources/dicts/non-diacritic");
		test("/home/ngan/Work/AHongPhuong/Intent_detection/data/tmp/non-diacritic-movies.txt",
				"/home/ngan/Work/AHongPhuong/Intent_detection/data/tmp/non-diacritic-movies-intent.out");
	}

}
