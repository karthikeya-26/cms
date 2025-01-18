package com.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

import com.dao.DaoException;
import com.dao.ServersDao;
import com.dbObjects.ServersObj;
import com.loggers.AppLogger;

public class RequestSender {
	private static AppLogger logger = new AppLogger(RequestSender.class.getName());
	
	public void send(String paramsAndVals) {
		HttpURLConnection connection = null;
		ServersDao dao = new ServersDao();
		List<ServersObj> servers = null;
		try {
			servers = dao.getServers();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(ServersObj server : servers) {
			try {
				URL url = new URL("http://"+server.getName()+":"+server.getPort()+"/contacts/Update&"+paramsAndVals);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				Integer response = connection.getResponseCode();
	        	logger.log(Level.SEVERE, "server Update of "+paramsAndVals +" status :"+response);

			}catch(Exception e) {
				logger.log(Level.WARNING, e.getMessage(),e);
			}
			
		}
		if(connection != null) {
			connection.disconnect();
		}
	}
}
