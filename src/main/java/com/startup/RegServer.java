package com.startup;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.Properties;

import com.dao.NewDao;
import com.loggers.AppLogger;

public class RegServer {
	private static Properties AppProp = new Properties();

	
	public static void readProp() {
		try(InputStream is = RegServer.class.getClassLoader().getResourceAsStream("resources/server.properties")){
			if (is == null) {
				throw new Exception("couldn't find application properties file");
			}
			AppProp.load(is);
			
		}catch(Exception e) {
			AppLogger.ApplicationLog(e);
			AppLogger.ApplicationLog("couldn't load file application.properties");
		}
	}
	
//	public static void readProp() {
//		try{
//			AppProp.load(RegServer.class.getClassLoader().getResourceAsStream("resources/server.properties"));
//		}catch(Exception e) {
//			AppLogger.ApplicationLog(e);
//			AppLogger.ApplicationLog("couldn't load file application.properties");
//		}
//	}
	
	public static void register_server_in_db() {
		System.out.println(AppProp);
		System.out.println(AppProp.getProperty("servername"));
		NewDao.registerServer(getServerName(), getServerPort());
		
	}
	
	public static void deregister_server_in_db() {
		NewDao.deregisterServer(getServerName(), getServerPort());
	}
	
	public static String getServerName() {
		if(AppProp.size() == 0) {
			readProp();
		
		}
		return AppProp.getProperty("servername");
	}
	public static String getServerPort() {
		if(AppProp.size() == 0) {
			readProp();
		}
		return AppProp.getProperty("port");
	}
	public static String getProjectName() {
		if(AppProp.size() == 0) {
			readProp();
		}
		return AppProp.getProperty("projectname");
	}
}
