package com.loggers;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.dbconn.Database;


public class AppLogger {
    private Logger app_logger ;
    static FileHandler fh;
    
    static {
//    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	String logFile = "/home/karthi-pt7680/contact_app_logs/"+OffsetDateTime.now(ZoneId.of("UTC"))+"-"+Database.prop.getProperty("server_name")+":"+Database.prop.getProperty("server_port")+"-app.log";
    	try {
			fh = new FileHandler(logFile,true);
			fh.setFormatter(new SimpleFormatter());
		} catch (SecurityException | IOException e) {
			System.out.println("Initialization of app logger failed.");
			e.printStackTrace();
		}
    }
    
    public AppLogger(String className) {
    	this.app_logger = Logger.getLogger(className);
    	
    	this.app_logger.addHandler(fh);
    	this.app_logger.setUseParentHandlers(false);
    }
//    static {
//    	try {
//            System.out.println("Setting up app log file");
//
//            // Define a timestamp format for today's date only (to reuse the daily log file)
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String logFileName = "/home/karthi-pt7680/contact_app_logs/" 
//                    + LocalDateTime.now().format(formatter) 
//                    + Database.prop.getProperty("server_name")+"-"+Database.prop.getProperty("server_port") + "-app.log";
//
//            // Check if the log file for today already exists
//            if (Files.exists(Paths.get(logFileName))) {
//                System.out.println("Today's log file already exists. Reusing it.");
//            } else {
//                System.out.println("Creating a new log file for today.");
//            }
//            
//            // Create the FileHandler with the (existing or new) log file name
//            fh = new FileHandler(logFileName, true); // 'true' for append mode
//
//            // Set formatter and attach to the logger
//            fh.setFormatter(new SimpleFormatter());
//            app_logger.addHandler(fh);
//            app_logger.setUseParentHandlers(false);
//    	}
//         catch (Exception e) {
//            System.out.println("couldn't create app logging file: " + e.getMessage());
//            e.printStackTrace(); // Print the stack trace to understand what went wrong
//        }
//    }
    
    

    // Method to log exceptions
    public synchronized void log(Level exceptionLevel, String message,Exception e) {
        this.app_logger.log(exceptionLevel,message,e);
    }

    // Method to log general messages
    public synchronized void log(Level exceptionLevel, String message) {
        this.app_logger.log(exceptionLevel,message);
    }

    public static void closeFileHandler() {
    	if(fh!=null) {
    		fh.close();
    	}
    }
    
}
