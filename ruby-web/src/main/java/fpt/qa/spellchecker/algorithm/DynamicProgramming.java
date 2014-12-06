package fpt.qa.spellchecker.algorithm;


public class DynamicProgramming {
	
	
	public static double SWSNonStopPassageScore(String candidate, String word) {
		if(candidate==null||word ==null) {
			return 0;
		}
		char[] cdd = candidate.toCharArray();
		char[] wrd = word.toCharArray();
		int cddLength = cdd.length;
		int wrdLength = wrd.length;
		
		int[][] c = new int[wrd.length+1][cdd.length+1];
		int max = 0;
		for (int i = 0; i < wrdLength ; i++) {
			for (int j = 0; j < cddLength; j++) {
				if (wrd[i]==cdd[j]) {
					c[i+1][j+1] = c[i][j] + 1;
				} else {
					c[i+1][j+1] = 0;
				}
				if (c[i+1][j+1] > max) {
					max = c[i+1][j+1];
				}
			}
		}
//		ArrayList<String> str = new ArrayList<String>(max);
//		for (int i = wrdLength; i >= 0; i--)
//			for (int j = cddLength; j >= 0; j--)
//				if (c[i][j] == max) {
//					for (int k = 0; k < max; k++)
//						str.add(wrd[i - k - 1]+"");
//				}
//		System.out.println("+MaxLength = " + max);
//		System.out.print("+Result :");
//		for(int i = max-1; i >= 0 ; i--) {
//			System.out.print("["+str.get(i)+"]");
//		}
		
		return (double) max ;
	}
	
	public static double SWSPassageScore(String candidate, String word) {
		if(candidate==null||word ==null) {
			return 0;
		}
		double score = 0;
		int cddLength = candidate.length();
		int wrdLength = word.length();
		
		int[][] c = new int[wrdLength+1][cddLength+1];
		
		int max = 0;
		for (int i = 1; i <=  wrdLength; i++) {
			for (int j = 1; j <=  cddLength; j++) {
				if (word.charAt(i-1)==candidate.charAt(j-1)) {
					c[i][j] = c[i - 1][j - 1] + 1;
				} else {
					c[i][j] = max(c[i][j-1], c[i-1][j]);
				}
			}
		}
		max = c[wrdLength][cddLength];
//		ArrayList<String> str = new ArrayList<String>(max);
//		int i, j;
//		i = wrdLength -1 ;
//		j = cddLength -1 ;
//		while (i >= 0 && j >= 0) {
//			if (wrd[i]==cdd[j]) {
//				str.add(cdd[j]+"");
//				i = i - 1;
//				j = j - 1;
//			} else {
//				if (c[i][j+1] > c[i+1][j])
//					i = i - 1;
//				else
//					j = j - 1;
//			}
//		}
//		System.out.println("+MaxLength = " + max);
//		//System.out.print("+Result :");
//		for (int k = str.size() - 1; k >= 0; k--) {
//			System.out.print("[" + str.get(k) + "]");
//		}
		score = max*2.0/(wrdLength+cddLength) ;
		//score = max*1.0/wrdLength;
		return score;
	}
	
	public static void print(char[] arr) {
		for(char c : arr) {
			System.out.print("["+c+"]");
		}
		System.out.println();
	}
	
	static int max(int x, int y) {
        return x>y?x:y;
    }
	
	public static void main(String[] args) {
		String word = "interstell";
		String candidate = "interstellar";
		double score = SWSPassageScore(candidate, word);
		System.out.println("Score = "+ score);
	}
}
