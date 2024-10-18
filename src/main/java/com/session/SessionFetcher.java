package com.session;

import com.dao.Dao;

public class SessionFetcher {
	
	public static  void getSessionsFromDatabase() {
		
		Dao.fetchSessionsFromDatabase();
	}

}
