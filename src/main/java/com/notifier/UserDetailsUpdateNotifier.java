package com.notifier;

import java.net.HttpURLConnection;

import java.net.URL;
import java.util.logging.Level;

import com.dao.ServersDao;
import com.dbObjects.ServersObj;
import com.loggers.AppLogger;

public class UserDetailsUpdateNotifier {
	private static AppLogger logger = new AppLogger(UserDetailsUpdateNotifier.class.getCanonicalName());

	public static void removeUserFromCache(String userId) {
		HttpURLConnection connection = null;
		try {
			ServersDao dao  = new ServersDao();
			
			for(ServersObj server : dao.getServers()) {
				URL url = new URL("http://"+server.getName()+":"+server.getPort()+"/contacts/uru?action=removeUserFromCache&user_id="+userId);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				Integer response_code = connection.getResponseCode();
				if(response_code == 200) {
					logger.log(Level.INFO, "s");
				}else {
					logger.log(Level.INFO, "Some thing went wrong while modifying cache in server :"+server.getServerId());
				}
				
			}
		}catch(Exception e){
			logger.log(Level.WARNING, e.getMessage(),e);
		}finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
	}

}
