package com.session;

import com.dao.NewDao;

public class SessionFetcher {
	
	public static  void getSessionsFromDatabase() {
		
		NewDao.fetchSessionsFromDb();
	}

}
