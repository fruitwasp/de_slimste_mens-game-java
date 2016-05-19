package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import models.Account;
import observer.AdminObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class AdminPanel extends BasePanel implements AdminObserver {
	
	private DefaultTableModel adminModel = new DefaultTableModel();
	private final static Object[] COLUMN_NAMES = {"Gebruikersnaam"};
	private JButton moreInfoButton, newAccountButton;
	private JPanel buttonPanel, bottomPanel;
	private JPanel topBorderPanel, leftBorderPanel, rightBorderPanel;
	private Dimension borderDimension = new Dimension(200, 40);
	
	public AdminPanel(MainController mainController) {
		super(mainController);
	
		JTable adminTable = new JTable(){
			public boolean isCellEditable(int data, int columnNames){
				return false;
			}
		};
		//adminTable.setBackground(CustomColor.RED);
		/**
		 * the moreInfoButton saves the selected name from the adminTable when it is pressed
		 */
		moreInfoButton = new JButton("Meer informatie");
		moreInfoButton.setPreferredSize(borderDimension);
		moreInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selectedRow = adminTable.getSelectedRow();
				if (selectedRow == -1) { return; }
				
				String result = (String) adminModel.getValueAt(adminTable.getSelectedRow(), 0);

				mainController.getAdminController().onUserRowClicked(result);
			}
		});	
		
		newAccountButton = new JButton("Maak een nieuw account");
		newAccountButton.setPreferredSize(borderDimension);
		newAccountButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainController.getAdminController().goToCreatePage();
			}
		});	
		
		createBorderPanels();
		createBottomPanels();
		
		adminTable.getTableHeader().setReorderingAllowed(false);
		adminTable.setAutoCreateRowSorter(true);
		adminTable.setOpaque(false);
		
		adminModel.setColumnIdentifiers(COLUMN_NAMES);
		
		adminTable.setModel(adminModel);

		JScrollPane adminScroller = new JScrollPane(adminTable);
		adminScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(topBorderPanel, BorderLayout.NORTH);
		add(adminScroller, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		//add(moreInfoButton, BorderLayout.SOUTH);
		
		this.mainController.getAdminController().addAdminObserver(this);
		
	}
	
	public void createBorderPanels(){
		topBorderPanel = new JPanel();
		topBorderPanel.setOpaque(false);
		topBorderPanel.setPreferredSize(borderDimension);
		
		leftBorderPanel = new JPanel();
		leftBorderPanel.setLayout(new BorderLayout());
		leftBorderPanel.setOpaque(false);
		leftBorderPanel.setPreferredSize(borderDimension);
		
		rightBorderPanel = new JPanel();
		rightBorderPanel.setLayout(new BorderLayout());
		rightBorderPanel.setOpaque(false);
		rightBorderPanel.setPreferredSize(borderDimension);
	}
	
	public void createBottomPanels(){
		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new BorderLayout());
		
		buttonPanel.add(moreInfoButton, BorderLayout.WEST);
		buttonPanel.add(newAccountButton, BorderLayout.EAST);
		
		bottomPanel = new JPanel();
		bottomPanel.setOpaque(false);
		bottomPanel.setLayout(new BorderLayout());
		
		bottomPanel.add(leftBorderPanel, BorderLayout.WEST);
		bottomPanel.add(buttonPanel, BorderLayout.CENTER);
		bottomPanel.add(rightBorderPanel, BorderLayout.EAST);
		
		
	}



	@Override
	public void onPlayerSelected(Account account) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * fills the list with username's of the registered accounts
	 */
	
	@Override
	public void onPlayersLoaded(ArrayList<Account> accounts) {
		
		// TODO Auto-generated method stub
		
		while(adminModel.getRowCount() > 0){
			adminModel.removeRow(0);
		}
		
		for(Account account : accounts){
			Object[] rowData = new Object[1];
			rowData[0] = account.getUsername();
			
			adminModel.addRow(rowData);
		}
		
	}



	@Override
	public void onPlayerPasswordChanged(String username, String password) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onPlayerUsernameChanged(String oldUsername, String username) {
		// TODO Auto-generated method stub
		
	}
}
