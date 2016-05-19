package models;

import java.util.ArrayList;

public class Game {	
	private int id;
	private String player1, player2;
	private int player1Points, player2Points;
	private boolean turn;
	private String stateType;
	private int competitionId;
	private String replyType;
	
	private ArrayList<Round> rounds;
	private ArrayList<ChatMessage> chatMessages;
	
	public Game(int id, String player1, String player2, String stateType, int competitionId, String replyType) {		
		this.id = id;
		this.player1 = player1;
		this.player2 = player2;		
		this.stateType = stateType;
		this.competitionId = competitionId;
		this.replyType = replyType;
		
		rounds = new ArrayList<Round>();
		chatMessages = new ArrayList<>();
	}

	public ChatMessage addChatMessage(String message, String username, int timestamp, int millisec) {
		ChatMessage chatMessageObj = new ChatMessage(this, message, username, timestamp, millisec);
		
		chatMessages.add(chatMessageObj);
		
		return chatMessageObj;
	}
	
	public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
		this.chatMessages = chatMessages;
	}
	
	public ArrayList<ChatMessage> getChatMessages() {
		return chatMessages;
	}

	public int getId() {
		return id;
	}

	public String getState() {
		return stateType;
	}
	
	public String getReply() {
		return replyType;
	}

	public String getTurn() {
		return turn ? player1 : player2;
	}

	public String getPlayer1() {
		return player1;
	}

	public String getPlayer2() {
		return player2;
	}
	
	public int getPlayer1Points() {
		return player1Points;
	}
	
	public int getPlayer2Points() {
		return player2Points;
	}
	
	public Round getFirstRound() {
		Round firstRound = null;
		
		for (Round round : rounds) {
			if (firstRound == null) {
				firstRound = round;
				continue;
			}
			
			if (round.getId() >= firstRound.getId()) {
				continue;
			}
			
			firstRound = round;
		}
		
		return firstRound;
	}
	
	public void setPlayer1Points(int player1Points) {
		this.player1Points = player1Points;
	}
	
	public void setPlayer2Points(int player2Points) {
		this.player2Points = player2Points;
	}
	
	public void addPlayer1Points(int player1Points) {
		this.player1Points += player1Points;
	}
	
	public void addPlayer2Points(int player2Points) {
		this.player2Points += player2Points;
	}
	
	public void setStateType(String stateType) {
		this.stateType = stateType;
	}
	
	public void setReplyType(String replyType) {
		this.replyType = replyType;
	}
	
	public ArrayList<Round> getRounds() {
		return rounds;
	}
	
	public void setRounds(ArrayList<Round> rounds) {
		this.rounds = rounds;
	}
	
	public Round getNextRound(Round currentRound) {
		if (currentRound == null) {
			return getLatestRound();
		}
		
		if (rounds.size() == 1) {
			return null;
		}
		
		for (Round round : rounds) {
			if (round.getId() == currentRound.getId() + 1) {
				return round;
			}			
		}
		
		return null;
	}
	
	public Round getPreviousRound(Round currentRound) {
		if (currentRound == null) {
			return getFirstRound();
		}
		
		if (rounds.size() == 1) {
			return null;
		}
		
		for (Round round : rounds) {
			if (round.getId() == currentRound.getId() - 1) {
				return round;
			}			
		}
		
		return null;
	}
	
	public Round getLatestRound() {
		if (rounds.size() == 0) {
			return null;
		}
		
		Round latestRound = null;
		
		for (Round round : rounds) {
			if (latestRound == null || round.getId() > latestRound.getId()) {
				latestRound = round;
			}
		}
		
		return latestRound;
	}
	
	private String getNextRoundName() {
		Round latestRound = getLatestRound();
		if (latestRound == null) {
			return "369";
		}
		
		String latestRoundName = latestRound.getName();
		
		return latestRoundName.equals("369") ? "opendeur" :
			latestRoundName.equals("opendeur") ? "puzzel" :
			latestRoundName.equals("puzzel") ? "ingelijst" :
			latestRoundName.equals("ingelijst") ? "finale" : "finale";			
	}
	
	public Round addRound(String roundName) {
		Round roundObj = new Round(this, roundName);
		
		rounds.add(roundObj);
		
		return roundObj;
	}
	
	public Round doNextRound() {
		String nextRoundName = getNextRoundName();
		
		Round roundObj = addRound(nextRoundName);
		
		return roundObj;
	}
	
	public int getCompetitionId(){
		return competitionId;
	}
}