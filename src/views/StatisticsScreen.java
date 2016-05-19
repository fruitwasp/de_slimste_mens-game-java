package views;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import models.Statistic;
import observer.StatisticsObserver;
import controllers.MainController;

@SuppressWarnings("serial")
public class StatisticsScreen extends BasePanel implements StatisticsObserver {

	private static final int STATISTICSPANELBORDER_TOP = 80;
	private static final int STATISTICSPANELBORDER_LEFT = 50;
	private static final int STATISTICSPANELBORDER_BOTTOM = 50;
	private static final int STATISTICSPANELBORDER_RIGHT = 50;
	
	private DefaultTableModel statModel = new DefaultTableModel();
	private final static Object[] columnNames = {"Naam", "Gespeeld", "Gewonnen", "Verloren", "Gem. seconden over"};	
	
	public StatisticsScreen(MainController mainController) {
		super(mainController);
	
		setBorder(BorderFactory.createEmptyBorder(STATISTICSPANELBORDER_TOP, STATISTICSPANELBORDER_LEFT, STATISTICSPANELBORDER_BOTTOM, STATISTICSPANELBORDER_RIGHT));
		
		JTable statisticsTable = new JTable(){
			public boolean isCellEditable(int data, int columnNames){
				return false;
			}
		};		
		statisticsTable.getTableHeader().setReorderingAllowed(false);
		statisticsTable.setAutoCreateRowSorter(true);
		
		statModel.setColumnIdentifiers(columnNames);
		
		statisticsTable.setModel(statModel);
		
		JScrollPane statisticsScroller = new JScrollPane(statisticsTable);
		statisticsScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(statisticsScroller, BorderLayout.CENTER);
		
		mainController.getStatisticsController().addStatisticsObserver(this);
	}

	/**
	 * 
	 * fills the list with all statistics from the database
	 * 
	 */
	
	@Override
	public void onStatisticsLoaded(ArrayList<Statistic> statistics) {
		statModel.setRowCount(0);
		
		for (Statistic statistic : statistics) {
			Object[] rowData = new Object[6];	
			rowData[0] = statistic.getUsername();
			rowData[1] = statistic.getAmountPlayed();
			rowData[2] = statistic.getAmountWon();
			rowData[3] = statistic.getAmountLost();
			rowData[4] = statistic.getAverageSecLeft();
			
			statModel.addRow(rowData);
		}		
	}

	@Override
	public void onStatisticsChanged() {
		// TODO Auto-generated method stub
		
	}
}
