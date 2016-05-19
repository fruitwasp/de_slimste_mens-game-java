package views;

import java.util.ArrayList;

import javax.swing.JTextField;

import models.Game;
import models.Round;
import models.Subquestion;
import models.Turn;
import observer.ObserverObserver;
import observer.TurnObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class TurnBasePanel extends BasePanel implements TurnObserver, ObserverObserver {
	protected JTextField answer;
	
	public TurnBasePanel(MainController mainController) {
		super(mainController);
	}
	
	@Override
	public void onTurnEnd(Game game, Round round, Turn turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimePlayer1Changed(int time) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onTimePlayer2Changed(int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnswerCorrect(String answer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnswerIncorrect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnBegin(Game game, Round round, Turn turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGamesLoaded(ArrayList<Game> endedGames) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnLoaded(Game game, Round round, Turn turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAttemptLoaded(String attempt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubQuestionLoaded(Game game, Round round, Turn turn, Subquestion subquestion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubquestionsLoaded(Game game, Round round, Turn turn, ArrayList<Subquestion> subquestions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoundLoaded(Game game, Round round) {		
		// TODO Auto-generated method stub
		
	}
		
	public void onSubquestionEnd(Game game, Round round, Turn turn, Subquestion subquestion) {
	
	}
	
	public void onScoresChanged(int scorePlayer1, int scorePlayer2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubquestionBegin(Game game, Round round, Turn turn, Subquestion subquestion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimerChanged(int time) {
		// TODO Auto-generated method stub
		
	}

}
