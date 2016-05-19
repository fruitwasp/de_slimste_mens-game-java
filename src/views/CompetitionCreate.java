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
import javax.swing.JTextField;

import models.Competition;
import observer.CompetitionObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class CompetitionCreate extends BasePanel implements CompetitionObserver {
	
	private JTextField nameField;

	private final static String PHRASE_COMPETITION_CREATE = "Nieuwe competitie aanmaken";
	private final static String PHRASE_CONFIRM_CREATE = "Bevestigen";
	private final static String PHRASE_TEXTFIELD_PLACEHOLDER = "Geef je competitie een unieke naam...";
	
	public CompetitionCreate(MainController mainController) {
		super(mainController);
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gridBag = new GridBagConstraints();
		gridBag.gridwidth = GridBagConstraints.REMAINDER;
		gridBag.insets = new Insets(5, 0, 5, 0);
		
		JLabel titleLabel = new JLabel(PHRASE_COMPETITION_CREATE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(Color.WHITE);
		
		add(titleLabel, gridBag);
		
		nameField = new JTextField(PHRASE_TEXTFIELD_PLACEHOLDER);
		nameField.setMargin(new Insets(5, 2, 2, 2));
		
		add(nameField, gridBag);
		
		JButton confirmButton = new JButton(PHRASE_CONFIRM_CREATE);
		confirmButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainController.getCompetitionController().onCreateCompetitionButtonClicked(nameField.getText());
			}
		});

		add(confirmButton, gridBag);
	}

	@Override
	public void onCompetitionsLoaded(ArrayList<Competition> competitions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCompetitionPlayersLoaded(ArrayList<String> members) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCompetitionAdded(Competition competition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCompetitionRemoved(Competition competition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerAdded(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerJoinFailed(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreateCompetitionFailed(String name) {
		
	}

	@Override
	public void onJoinedCompetitionsLoaded(ArrayList<Competition> joinedCompetitions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMembersLoaded(int competitionId, ArrayList<String> members) {
		// TODO Auto-generated method stub
		
	}

}
