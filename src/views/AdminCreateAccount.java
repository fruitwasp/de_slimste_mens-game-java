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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Account;
import observer.AdminObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class AdminCreateAccount extends BasePanel implements AdminObserver {

	private JTextField nameField, passwordField;
	private JLabel nameLabel, passwordLabel, typeLabel;
	private JButton createButton;
	private JPanel namePanel, passwordPanel, typePanel, buttonPanel, otherPanel;
	private JCheckBox typeBoxPlayer, typeBoxAdmin, typeBoxObserver;
	private final static Dimension PANELSIZE = new Dimension(266, 266);
	private GridBagConstraints gridBag;

	public AdminCreateAccount(MainController mainController) {
		super(mainController);

		gridBag = new GridBagConstraints();

		otherPanel = new JPanel();
		otherPanel.setOpaque(false);
		otherPanel.setLayout(new GridBagLayout());
		
		createNamePanel();
		createPasswordPanel();
		createTypePanel();
		createButtonPanel();
		
		
		this.add(otherPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		mainController.getAdminController().addAdminObserver(this);
	}

	/**
	 * 
	 */

	private void createNamePanel() {
		namePanel = new JPanel();
		namePanel.setLayout(new GridBagLayout());
		namePanel.setPreferredSize(PANELSIZE);

		
		// gridBag.gridwidth = GridBagConstraints.REMAINDER;
		gridBag.insets = new Insets(5, 0, 5, 0);
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.gridx = 0;
		gridBag.gridy = 0;

		namePanel.setOpaque(false);

		nameLabel = new JLabel("Gebruikersnaam");
		nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
		nameLabel.setForeground(Color.WHITE);

		nameField = new JTextField();
		nameField.setMargin(new Insets(5, 2, 2, 2));

		namePanel.add(nameLabel, gridBag);
		gridBag.gridy = 1;
		namePanel.add(nameField, gridBag);

		
		gridBag.gridx = 0;
		gridBag.gridy = 0;
		otherPanel.add(namePanel, gridBag);
	}

	/**
	 * 
	 */

	private void createPasswordPanel() {
		passwordPanel = new JPanel();
		passwordPanel.setLayout(new GridBagLayout());
		passwordPanel.setPreferredSize(PANELSIZE);

		// gridBag.gridwidth = GridBagConstraints.REMAINDER;
		gridBag.insets = new Insets(5, 0, 5, 0);
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.gridx = 1;
		gridBag.gridy = 0;

		passwordPanel.setOpaque(false);

		passwordLabel = new JLabel("Wachtwoord");
		passwordLabel.setFont(new Font("Arial", Font.BOLD, 24));
		passwordLabel.setForeground(Color.WHITE);

		passwordField = new JTextField();
		passwordField.setMargin(new Insets(5, 2, 2, 2));

		passwordPanel.add(passwordLabel, gridBag);
		gridBag.gridy = 1;
		passwordPanel.add(passwordField, gridBag);

		
		gridBag.gridx = 1;
		gridBag.gridy = 0;
		otherPanel.add(passwordPanel, gridBag);
	}

	/**
	 * 
	 */

	private void createTypePanel() {
		typePanel = new JPanel();
		typePanel.setLayout(new GridBagLayout());
		typePanel.setPreferredSize(PANELSIZE);
		
		ArrayList<JCheckBox> types = new ArrayList<>();

		gridBag.gridwidth = GridBagConstraints.REMAINDER;
		gridBag.insets = new Insets(5, 0, 5, 0);
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.gridx = 2;
		gridBag.gridy = 0;

		typePanel.setOpaque(false);

		typeLabel = new JLabel("Rol");
		typeLabel.setFont(new Font("Arial", Font.BOLD, 24));
		typeLabel.setForeground(Color.WHITE);

		typePanel.add(typeLabel);
		types.add(typeBoxPlayer = new JCheckBox("Speler"));
		types.add(typeBoxAdmin = new JCheckBox("Administrator"));
		types.add(typeBoxObserver = new JCheckBox("Toeschouwer"));
		
		
		for(JCheckBox check : types){
			typePanel.add(check, gridBag);
			gridBag.gridy++;
		}

		
		gridBag.gridx = 2;
		gridBag.gridy = 0;
		otherPanel.add(typePanel, gridBag);
	}
	
	private void createButtonPanel(){
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.setOpaque(false);
		
		GridBagConstraints gridBag = new GridBagConstraints();
		gridBag.gridwidth = GridBagConstraints.REMAINDER;
		gridBag.insets = new Insets(5, 0, 5, 0);
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		
		createButton = new CustomButton("Registreer account", CustomColor.DARK_RED,
				Color.WHITE);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainController.getAdminController().addAccount(
						nameField.getText(), passwordField.getText(),
						typeBoxAdmin.isSelected(), typeBoxPlayer.isSelected(),
						typeBoxObserver.isSelected());
			}
		});
		
		buttonPanel.add(createButton, gridBag);
		
		
	}

	@Override
	public void onPlayerSelected(Account account) {

	}

	@Override
	public void onPlayersLoaded(ArrayList<Account> accounts) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerPasswordChanged(String username, String password) {

	}

	@Override
	public void onPlayerUsernameChanged(String oldUsername, String username) {

	}
}
