package controllers;

import helper.LevenshteinDistance;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import models.Answer;
import models.Attempt;
import models.Game;
import models.Question;
import models.Round;
import models.Synonym;
import models.Turn;

public class TurnOpenDeurController extends TurnController {
	
	private ArrayList<String> correctAnswers;	
	
	public TurnOpenDeurController(MainController mainController) {
		super(mainController);
	}

	@Override
	public void beginTurn(Game game, Round round, Turn turn) {
		super.beginTurn(game, round, turn);
		
		correctAnswers = new ArrayList<>();
		
		// Fetch question if none yet.
		if (turn.getQuestion() == null) {
			Question question = mainController.getGameController().fetchRandomQuestion("opendeur", turn.getUsername());
			
			turn.setQuestion(question);
			
			databaseController.insertQuery("UPDATE beurt SET vraag_id = " + question.getId() + " WHERE spel_id = " + game.getId() + " AND rondenaam = 'opendeur' AND beurt_id = " + turn.getId());
		}		
		
		startTimer(game, round, turn);
		
		turnObserver.onTurnBegin(game, round, turn);
	}

	@Override
	public void endTurn(Game game, Round round, Turn turn) {
		super.endTurn(game, round, turn);

		stopTimer();
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
			+ "VALUES(" + game.getId() + ", 'opendeur', " + turn.getId() + ", " + nextAttemptId + ", '" + attempt + "', " + getCurrentScore() + ")");
		
		boolean correct = false;
		String answerStr = null;
		
		for (Answer answer : turn.getQuestion().getAnswers()) {			
			if (correctAnswers.contains(answer.getAnswer())) {
				continue;
			}
			
			if (LevenshteinDistance.goodAnswer(attempt, answer.getAnswer())) {				
				correct = true;
				answerStr = answer.getAnswer();
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
			addDelta(40);
			
			correctAnswers.add(answerStr);
			
			turnObserver.onAnswerCorrect(answerStr);
			
			if (correctAnswers.size() == turn.getQuestion().getAnswers().size()) {
				System.out.println("alles goed");
				
				JOptionPane.showMessageDialog(mainController.getMainFrame(), "Je hebt alle antwoorden goed! Wacht op je tegenspeler...");
				
				turn.setStatus("goed");				
				endTurn(game, round, turn);
				return;
			}
			
			JOptionPane.showMessageDialog(mainController.getMainFrame(), "Goedzo, het ingevoerde antwoord is correct!");
			
			return;
		}
		
		JOptionPane.showMessageDialog(mainController.getMainFrame(), "Helaas, het ingevoerde antwoord is incorrect.");
		
		turnObserver.onAnswerIncorrect();
		
		System.out.println("komt niet overeen met een van de ingevulde antwoorden; doe niks");
	}

	@Override
	public void onSkipButtonClicked() {		
		turn.setStatus("pas");
		
		endTurn(game, round, turn);
	}

}
