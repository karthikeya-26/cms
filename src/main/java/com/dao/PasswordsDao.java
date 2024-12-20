package com.dao;

import java.util.List;

import com.dbObjects.*;
import com.enums.Operators;
import com.enums.Passwords;
import com.enums.Table;
import com.queryLayer.*;

public class PasswordsDao {
	//SELECT
	public PasswordsObj getPasswordObjWithUserId(Integer userId) throws Exception {
		PasswordsObj password = null;
		Select s = new Select();
		s.table(Table.Passwords)
		.condition(Passwords.USER_ID, Operators.Equals, userId.toString());
		List<ResultObject> passwords = s.executeQuery(PasswordsObj.class);
		if (passwords.size()>0) {
			password = (PasswordsObj) passwords.get(0);
		}
		return password;
	}
	
	@Deprecated
	public String getPassword(Integer userId) throws Exception {
		PasswordsObj password= getPasswordObjWithUserId(userId);
		return password.getPassword();
	}
	
	//INSERT
	public boolean createPassword(Integer userId, String password, Integer pw_version, Long created_at, Long modifiedt_at) {
		Insert i = new Insert();
		i.table(Table.Passwords).values(userId.toString(),password,pw_version.toString(),created_at.toString(),modifiedt_at.toString());
		return i.executeUpdate() == 0;
	}
	
	//UPDATE 
	public boolean changePassword(Integer userId, String newPassword) {
		Update u = new Update();
		u.table(Table.Passwords).columns(Passwords.PASSWORD).values(newPassword)
		.condition(Passwords.USER_ID, Operators.Equals, userId.toString());
		return u.executeUpdate()==0;
	}
	
	//DELETE 
	//--> Password once set can't be removed unless account is deleted..
}
