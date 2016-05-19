package models;

import java.util.ArrayList;

public class Answer {
	private Question question;
	private String answer;
	private ArrayList<Synonym> synonyms;
	private boolean correct;

	public Answer(Question question, String answer) {
		this.question = question;
		this.answer = answer;

		synonyms = new ArrayList<Synonym>();
	}

	public Synonym addSynonym(String synonym) {
		Synonym synonymObj = new Synonym(this, synonym);

		synonyms.add(synonymObj);

		return synonymObj;
	}

	public Question getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}

	public ArrayList<Synonym> getSynonyms() {
		return synonyms;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
	
}
