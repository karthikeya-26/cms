package com.notifier;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.dao.ServersDao;
import com.dbObjects.ServersObj;
import com.dbconn.Database;
import com.loggers.AppLogger;
import com.session.SessionDataManager;

public class SessionmapUpdateNotifier {
	
	public static void sendSessionUpdate(String session_id, Long last_accessed_time) {
		HttpURLConnection connection = null;
		try {
			ServersDao dao = new ServersDao();
			List<ServersObj> servers = dao.getServers();
//			System.out.println("Servers"+servers);
			for(ServersObj server : servers) {
				
				if(server.getStatus() ==1) {
					URL url = new URL("http://"+server.getName()+":"+server.getPort()+"/contacts/sru?action=SessionResourceUpdate"+"&session_id="+session_id+"&last_accessed_time="+last_accessed_time);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
				
//					connection.setRequestProperty("Content-Type", 
//							"application/x-www-form-urlencoded");
//					connection.setDoOutput(true);
					
//					String parameters = "&session_id=" + session_id + "&last_accessed_time=" + last_accessed_time;
//					System.out.println(parameters);
//					try(OutputStream os = connection.getOutputStream()) {
//						byte [] input = parameters.getBytes(StandardCharsets.UTF_8); 
//						os.write(input,0,input.length);
//					}
					
					Integer responseCode = connection.getResponseCode();
					System.out.println(responseCode);
					AppLogger.ApplicationLog("status of session update "+responseCode.toString());
				}
			}
			
		}catch(Exception e) {
			AppLogger.ApplicationLog(Database.prop.getProperty("server_name")+"session update in another server failed");
			AppLogger.ApplicationLog(e);
			
		}finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
	}

	public static void removeSession(String sid) {
		HttpURLConnection connection = null;
		try {
			ServersDao dao = new ServersDao();
			
			List<ServersObj> servers = dao.getServers();
			for(ServersObj server : servers) {
				URL url = new URL("http://"+server.getName()+":"+server.getPort()+"/contacts/sru?action=SessionResourceDelete"+"&session_id="+sid);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				Integer response = connection.getResponseCode();
				AppLogger.ApplicationLog("Removing session "+sid+" from servers. Response :"+response);
				AppLogger.ApplicationLog(SessionDataManager.session_data.toString()+"AFTER DELETING THE SESSION");
			}
		}catch(Exception e) {
			AppLogger.ApplicationLog(e);
			
		}
	}
}
