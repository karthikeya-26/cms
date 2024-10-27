package com.loggers;
import java.time.LocalDateTime;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ReqLogger {
	
	private static Logger logger = Logger.getLogger(ReqLogger.class.getName());
	static FileHandler fh;

    static {	
        try {
        	System.out.println("creating log file");
            fh = new FileHandler("/home/karthi-pt7680/contact_app_logs/"+LocalDateTime.now().toString()+".log");
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        }catch (Exception e) {
			// TODO: handle exception
        	System.out.println("could not initiate the logger file");
        	
		}
    }

    public static void AccessLog(String message) {
    	logger.info(message);
    }
    
}
