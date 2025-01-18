package com.session;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.dao.DaoException;
import com.dao.SessionsDao;
import com.dbObjects.SessionsObj;
import com.loggers.AppLogger;


public class SessionFetcher {
	
	private static AppLogger logger = new AppLogger(SessionFetcher.class.getCanonicalName());
	public static  void getSessionsFromDatabase() {
		
		SessionsDao dao = new SessionsDao();
		try {
			List<SessionsObj> sessionsFromDatabase = dao.getSessions();
			for(SessionsObj session : sessionsFromDatabase) {
				SessionDataManager.session_data.put(session.getSessionId(), session);
			}
		} catch (DaoException e) {
			logger.log(Level.WARNING, e.getMessage(),e);
		}
	}
}
