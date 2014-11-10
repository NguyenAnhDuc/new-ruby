/*package com.fpt.ruby.conjunction;

import java.io.File;

import fpt.qa.additionalinformation.modifier.ConjunctionWithType;
import fpt.qa.mdnlib.struct.pair.Pair;

// IMPORT CLASS

public class ConjunctionTest{
    private static final String TEXT = "rạp dân chủ và rạp quốc gia ở đâu?";
    private static final String TEXT2 = "rạp chủ dân ở đâu?";
    private static final String TEXT3 = "rạp lotte landmark chiếu phim vệ binh dải ngân hà chiếu mấy giờ?";

    public static void main( String[] args ){

	// INITIALIZE A CONJUNCTION CHECKER
        ConjunctionWithType conjunctionWithType = new ConjunctionWithType( "src/main/resources" );

	// LOAD CONJUNCTION FILE
        conjunctionWithType.loadConjunctionType( new File( "movies_infor.txt" ) );

	// METHOD TO GET CONJUNCTION WITH TYPE
	// INPUT: A TEXT STRING
	// OUTPUT: A LIST OF PAIR, WITH 1ST ELEMENT IS A CONJUNCTION, 2ND ELEMENT IS THE TYPE OF THE CONJUNCTION (TEN PHIM, TEN DIEN VIEN, ETC)
        for( Pair< String, String > element : conjunctionWithType.getOriginRelevantConjunctionsWithType( TEXT3 ) ){
            System.out.println( element.first + " " + element.second );
        }
    }
}*/