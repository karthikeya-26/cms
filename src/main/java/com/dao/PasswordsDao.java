package com.dao;

import java.util.List;


import com.dbObjects.*;
import com.enums.Operators;
import com.enums.Passwords;
import com.enums.Table;
import com.queryLayer.Insert;
import com.queryLayer.QueryException;
import com.queryLayer.Select;
import com.queryLayer.Update;

public class PasswordsDao {
	
    // SELECT
    public PasswordsObj getPasswordObjWithUserId(Integer userId) throws DaoException {
        PasswordsObj password = null;
        try {
            Select s = new Select();
            s.table(Table.Passwords).condition(Passwords.USER_ID, Operators.Equals, userId.toString());
            List<ResultObject> passwords = s.executeQuery(PasswordsObj.class);
            if (passwords.size() > 0) {
                password = (PasswordsObj) passwords.get(0);
            }
        } catch (QueryException e) {
            throw new DaoException("Error fetching password object for user ID: " + userId, e);
        }
        return password;
    }

    @Deprecated
    public String getPassword(Integer userId) throws DaoException {
        PasswordsObj password = getPasswordObjWithUserId(userId);
		return password.getPassword();
    }

    // INSERT
    public boolean createPassword(Integer userId, String password, Integer pwVersion) throws DaoException {
        try {
            Insert i = new Insert();
            i.table(Table.Passwords).columns(Passwords.USER_ID,Passwords.PASSWORD,Passwords.PASSWORD_VERSION).values(userId.toString(), password, pwVersion.toString());
            return i.executeUpdate() != 0;
        } catch (QueryException e) {
            throw new DaoException("Error creating password for user ID: " + userId, e);
        }
    }

    // UPDATE
    public boolean changePassword(Integer userId, String newPassword) throws DaoException {
        try {
            Update u = new Update();
            u.table(Table.Passwords).columns(Passwords.PASSWORD).values(newPassword)
                    .condition(Passwords.USER_ID, Operators.Equals, userId.toString());
            return u.executeUpdate() >= 0;
        } catch (QueryException e) {
            throw new DaoException("Error changing password for user ID: " + userId, e);
        }
    }

    // DELETE --> Password once set can't be removed unless account is deleted..
}
