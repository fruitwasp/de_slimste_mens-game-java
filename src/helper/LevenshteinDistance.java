package helper;

public class LevenshteinDistance {
	
	/**
	 * The algorithm that compares the correct answer with the player's answer
	 * 
	 * @param correctAnswer
	 * @param correctAnswerChars
	 * @param playerAnswer
	 * @param playerAnswerChars
	 * @return
	 */
	private static int LevenshteinDistance(String correctAnswer, int correctAnswerChars, String playerAnswer, int playerAnswerChars) {
		/* base case: empty strings */
		if (correctAnswerChars == 0)
			return playerAnswerChars;
		if (playerAnswerChars == 0)
			return correctAnswerChars;

		/* test if last characters of the strings match */
		int cost = correctAnswer.charAt(correctAnswerChars - 1) == playerAnswer.charAt(playerAnswerChars - 1) ? 0 : 1;
		
		/*
		 * return minimum of delete char from s, delete char from t, and delete
		 * char from both
		 */
		
		int one = LevenshteinDistance(correctAnswer, correctAnswerChars - 1, playerAnswer, playerAnswerChars) + 1;
		int two = LevenshteinDistance(correctAnswer, correctAnswerChars, playerAnswer, playerAnswerChars - 1) + 1;
		int three = LevenshteinDistance(correctAnswer, correctAnswerChars - 1, playerAnswer, playerAnswerChars - 1) + cost;
	
		if (two < one) {
			one = two;
		}
		
		if (three < one) {
			one = three;
		}
		
		return one;
		
		/*
		return Math.min(
				LevenshteinDistance(correctAnswer, correctAnswerChars - 1, playerAnswer, playerAnswerChars) + 1, 
					Math.min(
						LevenshteinDistance(correctAnswer, correctAnswerChars, playerAnswer, playerAnswerChars - 1) + 1,
						LevenshteinDistance(correctAnswer, correctAnswerChars - 1, playerAnswer, playerAnswerChars - 1) + cost)
				); */
	}

	/**
	 * Check if the player's answer is correct
	 * 
	 * distance: the difference between the correct answer and the given attempt
	 * twenty: 20% of the length from the correct answer
	 * 
	 * if the distance between 2 answer and attempt is smaller than the twenty the attempt is correct
	 * @return
	 */
	public static boolean goodAnswer(String answer, String attempt) {
		answer = answer.replaceAll("\\s+", "").trim();
		attempt = attempt.replaceAll("\\s+", "").trim();
		
		if (answer.equals(attempt)) {
			return true;
		}
		
		System.out.println(answer);
		System.out.println(attempt);
		
		int distance = LevenshteinDistance(answer, answer.length(), attempt, attempt.length());
		double twenty = Math.floor(answer.length() / 5.0);
				
		/*System.out.println("------------");
		System.out.println("answer length: " +answer.length());
		System.out.println("twenty: " + twenty);
		System.out.println("attempt / answer: " + attempt + " / " + answer);
		System.out.println("Levensthein: " + distance);
		System.out.println("-------------");*/
		
		if (distance > twenty) {
			return false;
		}
		return true;
	}
	
}
