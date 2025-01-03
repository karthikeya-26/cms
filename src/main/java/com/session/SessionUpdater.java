package com.session;

import java.util.Map;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.dao.SessionsDao;
import com.dbObjects.SessionsObj;
import com.loggers.AppLogger;

public class SessionUpdater {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startSessionUpdateTask() {
    	System.out.println(  "starting scheduler");
        scheduler.scheduleAtFixedRate(() -> {
            try {
            	System.out.println("updating session map to db");
            	Map<String, SessionsObj> session_data = SessionDataManager.getSessionMapforUpdate();
            	SessionsDao dao = new SessionsDao();
            	for(Map.Entry<String, SessionsObj> session: session_data.entrySet()) {
            		dao.updateSession(session.getKey(), session.getValue().getLastAccessedTime());
            	}
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

