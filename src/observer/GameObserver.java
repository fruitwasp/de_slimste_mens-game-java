package observer;

import java.util.ArrayList;

import models.Game;

public interface GameObserver {
	
	public void onGamesLoaded(ArrayList<Game> games);
	public void onGameAdded(Game game);
	public void onGameRemoved(Game game);
	public void onGameStateChanged(Game game);
	public void onGameReplyChanged(Game game);
}
