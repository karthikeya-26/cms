package com.dbconn;

import com.loggers.AppLogger;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

public class Database {

	private static final ComboPooledDataSource cpds = new ComboPooledDataSource();
	public static final Properties AppProp = new Properties();
	private static AppLogger logger;

	static {
		loadDatabaseConfig();
		logger = new AppLogger(Database.class.getCanonicalName());
	}

	public static Connection getConnection() {
		try {
			return cpds.getConnection();
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Failed to obtain a database connection: " + e.getMessage(), e);
			return null;
		}
	}

	public static void reloadDatabaseConfig() {
		loadDatabaseConfig();
	}

	private static void loadDatabaseConfig() {
		try (InputStream input = Database.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new IOException("Properties file not found in resources folder.");
			}
			AppProp.load(input);

			cpds.setDriverClass(AppProp.getProperty("database_driver"));
			cpds.setJdbcUrl(AppProp.getProperty("database_url"));
			cpds.setUser(AppProp.getProperty("database_username"));
			cpds.setPassword(AppProp.getProperty("database_password"));
			cpds.setInitialPoolSize(getIntProperty("database_initialPoolSize"));
			cpds.setMinPoolSize(getIntProperty("database_minPoolSize"));
			cpds.setAcquireIncrement(getIntProperty("database_acquireIncrement"));
			cpds.setMaxPoolSize(getIntProperty("database_maxPoolSize"));
			cpds.setMaxStatements(getIntProperty("database_maxStatements"));
			cpds.setUnreturnedConnectionTimeout(getIntProperty("database_unreturnedConnectionTimeout"));
			cpds.setDebugUnreturnedConnectionStackTraces(
					getBooleanProperty("database_debugUnreturnedConnectionStackTraces"));

		} catch (IOException | PropertyVetoException e) {
			logger.log(Level.WARNING, "Failed to load database configuration: " + e.getMessage(), e);
		}
	}

	private static int getIntProperty(String key) {
		return Integer.parseInt(AppProp.getProperty(key, "0"));
	}

	private static boolean getBooleanProperty(String key) {
		return Boolean.parseBoolean(AppProp.getProperty(key, "false"));
	}

	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.log(Level.WARNING, "Failed to close connection: " + e.getMessage(), e);
			}
		}
	}

	public static void closeConnectionPool() {
		if (cpds != null) {
			cpds.close();
			logger.log(Level.INFO, "Connection pool closed.");
		}
	}
}
