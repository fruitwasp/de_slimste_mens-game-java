package models;

import java.util.ArrayList;

public class Account {
	private String username;
	private String password;
	private ArrayList<String> accountTypes;

	public Account(String username, String password, ArrayList<String> accountTypes) {
		this.username = username;
		this.password = password;
		this.accountTypes = accountTypes;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public ArrayList<String> getAccountTypes() {
		return accountTypes;
	}

	public void addAccountType(String accountType) {
		accountTypes.add(accountType);
	}

	public void resetAccountType() {
		accountTypes.clear();
	}
}
