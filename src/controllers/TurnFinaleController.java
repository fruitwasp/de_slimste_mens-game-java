package controllers;

import helper.LevenshteinDistance;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import views.EndGameView;
import models.Answer;
import models.Attempt;
import models.Game;
import models.Question;
import models.Round;
import models.Synonym;
import models.Turn;

public class TurnFinaleController extends TurnController {
	
	private ArrayList<String> correctAnswers;
	
	public TurnFinaleController(MainController mainController) {
		super(mainController);
	}

	@Override
	public void beginTurn(Game game, Round round, Turn turn) {
		super.beginTurn(game, round, turn);
		
		correctAnswers = new ArrayList<>();
		
		// Fetch question if none yet.
		if (turn.getQuestion() == null) {
			Question question = mainController.getGameController().fetchRandomQuestion("finale", turn.getUsername());
			
			turn.setQuestion(question);
			
			databaseController.insertQuery("UPDATE beurt SET vraag_id = " + question.getId() + " WHERE spel_id = " + game.getId() + " AND rondenaam = 'finale' AND beurt_id = " + turn.getId());
		}		
		
	//	startTimer(game, round, turn);
		
		turnObserver.onTurnBegin(game, round, turn);
	}

	@Override
	public void endTurn(Game game, Round round, Turn turn) {
		super.endTurn(game, round, turn);
		
	//	stopTimer();
	}

	@Override
	public void onAttemptSubmitted(String attempt) {		
		int nextAttemptId = 1;
		Attempt latestAttempt = turn.getLatestAttempt();
		if (latestAttempt != null) {
			nextAttemptId = turn.getLatestAttempt().getId() + 1;
		}		
		
		databaseController.insertQuery(""
			+ "INSERT INTO spelerantwoord (spel_id, rondenaam, beurt_id, antwoord_id, antwoord, moment) "
			+ "VALUES(" + game.getId() + ", 'finale', " + turn.getId() + ", " + nextAttemptId + ", '" + attempt + "', " + getCurrentScore() + ")");
		
		boolean correct = false;
		String answerStr = null;
		
		for (Answer answer : turn.getQuestion().getAnswers()) {
			if (correctAnswers.contains(answer.getAnswer())) {
				continue;
			}			
			
			if (LevenshteinDistance.goodAnswer(attempt, answer.getAnswer())) {				
				answerStr = answer.getAnswer();
				correct = true;				
				break;
			}
			
			for (Synonym synonym : answer.getSynonyms()) {
				if (LevenshteinDistance.goodAnswer(attempt, synonym.getSynonym())) {					
					answerStr = answer.getAnswer();
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
			addDeltaFinale(30);
			
			checkWinner();
			
			correctAnswers.add(answerStr);
			
			turnObserver.onAnswerCorrect(answerStr);
			
			if (correctAnswers.size() == turn.getQuestion().getAnswers().size()) {
				System.out.println("alles goed");
				
				JOptionPane.showMessageDialog(mainController.getMainFrame(), "Je hebt alle antwoorden goed! Wacht op je tegenspeler...");
				
				turn.setStatus("goed");				
				endTurn(game, round, turn);
				return;
			}
			
			JOptionPane.showMessageDialog(mainController.getMainFrame(), "Het ingevoerde antwoord is correct!");
			
			return;
		}		
		
		JOptionPane.showMessageDialog(mainController.getMainFrame(), "Helaas, dat antwoord is incorrect.");
		
		turnObserver.onAnswerIncorrect();
		
		System.out.println("komt niet overeen met een van de ingevulde antwoorden; doe niks");
	}

	@Override
	public void onSkipButtonClicked() {
		turn.setStatus("pas");
		
		endTurn(game, round, turn);
	}
	
	private void makeWinner() {
		stopTimer();
		
		endTurn(game, round, turn);
		
		mainController.getMainFrame().setContentView(new EndGameView(mainController, false));
		
		databaseController.insertQuery("UPDATE spel SET toestand_type = 'afgelopen' WHERE spel_id = " + game.getId());
	}
	
	private void checkWinner() {
		boolean isPlayer1 = turn.getUsername().equals(game.getPlayer1());
		
		if (isPlayer1) {
			if (game.getPlayer2Points() + getDeltaFinale() <= 0) {
				makeWinner();
			}
		}
		else {
			if (game.getPlayer1Points() + getDeltaFinale() <= 0) {
				makeWinner();
			}
		}
		
	}

}
