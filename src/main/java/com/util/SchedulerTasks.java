package com.util;

import java.time.Instant;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.dao.ContactsSyncDao;
import com.dao.DaoException;
import com.dao.SessionsDao;
import com.dbObjects.ContactsSyncObj;
import com.dbObjects.SessionsObj;
import com.enums.AccessTokens;
import com.enums.AuthorizationCodes;
import com.enums.Operators;
import com.enums.Sessions;
import com.enums.Table;
import com.handlers.GoogleContactsSyncHandler;
import com.queryLayer.Delete;
import com.queryLayer.QueryException;
import com.session.SessionDataManager;

public class SchedulerTasks {
	private static Logger logger =  Logger.getLogger(SchedulerTasks.class.getCanonicalName());
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
		Thread.currentThread().setName("SessionUpdateTask");
		try {
			Map<String, SessionsObj> session_data = SessionDataManager.getSessionMapforUpdate();
			SessionsDao dao = new SessionsDao();
			for (Map.Entry<String, SessionsObj> session : session_data.entrySet()) {
				dao.updateSession(session.getKey(), session.getValue().getLastAccessedTime());
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);

		}

	};

	public static Runnable authTokenDeletion = () -> {
		Thread.currentThread().setName("AuthTokenDeletionTask");

		Delete deleteExpiredAuthTokens = new Delete();
		deleteExpiredAuthTokens.table(Table.AuthorizationCodes).condition(AuthorizationCodes.CREATED_AT,
				Operators.LessThan, String.valueOf(Instant.now().toEpochMilli() - 60 * 60 * 1000));

		try {
			deleteExpiredAuthTokens.executeUpdate();
		} catch (QueryException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			e.printStackTrace();
		}

	};

	public static Runnable accessTokenDeletion = () -> {
		Thread.currentThread().setName("AccessTokenDeletionTask");
		Delete deleteExpiredAccessTokens = new Delete();
		deleteExpiredAccessTokens.table(Table.AccessTokens).condition(AccessTokens.CREATED_AT, Operators.LessThan,
				String.valueOf(Instant.now().toEpochMilli() - 60 * 60 * 1000));
		try {
			deleteExpiredAccessTokens.executeUpdate();
		} catch (QueryException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			e.printStackTrace();
		}

	};

	public static Runnable sessionDeletion = () -> {
		Thread.currentThread().setName("SessionDeletion");
		Delete deleteExpiredSessions = new Delete();
		deleteExpiredSessions.table(Table.Sessions).condition(Sessions.CREATED_TIME, Operators.LessThan,
				String.valueOf(Instant.now().toEpochMilli() - 30 * 60 * 1000));
		try {
			deleteExpiredSessions.executeUpdate();
		} catch (QueryException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			e.printStackTrace();
		}
	};

	public static Runnable contactsBackGroundSync = () -> {
		Thread.currentThread().setName("ContactsBackgroundSyncTask");
		ContactsSyncDao syncDao = new ContactsSyncDao();
		List<ContactsSyncObj> allSyncAccounts = null;
		try {
			allSyncAccounts = syncDao.getAllTokens();
		} catch (DaoException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			e.printStackTrace();
		}
		if (allSyncAccounts != null && !allSyncAccounts.isEmpty()) {
			ExecutorService pool = Executors.newCachedThreadPool();
			for (ContactsSyncObj synObj : allSyncAccounts) {
				System.out.println("syn obj :" + synObj);
				if (synObj.getLastUpdatedAt() < Instant.now().toEpochMilli() - 86400000) {
					Future<?> future = pool.submit(() -> {
						Thread.currentThread().setName("ContactsSync");
						GoogleContactsSyncHandler h = new GoogleContactsSyncHandler();
						String accessToken = h.getAccessToken(synObj.getRefreshToken());
						h.getAndHandleContacts(synObj, accessToken, null);
					});
					try {
						future.get();
						syncDao.updateRefreshToken(synObj.getUserId(), synObj.getRefreshToken(),Instant.now().toEpochMilli());
					} catch (InterruptedException | ExecutionException | DaoException e) {
						e.printStackTrace();
					}
				}

			}
			pool.shutdown();
		}
	};

}
