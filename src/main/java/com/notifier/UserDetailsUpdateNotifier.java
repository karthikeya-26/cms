package com.notifier;

import java.net.HttpURLConnection;

import java.net.URL;

import com.dao.ServersDao;
import com.dbObjects.ServersObj;
import com.loggers.AppLogger;

public class UserDetailsUpdateNotifier {

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
					AppLogger.ApplicationLog("Removed user "+userId+" from all servers cache successfully.");
				}else {
					AppLogger.ApplicationLog("Something went wrong while removing user "+userId+" from servers cache. Response code :"+response_code);
				}
				
			}
		}catch(Exception e){
			AppLogger.ApplicationLog("Remove User from Cache method");
			AppLogger.ApplicationLog(e);
		}finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
	}

}
