package com.session;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import com.dbObjects.*;

public class SessionDataManager {
	private static int maxSize = 1000;
	@SuppressWarnings("serial")
	public static Map<String, SessionsObj> sessionData = Collections
			.synchronizedMap(new LinkedHashMap<String, SessionsObj>(16, 0.75f, true) {
				@Override
				protected boolean removeEldestEntry(Map.Entry<String, SessionsObj> eldest) {
					return size() > maxSize;
				}
			});

	@SuppressWarnings("serial")
	public static Map<Integer, UserDetailsObj> usersData = Collections
			.synchronizedMap(new LinkedHashMap<Integer, UserDetailsObj>(16, 0.75f, true) {
				@Override
				protected boolean removeEldestEntry(Map.Entry<Integer, UserDetailsObj> eldest) {
					return size() > maxSize;
				}
			});

	public static List<SessionChangeLog> sessionChangeLogs = Collections
			.synchronizedList(new java.util.ArrayList<SessionChangeLog>());

	public static void clearUser(int userId) {
		usersData.remove(userId);
	}

	public static boolean addUsertoSession(String session_id, UserDetailsObj u) {
		SessionsObj su = new SessionsObj();
		su.setUserId(u.getUserId());
		su.setCreatedTime(Instant.now().toEpochMilli());
		su.setLastAccessedTime(Instant.now().toEpochMilli());
		su.setSessionId(session_id);
		sessionData.put(session_id, su);
		usersData.putIfAbsent(u.getUserId(), u);
		return false;
	}

	public static boolean removeUserFromSession(String session_id, long lastUpdatedTime) {
		return sessionData.remove(session_id) != null;
	}

	public static int getUserwithId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String s_id = null;
		for (Cookie c : cookies) {
			if (c.getName().equals("session_id")) {
				s_id = c.getValue();
			}
		}
		return sessionData.get(s_id).getUserId();
	}

	public static Map<String, SessionsObj> getSessionMapforUpdate() {
		Map<String, SessionsObj> sessions = sessionData;
		sessionData = Collections.synchronizedMap(new LinkedHashMap<String, SessionsObj>(16, 0.75f, true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<String, SessionsObj> eldest) {
				return size() > maxSize;
			}
		});
		return sessions;
	}

	public static List<SessionChangeLog> getSessionChangeLogsforUpdate() {
		List<SessionChangeLog> logs = sessionChangeLogs;
		sessionChangeLogs = Collections.synchronizedList(new ArrayList<SessionChangeLog>());
		return logs;
	}
}
