package controllers;

import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import models.Answer;
import models.Attempt;
import models.Game;
import models.Question;
import models.Round;
import models.Subquestion;
import models.Turn;
import observer.ObserverObserver;
import views.TurnBasePanel;

public class ObserverController {

	private MainController mainController;
	private DatabaseController databaseController;
	private ArrayList<Game> allGames;
	private ArrayList<ObserverObserver> observerObservers;

	private Game game;
	private Round round;
	private Turn turn;
	private Attempt attempt;
	private Subquestion subQuestion;

	private int scorePlayer1, scorePlayer2;
	
	private static final String ERROR_PHRASE = "Er is geen verdere data meer beschikbaar van dit spel, wilt u terug naar het hoofdscherm?";
	private static final Object[] DIALOG_OPTIONS = {"Ja", "Annuleer"};
	
	public ObserverController(MainController mainController) {
		this.mainController = mainController;
		this.databaseController = mainController.getDatabaseController();
		
		allGames = new ArrayList<>();
		observerObservers = new ArrayList<>();
	}

	public void addObserverObserver(ObserverObserver observerObserver) {
		observerObservers.add(observerObserver);
	}

	public ArrayList<ObserverObserver> getObserverObservers() {
		return observerObservers;
	}

	/**
	 * Return the turn view that matches the round name
	 * 
	 * @param round
	 * @return
	 */
	// <T> forces the method to return the actual obserview
	private <T> TurnBasePanel getMatchingObserview(Round round) {
		String roundName = round.getName();

		return roundName.equals("finale") ? mainController.getFinaleObserview()
				: roundName.equals("opendeur") ? mainController.getOpenDeurObserview() 
				: roundName.equals("puzzel") ? mainController.getPuzzelObserview()
				: roundName.equals("ingelijst") ? mainController.getIngelijstObserview() 
				: mainController.getDrieZesNegenObserview();
	}

	public void onObserveGamesButtonClicked() {
		mainController.getMainFrame().setContentView(mainController.getReplayGamesPanel());

		this.allGames = fetchAllGames();

		for (ObserverObserver observerObserver : observerObservers) {
			observerObserver.onGamesLoaded(allGames);
		}
	}

	public void onPlayedGameClicked(Game game) {
		this.game = game;
		this.scorePlayer1 = 0;
		this.scorePlayer2 = 0;
		
		game.setRounds(fetchRounds(game));

		for (Round round : game.getRounds()) {
			System.out.println("ophalen ronde " + round.getName());
			
			round.setTurns(fetchTurns(game, round));

			for (Turn turn : round.getTurns()) {
				boolean is369 = round.getName().equals("369");
				
				turn.setAttempts(fetchAttempts(game, round, turn));
				turn.setSubquestions(fetchSubQuestions(game, round, turn, is369));
			}
		}
		
		this.round = game.getFirstRound();
		this.turn = round.getFirstTurn();
		
		getMatchingObserview(round).onRoundLoaded(game, round);
		getMatchingObserview(round).onScoresChanged(scorePlayer1, scorePlayer2);
		
		onNextTurnClicked();
	}

