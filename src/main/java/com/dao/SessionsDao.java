package com.dao;
import java.util.*;

import com.dbObjects.ResultObject;
import com.dbObjects.SessionsObj;
import com.enums.Operators;
import com.enums.Sessions;
import com.enums.Table;
import com.queryLayer.*;

public class SessionsDao {
	//SELECT ALL
	public List<SessionsObj> getSessions(){
		List<SessionsObj> sessions= new ArrayList<SessionsObj>();
		Select s = new Select();
		s.table(Table.Sessions);
		List<ResultObject> resultList  = s.executeQuery(SessionsObj.class);
		for(ResultObject session : resultList) {
			sessions.add((SessionsObj) session);
		}
		return sessions;
	}
	
	//SELECT ONE WITH ID
	public SessionsObj getSessionWithId(String sessionId) {
		Select s = new Select();
		s.table(Table.Sessions)
		.condition(Sessions.SESSION_ID, Operators.Equals, sessionId);
		List<ResultObject> result = s.executeQuery(SessionsObj.class);
		return result.size() > 0 ? (SessionsObj) result.get(0) : null;
	}
	
	//INSERT
	public boolean insertSession(String sessionId, Integer userId, Long createdTime, Long modifiedTime) {
		Insert i = new Insert();
		i.table(Table.Sessions)
		.columns(Sessions.SESSION_ID,Sessions.USER_ID)
		.values(sessionId,userId.toString());
		return i.executeUpdate() >0;
	}
	
	//UPDATE
	public boolean updateSession(String sessionId, Long lastAccessedTime) {
		Update u = new Update();
		u.table(Table.Sessions)
		.columns(Sessions.LAST_ACCESSED_TIME)
		.values(lastAccessedTime.toString())
		.condition(Sessions.SESSION_ID, Operators.Equals, sessionId);
		return u.executeUpdate() == 0;
	}
	
	//DELETE
	public boolean deleteSession(String sessionId) {
		Delete d = new Delete();
		d.table(Table.Sessions)
		.condition(Sessions.SESSION_ID, Operators.Equals, sessionId);
		return d.executeUpdate() > 0;
	}
	
	
}
