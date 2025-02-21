package com.foryuum.frontend.scheduler;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.foryuum.frontend.common.ComConstant;
import com.foryuum.frontend.common.util.CommonUtil;
import com.foryuum.frontend.item.service.ItemService;
import com.foryuum.frontend.linkage.service.Ecount;

import jakarta.annotation.Resource;

@Service
public class SchedulerService {
	private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
	
	@Resource(name = "itemService")
	private ItemService itemService;
	
	private final static String RUN_MODE = System.getProperties().getProperty("jasypt.run.mode");
	private boolean checkTrackingNumberFlag = true;
	private LocalDate lastExecutedDate = LocalDate.now(ZoneOffset.ofHours(9));
	
	@Scheduled(cron = "0 */1 * * * *", zone="Asia/Seoul")
	private void searchTrackingNumberProcess() {
		if(RUN_MODE.equals(ComConstant.REAL)) {
			LocalDate today = LocalDate.now(ZoneOffset.ofHours(9)); 
			
			if (!today.equals(lastExecutedDate)) {
				checkTrackingNumberFlag = true;
				lastExecutedDate = today;
			}
			
			if(checkTrackingNumberFlag) {
				LOG.info("searchTrackingNumberProcess start!");
				Map<String, Object> ecountLoginInfo = itemService.getLoginInfo("olaf", "ecount");
				
				Ecount ecount = new Ecount();
				ecount.login(ecountLoginInfo);
				
				List<Map<String, String>> returnData = ecount.getTrackingNumber();
				
				if(returnData.size() > 0) {
					Map<String, Object> naverLoginInfo = itemService.getLoginInfo("", "");
					for(Map<String, String> requestData : returnData) {
						if(CommonUtil.isNullOrEmpty(requestData.get("P_RESULT"))) {
							itemService.setTrackingNumber(naverLoginInfo, requestData);
						}
						itemService.updateOrderInfo(requestData);
					}
					
					checkTrackingNumberFlag = false;
				}
			}
		}
	}
	
	@Scheduled(cron = "0 0 * * * *")
	private void clearChromeFiles() { 
		if(RUN_MODE.equals(ComConstant.REAL)) {
			try {
				String scriptPath = "/usr/local/apache-tomcat-10.1.35/bin/clean_tmp.sh";
				
				ProcessBuilder processBuilder = new ProcessBuilder(scriptPath);
				Process process = processBuilder.start();
				
				process.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
