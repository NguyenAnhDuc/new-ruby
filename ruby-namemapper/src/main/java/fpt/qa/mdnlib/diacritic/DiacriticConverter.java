
package fpt.qa.mdnlib.diacritic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hieupx
 */
public class DiacriticConverter{
	private static final Set< Character > diacriticChars = new HashSet();
	private static final Map< Character, Character > vnCharMap = new HashMap();

	static{
		String chars = "ÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬĐÐÈẺẼÉẸÊỀỂỄẾỆÌỈĨÍỊÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢÙỦŨÚỤƯỪỬỮỨỰỲỶỸÝỴàảãáạăằẳẵắặâầẩẫấậđèẻẽéẹêềểễếệìỉĩíịòỏõóọôồổỗốộơờởỡớợùủũúụưừửữứựỳỷỹýỵ";

		for( int i = 0; i < chars.length(); i++ ){
			diacriticChars.add( chars.charAt( i ) );
		}

		vnCharMap.put( 'à', 'a' );
		vnCharMap.put( 'ả', 'a' );
		vnCharMap.put( 'ã', 'a' );
		vnCharMap.put( 'á', 'a' );
		vnCharMap.put( 'ạ', 'a' );
		vnCharMap.put( 'ă', 'a' );
		vnCharMap.put( 'ằ', 'a' );
		vnCharMap.put( 'ẳ', 'a' );
		vnCharMap.put( 'ẵ', 'a' );
		vnCharMap.put( 'ắ', 'a' );
		vnCharMap.put( 'ặ', 'a' );
		vnCharMap.put( 'â', 'a' );
		vnCharMap.put( 'ầ', 'a' );
		vnCharMap.put( 'ẩ', 'a' );
		vnCharMap.put( 'ẫ', 'a' );
		vnCharMap.put( 'ấ', 'a' );
		vnCharMap.put( 'ậ', 'a' );
		vnCharMap.put( 'đ', 'd' );
		vnCharMap.put( 'è', 'e' );
		vnCharMap.put( 'ẻ', 'e' );
		vnCharMap.put( 'ẽ', 'e' );
		vnCharMap.put( 'é', 'e' );
		vnCharMap.put( 'ẹ', 'e' );
		vnCharMap.put( 'ê', 'e' );
		vnCharMap.put( 'ề', 'e' );
		vnCharMap.put( 'ể', 'e' );
		vnCharMap.put( 'ễ', 'e' );
		vnCharMap.put( 'ế', 'e' );
		vnCharMap.put( 'ệ', 'e' );
		vnCharMap.put( 'ì', 'i' );
		vnCharMap.put( 'ỉ', 'i' );
		vnCharMap.put( 'ĩ', 'i' );
		vnCharMap.put( 'í', 'i' );
		vnCharMap.put( 'ị', 'i' );
		vnCharMap.put( 'ò', 'o' );
		vnCharMap.put( 'ỏ', 'o' );
		vnCharMap.put( 'õ', 'o' );
		vnCharMap.put( 'ó', 'o' );
		vnCharMap.put( 'ọ', 'o' );
		vnCharMap.put( 'ô', 'o' );
		vnCharMap.put( 'ồ', 'o' );
		vnCharMap.put( 'ổ', 'o' );
		vnCharMap.put( 'ỗ', 'o' );
		vnCharMap.put( 'ố', 'o' );
		vnCharMap.put( 'ộ', 'o' );
		vnCharMap.put( 'ơ', 'o' );
		vnCharMap.put( 'ờ', 'o' );
		vnCharMap.put( 'ở', 'o' );
		vnCharMap.put( 'ỡ', 'o' );
		vnCharMap.put( 'ớ', 'o' );
		vnCharMap.put( 'ợ', 'o' );
		vnCharMap.put( 'ù', 'u' );
		vnCharMap.put( 'ủ', 'u' );
		vnCharMap.put( 'ũ', 'u' );
		vnCharMap.put( 'ú', 'u' );
		vnCharMap.put( 'ụ', 'u' );
		vnCharMap.put( 'ư', 'u' );
		vnCharMap.put( 'ừ', 'u' );
		vnCharMap.put( 'ử', 'u' );
		vnCharMap.put( 'ữ', 'u' );
		vnCharMap.put( 'ứ', 'u' );
		vnCharMap.put( 'ự', 'u' );
		vnCharMap.put( 'ỳ', 'y' );
		vnCharMap.put( 'ỷ', 'y' );
		vnCharMap.put( 'ỹ', 'y' );
		vnCharMap.put( 'ý', 'y' );
		vnCharMap.put( 'ỵ', 'y' );
	}

	public static boolean hasDiacriticAccents( String str ) {
		for( int i = 0; i < str.length(); i++ ){
			if( diacriticChars.contains( str.charAt( i ) ) ){
				return true;
			}
		}

		return false;
	}

	public static String removeDiacritics( String str ) {
		String result = "";

		for( int i = 0; i < str.length(); i++ ){
			Character sourceChar = str.charAt( i );
			Character destinationChar = vnCharMap.get( sourceChar );
			if( destinationChar != null ){
				result += destinationChar;
			}else{
				result += sourceChar;
			}
		}

		return result;
	}

	public static void main(String[] args){
		String input = "xin chào";
		System.out.println(removeDiacritics(input));


	}
}
