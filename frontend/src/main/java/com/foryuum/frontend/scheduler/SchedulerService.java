package com.foryuum.frontend.scheduler;

import com.foryuum.frontend.common.ComConstant;
import com.foryuum.frontend.item.service.ItemService;
import com.foryuum.frontend.linkage.service.CoolSMS;
import com.foryuum.frontend.linkage.service.Ecount;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchedulerService {
	private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
	private final static String RUN_MODE = System.getProperties().getProperty("jasypt.run.mode");
	@Resource(name = "itemService")
	private ItemService itemService;

	@Resource(name = "coolSMS")
	private CoolSMS coolSMS;
	private boolean checkTrackingNumberFlag = true;
	private LocalDate lastExecutedDate = LocalDate.now(ZoneOffset.ofHours(9));
	
	@Scheduled(cron = "0 */5 * * * *", zone="Asia/Seoul")
	private void searchTrackingNumberProcess() {
		if(RUN_MODE.equals(ComConstant.DEV)) {
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

				if(!returnData.isEmpty()) {
					HashMap<String, String> resultMap = new HashMap<String, String>();
					StringBuilder sb = new StringBuilder();
					int totalCnt = returnData.size();
					int successCnt = 0;

					Map<String, Object> naverLoginInfo = itemService.getLoginInfo("olaf", ComConstant.MODERN);
					for(Map<String, String> requestData : returnData) {
						String resultMsg = itemService.setTrackingNumber(naverLoginInfo, requestData);
						sb.append(resultMsg);

						if(requestData.get("P_RESULT").equals("T")) {
							successCnt++;
						}
						itemService.updateOrderInfo(requestData);
					}

					resultMap.put("DATE", today.toString());
					resultMap.put("TOTAL_COUNT", String.valueOf(totalCnt));
					resultMap.put("SUCCEESS_COUNT", String.valueOf(successCnt));
					resultMap.put("FAIL_COUNT", String.valueOf(totalCnt - successCnt));
					resultMap.put("RESULT", sb.toString());

					coolSMS.sendKakaoTalk(resultMap);
					LOG.info("송장번호 확인 결과 :: {}", resultMap);
					checkTrackingNumberFlag = false;
				}
				LOG.info("searchTrackingNumberProcess End!");
			}
		}

	}
}