	public void onNextTurnClicked() {
		if (round == null) {
			return;
		}
	
		Attempt nextAttempt = null;
		
		switch(round.getName()) {
		case "369":

			Subquestion nextSubquestion = turn.getNextSubQuestion(subQuestion);
			if (nextSubquestion == null) {
				Turn nextTurn = round.getNextTurn(turn);
				
				if (nextTurn == null) {		
					Round nextRound = game.getNextRound(round);
					
					if (nextRound == null) {
						
						int choice = JOptionPane.showOptionDialog(mainController.getMainFrame(), ERROR_PHRASE, "Fout", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, DIALOG_OPTIONS, null);
						if (choice == JOptionPane.YES_OPTION) {
							mainController.getMainFrame().setContentView(mainController.getMainPanel());
						}

						
						return;
					}
							
					this.round = nextRound;
					this.turn = round.getFirstTurn();				
					
					getMatchingObserview(round).onRoundLoaded(game, round);
					
					System.out.println("ronde:" + round.getName());
					System.out.println("turn: " + turn.getId());					
				}
				else {		
					this.turn = nextTurn;
					
					addSecEarned(game, round, turn);
				}
			
				this.subQuestion = null;			
				
				System.out.println("turn: " + turn.getId());
				
				onNextTurnClicked();
				return;
			}
			else {
				this.subQuestion = nextSubquestion;
			}
			
			getMatchingObserview(round).onAttemptLoaded(subQuestion.getAttempt() != null ? subQuestion.getAttempt() : "");			
			getMatchingObserview(round).onSubQuestionLoaded(game, round, turn, subQuestion);
			
			break;
		case "puzzel":			
			nextAttempt = turn.getNextAttempt(attempt);
			
			if (nextAttempt == null) {
				Turn nextTurn = round.getNextTurn(turn);
				
				if (nextTurn == null) {
					Round nextRound = game.getNextRound(round);

					if (nextRound == null) {
					
						int choice = JOptionPane.showOptionDialog(mainController.getMainFrame(), ERROR_PHRASE, "Fout", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, DIALOG_OPTIONS, null);
						if (choice == JOptionPane.YES_OPTION) {
							mainController.getMainFrame().setContentView(mainController.getMainPanel());
						}
						
						return;
					}
					
					this.round = nextRound;					
					this.turn = round.getFirstTurn();
					
					getMatchingObserview(round).onRoundLoaded(game, round);
				}
				else {			
					this.turn = nextTurn;
					
					addSecEarned(game, round, turn);
				}				

				this.attempt = null;				
				
				onNextTurnClicked();
				return;
			}
			else {			
				this.attempt = nextAttempt;
			}
			
			getMatchingObserview(round).onSubquestionsLoaded(game, round, turn, turn.getSubQuestions());
			getMatchingObserview(round).onAttemptLoaded(attempt.getAttempt());
			break;
		default:
			nextAttempt = turn.getNextAttempt(attempt);
			
			if (nextAttempt == null) {
				Turn nextTurn = round.getNextTurn(turn);
				
				if (nextTurn == null) {
					Round nextRound = game.getNextRound(round);
					
					if (nextRound == null) {
						
						int choice = JOptionPane.showOptionDialog(mainController.getMainFrame(), ERROR_PHRASE, "Fout", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, DIALOG_OPTIONS, null);
						if (choice == JOptionPane.YES_OPTION) {
							mainController.getMainFrame().setContentView(mainController.getMainPanel());
						}
						
						return;
					}
					
					this.round = nextRound;
					this.turn = round.getFirstTurn();
					
					getMatchingObserview(round).onRoundLoaded(game, round);
				}
				else {
					this.turn = nextTurn;
					
					addSecEarned(game, round, turn);
					
					if (round.getName().equals("finale")) {
						addSecFinaleOff(game, round, turn);
					}
				}				
				
				this.attempt = null;
				
				onNextTurnClicked();
				return;
			}
			else {
				this.attempt = nextAttempt;
			}
			
			getMatchingObserview(round).onTurnLoaded(game, round, turn);
			getMatchingObserview(round).onAttemptLoaded(nextAttempt.getAttempt());
			break;
		}
		
		getMatchingObserview(round).onScoresChanged(scorePlayer1, scorePlayer2);
		mainController.getMainFrame().setContentView(getMatchingObserview(round));
	}
	
	private void addSecEarned(Game game, Round round, Turn turn) {
		if (game.getPlayer1().equals(turn.getUsername())) {
			scorePlayer1 = scorePlayer1 + turn.getSecEarned();
			return;
		}

		scorePlayer2 = scorePlayer2 + turn.getSecEarned();
	}
	
	private void addSecFinaleOff(Game game, Round round, Turn turn) {
		if (game.getPlayer1().equals(turn.getUsername())) {
			scorePlayer2 = scorePlayer2 + turn.getSecFinaleOff();
			return;
		}
		
		scorePlayer1 = scorePlayer1 + turn.getSecFinaleOff();
		System.out.println("dnadsdadasd " + scorePlayer2);
	}

