package observer;

import models.Game;
import models.Round;
import models.Subquestion;
import models.Turn;

public interface TurnObserver {

	public void onTurnBegin(Game game, Round round, Turn turn);
	public void onTurnEnd(Game game, Round round, Turn turn);
	public void onTimePlayer1Changed(int time);
	public void onTimePlayer2Changed(int time);
	public void onAnswerCorrect(String answer);
	public void onAnswerIncorrect();
	public void onSubquestionBegin(Game game, Round round, Turn turn, Subquestion subquestion);
	public void onSubquestionEnd(Game game, Round round, Turn turn, Subquestion subquestion);
	public void onTimerChanged(int time);
}
