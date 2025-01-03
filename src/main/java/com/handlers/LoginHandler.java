package com.handlers;

import com.dao.DaoException;

import com.dao.PasswordsDao;
import com.dao.UserDetailsDao;
import com.dbObjects.PasswordsObj;
import com.dbObjects.UserDetailsObj;
import com.dbconn.BCrypt;

public class LoginHandler {
	
	public static UserDetailsObj validateUser(String mail,String password ) throws DaoException {
		UserDetailsDao dao = new UserDetailsDao();
		System.out.println("hii");
		UserDetailsObj user = dao.getUserWithMail(mail);
		System.out.println(user);
		if(user == null) { 
			return null;
		}
		
		if(checkInPasswords(user.getUserId(), password)){
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
	
	private static boolean checkInPasswords(Integer user_id, String enteredPassword) throws DaoException {
		PasswordsDao dao = new PasswordsDao();
		PasswordsObj password = dao.getPasswordObjWithUserId(user_id);
		return checkPassword(enteredPassword, password.getPassword(), password.getPasswordVersion());
	}
	
}
