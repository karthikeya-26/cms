package com.handlers;

import com.dao.NewDao;
import com.dbObjects.PasswordsObj;
import com.dbObjects.UserDetailsObj;
import com.dbconn.BCrypt;
import com.queryLayer.Insert;
import com.queryLayer.Update;
import com.tables.Passwords;
import com.tables.Table;
import com.tables.UserDetails;

public class LoginHandler {
	
	public static UserDetailsObj validate_user(String mail,String password ) {
		UserDetailsObj user = getUser(mail);
		if(user == null) {
			return user;
		}
		else if(user.getPassword() == "updated" &&checkInPasswords(user.getUser_id(),password)) {
			return user;
		}
		else if(checkPassword(password, user.getPassword(),user.getPw_version())) {
			Insert updatePassword = new Insert();
			updatePassword.table(Table.Passwords).columns(Passwords.USER_ID,Passwords.PASSWORD,Passwords.PW_VERSION,Passwords.MODIFIED_AT)
			.values(user.getUser_id().toString(),user.getPassword(),user.getPw_version().toString(),user.getPw_last_changed_at().toString());
			updatePassword.executeUpdate();
			Update removePasswordInUserDetails = new Update();
			removePasswordInUserDetails.table(Table.UserDetails).columns(UserDetails.PASSWORD, UserDetails.PW_VERSION, UserDetails.PW_LAST_CHANGED_AT)
			.values("updated","-1","-1");
			removePasswordInUserDetails.executeUpdate();
			return user;
		}
		return null;
	}
	
	private static boolean checkPassword(String enteredPass, String dbPass, Integer version) {
		
		switch (version) {
		case 0:
			//if we move to another algo add conversion here
			return BCrypt.checkpw(enteredPass, dbPass);
		case 1:
			//any algorithm
			return true;
		default:
			return false;
		}
	}
	
	private static boolean checkInPasswords(Integer user_id, String enteredPassword) {
		PasswordsObj password = NewDao.getPasswordWithUserId(user_id);
		return checkPassword(enteredPassword, password.getPassword(), password.getPw_version());
	}
	
	public static UserDetailsObj getUser(String mail) {
		return NewDao.loginUser(mail);
	}
}
