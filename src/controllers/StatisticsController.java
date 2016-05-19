package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import observer.StatisticsObserver;
import views.StatisticsScreen;
import models.Statistic;

public class StatisticsController {
	
	private MainController mainController;
	private DatabaseController databaseController;	
	
	private ArrayList<Statistic> statistics;
	
	private ArrayList<StatisticsObserver> statisticsObservers;

	public StatisticsController(MainController mainController) {
		this.mainController = mainController;
		this.databaseController = mainController.getDatabaseController();
		
		statistics = new ArrayList<Statistic>();
		
		statisticsObservers = new ArrayList<StatisticsObserver>();
	}
	
	/**
	 * 
	 * method for filling an arraylist with statistics from a specific competition ID
	 * 
	 * @param id (specific comp id)
	 * @return 	 (list with specific id statistics)
	 */
	
	public ArrayList<Statistic> getStatistics(int id) {
		ArrayList<Statistic> currentList = new ArrayList<>();
		
		ResultSet rs = databaseController.selectQuery("SELECT speler, aantal_gespeelde_spellen, aantal_gewonnen_spellen, aantal_verloren_spellen, gemiddeld_aantal_seconden_over FROM competitiestand WHERE comp_id = " + id);
		try {
			while (rs.next()) {				
				currentList.add(new Statistic(id, rs.getString("speler"), rs.getInt("aantal_gespeelde_spellen"), rs.getInt("aantal_gewonnen_spellen"), rs.getInt("aantal_verloren_spellen"), rs.getInt("gemiddeld_aantal_seconden_over")));				
			}
		} catch (SQLException e) {
			System.out.println("Er is iets fout gegaan bij het ophalen van de competitiestand van competitie " + id);
			e.printStackTrace();
		}
		
		return currentList;
	}
	
	public ArrayList<StatisticsObserver> getStatisticsObservers() {
		return statisticsObservers;
	}
	
	public void addStatisticsObserver(StatisticsObserver statisticsObserver) {
		statisticsObservers.add(statisticsObserver);
	}
	
	/**
	 * 
	 * if you clicked the button, the result will load in the JTable
	 * view will be loaded.
	 * 
	 * @param result (result is the specific statistic list)
	 */
	
	public void onViewMembersButtonClicked(int result) {
		
		StatisticsScreen statisticsScreen = (StatisticsScreen) mainController.getStatisticsScreenPanel();
		statisticsScreen.onStatisticsLoaded(getStatistics(result));
		
		mainController.getMainFrame().setContentView(statisticsScreen);
	}
	
	/**
	 * 
	 * returns the database statistics from the called query
	 * 
	 */

	public void fillList() {
		ResultSet rs = databaseController.selectQuery("SELECT * FROM competitiestand");				

		try {
			while(rs.next()) {
				statistics.add(new Statistic(rs.getInt("comp_id"), rs.getString("speler"), rs.getInt("aantal_gespeelde_spellen"), rs.getInt("aantal_gewonnen_spellen"), rs.getInt("aantal_verloren_spellen"), rs.getInt("gemiddeld_aantal_seconden_over")));
			}
		} catch (SQLException e) {}
		
		for (StatisticsObserver statisticsObserver : statisticsObservers) {
			statisticsObserver.onStatisticsLoaded(statistics);
		}
	}
}
