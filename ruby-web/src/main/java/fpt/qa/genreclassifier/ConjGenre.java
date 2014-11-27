package fpt.qa.genreclassifier;

import com.fpt.ruby.business.service.NameMapperService;
import fpt.qa.additionalinformation.modifier.SurroundingWords;
import fpt.qa.additionalinformation.name_mapper.NameMapperEngine;
import fpt.qa.mdnlib.nlp.vn.vntokenizer.VnTokenizer;
import fpt.qa.mdnlib.struct.conjunction.ConjunctionChecker;
import fpt.qa.mdnlib.struct.pair.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ConjGenre extends ConjunctionChecker{
	private Map< String, HashSet< String > > conjunctionType;
	private NameMapperEngine nameMapperEngine;
	private SurroundingWords surroundingWords;
	private NameMapperService nameMapperService;

	public NameMapperEngine getNameMapperEngine() {
		return nameMapperEngine;
	}

	public ConjGenre(String resourcePath){
		conjunctionType = new HashMap< String, HashSet< String > >();

		nameMapperEngine = new NameMapperEngine( resourcePath );
		nameMapperEngine.loadDomainMapper( "Genre", "Genre.txt" );
		surroundingWords = new SurroundingWords( resourcePath );

		loadConjunctionFromNameMapper( nameMapperEngine );


		VnTokenizer.loadSpecialChars( resourcePath + "/dicts/specialchars/special-chars.xml" );
		VnTokenizer.loadRegexXMLFile( resourcePath + "/regexes/regular-expressions.xml" );
	}

	public void reloadNameMapperEngine() {

//		nameMapperEngine.re
	}

	public void loadConjunctionType( File file ) {
		try{
			BufferedReader reader = new BufferedReader( new FileReader( file ) );

			String line = null;
			while( ( line = reader.readLine() ) != null ){
				String[] elements = line.split( "\t" );
				int numberOfElements = elements.length;

				String type = elements[ 0 ];

				if( numberOfElements == 2 ){
					String conjunction = elements[ 1 ];
					addConjunctionWithType( conjunction, type );
				}else{
					for( int i = 1; i < numberOfElements; i++ ){
						String conjunction = elements[ i ];
						addConjunctionWithType( conjunction, type );
					}
				}
			}

			reader.close();
		}catch ( IOException e ){
			e.printStackTrace();
		}
	}

	private void loadConjunctionFromNameMapper( NameMapperEngine nameMapperEngine ) {
		for( Pair< String, String > pair : nameMapperEngine.getAllNames() ){
			String name = pair.first;
			String type = pair.second;
			addConjunctionWithType( name, type );
		}
	}

	public void addConjunctionWithType( String str, String type ) {
		addConjunction( "{" + str + "}" );
		String normalizedConjunction = str.toLowerCase();
		if( conjunctionType.containsKey( normalizedConjunction ) ){
			conjunctionType.get( normalizedConjunction ).add( type );
		}else{
			conjunctionType
					.put( normalizedConjunction, new HashSet< String >( Arrays.asList( new String[] { type } ) ) );
		}
	}

	private HashSet< String > getConjunctionType( String conjunction ) {
		return conjunctionType.get( conjunction.toLowerCase().substring( 1, conjunction.length() - 1 ) );
	}

	public List< Pair< String, String > > getOriginRelevantConjunctionsWithType( String text ) {
		List< Pair< String, String > > relConjunctions = new ArrayList< Pair< String, String > >();

		Set< String > originSets = new HashSet< String >();

		List< String > rawConj = getRelevantConjunctions( VnTokenizer.tokenize( text ), true );
		//System.err.println( "[ConjWithType] [RawConj] " + rawConj );

		List< Pair< String, String > > rawConjWithType = new ArrayList< Pair< String, String > >();
		for( String conj : rawConj ){
			for( String type : getConjunctionType( conj ) ){
				rawConjWithType.add( new Pair< String, String >( conj, type ) );
			}
		}

		for( Pair< String, String > conj : getLongerOverlappedResult( getBetterSurroundingContext( rawConjWithType,
				text ) ) ){
			String origin = nameMapperEngine.getFinalName( conj.second,
					conj.first.substring( 1, conj.first.length() - 1 ) );
			if( !originSets.contains( origin ) ){
				relConjunctions.add( new Pair< String, String >( origin, conj.second ) );
				originSets.add( origin );
			}
		}

		//System.err.println( "[ConjWithType] [Final Conj] " + relConjunctions );

		return relConjunctions;
	}

	public List< Pair< String, String > > getOriginRelevantConjunctionsWithType( String domain, String text ) {
		List< Pair< String, String > > relConjunctions = new ArrayList< Pair< String, String > >();

		Set< String > originSets = new HashSet< String >();

		List< String > rawConj = getRelevantConjunctions( VnTokenizer.tokenize( text ), true );
		//System.err.println( "[ConjWithType] [RawConj] " + rawConj );

		List< Pair< String, String > > rawConjWithType = new ArrayList< Pair< String, String > >();
		for( String conj : rawConj ){
			for( String type : getConjunctionType( conj ) ){
				rawConjWithType.add( new Pair< String, String >( conj, type ) );
			}
		}

		for( Pair< String, String > conj : getLongerOverlappedResult( getBetterSurroundingContext( rawConjWithType,
				text ) ) ){
			String origin = nameMapperEngine.getFinalName( domain, conj.second,
					conj.first.substring( 1, conj.first.length() - 1 ) );
			if( !originSets.contains( origin ) && origin != null ){
				relConjunctions.add( new Pair< String, String >( origin, conj.second ) );
				originSets.add( origin );
			}
		}

		//System.err.println( "[ConjWithType] [Final Conj] " + relConjunctions );

		return relConjunctions;
	}

	public List< Pair< ArrayList< String >, String > > getListRelevantConjunctionsWithType( String text ) {
		List< Pair< String, String > > origins = getOriginRelevantConjunctionsWithType( text );
		List< Pair< ArrayList< String >, String > > relConjunctions = new ArrayList< Pair< ArrayList< String >, String > >();

		for( Pair< String, String > conjWithType : origins ){
			String conj = conjWithType.first;
			String type = conjWithType.second;
			relConjunctions.add( new Pair< ArrayList< String >, String >( new ArrayList< String >( nameMapperEngine
					.getVariationNames( type, conj ) ), type ) );
		}

		return relConjunctions;
	}

	//
	// public List< Pair< String, String > > pruneMultipleResults( List< Pair<
	// String, String > > rawResult ){
	// return rawResult;
	// }

	private List< Pair< String, String > > getLongerOverlappedResult( List< Pair< String, String > > conjunctionList ) {
		List< Pair< String, String > > newList = new ArrayList< Pair< String, String > >();
		newList.addAll( conjunctionList );

		for( int i = 0; i < newList.size() - 1; i++ ){
			for( int j = i + 1; j < newList.size(); j++ ){
				if( isSmallerSubsetOf( newList.get( j ).first, newList.get( i ).first ) ){
					newList.remove( j );
					j--;
					continue;
				}
				if( isSmallerSubsetOf( newList.get( i ).first, newList.get( j ).first ) ){
					Pair temp = newList.get( i );
					newList.set( i, newList.get( j ) );
					newList.set( j, temp );
					newList.remove( j );
					j--;
				}
			}
		}

		//System.err.println( "[ConjWithType] [Longer Conj] " + newList );
		return newList;
	}

	private List< Pair< String, String > > getBetterSurroundingContext( List< Pair< String, String > > conjunctionList,
			String sentence ) {
		List< Pair< String, String > > newList = new ArrayList< Pair< String, String > >();
		newList.addAll( conjunctionList );

		for( int i = 0; i < newList.size() - 1; i++ ){
			for( int j = i + 1; j < newList.size(); j++ ){
				if( isIntersect( newList.get( i ).first, newList.get( j ).second ) ){
					if( surroundingWords.countSurroundingWords( sentence, newList.get( i ).first,
							newList.get( i ).second ) > surroundingWords.countSurroundingWords( sentence,
							newList.get( j ).first, newList.get( j ).second ) ){
						newList.remove( j );
						j--;
					}else if( surroundingWords.countSurroundingWords( sentence, newList.get( i ).first,
							newList.get( i ).second ) < surroundingWords.countSurroundingWords( sentence,
							newList.get( j ).first, newList.get( j ).second ) ){
						Pair temp = newList.get( i );
						newList.set( i, newList.get( j ) );
						newList.set( j, temp );
						newList.remove( j );
						j--;
					}
				}
			}
		}

		//System.err.println( "[ConjWithType] [Better Surr Conj] " + newList );

		return newList;
	}

	private static boolean isSmallerSubsetOf( String lhs, String rhs ) {
		Set< String > lhsSet = new HashSet< String >( Arrays.asList( lhs.toLowerCase().split( "[\\s\\{\\}]+" ) ) );
		Set< String > rhsSet = new HashSet< String >( Arrays.asList( rhs.toLowerCase().split( "[\\s\\{\\}]+" ) ) );
		lhsSet.removeAll( Arrays.asList( new String[] { "" } ) );
		rhsSet.removeAll( Arrays.asList( new String[] { "" } ) );
		return rhsSet.containsAll( lhsSet ) && rhsSet.size() > lhsSet.size();
	}

	private static boolean isIntersect( String lhs, String rhs ) {
		Set< String > lhsSet = new HashSet< String >( Arrays.asList( lhs.split( "[\\s\\{\\}]+" ) ) );
		Set< String > rhsSet = new HashSet< String >( Arrays.asList( rhs.split( "[\\s\\{\\}]+" ) ) );
		rhsSet.removeAll( Arrays.asList( new String[] { "" } ) );
		lhsSet.removeAll( Arrays.asList( new String[] { "" } ) );
		lhsSet.retainAll( rhsSet );
		return lhsSet.size() > 0;
	}
	
	public NameMapperEngine getNameMapper() {
		return nameMapperEngine;
	}

	public static void main( String[] args ) {
		System.out.println( isIntersect( "{Fidel Castro}", "{Castrol Poster}" ) );
	}
}

