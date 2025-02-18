package com.foryuum.frontend.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {
	private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
	
	@Scheduled(cron = "0 */5 * * * *")
	private void searchPostIdProcess() {
		
	}
}
