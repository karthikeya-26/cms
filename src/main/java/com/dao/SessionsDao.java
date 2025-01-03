package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.dbObjects.ResultObject;
import com.dbObjects.SessionsObj;
import com.enums.Operators;
import com.enums.Sessions;
import com.enums.Table;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.QueryException;
import com.queryLayer.Select;
import com.queryLayer.Update;

public class SessionsDao {

    // SELECT ALL
    public List<SessionsObj> getSessions() throws DaoException {
        List<SessionsObj> sessions = new ArrayList<>();
        try {
            Select s = new Select();
            s.table(Table.Sessions);
            List<ResultObject> resultList = s.executeQuery(SessionsObj.class);
            for (ResultObject session : resultList) {
                sessions.add((SessionsObj) session);
            }
        } catch (QueryException e) {
            throw new DaoException("Error fetching sessions", e);
        }
        return sessions;
    }

    // SELECT ONE WITH ID
    public SessionsObj getSessionWithId(String sessionId) throws DaoException {
        try {
            Select s = new Select();
            s.table(Table.Sessions)
                    .condition(Sessions.SESSION_ID, Operators.Equals, sessionId);
            List<ResultObject> result = s.executeQuery(SessionsObj.class);
            return result.size() > 0 ? (SessionsObj) result.get(0) : null;
        } catch (QueryException e) {
            throw new DaoException("Error fetching session with ID: " + sessionId, e);
        }
    }

    // INSERT
    public boolean insertSession(String sessionId, Integer userId, Long createdTime, Long lastAccessedTime) throws DaoException {
        try {
            Insert i = new Insert();
            i.table(Table.Sessions)
                    .columns(Sessions.SESSION_ID, Sessions.USER_ID, Sessions.CREATED_TIME, Sessions.LAST_ACCESSED_TIME )
                    .values(sessionId, userId.toString(), createdTime.toString(), lastAccessedTime.toString());
            return i.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error inserting session with ID: " + sessionId, e);
        }
    }

    // UPDATE
    public boolean updateSession(String sessionId, Long lastAccessedTime) throws DaoException {
        try {
            Update u = new Update();
            u.table(Table.Sessions)
                    .columns(Sessions.LAST_ACCESSED_TIME)
                    .values(lastAccessedTime.toString())
                    .condition(Sessions.SESSION_ID, Operators.Equals, sessionId);
            return u.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error updating session with ID: " + sessionId, e);
        }
    }

    // DELETE
    public boolean deleteSession(String sessionId) throws DaoException {
        try {
            Delete d = new Delete();
            d.table(Table.Sessions)
                    .condition(Sessions.SESSION_ID, Operators.Equals, sessionId);
            return d.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error deleting session with ID: " + sessionId, e);
        }
    }
}
