package controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JMenu;

import models.Account;
import observer.AccountObserver;

public class LoginController {
	private MainController mainController;
	private DatabaseController databaseController;
	private JMenu logoutButton;

	private Account account;

	private ArrayList<AccountObserver> accountObservers;

	public LoginController(MainController mainController) {
		this.mainController = mainController;
		this.databaseController = mainController.getDatabaseController();
		
		accountObservers = new ArrayList<AccountObserver>();
		
		this.logoutButton = this.mainController.getMainFrame().getLogoutButton();

		logoutButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				doLogout();
			}

		});

	}

	private void onLoginFailed(String msg) {
		for (AccountObserver accountObserver : accountObservers) {
			accountObserver.onLogInFailed(msg);
		}
	}

	/**
	 * This method handles the user login
	 */
	public void doLogin(String username, String password) {
		username = username.toLowerCase().trim();
		password = password.toLowerCase().trim();

		if (!validString(username)) {
			onLoginFailed("Een geldige gebruikersnaam bestaat uit letters, cijfers en spaties!");

			return;
		}

		if (!validString(password)) {
			onLoginFailed("Een geldig wachtwoord bestaat uit letters, cijfers en spaties!");

			return;
		}

		if (!validCredentials(username, password)) {
			onLoginFailed("De combinatie van gebruikersnaam en wachtwoord is niet geldig.");

			return;
		}

		ResultSet rs = databaseController.selectQuery("SELECT naam, wachtwoord FROM account WHERE lower(naam) = '" + username + "'");

		try {
			rs.first();
			
			username = rs.getString("naam");
			password = rs.getString("wachtwoord");			
		} catch (SQLException e) {
			System.out.println("Er is iets fout gegaan bij het ophalen van de gebuikersnaam.");
		}

		ArrayList<String> accountTypes = new ArrayList<String>();

		rs = databaseController.selectQuery("SELECT rol_type FROM accountrol WHERE account_naam = '" + username + "'");

		try {
			while (rs.next()) {
				accountTypes.add(rs.getString("rol_type"));
			}
		} catch (SQLException e) {
			System.out.println("Er is iets fout gegaan bij het ophalen van de accountrollen van de gebruiker.");
		}

		account = new Account(username, password, accountTypes);

		mainController.getMainFrame().setContentView(mainController.getMainPanel());
		
		for (AccountObserver accountObserver : accountObservers) {
			accountObserver.onLoggedIn(username, password, accountTypes);
		}
		
		mainController.createDatabaseThread().start();
	}

	/**
	 * This method handles the logout button
	 */
	public void doLogout() {
		// Set contentview to the login view. Create a new one to be sure the
		// username and password fields are empty
		this.mainController.getMainFrame().setContentView(
				mainController.getLoginPanel());
		// Set the account to null
		this.account = null;
		this.mainController.getMainFrame().clearViewHistory();
		
		for (AccountObserver accountObserver : accountObservers) {
			accountObserver.onLoggedOut();
		}
		
		this.mainController.stopThread();
	}

	private void onRegisterFailed(String msg) {
		for (AccountObserver accountObserver : accountObservers) {
			accountObserver.onRegisterFailed(msg);
		}
	}

	/**
	 * This function handles the register function
	 */
	public void doRegister(String username, String password) {
		username = username.toLowerCase().trim();
		password = password.toLowerCase().trim();

		if (!validString(username)) {
			onRegisterFailed("Een geldige gebruikersnaam bestaat uit letters, cijfers en spaties! Probeer het opnieuw.");

			return;
		}

		if (!validString(password)) {
			onRegisterFailed("Een geldig wachtwoord bestaat uit letters, cijfers en spaties! Probeer het opnieuw.");

			return;
		}

		if (accountExists(username)) {
			onRegisterFailed("Er bestaat al een account met de ingevoerde gebruikersnaam. Log in of kies een andere gebruikersnaam.");

			return;
		}

		databaseController
				.insertQuery("INSERT INTO account (naam, wachtwoord) VALUES('"
						+ username + "', '" + password + "')");
		databaseController
				.insertQuery("INSERT INTO accountrol (account_naam, rol_type) VALUES('"
						+ username + "', 'speler')");

		for (AccountObserver accountObserver : accountObservers) {
			accountObserver
					.onRegisterSucceeded("Het account is succesvol geregistreerd. Log in om door te gaan.");
		}
	}

	/**
	 * This method checks if an user already exists in the database
	 */
	private boolean accountExists(String username) {
		ResultSet rs = databaseController
				.selectQuery("SELECT COUNT(*) AS totaal FROM account WHERE LOWER(naam) = '"
						+ username.toLowerCase() + "'");

		try {
			rs.first();

			return rs.getInt("totaal") > 0;
		} catch (SQLException e) {
			System.out
					.println("Er is iets mis gegaan bij het controleren van het bestaan van het account.");

			return true;
		}
	}

	/**
	 * This method checks if the credentials are right
	 */
	private boolean validCredentials(String username, String password) {
		ResultSet rs = databaseController
				.selectQuery("SELECT 1 AS count FROM account WHERE LOWER(naam) = '"
						+ username.toLowerCase()
						+ "' AND LOWER(wachtwoord) = '" + password + "'");

		try {
			while (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Er is iets mis gegaan bij het valideren van de logingegevens.");
			
			e.printStackTrace();
		}

		return false;
	}

	private boolean validString(String s) {
		for (char c : s.toCharArray()) {
			if (!Character.isWhitespace(c) && !Character.isLetter(c)
					&& !Character.isDigit(c)) {
				return false;
			}
		}
		
		// < 3 = <accountgegevens> bestaan uit tenminste 3 letters
		if(s.length() < 3) {
			return false;
		}

		return true;
	}

	public Account getAccount() {
		return account;
	}

	public boolean getLoggedIn() {
		return account == null ? false : true;
	}

	public ArrayList<AccountObserver> getAccountObservers() {
		return accountObservers;
	}
	
	public void addAccountObserver(AccountObserver accountObserver) {
		accountObservers.add(accountObserver);
	}
}
