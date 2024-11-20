//package com.notifier;
//
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.dao.NewDao;
//import com.loggers.AppLogger;
//
//public class UserDetailsUpdateNotifier {
//
//	public static void removeUserFromCache(String user_id) {
//		HttpURLConnection connection = null;
//		try {
//			List<HashMap<String,Object>> servers = NewDao.getRegisteredServers();
//			for(Map<String,Object> server : servers) {
//				URL url = new URL("http://"+server.get("server_name")+":"+server.get("port")+"/contacts/uru?action=removeUserFromCache&user_id="+user_id);
//				connection = (HttpURLConnection) url.openConnection();
//				connection.setRequestMethod("POST");
//				Integer response_code = connection.getResponseCode();
//				AppLogger.ApplicationLog("Removing user "+user_id+"from cache in all servers");
//				
//			}
//		}catch(Exception e){
//			AppLogger.ApplicationLog("Remove User from Cache method");
//			AppLogger.ApplicationLog(e);
//		}finally {
//			if(connection != null) {
//				connection.disconnect();
//			}
//		}
//	}
//
//}
