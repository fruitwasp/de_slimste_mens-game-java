package controllers;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import models.Game;
import models.Round;
import models.Subquestion;
import models.Turn;
import observer.TurnObserver;
import views.EndGameView;
import views.TurnBasePanel;

public class TurnController {
	protected MainController mainController;
	protected DatabaseController databaseController;

	private Timer timer;

	protected Game game;
	protected Round round;
	protected Turn turn;
	protected int points;
	
	private int delta, deltaFinale;
	
	protected TurnObserver turnObserver;
	
	public TurnController(MainController mainController) {
		this.mainController = mainController;
		this.databaseController = mainController.getDatabaseController();
		
		timer = new Timer();
		
		// I've chosen not to place this in the MainFrame itself, because of the need of a few instance variables (game, round, turn)
		// and placing it in the MainFrame would require a getter to check which turn panel is active (or not), and which turn controller belongs to that panel.
		mainController.getMainFrame().addWindowListener(new WindowAdapter() {
			
			@Override
	        public void windowClosing(WindowEvent we) {
	            if (game == null || round == null || turn == null) {
	            	return;
	            }
	            
	            if (turn.getStatus().equals("bezig")) {
	            	turn.setStatus("pas");
	            	
	            	endTurn(game, round, turn);
	            }
	        }

	    });
	}

	public void beginTurn(Game game, Round round, Turn turn) {
		this.game = game;
		this.round = round;
		this.turn = turn;
		this.delta = 0;
		this.deltaFinale = 0;
		
		mainController.getMainFrame().setContentView(getMatchingView(round));
	}

	public void endTurn(Game game, Round round, Turn turn) {		
		databaseController.insertQuery("UPDATE beurt SET beurtstatus = '" + turn.getStatus() + "', sec_verdiend = " + delta + " WHERE spel_id = " + game.getId() + " AND rondenaam = '" + round.getName() + "' AND beurt_id = " + turn.getId());

		if (round.getName().equals("finale")) {
			databaseController.insertQuery("UPDATE beurt SET sec_finale_af = " + deltaFinale + " WHERE spel_id = " + game.getId() + " AND rondenaam = '" + round.getName() + "' AND beurt_id = " + turn.getId());
		}
		
		if (turn.getUsername().equals(game.getPlayer1())) {
			game.addPlayer1Points(delta);
		}
		else {
			game.addPlayer2Points(delta);
		}
		
		turnObserver.onTurnEnd(game, round, turn);
		
		mainController.getMainFrame().setContentView(mainController.getMainPanel());
	}
	
	public void beginSubquestion(Game game, Round round, Turn turn, Subquestion subquestion) {
		// Overwrite this in the extends.
	}
	
	public void endSubquestion(Game game, Round round, Turn turn, Subquestion subquestion) {
		// Overwrite this in the extends.
	}

	public void onAttemptSubmitted(String attempt) {
		// Overwrite this in the extends.
	}

	public void onSkipButtonClicked() {
		// Overwrite this in the extends.
	}

	private void reduceTime(Game game, Round round, Turn turn) {
		delta = delta - 1;
		
		int points;
		if (turn.getUsername().equals(game.getPlayer1())) {
			points = game.getPlayer1Points() + delta;
			
			turnObserver.onTimePlayer1Changed(points);
		}	
		else {
			points = game.getPlayer2Points() + delta;
			
			turnObserver.onTimePlayer2Changed(points);	
		}
		
		if (points <= 0) {
			turn.setStatus("timeout");
			
			stopTimer();
			
			endTurn(game, round, turn);
			
			mainController.getMainFrame().setContentView(new EndGameView(mainController, false));
			
			databaseController.insertQuery("UPDATE spel SET toestand_type = 'afgelopen' WHERE spel_id = " + game.getId());
		}
	}
	
	protected void startTimer(Game game, Round round, Turn turn) {
		points = turn.getUsername().equals(game.getPlayer1()) ? game.getPlayer1Points() : game.getPlayer2Points();		
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				reduceTime(game, round, turn);
			}
		}, 0, 1000);
	}

	protected void stopTimer() {
		timer.cancel();
	}
	
	public void setTurnObserver(TurnObserver turnObserver) {
		this.turnObserver = turnObserver;
	}
	
	protected void addDelta(int delta) {
		this.delta += delta;
		
		if (turn.getUsername().equals(game.getPlayer1())) {
			turnObserver.onTimePlayer1Changed(game.getPlayer1Points() + delta);
		}
		else {
			turnObserver.onTimePlayer2Changed(game.getPlayer2Points() + delta);
		}
	}
	
	protected void addDeltaFinale(int deltaFinale) {
		this.deltaFinale += deltaFinale;
		
		if (turn.getUsername().equals(game.getPlayer1())) {
			turnObserver.onTimePlayer2Changed(game.getPlayer2Points() + delta);
		}
		else {
			turnObserver.onTimePlayer1Changed(game.getPlayer1Points() + delta);
		}
		
	}
	
	protected int getDelta() {
		return delta;
	}
	
	protected int getDeltaFinale() {
		return deltaFinale;
	}
	
	protected int getCurrentScore() {
		if (turn.getUsername().equals(game.getPlayer1())) {
			return game.getPlayer1Points() + delta;
		}
		
		return game.getPlayer2Points() + delta;
	}
	
	protected <T> TurnBasePanel getMatchingView(Round round) {
		String roundName = round.getName();

		return roundName.equals("finale") ? mainController.getTurnFinalePanel()
			: roundName.equals("ingelijst") ? mainController.getTurnIngelijstPanel()
			: roundName.equals("opendeur") ? mainController.getTurnOpenDeurPanel() 
			: roundName.equals("puzzel") ? mainController.getTurnPuzzelPanel() 
			: mainController.getTurnDrieZesNegenPanel();
	}
	
	protected Game getGame() {
		return game;
	}
	
	protected Round getRound() {
		return round;
	}
	
	protected Turn getTurn() {
		return turn;
	}

}
