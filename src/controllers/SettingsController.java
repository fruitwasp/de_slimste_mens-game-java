package controllers;

import java.util.ArrayList;

import observer.AccountObserver;

public class SettingsController {

	private MainController mainController;
	private ArrayList<AccountObserver> accountObservers;
	private DatabaseController databaseController;
	
	public SettingsController(MainController mainController) {
		
		this.mainController = mainController;
		this.databaseController = mainController.getDatabaseController();
		this.accountObservers = mainController.getLoginController().getAccountObservers();
	}
	
	/**
	 * 
	 * observer that returns a message if something went wrong
	 * 
	 * @param msg (String)
	 */
	
	private void onPasswordChangeFailed(String msg) {
		for (AccountObserver accountObserver : accountObservers) {
			accountObserver.onLogInFailed(msg);
		}
	}
	
	/**
	 * 
	 * method to change password, entered password/repeatPassword will be lowercase and without whitespace
	 * if password is not equal to repeatPassword / it doesn't have valid characters / password length is < 3:
	 * return
	 * 
	 * else
	 * update database with new password
	 * 
	 * @param username (Account username)
	 * @param password (new password)
	 * @param repeatPassword (repeat new password)
	 */
	
	public void changePassword(String username, String password, String repeatPassword) {
		password = password.toLowerCase().trim();
		repeatPassword = repeatPassword.toLowerCase().trim();

		if (!password.equals(repeatPassword)) {
			onPasswordChangeFailed("De wachtwoorden komen niet overeen!");
			
			return;
		}
		
		if (!validString(password)) {
			onPasswordChangeFailed("Een geldige wachtwoord bestaat uit letters, cijfers en spaties!");

			return;
		}
		
		if(password.length() < 3) {
			onPasswordChangeFailed("Een geldig wachtwoord bevat minimaal 3 tekens!");
			
			return;
		}
		
		databaseController.insertQuery("UPDATE account SET wachtwoord = '"
				+ password + "' WHERE naam = '" + username + "'");
		
		for (AccountObserver accountObserver : accountObservers) {
			accountObserver.onPasswordChanged(password);
		}
	}
	
	private boolean validString(String s) {
		for (char c : s.toCharArray()) {
			if (!Character.isWhitespace(c) && !Character.isLetter(c)
					&& !Character.isDigit(c)) {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * if button is clicked, set the view to settingsPanel
	 */
	
	public void onSettingsButtonClicked() {
		mainController.getMainFrame().setContentView(mainController.getSettingsPanel());
	}
}
