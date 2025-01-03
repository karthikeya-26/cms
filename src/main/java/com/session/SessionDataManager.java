package com.session;
import java.time.Instant;
import java.util.Collections;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dbObjects.*;
import com.dto.SessionData;


public class SessionDataManager {
	private static int maxSize = 1000;
	@SuppressWarnings("serial")
	public static Map<String, SessionsObj> session_data = Collections.synchronizedMap(
            new LinkedHashMap<String, SessionsObj>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, SessionsObj> eldest) {
                    return size() > maxSize; // Remove the eldest entry if the size exceeds maxSize
                }
            }
        );		
	
	@SuppressWarnings("serial")
	public static Map<Integer, UserDetailsObj> users_data =  Collections.synchronizedMap(
            new LinkedHashMap<Integer, UserDetailsObj>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, UserDetailsObj> eldest) {
                    return size() > maxSize; // Remove the eldest entry if the size exceeds maxSize
                }
            }
        );		
		
		public static void clearUser(int userId) {
			users_data.remove(userId);
		}
		
		
		public synchronized static  boolean addUsertoSession(String session_id,UserDetailsObj u) {
			SessionsObj su = new SessionsObj();
			su.setUserId(u.getUserId());
			su.setCreatedTime(Instant.now().toEpochMilli());
			su.setLastAccessedTime(Instant.now().toEpochMilli());
			su.setSessionId(session_id);
			session_data.put(session_id,su);                                                                                 
			users_data.putIfAbsent(u.getUserId(), u);
			return false;
		}
		
		public static  synchronized boolean removeUserFromSession(String session_id, long lastUpdatedTime) {
//			if session_data.containsKey(session_id){
//				session_data.get(session_id).setLast_accessed_at(lastUpdatedTime);
			return session_data.remove(session_id)!=null;
		}
		public synchronized static SessionsObj getUserwithId(String s_id) {
			return session_data.get(s_id);
		}
		
		public static synchronized void clearSessionData() {
			session_data = new ConcurrentHashMap<String, SessionsObj>();
		}
		
		public static synchronized boolean  checkSid(String sid) {
			return session_data.containsKey(sid);
		}
		public static Map<String,SessionsObj> getSessionMapforUpdate(){
			Map<String,SessionsObj> sessions = session_data;
			session_data = Collections.synchronizedMap(
		            new LinkedHashMap<String, SessionsObj>(16, 0.75f, true) {
		                private static final long serialVersionUID = 1L;

						@Override
		                protected boolean removeEldestEntry(Map.Entry<String, SessionsObj> eldest) {
		                    return size() > maxSize; // Remove the eldest entry if the size exceeds maxSize
		                }
		            }
		        );
			return sessions;
		}
}
