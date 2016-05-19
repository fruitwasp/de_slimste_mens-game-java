package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import models.Answer;
import models.Attempt;
import models.Game;
import models.Question;
import models.Round;
import models.Subquestion;
import models.Turn;
import observer.ChatObserver;
import observer.GameObserver;
import views.Chat;
import views.MainPanel;
import views.TurnBasePanel;

public class GameController {

	private MainController mainController;
	private DatabaseController databaseController;

	private ArrayList<Game> games;

	private ArrayList<GameObserver> gameObservers;
	private ArrayList<ChatObserver> chatObservers;

	public GameController(MainController mainController) {
		this.mainController = mainController;
		this.databaseController = mainController.getDatabaseController();

		games = new ArrayList<Game>();

		gameObservers = new ArrayList<GameObserver>();
		chatObservers = new ArrayList<ChatObserver>();
	}

	/**
	 * Adds an instance of GameObserver
	 * 
	 * @param gameObserver
	 */
	public void addGameObserver(GameObserver gameObserver) {
		gameObservers.add(gameObserver);
	}

	/**
	 * Returns an array of instances of GameObserver
	 * 
	 * @return
	 */
	public ArrayList<GameObserver> getGameObservers() {
		return gameObservers;
	}

	/**
	 * Add an instance of ChatObserver
	 * 
	 * @param chatObserver
	 */
	public void addChatObserver(ChatObserver chatObserver) {
		chatObservers.add(chatObserver);
	}

	/**
	 * Returns an array of all instances of ChatObserver
	 * 
	 * @return
	 */
	public ArrayList<ChatObserver> getChatObservers() {
		return chatObservers;
	}

	public ArrayList<Game> getGames() {
		return games;
	}

	/**
	 * Returns the game with the specified gameId (if exists)
	 * 
	 * @param gameId
	 * @return
	 */
	public Game getGameById(int gameId) {
		for (Game game : games) {
			if (game.getId() != gameId) {
				continue;
			}

			return game;
		}

		return null;
	}

	/*
	 * 
	 * public Turn createTurn(Game game, Round round) { String username =
	 * mainController.getLoginController().getAccount() .getUsername();
	 * 
	 * int turnId = fetchLatestTurnId(game, round) + 1; if (turnId == 0) {
	 * turnId = 1; } databaseController .insertQuery(
	 * "INSERT INTO beurt (spel_id, rondenaam, beurt_id, speler, beurtstatus) VALUES("
	 * + game.getId() + ", '" + round.getName() + "', " + turnId + ", '" +
	 * username + "', 'bezig')");
	 * 
	 * return round.addTurn(new Question(1, "Lust jij bier?"), turnId, "bezig",
	 * username); }
	 */

	/**
	 * Create a new round
	 * 
	 * @param game
	 * @param roundName
	 * @return
	 */
	public Round createRound(Game game, String roundName) {

		databaseController
				.insertQuery("INSERT INTO ronde (spel_id, rondenaam) VALUES("
						+ game.getId() + ", '" + roundName + "')");

		Round round = new Round(game, roundName);

		return round;
	}

	/**
	 * Fetch a game by its id
	 * 
	 * @param gameId
	 * @return
	 */
	/*
	 * private Game fetchGameById(int gameId) { ResultSet rs =
	 * databaseController.selectQuery(
	 * "SELECT speler1, speler2, toestand_type, comp_id, reaktie_type FROM spel WHERE spel_id = "
	 * + gameId);
	 * 
	 * try { rs.first();
	 * 
	 * return new Game(gameId, rs.getString("speler1"), rs.getString("speler2"),
	 * rs.getString("toestand_type"), rs.getInt("comp_id"),
	 * rs.getString("reaktie_type")); } catch (Exception e) {
	 * System.out.println("Er is iets fout gegaan bij het ophalen van spel " +
	 * gameId); }
	 * 
	 * return null; }
	 */

