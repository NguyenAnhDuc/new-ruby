package fpt.qa.spellchecker.util;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author TIT
 */
public class RemoveAccent implements Serializable{
    private static final long serialVersionUID = 1L;
    private static char[] SPECIAL_CHARACTERS = {'À', 'À', 'Á', 'Â', 'Ã', 'È', 'É', 'Ê', 'Ì', 'Í', 'Ò',
                    'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â', 'ã', 'è', 'é', 'ê',
                    'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý', 'Ă', 'ă', 'Đ', 'đ',
                    'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ', 'ạ', 'Ả', 'ả', 'Ấ',
                    'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ', 'Ắ', 'ắ', 'Ằ', 'ằ',
                    'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ', 'ẻ', 'Ẽ', 'ẽ', 'Ế',
                    'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ', 'Ỉ', 'ỉ', 'Ị', 'ị',
                    'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ', 'ổ', 'Ỗ', 'ỗ', 'Ộ',
                    'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ', 'ợ', 'Ụ', 'ụ',
                    'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ', 'ữ', 'Ự', 'ự'};

    static int[] map = new int[10000];
    private static char[] REPLACEMENTS = {'A', 'A', 'A', 'A', 'A', 'E', 'E', 'E',
                    'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a', 'a', 'a',
                    'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u', 'y', 'A',
                    'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A', 'a',
                    'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
                    'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E', 'e',
                    'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'I',
                    'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
                    'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
                    'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
                    'U', 'u'};
    public RemoveAccent() {
		init();
	}
    
    private static void init() {
    	for (int i = 0; i < SPECIAL_CHARACTERS.length; i++) {
    		int x = SPECIAL_CHARACTERS[i];
    		map[x] = i ;
		}
    }
    public static String removeAccent(String s) {
        //s = trimString(s);
    	
    	
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
                sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }
    
    public static char removeAccent(char ch) {
    	int x = ch ;
    
    	int index = Arrays.binarySearch(SPECIAL_CHARACTERS, ch);
        //int index = map[x];
        if (index > 0) {
                ch = REPLACEMENTS[index];
        }
        return ch;
    }
    
    public static String trimString(String s) {
        return s.trim().replaceAll("\\s+", " ");
    }
    
     public static void main(String[] args) {
	}
    
     public static List<String> fileReader(String path) {
 		List<String> list = new ArrayList<String>();
 		try {
 			FileInputStream is = new FileInputStream(path);
 			Scanner input = new Scanner(is, "UTF-8");
 			while (input.hasNextLine()) {
 				String line = input.nextLine();
 				if(line.equalsIgnoreCase(""));
 					list.add(line.trim());
 			}
 			is.close();
 			input.close();
 			
 		} catch (IOException e) {
 			System.err.println(e.getMessage());
 			//e.printStackTrace();
 		}
 		return list ;
 	}
     
}

