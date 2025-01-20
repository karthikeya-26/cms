package com.listeners;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.dao.DaoException;
import com.dao.SessionsDao;
import com.dbObjects.SessionsObj;
import com.dbconn.Database;
import com.loggers.AppLogger;
import com.loggers.ReqLogger;
import com.session.SessionDataManager;
import com.session.SessionFetcher;
import com.session.SessionUpdater;
import com.util.SchedulerTasks;

/**
 * Application Lifecycle Listener implementation class UpdateSessionDetails
 *
 */
@WebListener
public class ScheduledTaskExecutor implements ServletContextListener {
	private static AppLogger logger = new AppLogger(ScheduledTaskExecutor.class.getCanonicalName());
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
	
    /**
     * Default constructor. 
     */
    public ScheduledTaskExecutor() {
        // TODO Auto-generated constructor stub
    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    	
    	System.out.println("shutting down");
    	
//    	Dao.updateSessionsToDatabase(SessionDataManager.session_data);
    	Map<String,SessionsObj> session_map = SessionDataManager.getSessionMapforUpdate();
    	SessionsDao dao = new SessionsDao();
    	for(Map.Entry<String, SessionsObj> session : session_map.entrySet()) {
    		try {
				dao.updateSession(session.getKey(), session.getValue().getLastAccessedTime());
			} catch (DaoException e) {
				logger.log(Level.WARNING, e.getMessage(),e);
				e.printStackTrace();
			}
    	}
    	
    	if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Failed to shut down scheduler cleanly", e);
                scheduler.shutdownNow();
            }
        }
    	
    	Database.closePool();
    	ReqLogger.closeFileHandler();
    	AppLogger.closeFileHandler();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	
    	
    		Connection c = Database.getConnection();
    	
        scheduler = Executors.newScheduledThreadPool(5);
        scheduler.scheduleAtFixedRate(SchedulerTasks.sessionDeletion, 0, 30, TimeUnit.MINUTES); // Configurable intervals
        scheduler.scheduleAtFixedRate(SchedulerTasks.authTokenDeletion, 5, 60, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(SchedulerTasks.accessTokenDeletion, 5, 60, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(SchedulerTasks.sessionUpdateTask, 5, 15, TimeUnit.MINUTES);
    }


	
}
