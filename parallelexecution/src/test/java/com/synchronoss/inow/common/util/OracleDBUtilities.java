package com.synchronoss.inow.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;

import com.synchronoss.inow.common.constants.Constants;
import com.synchronoss.inow.common.constants.PropertyFile;

public class OracleDBUtilities {

	public Logger logger = Logger.getLogger(OracleDBUtilities.class.getName());

	String Ordernum;
	public String TXID;
	public String Monitoring;
	public ArrayList<String> categoryNState = new ArrayList<String>();
	int size;
	String[] expectedValue;

	PropertyFile propertyFile = new PropertyFile();
	Constants constantValues = new Constants();

	public ResultSet rs;
	private Connection connection = null;

	public enum LogType {
		INFO, PASS, SOFTFAIL, WARNING, SCREENSHOT, UNCOMPLETED, TEXTLOGONLY, HARDFAIL
	};

	private void connect(String url, String userid, String password) {

		try {

			if (connection == null) {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				connection = DriverManager.getConnection(url, userid, password);
				logger.info("Oracle connection successful");
				logger.info("Connection url:" + url);
				logger.info("User id: " + userid + " Password: " + password);

			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("Couldn’t get the connection!");
			logger.info("Unable to connect to DB." + e.getMessage());
			logger.info("SQL Exception occured");
		}

		catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Couldn’t get the connection!" + ex.getMessage());
			logger.info(" Exception occured" + ex.getMessage());
			logger.info("Unable to connect to DB." + ex.getMessage());
		}

	}

