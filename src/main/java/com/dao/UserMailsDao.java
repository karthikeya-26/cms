package com.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dbObjects.ResultObject;
import com.dbObjects.UserMailsObj;
import com.enums.*;
import com.queryLayer.*;

public class UserMailsDao {
	// SELECT
	public List<UserMailsObj> getUserMails(Integer userId) {
		List<UserMailsObj> mails = new ArrayList<UserMailsObj>();
		Select s = new Select();
		s.table(Table.UserMails).condition(UserMails.USER_ID, Operators.Equals, userId.toString());
		List<ResultObject> resultList = s.executeQuery(UserMailsObj.class);
		for (ResultObject mail : resultList) {
			mails.add((UserMailsObj) mail);
		}
		resultList = null;
		return mails;
	}

	// INSERT 
	public boolean addMailForUser(Integer userId, String mail, Long createdAt, Long modifiedAt) {
		Insert insertMail = new Insert().table(Table.UserMails)
				.columns(UserMails.MAIL, UserMails.USER_ID)
				.values(mail, userId.toString());
		return insertMail.executeUpdate() > 0;
	}

	// UPDATE - mail name change
	public boolean updateMail(Integer mailId, Integer userId, String oldMail, String newMail) {
		Update updateMail = new Update();
		updateMail.table(Table.UserMails).columns(UserMails.MAIL).values(newMail)
				.condition(UserMails.MAIL_ID, Operators.Equals, mailId.toString())
				.condition(UserMails.MAIL, null, newMail);
		return updateMail.executeUpdate() > 0;
	}
	
	//UPDATE -> primary mail change
	public boolean setPrimaryMail(Integer user_id, String mail_id) {
		if(checkifMailbelongstoUser(user_id, mail_id)) {
			Update u = new Update();
			u.table(Table.UserMails).columns(UserMails.IS_PRIMARY).values("0").condition(UserMails.USER_ID,
					Operators.Equals, user_id.toString());

			int success = u.executeUpdate();
			if (success >= 0) {
				Update setprimary = new Update();
				setprimary.table(Table.UserMails).columns(UserMails.IS_PRIMARY).values("1")
						.condition(UserMails.USER_ID, Operators.Equals, user_id.toString())
						.condition(UserMails.MAIL_ID, Operators.Equals, mail_id);

				 return  setprimary.executeUpdate()==0;
			}
		}
		return false;
	}

	// DELETE
	public boolean deleteMailforUser(String mail) {
		Delete deleteMail = new Delete();
		deleteMail.table(Table.UserMails).condition(UserMails.MAIL, Operators.Equals, mail);
		return deleteMail.executeUpdate() > 0;
	}
	
	
	//VALIDATION
	public boolean checkifMailbelongstoUser(Integer user_id, String mail_id) {
		Select checkuserandmail = new Select();
		checkuserandmail.table(Table.UserMails).condition(UserMails.USER_ID, Operators.Equals, user_id.toString())
		.condition(UserMails.MAIL_ID, Operators.Equals, mail_id);
		List<HashMap<Columns,Object>> res = checkuserandmail.executeQuery();
		System.out.println(res);
		return res.size() >0;
	}
}
