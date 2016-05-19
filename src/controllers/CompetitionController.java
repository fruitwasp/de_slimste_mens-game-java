package controllers;

import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import models.Competition;
import observer.CompetitionObserver;
import views.ViewCompetitionMembers;

public class CompetitionController {

	private MainController mainController;
	private DatabaseController databaseController;
	private ArrayList<Competition> allCompetitions;
	private ArrayList<Competition> joinedCompetitions;

	private ArrayList<CompetitionObserver> competitionObservers;

	public CompetitionController(MainController mainController) {
		this.mainController = mainController;
		databaseController = mainController.getDatabaseController();

		allCompetitions = new ArrayList<Competition>();
		joinedCompetitions = new ArrayList<Competition>();

		competitionObservers = new ArrayList<CompetitionObserver>();
	}
	
	public ArrayList<CompetitionObserver> getCompetitionObservers() {
		return competitionObservers;
	}
	
	public void addCompetitionObserver(CompetitionObserver competitionObserver) {
		competitionObservers.add(competitionObserver);
	}
	
	public void setCompetitions(ArrayList<Competition> competitions) {
		this.allCompetitions = competitions;
	}
	
	/**
	 * Fetch all competitions
	 * @return
	 */
	public ArrayList<Competition> fetchCompetitions() {		
		ResultSet rs = databaseController.selectQuery("SELECT c.comp_id, comp_naam, account_naam_eigenaar, COUNT(d.account_naam) AS deelnemers_totaal FROM competitie AS c LEFT JOIN deelnemer AS d ON c.comp_id = d.comp_id GROUP BY c.comp_id");
		
		ArrayList<Competition> competities = new ArrayList<>();
		
		try {
			while (rs.next()) {
				competities.add(new Competition(rs.getInt("comp_id"), rs.getString("comp_naam"), rs.getString("account_naam_eigenaar"), rs.getInt("deelnemers_totaal")));
			}
		}
		catch (Exception e) {
			System.out.println("Er is iets fout gegaan bij het ophalen van alle competities.");
		}
		
		for (Competition competition : competities) {
			competition.setMembersTotal(databaseController);
		}
		
		return competities;
	}
	
