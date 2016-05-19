package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DatabaseController {
	private final static String JDBC_DRIVER_MYSQL = "com.mysql.jdbc.Driver";
/*
	private final static String DATABASE_NAME = "raarts4_db2";
	private final static String USER = "raarts4";
	private final static String PASSWORD = "Ab12345";
*/
	private final static String DATABASE_NAME = "2015_soprj4_slimstemens";
	private final static String USER = "42IN04SOc"; 
	private final static String PASSWORD = "clientcontrols";

	private final static String URL = "jdbc:mysql://databases.aii.avans.nl/" + DATABASE_NAME;
	
	private Connection connection;

	private MainController mainController;

	public DatabaseController(MainController mainController) {
		this.mainController = mainController;
	}

	/**
	 * Make a connection with the database
	 */
	public void connect() {
		if (connection != null) {
			return;
		}

		try {
			Class.forName(JDBC_DRIVER_MYSQL);

			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mainController.getMainFrame(), "Er kon geen verbinding met de database worden gemaakt. Probeer het later nogmaals.", "Database error", JOptionPane.ERROR_MESSAGE);

			System.exit(0);
		}
	}

	/**
	 * Disconnect the database connection
	 */
	public void disconnect() {
		if (connection == null) {
			return;
		}

		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("De verbinding met de database kon niet worden gesloten.");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Executes a select query
	 * 
	 * @param queryString
	 *            The query
	 */
	public ResultSet selectQuery(String queryString) {
		ResultSet resultSet = null;

		try {
			Statement createdStatement = connection.createStatement();

			resultSet = createdStatement.executeQuery(queryString);
		} catch (SQLException e) {
			return null;
		}

		return resultSet;
	}

	/**
	 * Updates the database
	 * 
	 * @param queryString
	 *            The query
	 */
	public boolean insertQuery(String queryString) {		
		try {
			Statement createdStatement = connection.createStatement();
			createdStatement.executeUpdate(queryString);
			createdStatement.close();
		} catch (SQLException e) {
			return false;
		}

		return true;
	}
}
