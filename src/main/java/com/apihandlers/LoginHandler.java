package com.apihandlers;
import com.dbObjects.*;
import com.dao.*;
public class LoginHandler {

	public UserDetailsObj getUser(String email, String password) {
		UserDetailsObj user = null;
		UserDetailsDao dao = new UserDetailsDao();
		try {
			 user = dao.getUserWithMail(email);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	

}
