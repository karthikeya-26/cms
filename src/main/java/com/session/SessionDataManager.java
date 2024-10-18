package com.session;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.models.SessionData;



import com.models.User;

public class SessionDataManager {
	private static int maxSize = 1000;
	@SuppressWarnings("serial")
	public static Map<String, SessionData> session_data = Collections.synchronizedMap(
            new LinkedHashMap<String, SessionData>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, SessionData> eldest) {
                    return size() > maxSize; // Remove the eldest entry if the size exceeds maxSize
                }
            }
        );		
	@SuppressWarnings("serial")
	public static Map<Integer, User> users_data =  Collections.synchronizedMap(
            new LinkedHashMap<Integer, User>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, User> eldest) {
                    return size() > maxSize; // Remove the eldest entry if the size exceeds maxSize
                }
            }
        );		
		
		public static void clearUser(int userID) {
			users_data.remove(userID);
		}
		
		
		public synchronized static  boolean addUsertoSession(String session_id,User u) {
			SessionData su = new SessionData();
			su.setUser_id(u.getUser_id());
			su.setCreated_time(System.currentTimeMillis());
			su.setLast_accessed_at(System.currentTimeMillis());
			su.setExpires_at(System.currentTimeMillis()+1800000);
			session_data.put(session_id,su);                                                                                 
			users_data.putIfAbsent(u.getUser_id(), u);
			System.out.println(session_data);
			
			return false; 
		}
		
		public static  synchronized boolean removeUserFromSession(String session_id) {
			return session_data.remove(session_id)!=null;
			
		}
		
		public synchronized static SessionData getUserwithId(String s_id) {
			return session_data.get(s_id);
		}
		
		public static synchronized void clearSessionData() {
			session_data = new ConcurrentHashMap<String, SessionData>();
		}
		
		public static synchronized boolean  checkSid(String sid) {
			return session_data.containsKey(sid);
		}
}
