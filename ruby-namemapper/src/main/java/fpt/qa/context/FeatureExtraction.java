package fpt.qa.context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class FeatureExtraction{
	
	private static final String DATA_FILE = "/home/dungx/git/rubyweb/src/main/resources/context/Dialogue-Contexts.txt";
	private static List< String > contexts = new ArrayList< String >();
	
	private static Random random = new Random();
	
	public static void main( String[] args ) {
		
		contexts = dataImport( DATA_FILE );
		
		// Print all contexts
//		for( String context : contexts.subList( 1, 2 ) ){
//			System.out.println( context.replaceAll( "\\n" , " " ) );
//		}
		
		featureExtractionNgram( contexts, "/home/dungx/git/rubyweb/src/main/resources/context/features.libsvm" );
		
	}

	private static List< String > dataImport( String dataFilePath ){
		try{
			List< String > contexts = new ArrayList< String >();
			File file = new File( dataFilePath );
			
			BufferedReader reader = new BufferedReader( new FileReader( file ) );
			String line;
			String singleContext = null;
			
			while( ( line = reader.readLine() ) != null ){
				if( line.equalsIgnoreCase( "" ) ){
					contexts.add( singleContext );
					singleContext = "";
					continue;
				}
				if( line.startsWith( "M:" ) ){
					continue;
				}
				singleContext += line + "\n";
			}
			
			reader.close();
			return contexts;
		}catch( Exception e ){
			e.printStackTrace();
			return null;
		}
	}
	
	private static void featureExtractionTemplate( List< String > contexts, String outputFile ){
		
	}
	
	private static void featureExtractionNgram( List< String > contexts, String outputFile ){
		Map< String, Integer > featureMap = new HashMap< String, Integer >();
		int nextIndex = 0;
		
		try{
			PrintWriter writer = new PrintWriter( new BufferedWriter( new FileWriter( new File( outputFile ) ) ) );
			//Extract in-context example
			for( String context : contexts ){
				if( context == null ) continue;
				context = context.toLowerCase();
				String[] sentences = context.split( "\\n" );
				if( sentences.length == 1 ){
					continue;
				}
				for( int i = 1; i < sentences.length; i++ ){
					List< Integer > featureVector = new ArrayList< Integer >();
					
					//Extract in-sentence n-gram
					String tokens[] = sentences[ i ].split( "[\\s\\?]+" );
					for( String token : tokens ){
						if( !featureMap.containsKey( token ) ){
							featureMap.put( token, nextIndex++ );
						}
						featureVector.add( featureMap.get( token ) );
					}
					
					//Extract previous-sentence n-gram
					String previousTokens[];
					String previousSentences = "";
					for( int j = 0; j < i; j++ ){
						previousSentences = previousSentences.concat( sentences[ j ] + " " );
					}
					previousTokens = previousSentences.split( "[\\s\\?]+" );
					for( String token : previousTokens ){
						if( !featureMap.containsKey( token.concat( "-prev" ) ) ){
							featureMap.put( token.concat( "-prev" ), nextIndex++ );
						}
						featureVector.add( featureMap.get( token.concat( "-prev" ) ) );
					}
					
					//Print feature vector
					Collections.sort( featureVector );
					writer.print( "+1" );
					for( int k = 0; k < featureVector.size(); k++ ){
						if( k > 0 && featureVector.get( k ) != featureVector.get( k - 1 ) ){
							writer.print( " " + featureVector.get( k ) + ":1" );
						}
					}
					writer.println();
				}
			}
			
			//Extract cross-context examples
			for( int i = 2; i < contexts.size(); i++ ){
				List< Integer > featureVector = new ArrayList< Integer >();
				String newContextSentence = contexts.get( i ).split( "\\n" )[0].toLowerCase();
				String previousContext = contexts.get( i - 1 ).toLowerCase();
				
				//Extract in-sentence n-gram
				String tokens[] = newContextSentence.split( "[\\s\\?]+" );
				for( String token : tokens ){
					if( !featureMap.containsKey( token ) ){
						featureMap.put( token, nextIndex++ );
					}
					featureVector.add( featureMap.get( token ) );
				}
				
				//Extract previous-sentence n-gram
				String previousTokens[];
				previousTokens = previousContext.split( "[\\s\\?]+" );
				for( String token : previousTokens ){
					if( !featureMap.containsKey( token.concat( "-prev" ) ) ){
						featureMap.put( token.concat( "-prev" ), nextIndex++ );
					}
					featureVector.add( featureMap.get( token.concat( "-prev" ) ) );
				}
				
				//Print feature vector
				Collections.sort( featureVector );
				writer.print( "-1" );
				for( int k = 0; k < featureVector.size(); k++ ){
					if( k > 0 && featureVector.get( k ) != featureVector.get( k - 1 ) ){
						writer.print( " " + featureVector.get( k ) + ":1" );
					}
				}
				writer.println();
			}
			
			writer.close();
		}catch( IOException e ){
			e.printStackTrace();
		}
	}
}
