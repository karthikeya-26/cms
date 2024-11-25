package com.session;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.dao.NewDao;
import com.dto.SessionData;
import com.loggers.AppLogger;

public class SessionUpdater {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startSessionUpdateTask() {
    	System.out.println("starting scheduler");
        scheduler.scheduleAtFixedRate(() -> {
            try {
            	System.out.println("updating session map to db");
            	Map<String, SessionData> session_data = SessionDataManager.getSessionMapforUpdate();
            	NewDao.updateSessionsToDatabase(session_data);
            } catch (Exception e) {
            	AppLogger.ApplicationLog(e);
                e.printStackTrace(); 
            }
        }, 1, 5, TimeUnit.MINUTES);  
    }

    

    public static void stopSessionUpdateTask() {
    	
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow(); }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}

