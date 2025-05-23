package com.loggers;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.dbconn.Database;

public class ReqLogger {
	
	private static Logger logger = Logger.getLogger(ReqLogger.class.getName());
	static FileHandler fh;

    static {	
        try {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String logFileName = "/home/karthi-pt7680/contact_logs/" 
                    + LocalDateTime.now().format(formatter) 
                    + Database.AppProp.getProperty("serve_name")+"-"+Database.AppProp.getProperty("server_port") + "-request.log";
            fh = new FileHandler(logFileName,true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        }catch (Exception e) {
        	e.printStackTrace();
        	System.out.println("could not initiate the logger file");
		}
    }

    public static void AccessLog(String message) {
    	logger.info(message);
    }
    
    public static void closeFileHandler() {
    	if(fh!= null) {
    		fh.close();
    	}
    }
    
}
