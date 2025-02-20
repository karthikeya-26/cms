package com.dao;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import com.dbObjects.ResultObject;
import com.dbObjects.UserMailsObj;
import com.enums.Columns;
import com.enums.Operators;
import com.enums.Table;
import com.enums.UserMails;
import com.queryLayer.Delete;
import com.queryLayer.Insert;
import com.queryLayer.QueryException;
import com.queryLayer.Select;
import com.queryLayer.Update;

public class UserMailsDao {
    
    // SELECT
    public List<UserMailsObj> getUserMails(Integer userId) throws DaoException {
        List<UserMailsObj> mails = new ArrayList<>();
        try {
            Select s = new Select();
            s.table(Table.UserMails)
             .condition(UserMails.USER_ID, Operators.Equals, userId.toString());
            List<ResultObject> resultList = s.executeQuery(UserMailsObj.class);
            for (ResultObject mail : resultList) {
                mails.add((UserMailsObj) mail);
            }
        } catch (QueryException e) {
            throw new DaoException("Error fetching mails for userId: " + userId, e);
        }
        return mails;
    }
    
    // INSERT
    public boolean addMailForUser(Integer userId, String mail) throws DaoException {
        try {
            Insert insertMail = new Insert()
                .table(Table.UserMails)
                .columns(UserMails.MAIL, UserMails.USER_ID)
                .values(mail, userId.toString());
            return insertMail.executeUpdate() > 0;
        } catch (QueryException e) {
            throw new DaoException("Error adding mail for userId: " + userId, e);
        }
    }
    
    // UPDATE - mail name change
    public boolean updateMail(Integer mailId, Integer userId, String newMail) throws DaoException {
        try {
            Update updateMail = new Update();
            updateMail.table(Table.UserMails)
                      .columns(UserMails.MAIL)
                      .values(newMail)
                      .condition(UserMails.MAIL_ID, Operators.Equals, mailId.toString());
            return updateMail.executeUpdate() >= 0;
        } catch (QueryException e) {
            throw new DaoException("Error updating mail with mailId: " + mailId, e);
        }
    }
    
    // UPDATE -> primary mail change
    public boolean setPrimaryMail(Integer userId, String mailId) throws DaoException {
        try {
            if (checkIfMailBelongsToUser(userId, mailId)) {
                Update unsetPrimaryMail = new Update();
                unsetPrimaryMail.table(Table.UserMails)
                                .columns(UserMails.IS_PRIMARY)
                                .values("0")
                                .condition(UserMails.USER_ID, Operators.Equals, userId.toString())
                                .condition(UserMails.IS_PRIMARY, Operators.Equals, "1");
                
                int success = unsetPrimaryMail.executeUpdate();
                if (success >= 0) {
                    Update setPrimaryMail = new Update();
                    setPrimaryMail.table(Table.UserMails)
                                  .columns(UserMails.IS_PRIMARY)
                                  .values("1")
                                  .condition(UserMails.USER_ID, Operators.Equals, userId.toString())
                                  .condition(UserMails.MAIL_ID, Operators.Equals, mailId);
                    return setPrimaryMail.executeUpdate() > 0;
                }
            }
        } catch (QueryException e) {
            throw new DaoException("Error setting primary mail for userId: " + userId, e);
        }
        return false;
    }
    
    // DELETE
    public boolean deleteMailForUser(Integer userId, String mailId) throws DaoException {
        try {
            Delete deleteMail = new Delete();
            deleteMail.table(Table.UserMails)
            .condition(UserMails.MAIL_ID, Operators.Equals, mailId)
            .condition(UserMails.USER_ID, Operators.Equals, userId.toString());
            return deleteMail.executeUpdate() >= 0;
        } catch (QueryException e) {
            throw new DaoException("Error deleting mail for user", e);
        }
    }
    
    // VALIDATION
    public boolean checkIfMailBelongsToUser(Integer userId, String mailId) throws DaoException {
        try {
            Select checkUserAndMail = new Select();
            checkUserAndMail.table(Table.UserMails)
                            .condition(UserMails.USER_ID, Operators.Equals, userId.toString())
                            .condition(UserMails.MAIL_ID, Operators.Equals, mailId);
            List<Map<Columns, Object>> res = checkUserAndMail.executeQuery();
            return !res.isEmpty();
        } catch (QueryException e) {
            throw new DaoException("Error checking if mail belongs to userId: " + userId + " and mailId: " + mailId, e);
        }
    }
}
