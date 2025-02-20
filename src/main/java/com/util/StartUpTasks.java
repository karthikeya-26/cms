package com.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContextEvent;

import com.dao.DaoException;
import com.dao.ServersDao;
import com.dbObjects.ResultObject;
import com.dbObjects.ServersObj;
import com.dbObjects.SessionsObj;
import com.enums.Operators;
import com.enums.Sessions;
import com.enums.Table;
import com.queryLayer.QueryException;
import com.queryLayer.Select;
import com.session.SessionDataManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
public class StartUpTasks {
	private static final Logger logger = Logger.getLogger(StartUpTasks.class);

	public void activateServer(String serverName, Integer port) throws DaoException {
		
		ServersDao serverDao = new ServersDao();
		ServersObj server = serverDao.getServer(serverName, port);
		if(server == null) {
			serverDao.addServer(serverName, port.toString());
		}else {
			serverDao.updateStatus(serverName, port, 1);
		}
			
	}

	public Runnable getValidSessionsFromDatabase = () -> {

		Select validSessionsQuery = new Select();
		validSessionsQuery.table(Table.Sessions).condition(Sessions.LAST_ACCESSED_TIME, Operators.GreaterThanOrEqualTo,
				String.valueOf(Instant.now().toEpochMilli() - 30 * 60 * 1000));
		try {
			List<ResultObject> validSessions = validSessionsQuery.executeQuery(SessionsObj.class);
			for (ResultObject session : validSessions) {
				SessionsObj sess = (SessionsObj) session;
				SessionDataManager.sessionData.put(sess.getSessionId(), sess);
			}
		} catch (QueryException e) {
			e.printStackTrace();
		}

	};

	public void loadLoggingConfig(ServletContextEvent sce) {

		File logDir = new File("logs");
		if(!logDir.exists()) {
			logDir.mkdir();
		} 
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		System.setProperty("app.logfile.name", "logs/app-"+date.format(new Date())+".log");
		System.getProperty("app.logfile.name");
		System.setProperty("access.logfile.name", "logs/access-"+date.format(new Date())+".log");

		PropertyConfigurator.configure(StartUpTasks.class.getClassLoader().getResourceAsStream("logging.properties"));
	}

	
}
