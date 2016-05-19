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
import controllers.MainController;

@SuppressWarnings("serial")
public class JoinCompetition extends BasePanel implements CompetitionObserver {
	
	private JPanel mainButtonPanel;
	private JPanel mainInfoPanel;
	
	private final static int MENUBORDER_X = 150, MENUBORDER_Y = 0, MENUBORDER_WIDTH = 2, MENUBORDER_HEIGHT = 350;
	private final static Dimension SPACE_BETWEEN_BUTTONS = new Dimension(0, 20);
	private final static Dimension BUTTON_SIZE = new Dimension(150, 40);	
	private final static Dimension MAIN_BUTTON_PANEL_SIZE = new Dimension(150, 400), JTABLE_SIZE = new Dimension(330, 625);
	
	private JTable competitionsTable;
	private JScrollPane competitionsScroller;
	private DefaultTableModel compModel = new DefaultTableModel();

	private static final String PHRASE_COMPETITION_JOIN = "Join", PHRASE_VIEW_MEMBERS = "Bekijk deelnemers", PHRASE_COMPETITION_CREATE = "Aanmaken competitie";
	private final static Object[] COLUMN_NAMES = {"Id", "Naam", "Eigenaar", "Aantal spelers"};

	public JoinCompetition(MainController mainController) {
		super(mainController);
		
		mainInfoPanel = new JPanel();
		mainInfoPanel.setOpaque(false);
		mainInfoPanel.setLayout(new BorderLayout());

		this.add(mainInfoPanel, BorderLayout.CENTER);

		mainButtonPanel = new JPanel();
		mainButtonPanel.setOpaque(false);
		mainButtonPanel.setLayout(new BoxLayout(mainButtonPanel,
				BoxLayout.Y_AXIS));
		mainButtonPanel.setPreferredSize(MAIN_BUTTON_PANEL_SIZE);

		this.add(mainButtonPanel, BorderLayout.WEST);

		mainInfoPanel
				.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		mainButtonPanel
				.setBorder(BorderFactory.createEmptyBorder(100, 5, 5, 5));

		createTable();
		createButtons();

		mainController.getCompetitionController().addCompetitionObserver(this);

	}

	private void createButtons() {

		JButton join = new CustomButton(PHRASE_COMPETITION_JOIN, Color.WHITE, CustomColor.DARK_RED);
		JButton viewMembers = new CustomButton(PHRASE_VIEW_MEMBERS, Color.WHITE, CustomColor.DARK_RED);
		JButton createCompetition = new CustomButton(PHRASE_COMPETITION_CREATE, Color.WHITE, CustomColor.DARK_RED);
		viewMembers.setMaximumSize(BUTTON_SIZE);
		createCompetition.setMaximumSize(BUTTON_SIZE);

		/**
		 * 
		 * checks if you have selected a row
		 * if this is not true, return
		 * else, get the row u selected.
		 * 
		 */
		
		viewMembers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (competitionsTable.getSelectedRow() == -1) { return; }
				
				int result = (int) compModel.getValueAt(competitionsTable.getSelectedRow(), 0);
				
				mainController.getStatisticsController().onViewMembersButtonClicked(result);

			}

		});

		createCompetition.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
				mainController.getMainFrame().setContentView(mainController.getCompetitionCreatePanel());

			}

		});

		join.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				int selectedRow = competitionsTable.getSelectedRow();
				if (selectedRow == -1) { return; }
				
				int result = (int) compModel.getValueAt(selectedRow, 0);

				mainController.getCompetitionController().onJoinCompetitionButtonClicked(result);
			}

		});

		mainButtonPanel.add(viewMembers);
		mainButtonPanel.add(Box.createRigidArea(SPACE_BETWEEN_BUTTONS));
		mainButtonPanel.add(createCompetition);

		mainInfoPanel.add(join, BorderLayout.SOUTH);

	}

	private void createTable() {

		competitionsTable = new JTable() {
			public boolean isCellEditable(int data, int columnNames) {
				return false;
			}
		};
		competitionsTable.getTableHeader().setReorderingAllowed(false);
		competitionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		compModel.setColumnIdentifiers(COLUMN_NAMES);

		competitionsTable.setModel(compModel);

		competitionsScroller = new JScrollPane(competitionsTable);
		competitionsScroller
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		competitionsScroller.setMaximumSize(JTABLE_SIZE);
		competitionsScroller.setMinimumSize(JTABLE_SIZE);
		mainInfoPanel.add(competitionsScroller);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(CustomColor.DARK_RED);
		g.fillRect(MENUBORDER_X, MENUBORDER_Y, MENUBORDER_WIDTH,
				MENUBORDER_HEIGHT);
	}

	@Override
	public void onCompetitionAdded(Competition competition) {
		Object[] rowData = new Object[4];
		rowData[0] = competition.getId();
		rowData[1] = competition.getName();
		rowData[2] = competition.getOwner();
		rowData[3] = competition.getMembersTotal();

		compModel.addRow(rowData);
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
	public void onCompetitionPlayersLoaded(ArrayList<String> members) {
		// TODO Auto-generated method stub

	}



	@Override
	public void onPlayerJoinFailed(String username) {
		showMessageDialog(username
				+ ", je bent al deelnemer van deze competitie!");

	}

	@Override
	public void onCreateCompetitionFailed(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompetitionsLoaded(ArrayList<Competition> competitions) {		
		while(compModel.getRowCount() > 0){
			compModel.removeRow(0);
		}
		
		for (Competition competition : competitions) {
			Object[] rowData = new Object[4];
			rowData[0] = competition.getId();
			rowData[1] = competition.getName();
			rowData[2] = competition.getOwner();
			rowData[3] = competition.getMembersTotal();

			compModel.addRow(rowData);
		}
		
		repaint();
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
