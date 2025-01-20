package com.util;

import java.time.Instant;
import java.util.Map;
import java.util.logging.Level;

import com.dao.SessionsDao;
import com.dbObjects.SessionsObj;
import com.enums.AccessTokens;
import com.enums.AuthorizationCodes;
import com.enums.Operators;
import com.enums.Sessions;
import com.enums.Table;
import com.loggers.AppLogger;
import com.queryLayer.Delete;
import com.queryLayer.QueryException;
import com.session.SessionDataManager;

public class SchedulerTasks {
	static AppLogger logger = new AppLogger(SchedulerTasks.class.getName());

	public static Runnable sessionUpdateTask = () -> {
//		Delete deleteExpiredSessions = new Delete();
//		deleteExpiredSessions.table(Table.Sessions);
//		deleteExpiredSessions.condition(Sessions.CREATED_TIME, Operators.LessThan,
//				String.valueOf(Instant.now().toEpochMilli() - 60 * 60 * 1000));
//		try {
//			deleteExpiredSessions.executeUpdate();
//		} catch (QueryException e) {
//			logger.log(Level.SEVERE, e.getMessage(), e);
//		}
		 try {
         	Map<String, SessionsObj> session_data = SessionDataManager.getSessionMapforUpdate();
         	SessionsDao dao = new SessionsDao();
         	for(Map.Entry<String, SessionsObj> session: session_data.entrySet()) {
         		dao.updateSession(session.getKey(), session.getValue().getLastAccessedTime());
         	}
         } catch (Exception e) {
         	logger.log(Level.INFO, e.getMessage(),e);
          
         }
		
	};

	public static Runnable authTokenDeletion = () -> {
		Delete deleteExpiredAuthTokens = new Delete();
		deleteExpiredAuthTokens.table(Table.AuthorizationCodes).condition(AuthorizationCodes.CREATED_AT,
				Operators.LessThan, String.valueOf(Instant.now().toEpochMilli() - 60 * 60 * 1000));

		try {
			deleteExpiredAuthTokens.executeUpdate();
		} catch (QueryException e) {
			logger.log(Level.WARNING, e.getMessage(),e);
			e.printStackTrace();
		}

	};

	public static Runnable accessTokenDeletion = () -> {
		Delete deleteExpiredAccessTokens = new Delete();
		deleteExpiredAccessTokens.table(Table.AccessTokens)
		.condition(AccessTokens.CREATED_AT, Operators.LessThan, String.valueOf(Instant.now().toEpochMilli()-60*60*1000));
		try {
			deleteExpiredAccessTokens.executeUpdate();
		} catch (QueryException e) {
			logger.log(Level.WARNING, e.getMessage(),e);
			e.printStackTrace();
		}
		
	};
	
	public static Runnable sessionDeletion = () ->{
		Delete deleteExpiredSessions = new Delete();
		deleteExpiredSessions.table(Table.Sessions)
		.condition(Sessions.CREATED_TIME, Operators.LessThan, String.valueOf(Instant.now().toEpochMilli()-30*60*1000));
		try {
			deleteExpiredSessions.executeUpdate();
		} catch (QueryException e) {
			logger.log(Level.WARNING, e.getMessage(),e);
			e.printStackTrace();
		}
	};
}
