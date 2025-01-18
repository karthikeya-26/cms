package com.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AuthorizationTokensScheduler {
	
	private static final ScheduledExecutorService authTokenScheduler = Executors.newScheduledThreadPool(1);
	
	Runnable authTokenDeleter = ()->{
		System.out.println("Deleting expired authorization tokens");
		
		
		
	};

}


