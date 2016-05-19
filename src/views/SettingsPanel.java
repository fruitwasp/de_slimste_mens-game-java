package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import observer.AccountObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class SettingsPanel extends BasePanel implements AccountObserver {
	private final static String PHRASE_SETTINGS = "Instellingen", PHRASE_USERNAME = "Gebruikersnaam:", PHRASE_PASSWORD = "Wachtwoord:", PHRASE_PASSWORD_REPEAT = "Herhaal wachtwoord:", PHRASE_EDIT = "Wijzigen";
	
	private JTextField usernameField;
	private JPasswordField passwordField, repeatPasswordField;
	
	/**
	 * Set a gridbaglayout
	 * make panels and fill them with labels
	 * make a button to confirm the new password
	 * @param mainController
	 */
	public SettingsPanel(MainController mainController) {		
		super(mainController);
		
		setLayout(new GridBagLayout());		
		
		GridBagConstraints c = new GridBagConstraints();	
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		
		JLabel titleLabel = new JLabel(PHRASE_SETTINGS);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(Color.WHITE);
		add(titleLabel, c);
		
		JLabel usernameLabel = new JLabel(PHRASE_USERNAME);
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		usernameLabel.setForeground(Color.WHITE);
		add(usernameLabel, c);
		
		usernameField = new JTextField();
		usernameField.setMargin(new Insets(5, 2, 5, 2));
		usernameField.setEditable(false);
		add(usernameField, c);
		
		JLabel passwordLabel = new JLabel(PHRASE_PASSWORD);
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		passwordLabel.setForeground(Color.WHITE);
		add(passwordLabel, c);
		
		passwordField = new JPasswordField();
		passwordField.setMargin(new Insets(5, 2, 5, 2));		
		add(passwordField, c);
		
		JLabel repeatPasswordLabel = new JLabel(PHRASE_PASSWORD_REPEAT);
		repeatPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		repeatPasswordLabel.setForeground(Color.WHITE);
		add(repeatPasswordLabel, c);
		
		repeatPasswordField = new JPasswordField();
		repeatPasswordField.setMargin(new Insets(5, 2, 5, 2));
		add(repeatPasswordField, c);
		
		JButton confirmButton = new JButton(PHRASE_EDIT);
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					mainController.getSettingsController().changePassword(usernameField.getText(),
							new String(passwordField.getPassword()), new String(repeatPasswordField.getPassword()));
			}
		});

		add(confirmButton, c);
		
		mainController.getLoginController().addAccountObserver(this);
	}

	@Override
	public void onLogInFailed(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoggedIn(String username, String password, ArrayList<String> accountTypes) {
		
		
		this.passwordField.setText(password);		
		this.usernameField.setText(username);		
	}

	@Override
	public void onLoggedOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegisterFailed(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegisterSucceeded(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPasswordChanged(String password) {		
		passwordField.setText(password);
		repeatPasswordField.setText("");
		
		showMessageDialog("Het wachtwoord is succesvol gewijzigd.");
	}

	@Override
	public void onPasswordChangeFailed(String error) {
		passwordField.setText("");
		repeatPasswordField.setText("");
		
	}

}
