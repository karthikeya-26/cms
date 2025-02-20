package com.loggers;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.dbconn.Database;

public class AppLogger {
	private Logger app_logger;
	private static FileHandler fileHandler;
	private static final String DEFAULT_LOG_PATH = "/home/karthi-pt7680/contact_app_logs/"+Database.AppProp.getProperty("server_name")+":"+Database.AppProp.getProperty("server_port")+"-";

	static {
		try {
			// Use a simpler initialization that doesn't depend on Database
			String logFile = DEFAULT_LOG_PATH + OffsetDateTime.now(ZoneId.of("UTC")) + "-app.log";
			fileHandler = new FileHandler(logFile, true);
			fileHandler.setFormatter(new SimpleFormatter());
		} catch (SecurityException | IOException e) {
			System.err.println("Initialization of app logger failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public AppLogger(String className) {
        this.app_logger = Logger.getLogger(className);
        if (fileHandler != null) {
            this.app_logger.addHandler(fileHandler);
            this.app_logger.setUseParentHandlers(false);
        }
	}

	public synchronized void log(Level exceptionLevel, String message, Exception e) {
		this.app_logger.log(exceptionLevel, message, e);
	}

	public synchronized void log(Level exceptionLevel, String message) {
		this.app_logger.log(exceptionLevel, message);
	}

	public static void closeFileHandler() {
		if (fileHandler != null) {
			fileHandler.close();
		}
	}

	

}
