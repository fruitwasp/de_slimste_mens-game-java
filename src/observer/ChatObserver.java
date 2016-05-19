package observer;

import java.util.ArrayList;

import models.ChatMessage;
import models.Game;

public interface ChatObserver {
	
	public void onChatMessageAdded(String message, String sender, Game game);
	public void onChatMessagesLoaded(ArrayList<ChatMessage> chatMessages);
	public void onChatView(Game game);
}
