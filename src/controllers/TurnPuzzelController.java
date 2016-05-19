package controllers;

import helper.LevenshteinDistance;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import models.Attempt;
import models.Game;
import models.Question;
import models.Round;
import models.Subquestion;
import models.Turn;

public class TurnPuzzelController extends TurnController {

	private ArrayList<String> correctAnswers;
	
	public TurnPuzzelController(MainController mainController) {
		super(mainController);
	}

	@Override
	public void beginTurn(Game game, Round round, Turn turn) {
		super.beginTurn(game, round, turn);
		
		// This removes the only reference to the previous correctAnswers object - long live the garbage collector
		correctAnswers = new ArrayList<>();
		
		// Fetch questions if none yet.
		if (turn.getSubQuestions().size() != 3) {
			
			for (int i = 1; i < 4; i++) {
				Question question =  mainController.getGameController().fetchRandomQuestion("puzzel", turn.getUsername());
				
				turn.addSubquestion(i, question);
				
				databaseController.insertQuery(""
					+ "INSERT INTO deelvraag (spel_id, rondenaam, beurt_id, volgnummer, vraag_id)"
					+ "VALUES(" + game.getId() + ", 'puzzel', " + turn.getId() + ", " + i + ", " + question.getId() + ")");
			}
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
		
		for (Subquestion subquestion : turn.getSubQuestions()) {
			Question question = subquestion.getQuestion();	
			
			if (correctAnswers.contains(question.getQuestion())) {
				System.out.println("contains");
				continue;
			}
			
			if (LevenshteinDistance.goodAnswer(attempt, question.getQuestion())) {
				correct = true;
				answerStr = question.getQuestion();
				break;
			}

		}
		
		if (correct) {
			addDelta(60);
			
			correctAnswers.add(answerStr);
			
			if (correctAnswers.size() == turn.getSubQuestions().size()) {
				System.out.println("alles goed");
				
				JOptionPane.showMessageDialog(mainController.getMainFrame(), "Je hebt alle antwoorden goed! Wacht op je tegenspeler...");
				
				turn.setStatus("goed");				
				endTurn(game, round, turn);
				return;
			}
			
			turnObserver.onAnswerCorrect(answerStr);
			
			JOptionPane.showMessageDialog(mainController.getMainFrame(), "Het ingevoerde antwoord is correct!");
			
			return;
		}
		
		JOptionPane.showMessageDialog(mainController.getMainFrame(), "Helaas, dat antwoord is incorrect.");
		
		turnObserver.onAnswerIncorrect();
		
		System.out.println("wtf doe je?! dit is alles behalve goed... sukkel.");
	}

	@Override
	public void onSkipButtonClicked() {
		turn.setStatus("pas");
		
		endTurn(game, round, turn);
	}

}