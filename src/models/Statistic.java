package models;

public class Statistic {	
	private int id;
	private String username;
	private int amountPlayed;
	private int amountWon;
	private int amountLost;
	private int averageSecLeft;
	
	public Statistic(int id, String username, int amountPlayed, int amountWon, int amountLost, int averageSecLeft) {
		this.id = id;
		this.username = username;
		this.amountPlayed = amountPlayed;
		this.amountWon = amountWon;
		this.amountLost = amountLost;
		this.averageSecLeft = averageSecLeft;
	}

	public String getUsername() {
		return username;
	}

	public int getAmountPlayed() {
		return amountPlayed;
	}

	public int getAmountWon() {
		return amountWon;
	}

	public int getAmountLost() {
		return amountLost;
	}

	public int getAverageSecLeft() {
		return averageSecLeft;
	}
	
	public int getCompId() {
		return id;
	}

}
