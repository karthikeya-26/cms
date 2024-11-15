package com.listeners;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.dao.Dao;
import com.dao.NewDao;
import com.dbconn.Database;
import com.loggers.AppLogger;
import com.loggers.ReqLogger;
import com.models.SessionData;
import com.session.SessionDataManager;
import com.session.SessionFetcher;
import com.session.SessionUpdater;
import com.startup.RegServer;

/**
 * Application Lifecycle Listener implementation class UpdateSessionDetails
 *
 */
@WebListener
public class UpdateSessionDetails implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public UpdateSessionDetails() {
        // TODO Auto-generated constructor stub
    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    	
    	System.out.println("shutting down");
    	
//    	Dao.updateSessionsToDatabase(SessionDataManager.session_data);
    	Map<String,SessionData> session_map = SessionDataManager.getSessionMapforUpdate();
    	NewDao.updateSessionsToDatabase(session_map);
    	RegServer.deregister_server_in_db();
    	Database.closePool();
    	ReqLogger.closeFileHandler();
    	AppLogger.closeFileHandler();
    	SessionUpdater.stopSessionUpdateTask();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    	System.out.println("app starting");
    	
    	try(Connection c =Database.getConnection()){
    		
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	RegServer.readProp();
    	
    	AppLogger.ApplicationLog("Created app log file");
    	RegServer.register_server_in_db();
    	
    	SessionFetcher.getSessionsFromDatabase();
    	SessionUpdater.startSessionUpdateTask();
    }
	
}
