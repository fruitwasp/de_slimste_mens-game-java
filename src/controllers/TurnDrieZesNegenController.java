package controllers;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import helper.LevenshteinDistance;
import models.Answer;
import models.Game;
import models.Question;
import models.Round;
import models.Subquestion;
import models.Synonym;
import models.Turn;

public class TurnDrieZesNegenController extends TurnController {

	private Subquestion subquestion;
	private int timeLeft;
	private Timer timer;
	
	public TurnDrieZesNegenController(MainController mainController) {
		super(mainController);
	}

	@Override
	public void beginTurn(Game game, Round round, Turn turn) {
		super.beginTurn(game, round, turn);
		
		// Fetch question if none yet.
		if (turn.getLatestSubquestion() == null) {			
			
			int followId = calculateFollowId(turn);			
			System.out.println("new followid: " + followId);
			
			Question question = mainController.getGameController().fetchRandomQuestion("369", turn.getUsername());
			
			this.subquestion = turn.addSubquestion(followId, question);
			
			System.out.println(followId);
			
			databaseController.insertQuery(""
					+ "INSERT INTO deelvraag (spel_id, rondenaam, beurt_id, volgnummer, vraag_id) "
					+ "VALUES(" + game.getId() + ", '369', " + turn.getId() + ", " + followId + ", " + question.getId() + ")");
		}
		
		this.subquestion = turn.getLatestSubquestion();
		
		timerStart();
		
		turnObserver.onTurnBegin(game, round, turn);
	}

	@Override
	public void endTurn(Game game, Round round, Turn turn) {
		super.endTurn(game, round, turn);
	}
	
	@Override
	public void beginSubquestion(Game game, Round round, Turn turn, Subquestion subquestion) {
		
		timerStart();
		
		turnObserver.onSubquestionBegin(game, round, turn, subquestion);
	}
	
	@Override
	public void endSubquestion(Game game, Round round, Turn turn, Subquestion subquestion) {
		
		turnObserver.onSubquestionEnd(game, round, turn, subquestion);
	}

	@Override
	public void onAttemptSubmitted(String attempt) {
		databaseController.insertQuery(""
				+ "UPDATE deelvraag "
				+ "SET antwoord = '" + attempt + "' "
				+ "WHERE spel_id = " + game.getId() + " AND rondenaam = '369' AND beurt_id = " + turn.getId() + " AND volgnummer = " + subquestion.getFollowId());
		
		// countdown
		timerStop();
		
		// TODO: validate input
		boolean correct = false;
		
		for (Answer answer : subquestion.getQuestion().getAnswers()) {			
			if (LevenshteinDistance.goodAnswer(attempt, answer.getAnswer())) {
				correct = true;
				break;
			}
			
			for (Synonym synonym : answer.getSynonyms()) {
				if (LevenshteinDistance.goodAnswer(attempt, synonym.getSynonym())) {
					correct = true;
					break;
				}
			}
			
			// A break only breaks out one loop.
			if (correct) {
				break;
			}
		}
		
		if (correct) {
			turnObserver.onAnswerCorrect("");
			
			System.out.println("correct answer given");
			
			// Add score if question number 3, 6 or 9.
			if (subquestion.getFollowId() % 3 == 0) {
				addDelta(20);
			}
			
			if (subquestion.getFollowId() == 9) {
				turn.setStatus("goed");
				
				System.out.println("super goed");
				
				endTurn(game, round, turn);
				addBonusTurns(game, round, turn);
				return;
			}
			
			int followId = subquestion.getFollowId() + 1;
			System.out.println("volgnr: " + followId);
			
			Question question = mainController.getGameController().fetchRandomQuestion("369", turn.getUsername());
			
			this.subquestion = turn.addSubquestion(followId, question);
			
			databaseController.insertQuery(""
					+ "INSERT INTO deelvraag (spel_id, rondenaam, beurt_id, volgnummer, vraag_id) "
					+ "VALUES(" + game.getId() + ", '369', " + turn.getId() + ", " + followId + ", " + question.getId() + ")");
			
			beginSubquestion(game, round, turn, subquestion);
			
			System.out.println("syso");
			
			return;
		}
		
		System.out.println("fout fout fout, niet goed dus.");		
				
		JOptionPane.showMessageDialog(mainController.getMainFrame(), "Helaas, dat antwoord is incorrect.");
		
		turnObserver.onAnswerIncorrect();
		
		turn.setStatus("fout");
		
		endTurn(game, round, turn);
		
		int nextFollowId = calculateFollowId(turn) + 1;
		System.out.println("stop next followid: " + nextFollowId);
		if (nextFollowId >= 9) {
			addBonusTurns(game, round, turn);
		}
	}

	@Override
	public void onSkipButtonClicked() {
		// countdown
		timerStop();
		
		turn.setStatus("pas");
		
		endTurn(game, round, turn);
		
		int nextFollowId = calculateFollowId(turn);
		System.out.println("pas next followid: " + nextFollowId);
		
		if (nextFollowId >= 9) {
			addBonusTurns(game, round, turn);
		}
	}
	
	/**
	 * Calculate the id of the upcoming turn
	 * @param turn
	 * @return
	 */
	private int calculateFollowId(Turn turn) {			
		Turn previousTurn = round.getPreviousTurn(turn);
		
		if (previousTurn == null || previousTurn.getLatestSubquestion() == null) {
			return 1;
		}
		
		Turn previousPreviousTurn = round.getPreviousTurn(previousTurn);
		
		if (previousPreviousTurn == null || previousPreviousTurn.getLatestSubquestion().getFollowId() == previousTurn.getLatestSubquestion().getFollowId()) {
			return previousTurn.getLatestSubquestion().getFollowId() + 1;
		}
		
		return previousTurn.getLatestSubquestion().getFollowId();
	}
	
	/**
	 * Add 2 bonus turns to the round, 1 for each player.
	 * @param game
	 * @param round
	 * @param turn
	 */
	private void addBonusTurns(Game game, Round round, Turn turn) {
		int i = 1;
		
		while (i < 3) {
			String username = i == 1 ? game.getPlayer1() : game.getPlayer2();
			
			System.out.println("naam " + i + ": " + username);
			
			databaseController.insertQuery(""
				+ "INSERT INTO beurt (spel_id, rondenaam, beurt_id, speler, beurtstatus, sec_verdiend)"
				+ "VALUES(" + game.getId() + ", '" + round.getName() + "', " + (turn.getId() + i) + ", '" + username + "', 'bonus', 60)");
		
			i++;
		}
	}
	
	// countdown
	private void timerStart() {
		timeLeft = 30;
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				timerHandle();
			}
		}, 0, 1000);
	}
	
	// countdown
	private void timerStop() {
		timer.cancel();
	}
	
	// countdown
	private void timerHandle() {
		timeLeft -= 1;
		
		if (timeLeft <= 0) {
			timeRanOut();
		}
		
		turnObserver.onTimerChanged(timeLeft);
	}
	
	// countdown time ran out
	private void timeRanOut() {
		timerStop();
		
		JOptionPane.showMessageDialog(mainController.getMainFrame(), "Helaas, je tijd is op.");		
		
		turnObserver.onAnswerIncorrect();
		
		turn.setStatus("fout");
		
		endTurn(game, round, turn);
		
		int nextFollowId = calculateFollowId(turn);
		System.out.println("stop next followid: " + nextFollowId);
		if (nextFollowId >= 9) {
			addBonusTurns(game, round, turn);
		}
		
	}	

}