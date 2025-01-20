package com.dao;

import java.util.List;

import com.dbObjects.ResultObject;
import com.dbObjects.UserDetailsObj;
import com.enums.Joins;
import com.enums.Operators;
import com.enums.Table;
import com.enums.UserDetails;
import com.enums.UserMails;
import com.queryBuilder.BuildException;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.QueryException;
import com.queryLayer.Select;
import com.queryLayer.Update;

public class UserDetailsDao {
    
    // SELECT -> User with userId
    public UserDetailsObj getUserWithId(Integer userId) throws DaoException {
        UserDetailsObj user = null;
        try {
            Select s = new Select();
            s.table(Table.UserDetails)
             .condition(UserDetails.USER_ID, Operators.Equals, userId.toString());
            List<ResultObject> users = s.executeQuery(UserDetailsObj.class);
            if (users.size() > 0) {
                user = (UserDetailsObj) users.get(0);
            }
        } catch (QueryException e) {
            throw new DaoException("Error fetching user with ID: " + userId, e);
        }
        return user;
    }

    // SELECT -> User with mail
    public UserDetailsObj getUserWithMail(String mail) throws DaoException {
        UserDetailsObj user = null;
        try {
            Select s = new Select();
            s.table(Table.UserDetails).columns(UserDetails.ALL_COLS)
             .join(Joins.InnerJoin, Table.UserMails, UserMails.USER_ID, Operators.Equals, Table.UserDetails, UserDetails.USER_ID)
             .condition(UserMails.MAIL, Operators.Equals, mail);
      
            List<ResultObject> users = s.executeQuery(UserDetailsObj.class);
            if (users.size() > 0) {
                user = (UserDetailsObj) users.get(0);
            }
        } catch (QueryException e) {
        	System.out.println(e.getMessage());
            throw new DaoException("Error fetching user with mail: " + mail, e);
        }
        return user;
    }
    
    //SELECT -> user with accountId
    public UserDetailsObj getUserWithAccountId(String accountId) {
    	UserDetailsObj user = null;
    	try {
    		Select getUserWithAcId = new Select();
    		getUserWithAcId.table(Table.UserDetails)
    		.condition(UserDetails.PROVIDER_AC_ID, Operators.Equals,accountId);
    		List<ResultObject> users = getUserWithAcId.executeQuery(UserDetailsObj.class);
    		if(users.size()>0) {
    			user = (UserDetailsObj) users.get(0);
    		}
    	}catch(QueryException e) {
    		return null;
    	}
    	return user;
    }
    
    

    // INSERT
    public int createUser(String userName, String firstName, String lastName, String contactType) throws DaoException {
        try {
            Insert i = new Insert();
            i.table(Table.UserDetails)
             .columns(UserDetails.USER_NAME, UserDetails.FIRST_NAME, UserDetails.LAST_NAME, UserDetails.CONTACT_TYPE)
             .values(userName, firstName, lastName, contactType);
            return i.executeUpdate();
        } catch (QueryException e) {
            throw new DaoException("Error creating user", e);
        }
    }
    public int createUser(String userName, String firstName, String lastName, String contactType,String accountId) throws DaoException {
        try {
            Insert i = new Insert();
            i.table(Table.UserDetails)
             .columns(UserDetails.USER_NAME, UserDetails.FIRST_NAME, UserDetails.LAST_NAME, UserDetails.CONTACT_TYPE,UserDetails.PROVIDER_AC_ID)
             .values(userName, firstName, lastName, contactType,accountId);
            return i.executeUpdate();
        } catch (QueryException e) {
            throw new DaoException("Error creating user", e);
        }
    }

    // UPDATE
    public boolean updateUser(Integer userId, String userName, String firstName, String lastName, String contactType) throws DaoException {
    
    	if(userId == null) {
    		throw new DaoException("UserId cant be null");
    	}
    
        try {
            Update u = new Update();
            u.table(Table.UserDetails);            
            if(userName != null) {
            	u.columns(UserDetails.USER_NAME);
            	u.values(userName);
            }
            if(firstName != null) {
            	u.columns(UserDetails.FIRST_NAME);
            	u.values(firstName);
            }
            if(lastName != null) {
            	u.columns(UserDetails.LAST_NAME);
            	u.values(lastName);
            }
            if(contactType != null) {
            	u.columns(UserDetails.CONTACT_TYPE);
            	u.values(contactType);
            }
            u.condition(UserDetails.USER_ID, Operators.Equals, userId.toString());
            return u.executeUpdate() == 1;
        } catch (QueryException e) {
            throw new DaoException("Error updating user with ID: " + userId, e);
        }
    }

    // DELETE
    public boolean deleteUser(Integer userId) throws DaoException {
        try {
            Delete d = new Delete();
            d.table(Table.UserDetails)
             .condition(UserDetails.USER_ID, Operators.Equals, userId.toString());
            return d.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error deleting user with ID: " + userId, e);
        }
    }
   
}
