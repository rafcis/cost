package core;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Process data from Sqlite database
 * 
 * @author Rafal
 * 
 */
public class SqlliteData implements Data {

	public static final String DRIVER = "org.sqlite.JDBC";
	public static final String DB_URL = "jdbc:sqlite:";

	private Connection conn;
	private Statement stat;
	private static final Logger logger = Logger.getLogger(SqlliteData.class);

	public SqlliteData(String fileName) {
		logger.info("File name: " + fileName);
		logger.debug("Driver: " + SqlliteData.DRIVER);
		try {
			Class.forName(SqlliteData.DRIVER);
			conn = DriverManager.getConnection(DB_URL + fileName);
			stat = conn.createStatement();
		} catch (ClassNotFoundException e) {
			logger.error("Missing JDBC deriver: " + e.toString());
		} catch (SQLException e) {
			logger.error("problem with connection: " + e.toString());
		}
	}

	/**
	 * Proccess data from Sqlite database
	 */
	@Override
	public void process() {
		logger.info("Start processing CSV file");
		try {
			List<Cost> list = getDate();
			Calculate calculate = new Calculate(list);
			LinkedHashMap<String, BigDecimal> data = calculate.sumByCategory();
			createAndClearResultTable();
			insertData(data);
			getResult();
			closeConnection();
		} catch (SQLException e) {
			logger.error("Process Sqlite db error: " + e.toString());
		}
		logger.info("End processing CSV file");

	}

	/**
	 * Print on screen data from table: wynik
	 * 
	 * @throws SQLException
	 */
	private void getResult() throws SQLException {
		ResultSet result = stat.executeQuery("SELECT * FROM wynik");
		while (result.next()) {
			float v;
			v = (float) result.getInt("value") / 100;
			String value = new BigDecimal(v).setScale(2,
					BigDecimal.ROUND_HALF_UP).toString();
			logger.info("Results, category: " + result.getString("category")
					+ ", value: " + value);
		}
	}

	/**
	 * Create if not exist table wynik and truncate table
	 * 
	 * @throws SQLException
	 */
	private void createAndClearResultTable() throws SQLException {
		stat.execute("CREATE TABLE IF NOT EXISTS wynik(category VARCHAR(255), value INTEGER);");
		stat.execute("DELETE FROM wynik");
		logger.info("Create and clean table wynik");

	}

	/**
	 * Insert data to table wynik
	 * 
	 * @param category
	 * @param value
	 * @throws SQLException
	 * @return true if record was added successfully, otherwise false
	 */
	public boolean insertResult(String category, Integer value)
			throws SQLException {
		PreparedStatement prepStmt = conn
				.prepareStatement("insert into wynik values (?, ?);");
		prepStmt.setString(1, category);
		prepStmt.setInt(2, value);
		logger.debug("Insert row into table wynik, category: " + category
				+ ", value: " + value.toString());
		return prepStmt.execute();
	}

	/**
	 * Save data in database
	 * 
	 * @param inputData
	 *            Data to save
	 * @throws SQLException
	 */
	public void insertData(LinkedHashMap<String, BigDecimal> inputData)
			throws SQLException {
		logger.debug("Insert row to table: wynik");
		for (String key : inputData.keySet()) {
			insertResult(key, inputData.get(key).intValue());
		}
	}

	/**
	 * Gets all the data for processing
	 * 
	 * @throws SQLException
	 * @return Cost list
	 */
	private List<Cost> getDate() throws SQLException {
		List<Cost> costList = new LinkedList<Cost>();
		logger.debug("Select data from table koszty");
		ResultSet result = stat.executeQuery("SELECT * FROM koszty");
		String category, currency, description;
		Integer value;
		while (result.next()) {
			category = result.getString("category");
			value = result.getInt("value");
			currency = result.getString("currency");
			description = result.getString("description");
			costList.add(new Cost(description, category, new BigDecimal(value),
					currency));
		}
		logger.debug("Coast list size: " + costList.size());
		return costList;
	}

	/**
	 * Close db connection
	 * 
	 * @throws SQLException
	 */
	private void closeConnection() throws SQLException {
		conn.close();
		logger.debug("Close db connection");
	}

}
