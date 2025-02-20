package com.listeners;

import java.net.InetAddress;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.logging.*;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
import com.util.SchedulerTasks;
import com.util.ShutDownTasks;
import com.util.StartUpTasks;

@WebListener
public class AppListener implements ServletContextListener {
//	private static Logger logger = Logger.getLogger(AppListener.class.getCanonicalName());
	private static Logger logger;
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

	public AppListener() {
		
	}

	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("shutting down");
		try {
			shutdownScheduler();
			flushSessionsToDb();
			new ShutDownTasks().deactiveServer(InetAddress.getLocalHost().getHostAddress(), Integer.parseInt(sce.getServletContext().getInitParameter("PortNumber")));
			Database.closeConnectionPool();
			ReqLogger.closeFileHandler();
			AppLogger.closeFileHandler();
		} catch (NumberFormatException | UnknownHostException | DaoException e) {
			e.printStackTrace();
		}
	}

	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("starting server");
		StartUpTasks startupTaskObject = new StartUpTasks();
		
		try {
			startupTaskObject.loadLoggingConfig(sce);
			logger = Logger.getLogger(AppListener.class.getCanonicalName());
			startupTaskObject.activateServer(InetAddress.getLocalHost().getHostAddress(), Integer.parseInt(sce.getServletContext().getInitParameter("PortNumber")));
			Connection c = Database.getConnection();
			Database.closeConnection(c);
			scheduler = Executors.newScheduledThreadPool(5);
			scheduler.scheduleAtFixedRate(SchedulerTasks.sessionDeletion, 0, 30, TimeUnit.MINUTES);
			scheduler.scheduleAtFixedRate(SchedulerTasks.authTokenDeletion, 5, 60, TimeUnit.MINUTES);
			scheduler.scheduleAtFixedRate(SchedulerTasks.accessTokenDeletion, 5, 60, TimeUnit.MINUTES);
			scheduler.scheduleAtFixedRate(SchedulerTasks.sessionUpdateTask, 5, 15, TimeUnit.MINUTES);
			scheduler.scheduleAtFixedRate(SchedulerTasks.contactsBackGroundSync, 2, 5, TimeUnit.DAYS);
		} catch (NumberFormatException | UnknownHostException | DaoException e) {
			e.printStackTrace();
		}
		
	}
	
	private void shutdownScheduler() {
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
	}

	private void flushSessionsToDb() {
		Map<String, SessionsObj> session_map = SessionDataManager.getSessionMapforUpdate();
		SessionsDao dao = new SessionsDao();
		for (Map.Entry<String, SessionsObj> session : session_map.entrySet()) {
			try {
				dao.updateSession(session.getKey(), session.getValue().getLastAccessedTime());
			} catch (DaoException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				e.printStackTrace();
			}
		}
	}
}
