package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import models.Account;
import observer.AdminObserver;
import views.AdminCreateAccount;
import views.AdminUserInfo;

public class AdminController {

	private MainController mainController;
	private DatabaseController databaseController;
	private String selectedName;
	private ArrayList<AdminObserver> adminObservers;

	private ArrayList<Account> accounts;

	public AdminController(MainController mainController) {
		adminObservers = new ArrayList<AdminObserver>();

		this.mainController = mainController;
		this.databaseController = mainController.getDatabaseController();

		accounts = new ArrayList<Account>();

	}

	/**
	 * method to check if the accountType equals admin
	 * 
	 * @return true if this is the case
	 */

	private boolean canDo() {
		for (String accountType : mainController.getLoginController()
				.getAccount().getAccountTypes()) {
			if (!accountType.equals("administrator")) {
				continue;
			}

			return true;
		}

		return false;
	}

	/**
	 * 
	 * allows the admin to set accountTypes for players
	 * 
	 * @param username
	 *            (username of the player)
	 * @param accountType
	 *            (accountType of the player)
	 */

	public void addAccountType(String username, String accountType) {
		if (!canDo()) {
			return;
		}

		databaseController
				.insertQuery("INSERT INTO accountrol (rol_type, account_naam) values ('"
						+ accountType + "', '" + username + "')");

		Account account = this.getAccountByUsername(username);
		account.addAccountType(accountType);

	}

	/**
	 * 
	 * allows the admin to remove accountTypes for players
	 * 
	 * @param username
	 * @param accountType
	 */

	public void removeAccountType(String username, String accountType) {
		if (!canDo()) {
			return;
		}

		databaseController
				.insertQuery("DELETE FROM accountrol WHERE rol_type = '"
						+ accountType + "' AND account_naam = '" + username
						+ "'");

	}

	/**
	 * 
	 * allows the admin to register himself as admin
	 * 
	 * @param username
	 * @param password
	 */

	public void adminRegister(String username, String password) {
		if (!canDo()) {
			return;
		}

		mainController.getLoginController().doRegister(username, password);
	}
	
	/**
	 * allows the admin to change usernames for accounts. goes through 2 checks:
	 * 
	 * if you are a "admin" else return if the new username has valid characters
	 * else return
	 * 
	 * @param oldUsername
	 * @param username
	 * @param oldPassword
	 */

	// Commented out because the customer restricted our database write access
	
/*	public void changeUsername(String oldUsername, String username,
			String oldPassword) {
		if (!canDo()) {
			return;
		}

		if (!validString(username)) {
			return;
		}
		
		if (accountExists(username)) {
			return;
		}

		databaseController
				.insertQuery("INSERT INTO account (naam, wachtwoord) VALUES('"
						+ username + "', '" + oldPassword + "')");
		databaseController.insertQuery("UPDATE accountrol SET account_naam = '"
				+ username + "' WHERE account_naam = '" + oldUsername + "'");
		databaseController.insertQuery("UPDATE deelnemer SET account_naam = '"
				+ username + "' WHERE account_naam = '" + oldUsername + "'");
		databaseController
				.insertQuery("UPDATE competitie SET account_naam_eigenaar = '"
						+ username + "' WHERE account_naam_eigenaar = '"
						+ oldUsername + "'");
		databaseController
				.insertQuery("UPDATE chatregel SET account_naam_zender = '"
						+ username + "' WHERE account_naam_zender = '"
						+ oldUsername + "'");
		databaseController.insertQuery("UPDATE spel SET speler1 = '" + username
				+ "' WHERE speler1 = '" + oldUsername + "'");
		databaseController.insertQuery("UPDATE spel SET speler2 = '" + username
				+ "' WHERE speler2 = '" + oldUsername + "'");
		databaseController.insertQuery("UPDATE beurt SET speler = '" + username
				+ "' WHERE speler = '" + oldUsername + "'");
		databaseController.insertQuery("DELETE from account WHERE naam = '"
				+ oldUsername + "'");

		for (AdminObserver adminObserver : adminObservers) {
			adminObserver.onPlayerUsernameChanged(oldUsername, username);
		}
	} */

	public void changePassword(String username, String password) {
		if (!canDo()) {
			return;
		}

		if (!validString(password)) {
			return;
		}

		databaseController.insertQuery("UPDATE account SET wachtwoord = '"
				+ password + "' WHERE naam = '" + username + "'");

		for (AdminObserver adminObserver : adminObservers) {
			adminObserver.onPlayerPasswordChanged(username, password);
		}
	}

	/**
	 * 
	 * check for valid characters for username/password registers & changes if
	 * the character is NOT a white space, letter or digit, return false
	 * 
	 * @param s
	 *            (String)
	 * @return
	 */