	/**
	 * Fetch all joined competitions
	 * @return
	 */
	public ArrayList<Competition> fetchPlayerCompetitions() {
		if (mainController.getLoginController().getAccount() == null) {
			return null;
		}
		
		String username = mainController.getLoginController().getAccount().getUsername();
		
		ResultSet rs = databaseController.selectQuery("SELECT c.comp_id, comp_naam, account_naam_eigenaar, COUNT(d.account_naam) AS deelnemers_totaal FROM competitie AS c LEFT JOIN deelnemer AS d ON c.comp_id = d.comp_id WHERE d.account_naam = '" + username + "' OR account_naam_eigenaar = '" + username + "' GROUP BY c.comp_id");
		
		ArrayList<Competition> joinedCompetitions = new ArrayList<>();
		
		try {			
			while (rs.next()) {
				joinedCompetitions.add(new Competition(rs.getInt("comp_id"), rs.getString("comp_naam"), rs.getString("account_naam_eigenaar"), rs.getInt("deelnemers_totaal")));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Er is iets fout gegaan bij het ophalen van de competities waaraan je deelneemt.");
		}
		
		for (Competition competition : joinedCompetitions) {
			competition.setMembersTotal(databaseController);
		}
		
		return joinedCompetitions;
	}

	/**
	 * Fetch all members of a specific competition
	 * @param competition
	 * @return
	 */
	public ArrayList<String> fetchCompetitionMembers(Competition competition) {
		ResultSet rs = databaseController.selectQuery("SELECT account_naam FROM deelnemer WHERE comp_id = " + competition.getId());
		
		ArrayList<String> members = new ArrayList<>();
		
		try {
			while (rs.next()) {
				members.add(rs.getString("account_naam"));
			}
		}
		catch (Exception e) {
			System.out.println("Er is iets fout gegaan bij het ophalen van de deelnemers van competitie " + competition.getId());
		}
		
		return members;
	}
	
	/**
	 * Fetch a competition by its name
	 * @param name
	 * @return
	 */
	public Integer fetchCompetitionIdByName(String name) {
		ResultSet rs = databaseController.selectQuery("SELECT comp_id FROM competitie WHERE comp_naam = '" + name + "'");

		try {
			rs.first();

			return rs.getInt("comp_id");
		} catch (Exception e) {
			System.out.println("Er is iets fout gegaan bij het ophalen van het id van de competitie " + name);
		}

		return null;
	}

	/**
	 * Add a competition to the competitionlist
	 * 
	 * @param competition
	 */
	public void addCompetition(Competition competition) {
		allCompetitions.add(competition);
	}

	public ArrayList<Competition> getJoinedCompetitions() {
		return joinedCompetitions;
	}

	public ArrayList<Competition> getAllCompetitions() {
		return allCompetitions;
	}

	/*
	 * Get a competition by its id
	 */
	public Competition getCompetitionById(int id) {
		for (Competition competition : allCompetitions) {
			if (competition.getId() != id) {
				continue;
			}

			return competition;
		}

		return null;
	}
	
	public void onJoinCompetitionButtonClicked(int competitionId) {
		Competition competition = getCompetitionById(competitionId);
		
		String username = mainController.getLoginController().getAccount().getUsername();
		
		databaseController.insertQuery("INSERT INTO deelnemer (comp_id, account_naam) VALUES(" + competition.getId() + ", '" + username + "')");
		
		JOptionPane.showMessageDialog(mainController.getMainFrame(), "Je neemt nu deel aan competitie " + competition.getName());
	}

	public void onViewJoinedCompetitionsButtonClicked() {
		mainController.getMainFrame().setContentView(mainController.getCompetitionScreenPanel());
	}
	
	public void onShowCompetitionsViewButtonClicked() {
		mainController.getMainFrame().setContentView(mainController.getJoinCompetitionPanel());		
	}

	public void onCreateCompetitionViewButtonClicked() {
		mainController.getMainFrame().setContentView(mainController.getCompetitionCreatePanel());
	}

	public void onCreateCompetitionButtonClicked(String compName) {
		String username = mainController.getLoginController().getAccount().getUsername();

		ResultSet rs1 = databaseController.selectQuery("SELECT comp_naam FROM competitie WHERE account_naam_eigenaar = '" + username + "'");
		ResultSet rs2 = databaseController.selectQuery("SELECT comp_naam FROM competitie WHERE comp_naam = '" + compName + "'");

		try {
			if (rs1.next()) {
				JOptionPane.showMessageDialog(mainController.getMainFrame(), "Je bent al eigenaar van een competitie.");
			}
			else if(rs2.next()){
				JOptionPane.showMessageDialog(mainController.getMainFrame(), "Er bestaat al een competitie met de naam '" + compName + "'.");
			}
			else{
				databaseController.insertQuery("INSERT INTO competitie (comp_naam, account_naam_eigenaar) VALUES('" + compName + "', '"+ username +"')");
				
				ResultSet rs3 = databaseController.selectQuery("SELECT comp_id FROM competitie WHERE comp_naam = '" + compName + "' AND account_naam_eigenaar = '" + username + "' LIMIT 1");
				
				while (rs3.next()) {
					int competitionId = rs3.getInt("comp_id");
					
					databaseController.insertQuery("INSERT INTO deelnemer (comp_id, account_naam) VALUES(" + competitionId + ", '" + username + "')");
				}				
				
				JOptionPane.showMessageDialog(mainController.getMainFrame(), "De competitie '" + compName + "' is aangemaakt.");
			}
		}
		catch (Exception e) {
			System.out.println("Er is iets fout gegaan bij het ophalen van de competities");
		}
		
	}
	
	public void onViewCompetitionMembersButtonClicked(int competitionId) {
		Competition competition = getCompetitionById(competitionId);
		
		competition.setMembers(fetchCompetitionMembers(competition));
		
		ViewCompetitionMembers viewCompetitionMembersPanel = (ViewCompetitionMembers) mainController.getViewCompetitionMembersPanel();
		viewCompetitionMembersPanel.onMembersLoaded(competition.getId(), competition.getMembers());
		
		mainController.getMainFrame().setContentView(viewCompetitionMembersPanel);
	}
	
	public void onRefreshCompetitionsButtonClicked() {
		this.allCompetitions = fetchCompetitions();
		this.joinedCompetitions = fetchPlayerCompetitions();

		for (CompetitionObserver competitionObserver : competitionObservers) {
			competitionObserver.onCompetitionsLoaded(allCompetitions);
		}
		
		for (CompetitionObserver competitionObserver : competitionObservers) {
			competitionObserver.onJoinedCompetitionsLoaded(joinedCompetitions);
		}
	
	}
	
}
