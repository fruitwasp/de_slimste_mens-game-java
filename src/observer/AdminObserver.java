package observer;

import java.util.ArrayList;

import models.Account;

public interface AdminObserver {
	public void onPlayersLoaded(ArrayList<Account> accounts);
	public void onPlayerSelected(Account account);
	public void onPlayerPasswordChanged(String username, String password);
	public void onPlayerUsernameChanged(String oldUsername, String username);
}
