package be.sioxox.utils;

public class SentenceSimilarity {

	double stringSimilarityOfStrings = 0.0;
	
	// On compare la taille des Strings
	public static int computeEditDistance(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		int[] costs = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0) {
					costs[j] = j;
				} else {
					if (j > 0) {
						int newValue = costs[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						}
						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if (i > 0) {
				costs[s2.length()] = lastValue;
			}
		}
		return costs[s2.length()];
	}
	
	// On récupère le pourcentage de ressemblance des Strings
	public double getPercentage() {
		return stringSimilarityOfStrings * 100;
	}
	
	// On estime le pourcentage de ressemblance des Strings
	public double sentenceSimilarity(String s1, String s2) {
		int editDistance = 0;
		if (s1.length() < s2.length()) {
			String swap = s1;
			s1 = s2;
			s2 = swap;
		}
		int bigLen = s1.length();
		editDistance = computeEditDistance(s1, s2);
		if (bigLen == 0) {
			stringSimilarityOfStrings = 1.0;
		} else {
			stringSimilarityOfStrings = (bigLen - editDistance) / (double) bigLen;
		}

		return stringSimilarityOfStrings * 100;
	}
}
