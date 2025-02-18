package com.foryuum.frontend.linkage;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foryuum.frontend.common.util.CommonUtil;
import com.foryuum.frontend.restApi.naverStore.NaverLinkageServiceImpl;

import jakarta.annotation.Resource;
import net.sf.json.JSONObject;

public class NaverStore {

	private static final Logger LOG = LoggerFactory.getLogger(NaverStore.class);

	@Resource(name = "naverLinkageService")
	private NaverLinkageServiceImpl naverLinkageServiceImpl;
	
	public boolean NaverStore(Map<String, Object> loginInfo, Long timeStamp, Map<String, Object> resultMap) {
		boolean result = true;
        // 연동 Token 발급
		JSONObject jResult = naverLinkageServiceImpl.getAuthToken(loginInfo.get("LOGIN_ID").toString(), loginInfo.get("LOGIN_PASSWORD").toString(), loginInfo.get("LOGIN_SUB_ID").toString(), timeStamp, LOG);
		
		if(CommonUtil.isNullOrEmpty(jResult) && CommonUtil.isNullOrEmpty(jResult.get("access_token"))) {
			result = false;
			resultMap.put("RESULT", false);
			resultMap.put("RESULT_MSG", "네이버스토어 연동API TOKEN 생성에 실패했습니다.\n 아빠를 불러주세요!");
			resultMap.put("RESULT_VALUE", "네이버스토어 연동API TOKEN 생성에 실패했습니다.\n 아빠를 불러주세요!");
		} else {
			resultMap.put("ACCES_TOKEN", jResult.get("access_token"));
			resultMap.put("TOKEN_TYPE", jResult.get("token_type"));
		}
        
		return result;
	}

	public boolean login(Map<String, Object> loginInfo, Map<String, Object> resultMap) {
		boolean result = true;
		
		try {
			
		} catch (Exception e) {
			LOG.error("NaverStore login Exception :: {}", e);
			result = false;
            resultMap.put("RESULT", false);
            resultMap.put("RESULT_MSG", "네이버스토어 로그인에 실패했습니다.\n 아빠를 불러주세요!");
            resultMap.put("RESULT_VALUE", "네이버스토어 로그인에 실패했습니다.\n 아빠를 불러주세요!");
		} 
		
		return result;
	}

}