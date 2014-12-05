package fpt.qa.spellchecker.util;

public class LanguageClassifier {

	public static Language getLanguage(String text) {
		text = text.toLowerCase().replaceAll("\\s+", " ");
		for (String s : IConstands.EN_FEATURES) {
			if (text.contains(s)) {
				return Language.EN;
			}
		}
		for (int i = 0; i < text.length(); i++) {
			if (IConstands.VIET_FEATURES.contains(text.charAt(i) + "")) {
				return Language.VI;
			}
			if ("fzjw".contains(text.charAt(i) + "")) {
				return Language.EN;
			}
		}
		return Language.EN;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		final String TEXT1 = "bão qua làng";
		final String TEXT2 = "banh ducc co xuong";
		final String TEXT3 = "nterstelllar";
		final String TEXT4 = "biggg hero 6";
		final String TEXT5 = "an tv";
		final String TEXT6 = "vt 3";

		System.err.println("\"" + TEXT1 + "\"" + " --- " + getLanguage(TEXT1));
		System.err.println("\"" + TEXT2 + "\"" + " --- " + getLanguage(TEXT2));
		System.err.println("\"" + TEXT3 + "\"" + " --- " + getLanguage(TEXT3));
		System.err.println("\"" + TEXT4 + "\"" + " --- " + getLanguage(TEXT4));
		System.err.println("\"" + TEXT5 + "\"" + " --- " + getLanguage(TEXT5));
		System.err.println("\"" + TEXT6 + "\"" + " --- " + getLanguage(TEXT6));
		System.err.println("\n\n==================Total time : " + (System.currentTimeMillis() - start)+" ms ==================");
	}
}
