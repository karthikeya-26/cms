package com.util;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.dao.DaoException;
import com.dao.ServersDao;
import com.dbObjects.ServersObj;

public class ShutDownTasks {
	
	public void deactiveServer(String ipAddress,Integer port) throws DaoException {
		ServersDao serverDao = new ServersDao();
		ServersObj server = serverDao.getServer(ipAddress, port);
		if(server != null && server.getStatus()== 1) {
			serverDao.updateStatus(ipAddress, port, 0);
		}
	}
	
	public void shutdownScheduler(ScheduledExecutorService scheduler) {
		if (scheduler != null) {
			scheduler.shutdown();
			try {
				if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
					scheduler.shutdownNow();
				}
			} catch (InterruptedException e) {
				scheduler.shutdownNow();
			}
		}
	}
}
