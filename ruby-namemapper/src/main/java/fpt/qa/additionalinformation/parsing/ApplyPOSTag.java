/*package fpt.qa.additionalinformation.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import vn.hus.nlp.tagger.VietnameseMaxentTagger;
import edu.stanford.nlp.ling.WordTag;

public class ApplyPOSTag {

	private VietnameseMaxentTagger POSTagger;

	public ApplyPOSTag() {
		POSTagger = new VietnameseMaxentTagger();
	}

	public void testTagger() {
		for (WordTag element : POSTagger
				.tagText2("tối nay lịch chiếu phim tại rạp quốc gia thế nào")) {
			System.out.println(element.word().replaceAll(" ", "_") + " " + element.tag() );
		}
	}

	public void tagFileMst( File inputFile, File outputFile ){
		try{
			BufferedReader reader = new BufferedReader( new FileReader( inputFile ) );
			PrintWriter writer = new PrintWriter( new FileWriter( outputFile ) );
			
			String line;
			while( ( line = reader.readLine() ) != null ){
				List< String > tokens = new ArrayList< String >();
				List< String > tags = new ArrayList< String >();
				for( WordTag element : POSTagger.tagText2( line ) ){
					tokens.add( element.word().replaceAll(" ", "_") );
					tags.add( element.tag() );
				}
				
				for( String token : tokens ){
					writer.print( token + "\t" );
				}
				writer.println();
				
				for( String tag : tags ){
					writer.print( tag + "\t" );
				}
				writer.println();
				
				for( int i = 0; i < 2; i++ ){
					writer.println();
				}
				
			}
			
			writer.close();
			reader.close();
		} catch( IOException e ){
			e.printStackTrace();
		}
	}
	
	public void tagFileNormal( File inputFile, File outputFile ){
		try{
			BufferedReader reader = new BufferedReader( new FileReader( inputFile ) );
			PrintWriter writer = new PrintWriter( new FileWriter( outputFile ) );
			
			String line;
			while( ( line = reader.readLine() ) != null ){
				int count = 0;
				for( WordTag element : POSTagger.tagText2( line ) ){
					StringBuilder strBuilder = new StringBuilder();
					strBuilder.append( ++count );
					strBuilder.append( "/" );
					strBuilder.append( element.word().replaceAll(" ", "_") );
					strBuilder.append( "/" );
					strBuilder.append( element.tag() );
					strBuilder.append( " " );
					writer.print( strBuilder.toString() );
				}
				writer.println();
			}
			
			writer.close();
			reader.close();
		} catch( IOException e ){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ApplyPOSTag applyPOSTag = new ApplyPOSTag();
		
		File inputFile = new File( "/home/dungx/Downloads/movies.txt" );
		File outputFile = new File( "train_movies.mst" );
		File outputNormalFile = new File( "movies.normal.txt" );
		
//		applyPOSTag.tagFileMst( inputFile, outputFile);
		applyPOSTag.tagFileNormal( inputFile, outputNormalFile );
	}

}
*/