package models;

public class Attempt {
	
	private Turn turn;
	private int id;
	private String attempt;
	private int moment;
	
	public Attempt(Turn turn, int id, String attempt, int moment) {
		this.turn = turn;
		this.id = id;
		this.attempt = attempt;
		this.moment = moment;
	}

	public Turn getTurn() {
		return turn;
	}

	public int getId() {
		return id;
	}

	public String getAttempt() {
		return attempt;
	}
	
	public int getMoment() {
		return moment;
	}
	
}
