package fpt.qa.spellchecker.util;

public interface IConstands {
	public static final String VIET_CHARS = "aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz";
	public static final String EN_CHARS = "qwertyuiopasdfghjklzxcvbnm\\+\\-";
	public static final String VIET_FEATURES = "àảãáạăằẳẵắặâầẩẫấậđèẻẽéẹêềểễếệìỉĩíịòỏõóọôồổỗốộơờởỡớợùủũúụưừửữứựỳỷỹýỵ";
	public static final String[] EN_FEATURES = {"af","ou","ed","ll","er","is","hg","ze","ug","yo","st","sh","ei","nl","ee"};
	public static final double SCORE = 0.05;
	static final String SEPARATOR_EXPRESSION = "\\s+";
}
