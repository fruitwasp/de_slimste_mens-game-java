package observer;

import java.util.ArrayList;

import models.Game;
import models.Round;
import models.Subquestion;
import models.Turn;

public interface ObserverObserver {
	public void onGamesLoaded(ArrayList<Game> endedGames);
	public void onRoundLoaded(Game game, Round round);
	public void onTurnLoaded(Game game, Round round, Turn turn);
	public void onAttemptLoaded(String attempt);
	public void onSubQuestionLoaded(Game game, Round round, Turn turn, Subquestion subquestion);
	public void onSubquestionsLoaded(Game game, Round round, Turn turn, ArrayList<Subquestion> subquestions);
	public void onScoresChanged(int scorePlayer1, int scorePlayer2);
}
