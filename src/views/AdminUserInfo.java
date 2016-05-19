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
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Account;
import observer.AdminObserver;
import controllers.AdminController;
import controllers.MainController;

@SuppressWarnings("serial")
public class AdminUserInfo extends BasePanel implements AdminObserver {

	private JTextField nameField, passwordField;
	private JLabel nameLabel, passwordLabel, typeLabel;
	private String oldusername;
	private CustomButton nameButton, passwordButton, typeButton;
	private JPanel namePanel, passwordPanel, typePanel;
	private JCheckBox typeBoxPlayer, typeBoxAdmin, typeBoxObserver;
	private final static Dimension PANELSIZE = new Dimension(266, 266);

	private AdminController adminController;

	public AdminUserInfo(MainController mainController) {
		super(mainController);

		this.adminController = mainController.getAdminController();

		createNamePanel();
		createPasswordPanel();
		createTypePanel();

		mainController.getAdminController().addAdminObserver(this);
	}

	/**
	 * Creates a panel which contains: - a label with the text "gebruikersnaam"
	 * - a textarea with the current username of the selected player (here you
	 * can type a new username) - a button which can be clicked to change the
	 * current username to the new username typed in the textarea
	 */

	public void createNamePanel() {
		namePanel = new JPanel();
		namePanel.setLayout(new GridBagLayout());
		namePanel.setPreferredSize(PANELSIZE);

		GridBagConstraints gridBag = new GridBagConstraints();
		gridBag.gridwidth = GridBagConstraints.REMAINDER;
		gridBag.insets = new Insets(5, 0, 5, 0);
		gridBag.fill = GridBagConstraints.HORIZONTAL;

		namePanel.setOpaque(false);

		nameLabel = new JLabel("Gebruikersnaam");
		nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
		nameLabel.setForeground(Color.WHITE);
		
		nameField = new JTextField();
		nameField.setMargin(new Insets(5, 2, 2, 2));
		nameField.setEditable(false);

		oldusername = "";

		nameButton = new CustomButton("Verander", CustomColor.DARK_RED,
				Color.WHITE);
		nameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			/*	mainController.getAdminController().changeUsername(oldusername,
						nameField.getText(), oldpassword);
				checkForOwn(); */ 
				
				// Commented out because the customer restricted database write access; see AdminController
			}
		});
		nameButton.setVisible(false);

		namePanel.add(nameLabel, gridBag);
		namePanel.add(nameField, gridBag);
		namePanel.add(nameButton, gridBag);

		this.add(namePanel, BorderLayout.WEST);
	}

	/**
	 * Creates a panel which contains: - a label with the text "wachtwoord" - a
	 * textarea with the current password of the selected player (here you can
	 * type a new password) - a button which can be clicked to change the
	 * current password to the new password typed in the textarea
	 */

	public void createPasswordPanel() {
		passwordPanel = new JPanel();
		passwordPanel.setLayout(new GridBagLayout());
		passwordPanel.setPreferredSize(PANELSIZE);

		GridBagConstraints gridBag = new GridBagConstraints();
		gridBag.gridwidth = GridBagConstraints.REMAINDER;
		gridBag.insets = new Insets(5, 0, 5, 0);
		gridBag.fill = GridBagConstraints.HORIZONTAL;

		passwordPanel.setOpaque(false);

		passwordLabel = new JLabel("Wachtwoord");
		passwordLabel.setFont(new Font("Arial", Font.BOLD, 24));
		passwordLabel.setForeground(Color.WHITE);

		passwordField = new JTextField("ikbenkoning");
		passwordField.setMargin(new Insets(5, 2, 2, 2));

		passwordButton = new CustomButton("Verander", CustomColor.DARK_RED,
				Color.WHITE);
		passwordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainController.getAdminController().changePassword(
						nameField.getText(), passwordField.getText());
			}
		});

		passwordPanel.add(passwordLabel, gridBag);
		passwordPanel.add(passwordField, gridBag);
		passwordPanel.add(passwordButton, gridBag);

		this.add(passwordPanel, BorderLayout.CENTER);
	}

	/**
	 * Creates a panel which contains: - a label with the text "account type" -
	 * 3 radiobuttons with the text "administrator", "speler" and "beschouwer".
	 * the radiobuttons are checked if the selected account mathes the account
	 * types - a button which can be clicked to add or remove an account type
	 * from the account where the checkboxes are selected or deselected
	 */

	public void createTypePanel() {
		typePanel = new JPanel();
		typePanel.setLayout(new GridBagLayout());
		typePanel.setPreferredSize(PANELSIZE);

		GridBagConstraints gridBag = new GridBagConstraints();
		gridBag.gridwidth = GridBagConstraints.REMAINDER;
		gridBag.insets = new Insets(5, 0, 5, 0);
		gridBag.fill = GridBagConstraints.HORIZONTAL;

		typePanel.setOpaque(false);

		typeLabel = new JLabel("Rol");
		typeLabel.setFont(new Font("Arial", Font.BOLD, 24));
		typeLabel.setForeground(Color.WHITE);

		typeBoxPlayer = new JCheckBox("Speler");
		typeBoxAdmin = new JCheckBox("Administrator");
		typeBoxObserver = new JCheckBox("Toeschouwer");

		/*
		 * typeField = new JTextField("Speler"); typeField.setMargin(new
		 * Insets(5, 2, 2, 2));
		 */

		typeButton = new CustomButton("Verander", CustomColor.DARK_RED,
				Color.WHITE);
		typeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adminController.onChangeUserAccountTypesButtonClicked(
						nameField.getText(), typeBoxPlayer.isSelected(),
						typeBoxAdmin.isSelected(), typeBoxObserver.isSelected());

				showMessageDialog("De account types zijn aangepast");
				checkForOwn();
			}
		});

		typePanel.add(typeLabel, gridBag);
		typePanel.add(typeBoxPlayer, gridBag);
		typePanel.add(typeBoxAdmin, gridBag);
		typePanel.add(typeBoxObserver, gridBag);
		// typePanel.add(typeField, gridBag);
		typePanel.add(typeButton, gridBag);

		this.add(typePanel, BorderLayout.EAST);
	}

	@Override
	public void onPlayerSelected(Account account) {
		nameField.setText(account.getUsername());
		oldusername = account.getUsername();
		passwordField.setText(account.getPassword());
		
		for (String accountType : account.getAccountTypes()) {
			if (accountType.equals("administrator")) {
				typeBoxAdmin.setSelected(true);
			}

			if (accountType.equals("speler")) {
				typeBoxPlayer.setSelected(true);
			}

			if (accountType.equals("toeschouwer")) {
				typeBoxObserver.setSelected(true);
			}
		}

	}

	@Override
	public void onPlayersLoaded(ArrayList<Account> accounts) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerPasswordChanged(String username, String password) {
		passwordField.setText(password);

		showMessageDialog("Het wachtwoord is veranderd");
	}

	@Override
	public void onPlayerUsernameChanged(String oldUsername, String username) {
		nameField.setText(username);

		showMessageDialog("De gebruikersnaam is veranderd");
	}

	public void checkForOwn() {
		System.out.println(oldusername);
		System.out.println(mainController.getLoginController().getAccount()
				.getUsername());
		if (oldusername.equals(mainController.getLoginController().getAccount()
				.getUsername())) {
			showMessageDialog("Wegens veranderingen aan het account moet je opnieuw inloggen");
			mainController.getLoginController().doLogout();
		}
	}
}