	public void onPreviousTurnClicked() {
		if (round == null) {
			return;
		}
		
		Attempt previousAttempt = null;
		
		switch(round.getName()) {
		case "369":
			Subquestion previousSubquestion = turn.getPreviousSubQuestion(subQuestion);
			
			if (previousSubquestion == null) {				
				Turn previousTurn = round.getPreviousTurn(turn);
				
				if (previousTurn == null) {
					Round previousRound = game.getPreviousRound(round);
					
					if (previousRound == null) {
						
						int choice = JOptionPane.showOptionDialog(mainController.getMainFrame(), ERROR_PHRASE, "Fout", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, DIALOG_OPTIONS, null);
						if (choice == JOptionPane.YES_OPTION) {
							mainController.getMainFrame().setContentView(mainController.getMainPanel());
							
						}
						    
						return;
					}
					
					this.round = previousRound;
					this.turn = round.getLatestTurn();
					
					getMatchingObserview(round).onRoundLoaded(game, round);
					
					System.out.println("ronde:" + round.getName());
					System.out.println("turn: " + turn.getId());
				}
				else {
					removeSecEarned(game, round, turn);
					
					this.turn = previousTurn;
				}
				
				System.out.println("turn: " + turn.getId());
				
				this.subQuestion = null;
				
				onPreviousTurnClicked();
				return;
			}
			else {			
				this.subQuestion = previousSubquestion;
			}
			
			System.out.println("subquestion: " + subQuestion.getFollowId());
			System.out.println("subq attempt: " + subQuestion.getAttempt());
			
			getMatchingObserview(round).onAttemptLoaded(subQuestion.getAttempt() != null ? subQuestion.getAttempt() : "");			
			getMatchingObserview(round).onSubQuestionLoaded(game, round, turn, subQuestion);
			
			break;
		case "puzzel":
			previousAttempt = turn.getPreviousAttempt(attempt);
			
			if (previousAttempt == null) {				
				Turn previousTurn = round.getPreviousTurn(turn);
				
				if (previousTurn == null) {
					Round previousRound = game.getPreviousRound(round);

					if (previousRound == null) {
						
						int choice = JOptionPane.showOptionDialog(mainController.getMainFrame(), ERROR_PHRASE, "Fout", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, DIALOG_OPTIONS, null);
						if (choice == JOptionPane.YES_OPTION) {
							mainController.getMainFrame().setContentView(mainController.getMainPanel());
						}
						
						return;
					}
					
					this.round = previousRound;					
					this.turn = round.getLatestTurn();
					
					getMatchingObserview(round).onRoundLoaded(game, round);
					
					System.out.println("ronde:" + round.getName());
					System.out.println("turn: " + turn.getId());
				}
				else {
					removeSecEarned(game, round, turn);
					
					this.turn = previousTurn;					
				}
				
				System.out.println("turn: " + turn.getId());
				
				this.attempt = null;
				
				onPreviousTurnClicked();
				return;
			}
			else {						
				this.attempt = previousAttempt;
			}
			
			System.out.println("attempt: " + attempt.getAttempt());
			
			getMatchingObserview(round).onSubquestionsLoaded(game, round, turn, turn.getSubQuestions());
			getMatchingObserview(round).onAttemptLoaded(attempt.getAttempt());
			
			break;
		default:
			previousAttempt = turn.getPreviousAttempt(attempt);
			
			if (previousAttempt == null) {				
				
				Turn previousTurn = round.getPreviousTurn(turn);
				
				if (previousTurn == null) {
					Round previousRound = game.getPreviousRound(round);
					
					if (previousRound == null) {
						
						int choice = JOptionPane.showOptionDialog(mainController.getMainFrame(), ERROR_PHRASE, "Fout", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, DIALOG_OPTIONS, null);
						if (choice == JOptionPane.YES_OPTION) {
							mainController.getMainFrame().setContentView(mainController.getMainPanel());
						}
						
						return;
					}
					
					this.round = previousRound;
					this.turn = round.getLatestTurn();
					
					getMatchingObserview(round).onRoundLoaded(game, round);
					
					System.out.println("ronde: " + round.getName());
					System.out.println("turn: " + turn.getId());
				}
				else {
					
					removeSecEarned(game, round, turn);
					
					if (round.getName().equals("finale")) {
						removeSecFinaleOff(game, round, turn);
					}
					
					this.turn = previousTurn;
				}
				
				System.out.println("turn: " + turn.getId());
				
				this.attempt = null;
				
				onPreviousTurnClicked();
				return;
			}
			else {			
				this.attempt = previousAttempt;
			}
			
			System.out.println("attempt: " + attempt.getAttempt());
			
			getMatchingObserview(round).onAttemptLoaded(attempt.getAttempt() != null ? attempt.getAttempt() : "");
			getMatchingObserview(round).onTurnLoaded(game, round, turn);
			
			break;
		}
		
		getMatchingObserview(round).onScoresChanged(scorePlayer1, scorePlayer2);
		mainController.getMainFrame().setContentView(getMatchingObserview(round));
	}
	