	/**
	 * Fetch all games you're in
	 * 
	 * @return
	 */
	private synchronized ArrayList<Game> fetchGames() {
		String username = mainController.getLoginController().getAccount()
				.getUsername();
		ArrayList<Game> games = new ArrayList<>();

		ResultSet rs = databaseController
				.selectQuery("SELECT sp.spel_id, sp.speler1, seconden1, sp.speler2, seconden2, sp.toestand_type, sp.comp_id, reaktie_type FROM spel AS sp LEFT JOIN score ON sp.spel_id = score.spel_id WHERE sp.speler1 = '"
						+ username + "' OR sp.speler2 = '" + username + "'");

		try {
			while (rs.next()) {
				Game game = new Game(rs.getInt("spel_id"),
						rs.getString("speler1"), rs.getString("speler2"),
						rs.getString("toestand_type"), rs.getInt("comp_id"),
						rs.getString("reaktie_type"));
				game.setPlayer1Points(rs.getInt("seconden1"));
				game.setPlayer2Points(rs.getInt("seconden2"));

				games.add(game);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Er is iets fout gegaan bij het ophalen van alle spellen waar "
							+ username + " aan deelneemt.");
		}

		return games;
	}

	/**
	 * Fetch all rounds of the game you request
	 * 
	 * @param game
	 * @return
	 */
	private ArrayList<Round> fetchRounds(Game game) {
		ArrayList<Round> rounds = new ArrayList<>();

		ResultSet rs = databaseController
				.selectQuery("SELECT ronde.rondenaam AS rn FROM ronde LEFT JOIN rondenaam ON ronde.rondenaam = rondenaam.type WHERE spel_id = "
						+ game.getId() + " ORDER BY rondenaam.volgnr");

		try {
			while (rs.next()) {
				rounds.add(new Round(game, rs.getString("rn")));
			}
		} catch (Exception e) {
			System.out
					.println("Er is iets fout gegaan bij het ophalen van de rondes van spel "
							+ game.getId() + ".");
		}

		return rounds;
	}

