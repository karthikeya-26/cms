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

    // INSERT
    public boolean addRefreshTokenToUser(Integer userId, String refreshToken, String provider) throws DaoException {
        try {
            Insert i = new Insert();
            i.table(Table.ContactsSync).columns(ContactsSync.USER_ID, ContactsSync.REFRESH_TOKEN, ContactsSync.PROVIDER);
            i.values(userId.toString(), refreshToken, provider);
            return i.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error adding refresh token to user", e);
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
