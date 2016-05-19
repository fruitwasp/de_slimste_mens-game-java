package views;

import helper.CustomButton;
import helper.CustomColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import models.Competition;
import observer.CompetitionObserver;
import controllers.CompetitionController;
import controllers.MainController;

@SuppressWarnings("serial")
public class CompetitionScreen extends BasePanel implements CompetitionObserver {

	private static final int BUTTONPANEL_WIDTH = 150;
	private static final int BUTTONPANEL_HEIGHT = 100;
	
	private static final int BUTTONPANELBORDER_TOP = 125;
	private static final int BUTTONPANELBORDER_LEFT = 5;
	private static final int BUTTONPANELBORDER_BOTTOM = 5;
	private static final int BUTTONPANELBORDER_RIGHT = 5;
	
	private static final int BUTTON_WIDTH = 150;
	private static final int BUTTON_HEIGHT = 40;
	
	private static final int BUTTONSPACING_WIDTH = 0;
	private static final int BUTTONSPACING_HEIGHT = 5;
	
	private static final int MENUBORDER_X = 150;
	private static final int MENUBORDER_Y = 0;
	private static final int MENUBORDER_WIDTH = 2;
	private static final int MENUBORDER_HEIGHT = 350;
	
	private static final int COMPLISTPANELBORDER_TOP = 30;
	private static final int COMPLISTPANELBORDER_LEFT = 30;
	private static final int COMPLISTPANELBORDER_BOTTOM = 30;
	private static final int COMPLISTPANELBORDER_RIGHT = 30;
	
	private Dimension buttonSizeCompScreen;
 
	private static final String PHRASE_COMPETITIONS_ALL = "Bekijk alle competities", PHRASE_MEMBERS_VIEW = "Bekijk deelnemers",
			PHRASE_ERROR_MEMBERS_VIEW = "Selecteer een competitie alvorens de deelnemers te kunnen bekijken.", PHRASE_LIST_REFRESH = "Lijst vernieuwen";
	private static final String[] COLUMN_NAMES = {"Id", "Naam", "Eigenaar"};
	
	private DefaultTableModel compModel;
	private JPanel compListPanel;
	private JTable compListTable;
	
	private CompetitionController competitionController;
	
	public CompetitionScreen(MainController mainController) {
		super(mainController);
		
		this.competitionController = mainController.getCompetitionController();
		
		buttonSizeCompScreen = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
		
		JButton showAllCompetitions = new CustomButton(PHRASE_COMPETITIONS_ALL, Color.WHITE, CustomColor.DARK_RED);
		showAllCompetitions.setMinimumSize(buttonSizeCompScreen);
		showAllCompetitions.setMaximumSize(buttonSizeCompScreen);		
		showAllCompetitions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				competitionController.onShowCompetitionsViewButtonClicked();				
			}
			
		});
		
		JButton viewMembers = new CustomButton(PHRASE_MEMBERS_VIEW, Color.WHITE, CustomColor.DARK_RED);
		viewMembers.setMinimumSize(buttonSizeCompScreen);
		viewMembers.setMaximumSize(buttonSizeCompScreen);		
		viewMembers.addActionListener(new ActionListener() {

			// if no row exists / is selected, return error dialog
			// else, show result of selected row
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int selectedRow = compListTable.getSelectedRow();
				if (selectedRow == -1) { 
					showMessageDialog(PHRASE_ERROR_MEMBERS_VIEW);
					
					return; 
				}
				
				int result = (int) compModel.getValueAt(selectedRow, 0);
				
				competitionController.onViewCompetitionMembersButtonClicked(result);				
			}
			
		});
		
		JButton refresh = new CustomButton(PHRASE_LIST_REFRESH, Color.WHITE, CustomColor.DARK_RED);
		refresh.setMinimumSize(buttonSizeCompScreen);
		refresh.setMaximumSize(buttonSizeCompScreen);		
		refresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				competitionController.onRefreshCompetitionsButtonClicked();
				
			}
			
		});
		
		// rigid areas are for spacing
				
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(BUTTONPANEL_WIDTH, BUTTONPANEL_HEIGHT));
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(BUTTONPANELBORDER_TOP, BUTTONPANELBORDER_LEFT, BUTTONPANELBORDER_BOTTOM, BUTTONPANELBORDER_RIGHT));
		buttonPanel.add(showAllCompetitions);
		buttonPanel.add(Box.createRigidArea(new Dimension(BUTTONSPACING_WIDTH, BUTTONSPACING_HEIGHT)));
		buttonPanel.add(viewMembers);
		buttonPanel.add(Box.createRigidArea(new Dimension(BUTTONSPACING_WIDTH, BUTTONSPACING_HEIGHT)));
		buttonPanel.add(refresh);
		
		compListPanel = new JPanel();
		compListPanel.setLayout(new BoxLayout(compListPanel, BoxLayout.Y_AXIS));
		compListPanel.setBorder(BorderFactory.createEmptyBorder(COMPLISTPANELBORDER_TOP, COMPLISTPANELBORDER_LEFT, COMPLISTPANELBORDER_BOTTOM, COMPLISTPANELBORDER_RIGHT));
		compListPanel.setOpaque(false);
	
		this.add(buttonPanel, BorderLayout.WEST);
		this.add(compListPanel, BorderLayout.CENTER);	
		
		createTable();
		
		mainController.getCompetitionController().addCompetitionObserver(this);
	}
	
	// initialize table	
	private void createTable() {
		
		compModel = new DefaultTableModel();
		compModel.setColumnIdentifiers(COLUMN_NAMES);
		
		compListTable = new JTable(compModel) {
			public boolean isCellEditable(int data, int columnNames) {
				return false;
			}
		};		
		compListTable.getTableHeader().setReorderingAllowed(false);
		compListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane compListScroller = new JScrollPane(compListTable);
		compListScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		compListPanel.add(compListScroller);		
	}
	
	// draws the menuborder
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(CustomColor.DARK_RED);
		g.fillRect(MENUBORDER_X, MENUBORDER_Y, MENUBORDER_WIDTH, MENUBORDER_HEIGHT);
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
		
		while (compModel.getRowCount() > 0) {
			compModel.removeRow(0);
		}
		
		for (Competition competition : joinedCompetitions) {
			Object[] rowData = new Object[3];
			rowData[0] = competition.getId();
			rowData[1] = competition.getName();
			rowData[2] = competition.getOwner();
						
			compModel.addRow(rowData);			
		}		
		
		revalidate();
	}

	@Override
	public void onMembersLoaded(int competitionId, ArrayList<String> members) {
		// TODO Auto-generated method stub
		
	}

}
