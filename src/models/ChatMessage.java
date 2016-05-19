package models;

public class ChatMessage {
	private Game game;
	private String message, sender;
	private int timestamp, millisec;

	public ChatMessage(Game game, String message, String sender, int timestamp, int millisec) {
		this.game = game;
		this.message = message;
		this.sender = sender;		
		this.timestamp = timestamp;
		this.millisec = millisec;
	}
	
	public Game getGame() {
		return game;
	}
	
	public String getMessage() {
		return message;
	}

	public String getSender() {
		return sender;
	}

	public int getTimestamp() {
		return timestamp;
	}
	
	public int getMillisec() {
		return millisec;
	}
}
