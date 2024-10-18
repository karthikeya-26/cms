package com.session;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.dao.Dao;

public class SessionUpdater {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startSessionUpdateTask() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
            	
                Dao.updateSessionsToDatabase(SessionDataManager.session_data);
            } catch (Exception e) {
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

