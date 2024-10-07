package com.session;
import com.models.SessionUserData;


import java.util.HashMap;


import com.models.User;

public class SessionData {
		private static HashMap<String, SessionUserData> session_data = new HashMap<String, SessionUserData>();
		
		public synchronized static  boolean addUsertoSession(String session_id,User u) {
			SessionUserData su = new SessionUserData();
			su.setUser(u);
			su.setCreated_time(System.currentTimeMillis());
			su.setLast_accessed_at(System.currentTimeMillis());
			su.setExpires_at(System.currentTimeMillis()+1800000);
			
			
			
			session_data.put(session_id,su);
			System.out.println(session_data);
			
			return false;
		}
		
		public static  synchronized boolean removeUserFromSession(String session_id) {
			return session_data.remove(session_id)!=null;
			
		}
		
		public synchronized static SessionUserData getUserwithId(String s_id) {
			return session_data.get(s_id);
		}
		
		public static synchronized void clearSessionData() {
			session_data = new HashMap<String, SessionUserData>();
		}
		
		public static synchronized boolean  checkSid(String sid) {
			return session_data.containsKey(sid);
		}
}