	private void removeSecEarned(Game game, Round round, Turn turn) {
		if (game.getPlayer1().equals(turn.getUsername())) {
			scorePlayer1 = scorePlayer1 - turn.getSecEarned();
			return;
		}
		
		scorePlayer2 = scorePlayer2 - turn.getSecEarned();
		System.out.println("dnadsdadasd " + scorePlayer2);
	}
	
	private void removeSecFinaleOff(Game game, Round round, Turn turn) {
		if (game.getPlayer1().equals(turn.getUsername())) {
			scorePlayer2 = scorePlayer2 - turn.getSecFinaleOff();
			return;
		}
		
		scorePlayer1 = scorePlayer1 - turn.getSecFinaleOff();
		System.out.println("dnadsdadasd " + scorePlayer2);
	}

	/**
	 * Fetch games
	 */

	public ArrayList<Game> fetchAllGames() {
		ArrayList<Game> games = new ArrayList<>();

		ResultSet rs = databaseController
				.selectQuery("SELECT * FROM spel WHERE reaktie_type = 'geaccepteerd'");

		try {
			while (rs.next()) {

				games.add(new Game(rs.getInt("spel_id"), rs
						.getString("speler1"), rs.getString("speler2"), rs
						.getString("toestand_type"), rs.getInt("comp_id"), rs
						.getString("reaktie_type")));

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Er is iets fout gegaan bij het ophalen van alle spellen.");
		}

		return games;
	}

	/**
	 * Fetch all rounds of the requested game
	 * 
	 * @param game
	 * @return
	 */
	public ArrayList<Round> fetchRounds(Game game) {
		ArrayList<Round> rounds = new ArrayList<>();

		ResultSet rs = databaseController
				.selectQuery("SELECT rondenaam FROM ronde WHERE spel_id = "
						+ game.getId());

		try {
			while (rs.next()) {
				rounds.add(new Round(game, rs.getString("rondenaam")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Er is iets fout gegaan bij het ophalen van alle rondes van spel "
							+ game.getId());
		}

		return rounds;
	}

	private ArrayList<Attempt> fetchAttempts(Game game, Round round, Turn turn) {
		ArrayList<Attempt> attempts = new ArrayList<>();

		ResultSet rs = databaseController
				.selectQuery("SELECT antwoord_id, antwoord, moment FROM spelerantwoord WHERE spel_id = "
						+ game.getId()
						+ " AND rondenaam = '"
						+ round.getName()
						+ "' AND beurt_id = " + turn.getId());

		try {
			while (rs.next()) {				
				attempts.add(new Attempt(turn, rs.getInt("antwoord_id"), rs
						.getString("antwoord"), rs.getInt("moment")));
				
				if (round.getName().equals("opendeur")){
					System.out.println(rs.getString("antwoord"));
				}	
			}
		} catch (Exception e) {
			System.out
					.println("Er is iets fout gegaan bij het ophalen van de antwoorden van de speler van beurt "
							+ turn.getId()
							+ " van ronde "
							+ round.getName()
							+ " van spel " + game.getId());
		}

		return attempts;
	}
	
	private Question fetchQuestionById(int questionId) {
		if (questionId == 0) {
			return null;
		}

		ResultSet rs = databaseController
				.selectQuery("SELECT vraagtekst FROM vraag WHERE vraag_id = "
						+ questionId);

		try {
			rs.first();

			Question question = new Question(questionId,
					rs.getString("vraagtekst"));

			ResultSet rs2 = databaseController
					.selectQuery("SELECT antwoord FROM sleutel WHERE vraag_id = "
							+ questionId);

			while (rs2.next()) {
				Answer answer = question.addAnswer(rs2.getString("antwoord"));

				ResultSet rs3 = databaseController
						.selectQuery("SELECT synoniem FROM alternatief WHERE vraag_id = "
								+ questionId
								+ " AND antwoord = '"
								+ answer.getAnswer() + "'");

				while (rs3.next()) {
					answer.addSynonym(rs3.getString("synoniem"));
				}
			}

			return question;
		} catch (Exception e) {
			System.out
					.println("Er is iets fout gegaan bij het ophalen van vraag "
							+ questionId);
		}

		return null;
	}
	
	private ArrayList<Subquestion> fetchSubQuestions(Game game, Round round,
			Turn turn, boolean is369) {
		ArrayList<Subquestion> subQuestions = new ArrayList<>();

		ResultSet rs = databaseController
				.selectQuery("SELECT volgnummer, vraag_id, antwoord FROM deelvraag WHERE spel_id = "
						+ game.getId()
						+ " AND rondenaam = '"
						+ round.getName()
						+ "' AND beurt_id = " + turn.getId());

		try {
			while (rs.next()) {
				Subquestion subQuestion = new Subquestion(turn,
						rs.getInt("volgnummer"),
						fetchQuestionById(rs.getInt("vraag_id")));

				if (is369) {
//					System.out.println(rs.getString("antwoord"));
					subQuestion.setAttempt(rs.getString("antwoord"));
				}

				subQuestions.add(subQuestion);
			}
		} catch (Exception e) {
			System.out
					.println("Er is iets fout gegaan bij het ophalen van de deelvragen van beurt "
							+ turn.getId()
							+ " van ronde "
							+ round.getName()
							+ " van spel " + game.getId());
		}

		return subQuestions;
	}

	/**
	 * Fetch the turns of a round in a game
	 * 
	 * @param game
	 * @param round
	 */
	private ArrayList<Turn> fetchTurns(Game game, Round round) {
		ArrayList<Turn> turns = new ArrayList<>();

		ResultSet rs = databaseController.selectQuery(""
				+ "SELECT beurt.beurt_id, beurt.vraag_id, beurt.speler, beurt.beurtstatus, beurt.sec_verdiend, beurt.sec_finale_af "
				+ "FROM beurt "
				+ "LEFT JOIN rondenaam ON beurt.rondenaam = rondenaam.type  "
				+ "WHERE beurt.spel_id = " + game.getId() + " AND beurt.rondenaam = '" + round.getName() + "' "
				+ "ORDER BY rondenaam.volgnr, beurt.beurt_id");

		try {
			while (rs.next()) {
				Turn turn = new Turn(round, rs.getInt("beurt_id"), rs.getString("beurtstatus"), rs.getString("speler"));
				turn.setSecEarned(rs.getInt("sec_verdiend"));
				
				switch (round.getName()) {
				default:
					turn.setQuestion(fetchQuestionById(rs.getInt("vraag_id")));
					break;

				case "369":
					turn.setSubquestions(fetchSubQuestions(game, round, turn, true));
				case "puzzel":
					turn.setSubquestions(fetchSubQuestions(game, round, turn, false));
				}

				if (!round.getName().equals("369")) {
					turn.setAttempts(fetchAttempts(game, round, turn));
					
					if (round.getName().equals("finale")) {
						turn.setSecFinaleOff(rs.getInt("sec_finale_af"));
					}
				}

				turns.add(turn);
			}
		} catch (Exception e) {
			System.out
					.println("Er is iets fout gegaan bij het ophalen van de beurten van de "
							+ round.getName()
							+ " ronde van spel "
							+ game.getId() + ".");
		}

		return turns;
	}

	/**
	 * Fetch all the competitions
	 * 
	 * @param game
	 */
	public String getCompetitionName(Game game) {
		ResultSet rs = databaseController
				.selectQuery("SELECT comp_naam FROM competitie WHERE comp_id = "
						+ game.getCompetitionId());

		try {
			rs.first();

			return rs.getString("comp_naam");
		} catch (Exception e) {
			System.out
					.println("Er is iets fout gegaan bij het ophalen van competitie "
							+ game.getCompetitionId());
		}

		return null;
	}

}
