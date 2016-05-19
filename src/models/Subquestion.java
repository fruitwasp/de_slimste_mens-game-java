package models;

public class Subquestion {

	private Turn turn;	
	
	private int followId;
	private Question question;
	private String attempt;
	
	public Subquestion(Turn turn, int followId, Question question) {
		this.turn = turn;
		this.followId = followId;	
		this.question = question;
	}
	
	public Turn getTurn() {
		return turn;
	}
	
	public int getFollowId() {
		return followId;
	}
	
	public Question getQuestion() {
		return question;
	}
	
	public String getAttempt() {
		return attempt;
	}
	
	public void setQuestion(Question question) {
		this.question = question;
	}

	public void setAttempt(String attempt) {
		this.attempt = attempt;
	}	
}
