package com.dao;

import java.util.ArrayList;

import java.util.List;

import com.dbObjects.ContactsSyncObj;
import com.dbObjects.ResultObject;
import com.enums.ContactsSync;
import com.enums.Operators;
import com.enums.Table;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.QueryException;
import com.queryLayer.Select;
import com.queryLayer.Update;

public class ContactsSyncDao {

    // Select refresh tokens of a user
    public List<ContactsSyncObj> getUserSyncTokens(Integer userId) throws DaoException {
        List<ContactsSyncObj> tokens = new ArrayList<>();
        try {
            Select s = new Select();
            s.table(Table.ContactsSync).condition(ContactsSync.USER_ID, Operators.Equals, userId.toString());
            List<ResultObject> result = s.executeQuery(ContactsSyncObj.class);
            if (result.size() > 0) {
                for (ResultObject token : result) {
                    tokens.add((ContactsSyncObj) token);
                }
            }
        } catch (QueryException e) {
            throw new DaoException("Error fetching user sync tokens", e);
        }
        return tokens;
    }
    
    public ContactsSyncObj getSyncObjWithAccountId(String accountId, Integer userId) {
    	Select getSyncObj = new Select();
    	getSyncObj.table(Table.ContactsSync).condition(ContactsSync.ACCOUNT_ID, Operators.Equals, accountId)
    	.condition(ContactsSync.USER_ID, Operators.Equals, userId.toString());
    	try {
        	List<ResultObject> resultSet = getSyncObj.executeQuery(ContactsSyncObj.class);
			return resultSet.size()>0 ? (ContactsSyncObj) resultSet.get(0) : null;
		} catch (QueryException e) {
			return null;
		}
    	
    }
    //Select all tokens
    public List<ContactsSyncObj> getAllTokens () throws DaoException{
    	List<ContactsSyncObj> tokens = new ArrayList<>();
        try {
            Select s = new Select();
            s.table(Table.ContactsSync);
            List<ResultObject> result = s.executeQuery(ContactsSyncObj.class);
            if (result.size() > 0) {
                for (ResultObject token : result) {
                    tokens.add((ContactsSyncObj) token);
                }
            }
        } catch (QueryException e) {
            throw new DaoException("Error fetching user sync tokens", e);
        }
        return tokens;
    }
    
    //SELECT accouut Id

    // INSERT
    public boolean addRefreshTokenToUser(Integer userId, String accountId, String refreshToken, String provider) throws DaoException {
        try {
            Insert i = new Insert();
            i.table(Table.ContactsSync).columns(ContactsSync.USER_ID,ContactsSync.ACCOUNT_ID, ContactsSync.REFRESH_TOKEN, ContactsSync.PROVIDER);
            i.values(userId.toString(), accountId, refreshToken, provider);
            return i.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error adding refresh token to user", e);
        }
    }
    
    //UPDATE 
    public boolean updateRefreshToken(Integer userId, String refreshToken, Long lastUpdatedAt) throws DaoException {
    	Update updateRefreshToken = new Update();
    	if( userId == null || refreshToken == null || lastUpdatedAt == null) {
    		throw new DaoException("Please provide all values");
    	}
    	
    	updateRefreshToken.table(Table.ContactsSync).columns(ContactsSync.LAST_UPDATED_AT).values(lastUpdatedAt.toString())
    	.condition(ContactsSync.USER_ID, Operators.Equals, userId.toString())
    	.condition(ContactsSync.REFRESH_TOKEN, Operators.Equals, refreshToken);
    	
    	
    	try {
			 return updateRefreshToken.executeUpdate() >=0;
		} catch (QueryException e) {
			throw new DaoException(e.getMessage(),e);
		}
    	
    }

    // DELETE
    public boolean deleteRefreshToken(Integer userId, String refreshToken) throws DaoException {
        try {
            Delete d = new Delete();
            d.table(Table.ContactsSync).condition(ContactsSync.USER_ID, Operators.Equals, userId.toString())
                    .condition(ContactsSync.REFRESH_TOKEN, Operators.Equals, refreshToken);
            return d.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error deleting refresh token", e);
        }
    }

    // VALIDATION
    public boolean checkUserExists(String accountId) throws DaoException {
        try {
            Select s = new Select();
            s.table(Table.ContactsSync).columns(ContactsSync.ACCOUNT_ID).condition(ContactsSync.ACCOUNT_ID, Operators.Equals, accountId);
            return s.executeQuery().size() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error checking if user exists", e);
        }
    }
}
