package com.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.dao.DaoException;
import com.dao.ServersDao;
import com.dbObjects.ServersObj;
import com.loggers.AppLogger;

public class RequestSender {
	
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
				AppLogger.ApplicationLog("server Update of "+paramsAndVals +" status :"+response);
			}catch(Exception e) {
				AppLogger.ApplicationLog(e);
				e.printStackTrace();
			}
			
		}
		if(connection != null) {
			connection.disconnect();
		}
	}
}