	/**
	 * Fetch all turns of the round of the game you request
	 * 
	 * @param game
	 * @param round
	 * @return
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
				Turn turn = new Turn(round, rs.getInt(1), rs.getString(4), rs.getString(3));

				if (!round.getName().equals("369")) {
					turn.setAttempts(fetchAttempts(game, round, turn));
				}
				
				switch (round.getName()) {

				default:
					turn.setQuestion(fetchQuestionById(game, round, turn, rs.getInt("vraag_id")));
					break;

				case "369":
					turn.setSubquestions(fetchSubQuestions(game, round, turn, true));
				case "puzzel":
					turn.setSubquestions(fetchSubQuestions(game, round, turn, false));

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

	private ArrayList<Subquestion> fetchSubQuestions(Game game, Round round, Turn turn, boolean is369) {
		ArrayList<Subquestion> subQuestions = new ArrayList<>();

		ResultSet rs = databaseController
				.selectQuery("SELECT volgnummer, vraag_id, antwoord FROM deelvraag WHERE spel_id = "
						+ game.getId()
						+ " AND rondenaam = '"
						+ round.getName()
						+ "' AND beurt_id = " + turn.getId());

		try {
			while (rs.next()) {
				Subquestion subQuestion = new Subquestion(turn, rs.getInt("volgnummer"), fetchQuestionById(game, round, turn, rs.getInt("vraag_id")));

				if (is369) {
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

	private Question fetchQuestionById(Game game, Round round, Turn turn, int questionId) {
		if (questionId == 0) {
			return null;
		}

		ResultSet rs = databaseController.selectQuery("SELECT vraagtekst FROM vraag WHERE vraag_id = " + questionId);

		try {
			rs.first();

			Question question = new Question(questionId, rs.getString("vraagtekst"));
			
			ResultSet rs2 = databaseController.selectQuery("SELECT antwoord FROM sleutel WHERE vraag_id = " + questionId);

			while (rs2.next()) {
				Answer answer = question.addAnswer(rs2.getString("antwoord"));
 
				ResultSet rs3 = databaseController.selectQuery("SELECT synoniem FROM alternatief WHERE vraag_id = " + questionId + " AND antwoord = '" 	+ answer.getAnswer() + "'");

				while (rs3.next()) {
					answer.addSynonym(rs3.getString("synoniem"));
				}
			}

			return question;
		} catch (Exception e) {
			System.out.println("Er is iets fout gegaan bij het ophalen van vraag " + questionId);
			e.printStackTrace();
		}

		return null;
	}

	public boolean openDeurNeedNewRound(Round round) {

		int right = 0;
		int pass = 0;

		for (Turn turn : round.getTurns()) {
			if (turn.getStatus().equals("goed")) {
				right++;
			} else if (turn.getStatus().equals("pas")) {
				pass++;
			}
		}

		if (right >= 2 || pass >= 4 || (right >= 1 && pass >= 2)) {
			return true;
		}

		return false;
	}

	public Question fetchRandomQuestion(String roundName, String username) {
		Question question = null;

		ResultSet rs = databaseController.selectQuery(""
			+ "SELECT sva.vraag_id, v.vraagtekst, sva.aantal_keer_gebruikt "
			+ "FROM speler_vraag_aantal AS sva "
			+ "LEFT JOIN vraag AS v ON sva.vraag_id = v.vraag_id "
			+ "WHERE speler = '" + username + "' AND sva.rondenaam = '" + roundName + "' "
			+ "ORDER BY aantal_keer_gebruikt, RAND() "
			+ "LIMIT 1");

		try {
			rs.first();

			question = new Question(rs.getInt("vraag_id"),
					rs.getString("vraagtekst"));

			rs = databaseController
					.selectQuery("SELECT antwoord FROM sleutel WHERE vraag_id = "
							+ question.getId());

			while (rs.next()) {

				Answer answer = question.addAnswer(rs.getString("antwoord"));

				ResultSet rs2 = databaseController
						.selectQuery("SELECT synoniem FROM alternatief WHERE vraag_id = "
								+ question.getId()
								+ " AND antwoord = '"
								+ answer.getAnswer() + "'");

				while (rs2.next()) {
					answer.addSynonym(rs2.getString("synoniem"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Er is iets fout gegaan bij het ophalen van een willekeurige vraag voor ronde "
							+ roundName);
		}

		return question;
	}

	public static int getQuestionTotal369(Round round) {
		int totalQuestions = 0;
		int previousQuestionId = 0;
		
		for (Turn turn : round.getTurns()) {
			for (Subquestion subQuestion : turn.getSubQuestions()) {
				if (previousQuestionId == 0 || previousQuestionId != subQuestion.getQuestion().getId()) {
					previousQuestionId = subQuestion.getQuestion().getId();
					totalQuestions++;
				}
			}
		}

		return totalQuestions;
	}

	private static int getQuestionTotalFinale(Round round) {
		int uniqueQuestionTotal = 0;
		int previousQuestionId = 0;

		for (Turn turn : round.getTurns()) {
			if (turn.getQuestion() == null) {
				continue;
			}
			
			if (previousQuestionId == turn.getQuestion().getId()) {
				continue;
			}

			uniqueQuestionTotal++;
			previousQuestionId = turn.getQuestion().getId();
		}

		return uniqueQuestionTotal;
	}

	public boolean hasLowestPoints(Game game) {
		String username = mainController.getLoginController().getAccount()
				.getUsername();

		if (username.equals(game.getPlayer1())) {
			if (game.getPlayer1Points() <= game.getPlayer2Points()) {
				return true;
			}
		} else {
			if (game.getPlayer2Points() < game.getPlayer1Points()) {
				return true;
			}
		}

		return false;
	}

	private boolean previousIsQuestionOpponent(Game game, Round round, Turn turn) {
		String username = mainController.getLoginController().getAccount().getUsername();
		
		Turn previousTurn = round.getPreviousTurn(turn);
		if (previousTurn == null) {
			return false;
		}
		
		Turn previousPreviousTurn = round.getPreviousTurn(previousTurn);
		if (previousPreviousTurn == null) {
			return false;
		}
		
		if (previousPreviousTurn.getLatestSubquestion() == null) {
			return false;
		}
		
		if (previousPreviousTurn.getLatestSubquestion().getQuestion().getId() == previousTurn.getLatestSubquestion().getQuestion().getId() && previousPreviousTurn.getUsername() != previousTurn.getUsername()) {
			return false;
		}
		
		if (!previousTurn.getUsername().equals(username) && (previousTurn.getStatus().equals("pas") || previousTurn.getStatus().equals("fout"))) {
			return true;
		}
		
		return false;
	}
	
	public boolean hasTurn(Game game) {
		Round latestRound = game.getLatestRound();
		if (latestRound == null) {
			return false;
		}

		Turn latestTurn = latestRound.getLatestTurn();

		if (mainController.getLoginController().getAccount() == null) {
			return false;
		}
		
		String username = mainController.getLoginController().getAccount().getUsername();

		switch (latestRound.getName()) {
		case "369":			
			if (latestTurn == null) {
				if (username.equals(game.getPlayer1())) {
					return true;
				}
				
				return false;
			}
			
			if (previousIsQuestionOpponent(game, latestRound, latestTurn)) {
				return true;
			}
			
			if (latestTurn.getStatus().equals("bonus")) {
				if (hasLowestPoints(game)) {
					return true;
				}
			}
			
			if (latestTurn.getUsername().equals(username)) {
				if (latestTurn.getStatus().equals("goed")
						|| latestTurn.getStatus().equals("bezig")) {
					return true;
				}
			} else {
				if (latestTurn.getStatus().equals("pas")
						|| latestTurn.getStatus().equals("fout")) {
					return true;
				}
			}
			
			return false;
		case "opendeur":
			System.out.println(game.getPlayer1() + ": " + game.getPlayer1Points());
			System.out.println(game.getPlayer2() + ": " + game.getPlayer2Points());
			
			if (this.openDeurNeedNewRound(latestRound)) {
				this.createNextRound(game, latestRound);
				break;
			}

			if (latestTurn == null) {
				if (hasLowestPoints(game)) {
					return true;
				}

				return false;
			}

			if (username.equals(latestTurn.getUsername())) {
				if (latestTurn.getStatus().equals("bezig")) {
					return true;
				}
			}

			if (latestTurn.getStatus().equals("pas") || latestTurn.getStatus().equals("goed")) {
				if (!username.equals(latestTurn.getUsername())) {
					return true;
				}
			}

			if (getQuestionTotalFinale(latestRound) % 2 == 0) {
				if (hasLowestPoints(game)) {
					return true;
				}
			}

			return false;
		case "puzzel":
			if (latestTurn == null) {
				if (username.equals(game.getPlayer1())) {
					if (game.getPlayer1Points() < game.getPlayer2Points()) {
						return true;
					}
				} else {
					if (game.getPlayer2Points() < game.getPlayer1Points()) {
						return true;
					}
				}

				return false;
			}

			if (latestTurn.getUsername().equals(username)) {
				if (latestTurn.getStatus().equals("bezig")) {
					return true;
				}
			}
			else {
				if (latestTurn.getStatus().equals("goed") || latestTurn.getStatus().equals("pas")) {
					return true;
				}
			}
			
			if (latestRound.getEndedPuzzel()) {
				if (hasLowestPoints(game)) {
					return true; // ff testen
				}
			}

			return false;
		case "ingelijst":
			if (latestTurn == null) {
				if (game.getPlayer1Points() < game.getPlayer2Points()) {
					if (username.equals(game.getPlayer1())) {
						return true;
					}
				} else {
					if (username.equals(game.getPlayer2())) {
						return true;
					}
				}

				return false;
			}

			if (latestRound.getTurns().size() == 2) {
				createNextRound(game, latestRound);
				break;
			}

			if (latestTurn.getUsername().equals(username)) {
				if (latestTurn.getStatus().equals("bezig")) {
					return true;
				}
			}
			else {
				if (!latestTurn.getStatus().equals("bezig")) {
					return true;
				}
			}

			return false;
		case "finale":
			if (latestTurn == null) {
				if (hasLowestPoints(game)) {
					return true;
				}

				return false;
			}

			if (latestTurn.getStatus().equals("bezig")) {
				if (username.equals(latestTurn.getUsername())) {
					return true;
				}
				
				return false;
			}
			
			if (getQuestionTotalFinale(latestRound) % 2 == 0 
				|| latestTurn.getStatus().equals("goed")) {
				if (hasLowestPoints(game)) {
					return true;
				}

				return false;
			}

			if (latestTurn.getStatus().equals("pas")) {
				if (!username.equals(latestTurn.getUsername())) {
					return true;
				}

				return false;
			}

			return false;
		}

		return false;
	}

	public void setNextTurn(Game game) {

		boolean needNewTurn = true;
		Round latestRound = game.getLatestRound();

		if (latestRound == null) {
			latestRound = createNextRound(game, latestRound);
		}

		Turn latestTurn = latestRound.getLatestTurn();

		if (latestTurn == null) {
			latestTurn = createNextTurn(game, latestRound);
			needNewTurn = false;
		}
		
		if (latestTurn.getStatus().equals("bezig") || latestTurn.getStatus().equals("bonus")) {
			needNewTurn = false;
		}

		Turn nextTurn = null;

		switch (latestRound.getName()) {
		case "369":
			if (latestRound.getTotalUsedQuestions369() >= 9 && latestTurn.getStatus().equals("bonus")) {	
				System.out.println("nieuwe ronde nieuwe kansen");
				Round nextRound = createNextRound(game, latestRound);
				nextTurn = createNextTurn(game, nextRound);
				needNewTurn = false;
			}
			
			if (needNewTurn) {
				nextTurn = createNextTurn(game, latestRound);
				
				if (shouldPassOnSubQuestion369(game, latestRound, latestTurn, nextTurn)) {
					Subquestion latestSubquestion = latestTurn.getLatestSubquestion();
					
					nextTurn.addSubquestion(latestSubquestion.getFollowId(), latestSubquestion.getQuestion());
					
					databaseController.insertQuery("INSERT INTO deelvraag (spel_id, rondenaam, beurt_id, volgnummer, vraag_id) VALUES(" + game.getId() + ", '" + latestRound.getName() + "', " + nextTurn.getId() + ", " + latestSubquestion.getFollowId() + ", " + latestSubquestion.getQuestion().getId() + ")");
				}
			}

			break;
		case "opendeur":
			if (this.openDeurNeedNewRound(latestRound)) {
				Round nextRound = createNextRound(game, latestRound);
				nextTurn = createNextTurn(game, nextRound);
			} else {
				if (needNewTurn) {
					nextTurn = createNextTurn(game, latestRound);
				}

				if (latestTurn.getStatus().equals("pas")) {
					Question question = latestTurn.getQuestion();
					
					nextTurn.setQuestion(question);
					
					databaseController.insertQuery("UPDATE beurt SET vraag_id = " + question.getId() + " WHERE spel_id = " + game.getId() + " AND rondenaam = 'opendeur' AND beurt_id = " + nextTurn.getId());
				}
			}

			break;
		case "puzzel":
			if (needNewTurn) {
				if (latestRound.getEndedPuzzel()) {
					Round nextRound = createNextRound(game, latestRound);
					nextTurn = createNextTurn(game, nextRound);
				} else {
					nextTurn = createNextTurn(game, latestRound);
					
					if (latestTurn.getStatus().equals("pas")) {
						ArrayList<Subquestion> subquestions = latestTurn.getSubQuestions();
						
						nextTurn.setSubquestions(subquestions);
						
						int i = 1;
						for (Subquestion subquestion : subquestions) {
							databaseController.insertQuery(""
								+ "INSERT INTO deelvraag (spel_id, rondenaam, beurt_id, volgnummer, vraag_id)"
								+ "VALUES(" + game.getId() + ", 'puzzel', " + nextTurn.getId() + ", " + i + ", " + subquestion.getQuestion().getId() + ")");
							
							i++;
						}
						
					}
					
				}
				
			}

			break;
		case "ingelijst":
			if (latestRound.getTurns().size() < 2) {
				if (!latestTurn.getStatus().equals("bezig")) {
					nextTurn = createNextTurn(game, latestRound);
					
					Question question = latestTurn.getQuestion();
					nextTurn.setQuestion(question);
					
					databaseController.insertQuery("UPDATE beurt SET vraag_id = " + question.getId() + " WHERE spel_id = " + game.getId() + " AND rondenaam = 'ingelijst' AND beurt_id = " + nextTurn.getId());
				}
			} else {
				if (latestTurn.getStatus().equals("bezig")) {
					return;
				}
				
				Round nextRound = createNextRound(game, latestRound);
				nextTurn = createNextTurn(game, nextRound);
			}

			break;
		case "finale":
			nextTurn = createNextTurn(game, latestRound);

			if (latestTurn.getStatus().equals("pas")) {
				nextTurn.setQuestion(latestTurn.getQuestion());
			}

			break;
		}

	}
	
	private boolean shouldPassOnSubQuestion369(Game game, Round round, Turn latestTurn, Turn nextTurn) {
		String username = mainController.getLoginController().getAccount().getUsername();
		
		if (!latestTurn.getUsername().equals(username) && (latestTurn.getStatus().equals("fout") || latestTurn.getStatus().equals("pas"))) {
			Turn latestLatestTurn = round.getPreviousTurn(latestTurn);
			
			if (latestLatestTurn == null) {
				return true;
			}
		
			Subquestion latestLatestSubQuestion = latestLatestTurn.getLatestSubquestion();
			
			if (latestLatestSubQuestion == null) {
				return true;
			}
			
			Subquestion latestSubQuestion = latestTurn.getLatestSubquestion();			
			if (latestSubQuestion != null && latestSubQuestion.getFollowId() != latestLatestSubQuestion.getFollowId()) {
				return true;
			}
			
		}
		
		return false;
	}

	public Round createNextRound(Game game, Round latestRound) {
		if (latestRound == null) {
			databaseController
					.insertQuery("INSERT INTO ronde (spel_id, rondenaam) VALUES("
							+ game.getId() + ", '369')");

			return game.addRound("369");
		}

		String nextRound = null;

		switch (latestRound.getName()) {
		case "369":
			nextRound = "opendeur";
			break;
		case "opendeur":
			nextRound = "puzzel";
			break;
		case "puzzel":
			nextRound = "ingelijst";
			break;
		case "ingelijst":
			nextRound = "finale";
			break;
		}

		if (nextRound == null) {
			return null;
		}

		databaseController
				.insertQuery("INSERT INTO ronde (spel_id, rondenaam) VALUES("
						+ game.getId() + ", '" + nextRound + "')");

		return game.addRound(nextRound);
	}

	public Turn createNextTurn(Game game, Round round) {
		String username = mainController.getLoginController().getAccount()
				.getUsername();

		Turn latestTurn = round.getLatestTurn();
		int nextTurnId = 1;

		if (latestTurn != null) {
			nextTurnId = latestTurn.getId() + 1;
			System.out.println("next turn id: " + nextTurnId);
		}
		
		databaseController
		.insertQuery("INSERT INTO beurt (spel_id, rondenaam, beurt_id, speler, beurtstatus) VALUES ("
				+ game.getId()
				+ ",  '"
				+ round.getName()
				+ "', "
				+ nextTurnId
				+ ", '"
				+ username
				+ "' , 'bezig')");
		
		latestTurn = round.addTurn(nextTurnId, "bezig", username);
		
		return latestTurn;
	}

	public synchronized void onRefreshGamesButtonClicked() {
		this.games = fetchGames();

		for (Game game : games) {

			game.setRounds(fetchRounds(game));

			for (Round round : game.getRounds()) {

				round.setTurns(fetchTurns(game, round));
			}
		}

		for (GameObserver gameObserver : gameObservers) {
			gameObserver.onGamesLoaded(games);
		}
	}

	public void onViewInvitesButtonClicked() {
		// TODO: refresh game invites

		mainController.getMainFrame().setContentView(
				mainController.getInvitesPanel());
	}

	public void onPlayGameButtonClicked(Game game) {
		setNextTurn(game);
		
		Round latestRound = game.getLatestRound();
		Turn latestTurn = latestRound.getLatestTurn();

		mainController.getMainFrame().setContentView(getMatchingView(latestRound));

		getMatchingTurnController(latestRound).beginTurn(game, latestRound, latestTurn);
	}

	public void onAcceptGameInviteButtonClicked(Game game) {
		databaseController.insertQuery("UPDATE spel SET toestand_type = 'bezig', reaktie_type = 'geaccepteerd' WHERE spel_id = " + game.getId());
		databaseController.insertQuery("INSERT INTO ronde (spel_id, rondenaam) VALUES(" + game.getId() + ", '369')"); 
		
		JOptionPane.showMessageDialog(mainController.getMainFrame(), "De uitnodiging is geaccepteerd.");
		
		onRefreshGamesButtonClicked();		
	}

	public void onRejectGameInviteButtonClicked(Game game) {
		databaseController
				.insertQuery("UPDATE spel SET toestand_type = 'afgelopen', reaktie_type = 'verworpen' WHERE spel_id = "
						+ game.getId());

		JOptionPane.showMessageDialog(mainController.getMainFrame(), "De uitnodiging is verworpen.");
		
		onRefreshGamesButtonClicked();	
	}

	public void onInvitePlayerButtonClicked(int competitionId, String username) {
		String localUser = mainController.getLoginController().getAccount().getUsername();
		
		// Return to the main panel
		MainPanel mainPanel = (MainPanel) mainController.getMainPanel();
		mainController.getMainFrame().setContentView(mainPanel);
		
		boolean alreadyInGame = false;
		for (Game game : games) {
			if (competitionId != game.getCompetitionId()) {
				continue;
			}
			
			if (game.getState().equals("afgelopen")) {
				continue;
			}
			
			if (!game.getPlayer2().equals(username)) {
				continue;
			}
			
			alreadyInGame = true;
			break;
		}
			
		if (alreadyInGame) {
			JOptionPane.showMessageDialog(mainController.getMainFrame(), "Je hebt al een actief spel met speler " + username);
			return;
		}
		
		databaseController.insertQuery("INSERT INTO spel (speler1, speler2, toestand_type, comp_id, reaktie_type) VALUES('"
			+ localUser
			+ "', '"
			+ username
			+ "', 'uitdaging', '"
			+ competitionId + "', 'onbekend')");
		
		JOptionPane.showMessageDialog(mainController.getMainFrame(), "De uitnodiging is verstuurd!");
		
		onRefreshGamesButtonClicked();
	}
	
	public void onViewRulesButtonClicked() {
		mainController.getMainFrame().setContentView(mainController.getTutorialScreenPanel());
	}

	public void onChatButtonClicked(Game game) {
		fillChat(game);

		Chat chatPanel = (Chat) mainController.getChatPanel();
		chatPanel.onChatView(game);

		mainController.getMainFrame().setContentView(chatPanel);
	}

	/**
	 * Return the controller that matches the round name
	 * 
	 * @param round
	 * @return
	 */
	private <T> TurnController getMatchingTurnController(Round round) {
		String roundName = round.getName();

		return roundName.equals("finale") ? mainController
				.getTurnFinaleController()
				: roundName.equals("ingelijst") ? mainController
						.getTurnIngelijstController() : roundName
						.equals("opendeur") ? mainController
						.getTurnOpenDeurController() : roundName
						.equals("puzzel") ? mainController
						.getTurnPuzzelController() : mainController
						.getTurnDrieZesNegenController();
	}

