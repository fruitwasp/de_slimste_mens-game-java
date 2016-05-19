package observer;

import java.util.ArrayList;

public interface AccountObserver {
	
	public void onLogInFailed(String error);
	public void onLoggedIn(String username, String password, ArrayList<String> accountTypes);
	public void onLoggedOut();
	public void onRegisterFailed(String error);
	public void onRegisterSucceeded(String msg);
	public void onPasswordChanged(String password);
	public void onPasswordChangeFailed(String error);
}
