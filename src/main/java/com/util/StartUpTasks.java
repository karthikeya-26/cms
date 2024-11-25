package com.util;

import javax.servlet.http.HttpServletRequest;

import com.dao.StartUpTasksDao;

public class StartUpTasks {
	
	private static StartUpTasksDao dao = new StartUpTasksDao();
	
	//this on monday
	void fetchAppConfig() {
		
	}
	//
	public void registerServer() {
		HttpServletRequest request = null ;
		System.out.println(request.getServerName());
		System.out.println(request.getServerPort());
	}
	
	
	
	
}