	public void oraConnCleanUp() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
				logger.info("Oracel connection closed successfully");

			} catch (SQLException e1) {
				e1.printStackTrace();
				logger.info("oraConnCleanUp : " + e1.getMessage());
			}
		}
	}

	// To get the Query Results
	public List<HashMap<String, String>> getQueryResult(String sql) throws IOException {

		Properties readDbPropertiesFile = propertyFile.readProperFile(constantValues.getDdPropertyFilePath());

		List<HashMap<String, String>> resultSet = new ArrayList<HashMap<String, String>>();

		PreparedStatement ps = null;

		ResultSetMetaData rsmd = null;
		HashMap<String, String> map = null;
		String dbUrl, uid, pw = null;
		logger.info("Query: " + sql);

		try {

			dbUrl = readDbPropertiesFile.getProperty("dbUrl");
			uid = readDbPropertiesFile.getProperty("userName");
			pw = readDbPropertiesFile.getProperty("password");

			connect(dbUrl, uid, pw);

			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			rsmd = rs.getMetaData();
			while (rs.next()) {
				map = new HashMap<String, String>();
				for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
					String key = rsmd.getColumnName(i);
					String value = rs.getString(key);
					value = value == null ? "" : value;
					map.put(key, value);

				}
				resultSet.add(map);
			}
			logger.info(resultSet.size() + " record(s) fetched from DB");

		} catch (Exception e) {
			logger.info("Unable to execute the query");
			logger.info(e.getMessage());

		} finally {
			oraConnCleanUp();
			logger.info("Connection closed");
		}

		return resultSet;
	}

	// To Fetch all the Data
	public String[] formatDBData(String sql) {

		try {
			OracleDBUtilities db = new OracleDBUtilities();
			List<HashMap<String, String>> queryResult;

			queryResult = db.getQueryResult(sql);
			logger.info("Successfully executed query : " + sql);
			Iterator<HashMap<String, String>> queryResIterator = queryResult.iterator();
			size = queryResult.size();
			System.out.println("Size :" + size);
			int d = 0;
			expectedValue = new String[size];
			while (queryResIterator.hasNext()) {

				HashMap<String, String> queryResNext = queryResIterator.next();
				Iterator<Entry<String, String>> iteratorQueryResNxt = queryResNext.entrySet().iterator();
				while (iteratorQueryResNxt.hasNext()) {
					Entry<String, String> next = iteratorQueryResNxt.next();
					expectedValue[d] = next.getValue();
					d = d + 1;
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
			Reporter.log("DB :" + e.getMessage(), true);
		}

		return expectedValue;
	}

	public ArrayList<String> DB_Query_ToFetch_CatState(String sql) {

		try {

			OracleDBUtilities ora = new OracleDBUtilities();
			List<HashMap<String, String>> rs;

			rs = ora.getQueryResult(sql);
			categoryNState = new ArrayList<String>();

			Iterator<HashMap<String, String>> itra1 = rs.iterator();
			ArrayList<String> category = new ArrayList<String>();
			ArrayList<String> state = new ArrayList<String>();

			while (itra1.hasNext()) {
				HashMap<String, String> it1 = itra1.next();
				@SuppressWarnings("rawtypes")
				Iterator ex1 = it1.entrySet().iterator();

				while (ex1.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map.Entry entry1 = (Map.Entry) ex1.next();

					if (entry1.getValue() != null) {

					}

					if (entry1.getKey().equals("CATEGORY")) {
						category.add(entry1.getValue().toString());
					}

					if (entry1.getKey().equals("TRANS_COMMENT")) {
						state.add(entry1.getValue().toString());
					}

				}

			}
			for (int i = 0; i < category.size(); i++) {
				String ca = category.get(i);
				String sa = state.get(i);
				categoryNState.add(ca + ":" + sa);
			}

		} catch (IOException e) {
			e.printStackTrace();
			Reporter.log("DB Fetch Cat State" + e.getMessage(), true);
		}

		logger.info("categoryNState" + categoryNState);
		Reporter.log("categoryNState" + categoryNState);
		return categoryNState;

	}

	public String setQuery(String queryName, ArrayList<String> setValue) {

		try {

			File file = new File(constantValues.getDdPropertyFilePath());
			FileInputStream dbCredentialPropertyHandler = new FileInputStream(file);
			Properties dbQuery = new Properties();
			dbQuery.load(dbCredentialPropertyHandler);

			queryName = dbQuery.getProperty(queryName);
			logger.info("queryName:" + queryName);

			int k = 1;

			for (int i = 0; i < setValue.size(); i++) {
				queryName = queryName.replace("setValue" + k, setValue.get(i).toString());
				k++;
			}

			logger.info("Final query is :" + queryName);
			return queryName;

		} catch (NullPointerException nullPointerException) {
			logger.info(
					" you are trying to get data from object which is referenced null/empty, i.e driver/any other object is not yet instantiated properly ");
			Assert.fail(
					"you are trying to get data from object which is referenced null/empty, i.e driver/any other object is not yet instantiated properly ");
			return null;
		}

		catch (Exception e) {
			logger.info(e.getMessage());
			logger.info("there is an un expected exception while excecuting DataBase program");
			Assert.fail("there is an un expected exception while excecuting DataBase program");
			return null;
		}

	}

	public String setQueryForCompleted(String queryName, ArrayList<String> setValue) {
		FileInputStream dbCredentialPropertyHandler = null;
		try {

			File file = new File(constantValues.getDdPropertyFilePath());
			dbCredentialPropertyHandler = new FileInputStream(file);
			Properties dbQuery = new Properties();
			dbQuery.load(dbCredentialPropertyHandler);

			queryName = dbQuery.getProperty(queryName);
			logger.info("queryName:" + queryName);

			int k = 1;
			String finalParameters = "";
			for (int i = 0; i < setValue.size(); i++) {
				String param = setValue.get(i).toString();
				finalParameters = finalParameters + "'" + param + "',";

			}
			finalParameters = finalParameters.substring(0, finalParameters.length() - 1);
			logger.info("After generating query parameters ::" + finalParameters);
			queryName = queryName.replace("'setValue" + k + "'", finalParameters);
			logger.info("Final query is :" + queryName);
			return queryName;

		} catch (NullPointerException nullPointerException) {
			logger.info(
					" you are trying to get data from object which is referenced null/empty, i.e driver/any other object is not yet instantiated properly ");
			Assert.fail(
					"you are trying to get data from object which is referenced null/empty, i.e driver/any other object is not yet instantiated properly ");
			return null;
		}

		catch (Exception e) {
			logger.info(e.getMessage());
			logger.info("there is an un expected exception while excecuting DataBase program");
			Assert.fail("there is an un expected exception while excecuting DataBase program");
			return null;
		} finally {
			if (dbCredentialPropertyHandler != null) {
				try {
					dbCredentialPropertyHandler.close();
				} catch (IOException e) {
					logger.error("Exception while closing streams ", e);
				}
			}
		}

	}

	public void executeUpdateQueryAndCommit(String sql) {

		PreparedStatement stmt = null;

		try {

			Properties readDbPropertiesFile = propertyFile.readProperFile(constantValues.getDdPropertyFilePath());
			String url = readDbPropertiesFile.getProperty("dbUrl");
			String userName = readDbPropertiesFile.getProperty("userName");
			String pwd = readDbPropertiesFile.getProperty("password");

			connect(url, userName, pwd);
			connection.setAutoCommit(false);
			logger.info("Set auto commit to false");

			logger.info("Creating statement...");
			logger.info(sql);
			stmt = connection.prepareStatement(sql);

			int updateFlag = stmt.executeUpdate();

			logger.info("Update Flag : " + updateFlag);

			logger.info("Commiting data here....");
			connection.commit();

			stmt.close();

		} catch (SQLException e1) {

			e1.printStackTrace();
			logger.info(e1.getMessage());

			logger.info("Rolling back data here....");

			try {

				if (connection != null)
					connection.rollback();

				logger.info("Rolling back is successful");

			} catch (SQLException e2) {
				e2.printStackTrace();
				logger.info(e2.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());

		} finally {
			oraConnCleanUp();
			logger.info("Connection closed");

		}

	}
}
