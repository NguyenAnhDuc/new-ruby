package fpt.qa.additionalinformation.modifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class SurroundingWords {
	
	private Map< String, HashSet< String> > surroundMap;
	
	public SurroundingWords(){
		surroundMap = new HashMap< String, HashSet< String > >();
	}
	
	public SurroundingWords( String resourcePath ){
		this();
		loadSurroundWords( new File( resourcePath + "/surroundWords/surroundWords.txt" ) );
//		loadSurroundWords( new File( resourcePath + "surroundWords/surroundWords.txt" ) );
	}
	
	private void loadSurroundWords( File file ){
		try{
			BufferedReader reader = new BufferedReader( new FileReader( file ) );
			
			String line;
			while( ( line = reader.readLine() ) != null ){
				String[] tokens = line.split( "\\," );
				HashSet< String > surroundWords = new HashSet< String >();
				for( int i = 1; i < tokens.length; i++ ){
					surroundWords.add( tokens[ i ].trim() );
				}
				surroundMap.put( tokens[ 0 ].trim(), surroundWords );
				System.err.println( "[SurrWords] " + tokens[ 0 ].trim() + surroundWords );
			}
			
			reader.close();
		}catch( IOException e ){
			e.printStackTrace();
		}
	}
	
	public int countSurroundingWords( String sentence, String conj, String type ){
		
		if( !surroundMap.containsKey( type ) ){
			System.err.println( "[SurrWords] Type not found: " + type );
			return 0;
		}
		
		Set< String > sentenceSet = new HashSet< String >( Arrays.asList( sentence.split( "[\\s\\{\\}\\,]+" ) ) );
		Set< String > conjSet = new HashSet< String >( Arrays.asList( conj.split( "[\\s\\{\\}]+" ) ) );
		
		sentenceSet.removeAll( conjSet );
		
		
		int count = 0;
		for( String word : surroundMap.get( type ) ){
			if( sentenceSet.containsAll( new HashSet( Arrays.asList( word.split( "[\\s]+" ) ) ) ) ){
				count++;
			}
		}
		
		return count;
	}
	
	public static void main(String[] args) {
		SurroundingWords testSW = new SurroundingWords( "src/main/resources" );
		System.out.println( testSW.countSurroundingWords(  "Rạp lotte chiếu phim gì?", "cin_name", "cin_name" ) );
	}

}
