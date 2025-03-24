package com.stp.autojob;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MyScheduledTask {

//	// Executes every minute
//	@Scheduled(cron = "0 * * * * ?")

	// Executes every day at midnight (12:00 AM)
//    @Scheduled(cron = "0 0 0 * * ?")

// Executes every minute
//	@Scheduled(cron = "0 0/30 * * * ?")

// Executes every day at 5:00 PM
//	@Scheduled(cron = "0 0 17 * * ?")

	public void executeTask() {
		System.out.println("Scheduled task executed at: " + System.currentTimeMillis());
	}
}
