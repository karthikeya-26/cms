package com.session;

import java.util.List;
import java.util.Map;

import com.dao.DaoException;
import com.dao.SessionsDao;
import com.dbObjects.SessionsObj;
import com.loggers.AppLogger;


public class SessionFetcher {
	
	public static  void getSessionsFromDatabase() {
		
		SessionsDao dao = new SessionsDao();
		try {
			List<SessionsObj> sessionsFromDatabase = dao.getSessions();
			for(SessionsObj session : sessionsFromDatabase) {
				SessionDataManager.session_data.put(session.getSessionId(), session);
			}
		} catch (DaoException e) {
			AppLogger.ApplicationLog("Falied to fetch sessions from database");
		}
	}
}
