package com.loggers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {
    private static Logger app_logger = Logger.getLogger(AppLogger.class.getName());
    static FileHandler fh;

    static {
        try {
            System.out.println("creating app log file");
            // Use a safe timestamp format to avoid issues with illegal characters
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            fh = new FileHandler("/home/karthi-pt7680/contact_app_logs/" + LocalDateTime.now().format(formatter) + "-app.log");
            
            // Set formatter and attach to the logger
            fh.setFormatter(new SimpleFormatter());
            app_logger.addHandler(fh);
            app_logger.setUseParentHandlers(false);
        } catch (Exception e) {
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
}
