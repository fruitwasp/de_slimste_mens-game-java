package models;

import java.util.ArrayList;
import java.util.HashMap;

public class Round {
	private Game game;
	
	private String name;
	private ArrayList<Turn> turns;
	
	private static final HashMap<String, Integer> roundIds = new HashMap<>();
	static {		
		roundIds.put("369", 1);
		roundIds.put("opendeur", 2);
		roundIds.put("puzzel", 3);
		roundIds.put("ingelijst", 4);
		roundIds.put("finale", 5);
	}
	
	public Round(Game game, String name) {
		this.game = game;
		this.name = name;
		
		turns = new ArrayList<Turn>();
	}

	public int getId() {
		return roundIds.get(name);
	}
	
	public Game getGame() {
		return game;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Turn> getTurns() {
		return turns;
	}
	
	public Turn getFirstTurn() {
		Turn firstTurn = null;
		
		for (Turn turn : turns) {
			if (firstTurn == null) {
				firstTurn = turn;
				continue;
			}
			
			if (turn.getId() >= firstTurn.getId()) {
				continue;
			}
			
			firstTurn = turn;
		}
		
		return firstTurn;
	}
	
	public void setTurns(ArrayList<Turn> turns) {
		this.turns = turns;
	}
	
	public Turn addTurn(int turnId, String turnStatus, String username) {
		Turn turnObj = new Turn(this, turnId, turnStatus, username);
		
		turns.add(turnObj);
		
		return turnObj;
	}
	
	public Turn getLatestTurn() {
		if (turns.size() == 0) {
			return null;
		}
		
		Turn latestTurn = null;
		
		for (Turn turn : turns) {
			if (latestTurn == null || turn.getId() > latestTurn.getId()) {
				latestTurn = turn;
			}
		}
		
		return latestTurn;
	}
	
	public Turn getPreviousTurn(Turn turn) {
		if (turn == null) {
			return getLatestTurn();
		}
		
		if (turns.size() == 1) {
			return null;
		}
		
		for (Turn previousTurn : turns) {
			if (previousTurn.getId() == turn.getId() - 1) {
				return previousTurn;
			}			
		}
		
		return null;
	}
	
	public Turn getNextTurn(Turn turn) {
		if (turn == null) {
			return getFirstTurn();
		}
		
		if (turns.size() == turn.getId()) {
			return null;
		}
		
		for (Turn nextTurn : turns) {
			if (nextTurn.getId() == turn.getId() + 1) {
				return nextTurn;
			}
		}
		
		return null;
	}
	
	public Turn getTurnById(int turnId) {
		for (Turn turn : turns) {
			if (turnId != turn.getId()) { continue; }
			
			return turn;
		}
		
		return null;
	}
	
	public int getTotalUsedQuestions() {
		int count = 0;
		
		int previousQuestionId = -1;
		
		for (Turn turn : turns) {			
			if (turn.getQuestion().getId() == previousQuestionId) {
				continue;
			}
			
			previousQuestionId = turn.getQuestion().getId();			
			count++;
		}
		
		return count;
	}
	
	private int getTotalTurnsGoed() {
		int total = 0;
		
		for (Turn turn : turns) {
			if (turn.getStatus().equals("goed")) {
				total++;
			}
		}
			
		return total;
	}
	
	public boolean getEndedPuzzel() {
		int turnsGoed = getTotalTurnsGoed();
		int turnsTotal = turns.size();
		
		return turnsTotal == 2 && turnsGoed == 2 ||
			turnsTotal == 4 ||
			turnsTotal == 3 && turnsGoed >= 1; // klein foutje
	}
	
	public int getTotalUsedQuestions369() {
		int count = 0;
		
		for (Turn turn : turns) {
			int previousQuestionId = 0;
			
			for (Subquestion subQuestion : turn.getSubQuestions()) {
				if (subQuestion == null || subQuestion.getQuestion() == null) {
					continue;
				}
				
				if (previousQuestionId == subQuestion.getQuestion().getId()) {
					continue;
				}	
				
				previousQuestionId = subQuestion.getQuestion().getId();
				count++;
			}			
			
		}
		
		return count;
	}

}
