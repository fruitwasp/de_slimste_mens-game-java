package models;

import java.util.ArrayList;

public class Question {
	private int id;
	private String question;
	private boolean correct;

	private ArrayList<Answer> answers;
	
	public Question(int id, String question) {
		this.id = id;
		this.question = question;
		
		answers = new ArrayList<Answer>();
	}
	
	public int getId() {
		return id;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public ArrayList<Answer> getAnswers() {
		return answers;
	}
	
	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public Answer addAnswer(String answer) {
		Answer answerObj = new Answer(this, answer);
		
		answers.add(answerObj);
		
		return answerObj;
	}
	
	public boolean getAllAnswersCorrect() {
		for (Answer answer : answers) {
			if (!answer.isCorrect()) {
				return false;
			}
		}
		
		return true;
	}	
	
}
