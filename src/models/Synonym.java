package models;

public class Synonym {
	private Answer answer;
	private String synonym;
	
	public Synonym(Answer answer, String synonym) {
		this.answer = answer;
		this.synonym = synonym;
	}
	
	public Answer getAnswer() {
		return answer;
	}
	
	public String getSynonym() {
		return synonym;
	}

}
