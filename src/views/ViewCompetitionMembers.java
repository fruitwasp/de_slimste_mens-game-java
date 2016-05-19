package views;

import helper.CustomButton;
import helper.CustomColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import models.Competition;
import observer.CompetitionObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class ViewCompetitionMembers extends BasePanel implements CompetitionObserver {
	
	private final static Object[] COLUMN_NAMES = {"Naam"};
	private final static String PHRASE_GAME_INVITE = "Uitnodigen voor spel";
	
	private JPanel tablePanel;
	private JPanel buttonPanel;
	private JTable membersTable;
	private JButton inviteButton;
	private JScrollPane membersScroller;
	private DefaultTableModel membersModel = new DefaultTableModel();	
	private int competitionId;
	
	/**
	 * Constructor ViewCompetitionMembers
	 * the create methods of the panels and tables will be called. 
	 *
	 * @param mainController
	 */
	public ViewCompetitionMembers(MainController mainController) {
		super(mainController);
		
		createPanels();
		createTable();
		createButton();
	}
	
	/**
	 * Both the table and the button panel will be created and placed
	 */
	private void createPanels() {
		
		tablePanel = new JPanel();
		buttonPanel = new JPanel();
		
		tablePanel.setOpaque(false);
		buttonPanel.setOpaque(false);
		
		tablePanel.setLayout(new BorderLayout());	
		
		tablePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 0, 40));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		
		this.add(tablePanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * The table will be created and placed in the tablePanel
	 */
	private void createTable() {			
		membersTable = new JTable() {
			public boolean isCellEditable(int data, int columnNames){
				return false;
			}
		};
		membersTable.getTableHeader().setReorderingAllowed(false);
		membersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		membersModel.setColumnIdentifiers(COLUMN_NAMES);
		
		membersTable.setModel(membersModel);
		
		membersScroller = new JScrollPane(membersTable);
		membersScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		membersScroller.setMaximumSize(new Dimension(tablePanel.getWidth() - 10,tablePanel.getHeight()));
		membersScroller.setMinimumSize(new Dimension(tablePanel.getWidth() - 10,tablePanel.getHeight()));
		tablePanel.add(membersScroller);
		
	}
	
	/**
	 * Create the buttons to be added to the panel 
	 * @param create
	 */
	private void createButton() {		
		inviteButton = new CustomButton(PHRASE_GAME_INVITE, Color.WHITE, CustomColor.DARK_RED);
		inviteButton.setVisible(true);
		
		inviteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				int selectedRow = membersTable.getSelectedRow();
				if (selectedRow == -1) {
					showMessageDialog("Selecteer een deelnemer alvorens je probeert een uitnodiging te versturen.");
					
					return;
				}
				
				String result = (String) membersModel.getValueAt(selectedRow, 0);
				
				if (result.equals(mainController.getLoginController().getAccount().getUsername())) {
					showMessageDialog("Selecteer een andere deelnemer - niet jezelf dus ;)");
					
					return;
				}
				
				mainController.getGameController().onInvitePlayerButtonClicked(competitionId, result);
			}
			
		});
		
		buttonPanel.add(inviteButton);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJoinedCompetitionsLoaded(ArrayList<Competition> joinedCompetitions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMembersLoaded(int competitionId, ArrayList<String> members) {
		this.competitionId = competitionId;
		
		membersModel.setRowCount(0);
		
		for (String username : members) {			
			Object[] data = new Object[1];
			
			data[0] = username;
			
			membersModel.addRow(data);
		}
	}
	
}
