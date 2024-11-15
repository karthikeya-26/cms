package com.notifier;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dao.NewDao;
import com.loggers.AppLogger;
import com.session.SessionDataManager;
import com.startup.RegServer;

public class SessionmapUpdateNotifier {
	
	public static void sendSessionUpdate(String session_id, Long last_accessed_time) {
		HttpURLConnection connection = null;
		try {
			List<HashMap<String,Object>> servers = NewDao.getRegisteredServers();
//			System.out.println("Servers"+servers);
			for(Map<String, Object> server : servers) {
				URL url = new URL("http://"+server.get("server_name")+":"+server.get("port")+"/contacts/sru?action=SessionResourceUpdate"+"&session_id="+session_id+"&last_accessed_time="+last_accessed_time);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
			
//				connection.setRequestProperty("Content-Type", 
//						"application/x-www-form-urlencoded");
//				connection.setDoOutput(true);
				
//				String parameters = "&session_id=" + session_id + "&last_accessed_time=" + last_accessed_time;
//				System.out.println(parameters);
//				try(OutputStream os = connection.getOutputStream()) {
//					byte [] input = parameters.getBytes(StandardCharsets.UTF_8); 
//					os.write(input,0,input.length);
//				}
				
				Integer responseCode = connection.getResponseCode();
				System.out.println(responseCode);
				AppLogger.ApplicationLog("status of session update "+responseCode.toString());
			}
			
		}catch(Exception e) {
			AppLogger.ApplicationLog(RegServer.getServerName()+"session update in another server failed");
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
			List<HashMap<String,Object>> servers = NewDao.getRegisteredServers();
			for(Map<String,Object> server : servers) {
				URL url = new URL("http://"+server.get("server_name")+":"+server.get("port")+"/contacts/sru?action=SessionResourceDelete"+"&session_id="+sid);
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
