package com.dao;

import java.time.Instant;
import java.util.List;

import com.dbObjects.*;
import com.queryLayer.*;
import com.tables.*;

public class UserDetailsDao {
	// SELECT -> User with userId
	public UserDetailsObj getUserWithId(Integer userId) {
		UserDetailsObj user = null;
		Select s = new Select();
		s.table(Table.UserDetails)
		.condition(UserDetails.USER_ID, Operators.Equals, userId.toString());
		List<ResultObject> users = s.executeQuery(UserDetailsObj.class);
		if(users.size()>0) {
			user = (UserDetailsObj) users.get(0);
		}
		return user;
	}
	
	// SELECT -> User with mail
	public UserDetailsObj getUserWithMail(String mail) {
		UserDetailsObj user = null;
		Select s = new Select();
		s.table(Table.UserDetails).column(UserDetails.ALL_COLS)
		.join(Joins.InnerJoin, Table.UserMails, UserMails.USER_ID, Operators.Equals, Table.UserDetails, UserDetails.USER_ID)
		.condition(UserMails.MAIL,Operators.Equals,mail);
		List<ResultObject> users = s.executeQuery(UserDetailsObj.class);
		if(users.size()>0) {
			user = (UserDetailsObj) users.get(0);
		}
		return user;
	}
	
	//INSERT
	public int createUser(String userName, String firstName, String lastName, String contactType){
		Long createdAt = Instant.now().toEpochMilli();
		Insert insertUser = new Insert();
		insertUser.table(Table.UserDetails)
		.values(userName,firstName,lastName,contactType,createdAt.toString(),createdAt.toString());
		System.out.println(insertUser.build());
		return insertUser.executeUpdate();
	}
	
	//UPDATE
	public boolean updateUser(Integer userId, String userName, String firstName, String lastName, String contactType) {
	
		Update u = new Update();
		u.table(Table.UserDetails)
		.columns(UserDetails.USER_NAME,UserDetails.FIRST_NAME,UserDetails.LAST_NAME,UserDetails.CONTACT_TYPE,UserDetails.MODIFIED_AT)
		.values(userName,firstName,lastName,contactType)
		.condition(UserDetails.USER_ID, Operators.Equals, userId.toString());
		System.out.println(u.build());
		return u.executeUpdate() == 0;
	}
	
	//DELETE
	public boolean deleteUser(Integer userId) {
		//account delete this will delete userdetails ,usermails, usergroups, contacts
		Delete d = new Delete();
		d.table(Table.UserDetails).condition(UserDetails.USER_ID, Operators.Equals, userId.toString());
		return d.executeUpdate()>0;	
	}
	

}