	/**
	 * Return the turn view that matches the round name
	 * 
	 * @param round
	 * @return
	 */
	private <T> TurnBasePanel getMatchingView(Round round) {
		String roundName = round.getName();

		return roundName.equals("finale") ? mainController.getTurnFinalePanel()
				: roundName.equals("ingelijst") ? mainController
						.getTurnIngelijstPanel()
						: roundName.equals("opendeur") ? mainController
								.getTurnOpenDeurPanel() : roundName
								.equals("puzzel") ? mainController
								.getTurnPuzzelPanel() : mainController
								.getTurnDrieZesNegenPanel();
	}

	/**
	 * Fetch all chat messages of the active games you're in
	 */
	public void fillChat(Game game) {
		ResultSet rs = databaseController
				.selectQuery("SELECT account_naam_zender, bericht, tijdstip, millisec FROM chatregel WHERE spel_id = "
						+ game.getId());

		try {
			rs.beforeFirst();
			
			if (!rs.next()) {
				return;
			}

			while (rs.next()) {
				game.addChatMessage(rs.getString("bericht"),
						rs.getString("account_naam_zender"),
						rs.getInt("tijdstip"), rs.getInt("millisec"));
			}
		} catch (SQLException e) {
			System.out
					.println("Er is iets misgegaan bij het ophalen van de chatberichten van spel "
							+ game.getId());
		}

		for (ChatObserver chatObserver : chatObservers) {
			chatObserver.onChatMessagesLoaded(game.getChatMessages());
		}

	}

	/**
	 * Send a chat message to the opponent of the current game
	 * 
	 * @param game
	 * @param message
	 */
	public void sendChatMessage(Game game, String message) {
		String sender = mainController.getLoginController().getAccount()
				.getUsername();
		databaseController
				.insertQuery("INSERT INTO chatregel (spel_id, account_naam_zender, bericht, tijdstip, millisec) VALUES('"
						+ game.getId()
						+ "', '"
						+ sender
						+ "', '"
						+ message
						+ "', CURRENT_TIMESTAMP(), 0)");

		// game.addChatMessage(message, sender);

		for (ChatObserver chatObserver : chatObservers) {
			chatObserver.onChatMessageAdded(message, sender, game);
		}
	}

}
