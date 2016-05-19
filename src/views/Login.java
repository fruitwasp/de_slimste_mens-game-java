package views;

import helper.CustomButton;
import helper.CustomColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

import observer.AccountObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class Login extends BasePanel implements KeyListener, AccountObserver {
	private JTextField username;
	private JPasswordField password;
	private Dimension textFields, labels, buttons;
	private CustomButton login, register;
	private int spaceBetweenButtons;
	private JLabel error, text, usernameLabel, passwordLabel;
	private Border labelBorder;
	private Insets labelInsets;

	private final static String PHRASE_LOGIN_REGISTER = "Inloggen / Registreren",
			PHRASE_LOGIN = "Inloggen",
			PHRASE_REGISTER = "Registreren",
			PHRASE_NAME = "Naam: ", PHRASE_PASSWORD = "Wachtwoord: ";

	public Login(MainController mainController) {
		super(mainController);

		setLayout(new GridBagLayout());

		text = new JLabel(PHRASE_LOGIN_REGISTER);
		usernameLabel = new JLabel(PHRASE_NAME);
		passwordLabel = new JLabel(PHRASE_PASSWORD);
		error = new JLabel("");
		login = new CustomButton(PHRASE_LOGIN, Color.WHITE, CustomColor.orange);
		register = new CustomButton(PHRASE_REGISTER, Color.WHITE,
				CustomColor.orange);
		username = new JTextField();
		password = new JPasswordField();
		textFields = new Dimension(150, 0);
		labels = new Dimension(300, 30);
		buttons = new Dimension(120, 30);
		labelBorder = BorderFactory.createEmptyBorder(10, 10, 10, 0);
		spaceBetweenButtons = 5;
		labelInsets = new Insets(10, 0, 0, 0);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		text.setHorizontalAlignment(JLabel.CENTER);

		text.setFont(new Font("default", Font.BOLD, 20));

		text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		text.setForeground(Color.white);

		this.add(text, c);

		// Error

		c.gridy = 1;
		c.insets = new Insets(10, 0, 0, 0);

		error.setBackground(Color.white);
		error.setForeground(Color.red);
		error.setFont(new Font("default", Font.BOLD, 12));
		error.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		error.setOpaque(true);
		error.setVisible(false);

		this.add(error, c);

		// Username

		JPanel usernamePanel = new JPanel();

		usernamePanel.setLayout(new BorderLayout());
		usernamePanel.setPreferredSize(labels);
		usernamePanel.setBackground(CustomColor.DARK_RED);

		// Username label

		c.gridy = 2;
		c.insets = labelInsets;
		usernameLabel.setForeground(CustomColor.white);
		usernameLabel.setBorder(labelBorder);
		usernamePanel.add(usernameLabel, BorderLayout.WEST);

		// Username textfield

		username.setPreferredSize(textFields);
		username.setMaximumSize(textFields);

		usernamePanel.add(username, BorderLayout.EAST);

		this.add(usernamePanel, c);

		// Password

		JPanel passwordPanel = new JPanel();

		passwordPanel.setLayout(new BorderLayout());
		passwordPanel.setPreferredSize(labels);
		passwordPanel.setBackground(CustomColor.DARK_RED);

		// Password label

		c.gridy = 3;
		c.insets = labelInsets;
		passwordLabel.setForeground(CustomColor.white);
		passwordLabel.setBorder(labelBorder);

		passwordPanel.add(passwordLabel, BorderLayout.WEST);

		// Password textfield

		password.setPreferredSize(textFields);
		password.setMaximumSize(textFields);

		passwordPanel.add(password, BorderLayout.EAST);

		this.add(passwordPanel, c);

		// Buttons

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new GridBagLayout());

		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, spaceBetweenButtons);

		// Login button

		login.setPreferredSize(buttons);

		buttonPanel.add(login, c);

		// Register button

		register.setPreferredSize(buttons);

		c.gridx = 1;
		c.insets = new Insets(0, 0, 0, 0);

		buttonPanel.add(register, c);
		buttonPanel.setBackground(CustomColor.orange);

		// Setup the layout
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(20, 0, 0, 0);
		c.gridy = 4;
		c.gridx = 0;

		this.add(buttonPanel, c);

		login.addKeyListener(this);
		password.addKeyListener(this);

		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainController.getLoginController().doLogin(username.getText(),
						new String(password.getPassword()));

			}
		});

		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainController.getLoginController().doRegister(
						username.getText(), new String(password.getPassword()));

			}
		});

		mainController.getLoginController().addAccountObserver(this);

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			login.doClick();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void onLogInFailed(String error) {
		password.setText("");
		
		JOptionPane.showMessageDialog(mainController.getMainFrame(), error,
				"Fout", JOptionPane.ERROR_MESSAGE);

	}

	@Override
	public void onLoggedIn(String username, String password,
			ArrayList<String> accountTypes) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoggedOut() {
		username.setText("");
		password.setText("");

	}

	@Override
	public void onRegisterFailed(String error) {

		JOptionPane.showMessageDialog(mainController.getMainFrame(), error,
				"Fout", JOptionPane.ERROR_MESSAGE);

	}

	@Override
	public void onRegisterSucceeded(String msg) {
		JOptionPane.showMessageDialog(mainController.getMainFrame(), msg,
				"Geregisteerd", JOptionPane.INFORMATION_MESSAGE);

	}

	@Override
	public void onPasswordChanged(String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPasswordChangeFailed(String error) {
		// TODO Auto-generated method stub

	}

}