	private boolean validString(String s) {
		for (char c : s.toCharArray()) {
			if (!Character.isWhitespace(c) && !Character.isLetter(c)
					&& !Character.isDigit(c)) {
				return false;
			}
		}
		
		if(s.length() < 3) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 * gets the accounttypes for a specific account
	 * 
	 * @param username
	 *            (the name u want to get the accountTypes from)
	 * @return (NULL if it doesn't have any accountTypes yet)
	 */

	private ArrayList<String> fetchAccountTypes(String username) {
		ArrayList<String> accountTypes = new ArrayList<>();

		ResultSet rs = databaseController
				.selectQuery("SELECT rol_type FROM accountrol WHERE account_naam = '"
						+ username + "'");

		try {
			while (rs.next()) {
				accountTypes.add(rs.getString("rol_type"));
			}

			return accountTypes;
		} catch (Exception e) {
			System.out
					.println("Er is iets fout gegaan bij het ophalen van de accountrollen voor het geselecteerde account.");
		}

		return null;
	}

	public void fetchAccounts() {
		accounts.clear();
		ResultSet rs = databaseController
				.selectQuery("SELECT naam, wachtwoord FROM account");

		try {
			while (rs.next()) {
				String username = rs.getString("naam");

				Account account = new Account(username,
						rs.getString("wachtwoord"), fetchAccountTypes(username));

				accounts.add(account);
			}
		} catch (Exception e) {
			System.out
					.println("Er is iets fout gegaan bij het ophalen van de accounts.");
		}

		for (AdminObserver observer : adminObservers) {
			observer.onPlayersLoaded(accounts);
		}

	}

	private Account getAccountByUsername(String username) {
		for (Account account : accounts) {
			if (account.getUsername().equals(username)) {
				return account;
			}

		}

		return null;
	}

	/**
	 * 
	 * gets the info from a specific account if account == null, return else set
	 * the contentView on the adminPanel
	 * 
	 * @param username
	 */

	public void onUserRowClicked(String username) {
		Account account = getAccountByUsername(username);
		if (account == null) {
			return;
		}

		AdminUserInfo adminUserInfoPanel = (AdminUserInfo) mainController
				.getAdminUserInfoPanel();
		adminUserInfoPanel.onPlayerSelected(account);

		mainController.getMainFrame().setContentView(adminUserInfoPanel);
	}

	public void onChangeUserAccountTypesButtonClicked(String username,
			boolean isPlayer, boolean isAdmin, boolean isObserver) {

		Account account = this.getAccountByUsername(username);

		for (String accountType : account.getAccountTypes()) {
			this.removeAccountType(username, accountType);
		}

		account.resetAccountType();

		if (isPlayer) {
			this.addAccountType(username, "speler");

		}
		if (isAdmin) {
			this.addAccountType(username, "administrator");
		}
		if (isObserver) {
			this.addAccountType(username, "toeschouwer");
		}

	}
	
	private void onRegisterFailed(String msg) {
		JOptionPane.showMessageDialog(mainController.getMainFrame(),
				msg, "Registreer fout", JOptionPane.ERROR_MESSAGE);
	}
	
	public void addAccount(String username, String password, boolean isAdmin, boolean isPlayer, boolean isObserver){
		username = username.toLowerCase().trim();
		password = password.toLowerCase().trim();

		if (!canDo()) {
				return;
			}
		
		if (!validString(username)) {
			onRegisterFailed("Een geldige gebruikersnaam bestaat uit letters, cijfers en spaties! Probeer het opnieuw.");

			return;
		}

		if (!validString(password)) {
			onRegisterFailed("Een geldig wachtwoord bestaat uit letters, cijfers en spaties! Probeer het opnieuw.");

			return;
		}

		if (accountExists(username)) {
			onRegisterFailed("Er bestaat al een account met de ingevoerde gebruikersnaam. Kies een andere gebruikersnaam.");

			return;
		}
		
		databaseController
				.insertQuery("INSERT INTO account (naam, wachtwoord) values ('"
						+ username + "', '" + password + "')");
		
		if(isAdmin){
			databaseController
			.insertQuery("INSERT INTO accountrol (rol_type, account_naam) values ('administrator', '" + username + "')");
		}
		
		if(isPlayer){
			databaseController
			.insertQuery("INSERT INTO accountrol (rol_type, account_naam) values ('speler', '" + username + "')");
		}
		
		if(isObserver){
			databaseController
			.insertQuery("INSERT INTO accountrol (rol_type, account_naam) values ('toeschouwer', '" + username + "')");
		}
		
	}
	
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
	
	public void goToCreatePage(){
		AdminCreateAccount adminCreateAccountPanel = (AdminCreateAccount) mainController
				.getAdminCreateAccountPanel();
		mainController.getMainFrame().setContentView(adminCreateAccountPanel);
	}

	public void setSelectedName(String name) {
		selectedName = name;
	}

	public String getSelectedName() {
		return selectedName;
	}

	public void addAdminObserver(AdminObserver observer) {
		this.adminObservers.add(observer);
	}
}
