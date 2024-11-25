package com.loggers;

import java.nio.file.Files;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.dbconn.Database;


public class AppLogger {
    private static Logger app_logger = Logger.getLogger(AppLogger.class.getName());
    static FileHandler fh;

    static {
    	try {
            System.out.println("Setting up app log file");

            // Define a timestamp format for today's date only (to reuse the daily log file)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String logFileName = "/home/karthi-pt7680/contact_app_logs/" 
                    + LocalDateTime.now().format(formatter) 
                    + Database.prop.getProperty("serve_name")+"-"+Database.prop.getProperty("server_port") + "-app.log";

            // Check if the log file for today already exists
            if (Files.exists(Paths.get(logFileName))) {
                System.out.println("Today's log file already exists. Reusing it.");
            } else {
                System.out.println("Creating a new log file for today.");
            }
            
            // Create the FileHandler with the (existing or new) log file name
            fh = new FileHandler(logFileName, true); // 'true' for append mode

            // Set formatter and attach to the logger
            fh.setFormatter(new SimpleFormatter());
            app_logger.addHandler(fh);
            app_logger.setUseParentHandlers(false);
    	}
         catch (Exception e) {
            System.out.println("couldn't create app logging file: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace to understand what went wrong
        }
    }

    // Method to log exceptions
    public static void ApplicationLog(Exception e) {
        app_logger.warning(e.toString());
    }

    // Method to log general messages
    public static void ApplicationLog(String message) {
        app_logger.info(message);
    }
    
    public static void closeFileHandler() {
    	if(fh!=null) {
    		fh.close();
    	}
    }
}
