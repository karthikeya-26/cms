package com.listeners;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.dao.Dao;
import com.dbconn.Database;
import com.session.SessionDataManager;
import com.session.SessionFetcher;
import com.session.SessionUpdater;

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
    	
    	Dao.updateSessionsToDatabase(SessionDataManager.session_data);
    	
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
    	SessionFetcher.getSessionsFromDatabase();
    	SessionUpdater.startSessionUpdateTask();
    }
	
}
