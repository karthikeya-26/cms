package com.util;

import java.time.Instant;
import java.util.logging.Level;

import com.enums.Operators;
import com.enums.Sessions;
import com.enums.Table;
import com.loggers.AppLogger;
import com.queryLayer.Delete;
import com.queryLayer.QueryException;

public class SchedulerTasks {
	 static AppLogger logger = new AppLogger(SchedulerTasks.class.getName());
	
	public static Runnable sessionUpdateTask = () -> {
		Delete deleteExpiredSessions = new Delete();
		deleteExpiredSessions.table(Table.Sessions);
		deleteExpiredSessions.condition(Sessions.CREATED_TIME, Operators.LessThan,  String.valueOf(Instant.now().toEpochMilli() - 30 * 60 * 1000));
		try {
			deleteExpiredSessions.executeUpdate();
		} catch (QueryException e) {
			logger.log(Level.SEVERE, e.getMessage(),e);		}		
	};
	
	public static Runnable authTokenDeletion = () -> {
		
	};
	
	public static Runnable accessTokenDeletion = () -> {
		
	};
}
