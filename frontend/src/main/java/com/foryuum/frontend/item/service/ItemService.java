package com.foryuum.frontend.item.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryuum.frontend.common.ComConstant;
import com.foryuum.frontend.common.util.CommonUtil;
import com.foryuum.frontend.common.util.LinkageUtil;
import com.foryuum.frontend.common.util.SessionUtil;
import com.foryuum.frontend.common.vo.UserInfoVo;
import com.foryuum.frontend.linkage.service.Ecount;
import com.foryuum.frontend.linkage.service.Feel;
import com.foryuum.frontend.linkage.service.NaverStore;
import com.foryuum.frontend.linkage.service.Shuline;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("itemService")
public class ItemService {

	private static final Logger LOG = LoggerFactory.getLogger(ItemService.class);
	private final static String NAME_SPACE = "item.";
	private final static String KEY = System.getProperties().getProperty("jasypt.security.key");
	
	@Resource(name = "sqlSession")
	private SqlSessionTemplate sqlSession;
	
	@Resource(name = "naverStore")
	private NaverStore naverStore;


	public Map<String, Object> getItemInfoByItemNo(Map<String, Object> requestData) {
		return sqlSession.selectOne(NAME_SPACE + "GET_ITEM_INFO", requestData);
	}
	
	public  Map<String, Object> getItemInfoByItemOrderNo(HttpSession session, Map<String, Object> requestData) {
		Map<String, Object> returnData = new HashMap<String, Object>();
		
		try {
			Map<String, Object> loginInfo = getLoginInfo(session, ComConstant.MODERN);
			if(!CommonUtil.isNullOrEmpty(loginInfo)) {
				naverStore.getItemInfoByItemOrderNo(loginInfo, returnData, requestData);
			}
		}catch (Exception e) {
			LOG.error("getItemInfoByItemOrderNo Exception :: {}", e);
		}
		
		return returnData;
	}

	public Map<String, Object> getLoginInfo(HttpSession session, String systemId) {

		UserInfoVo userInfo = SessionUtil.getUserInfo(session);

		Map<String, Object> requestData = new HashMap<String, Object>();

		requestData.put("P_USER_ID", userInfo.getUserId());
		requestData.put("P_SYSTEM_ID", systemId);
		requestData.put("P_SECURITY_KEY", KEY);

		return sqlSession.selectOne(NAME_SPACE + "GET_SYSTEM_LOGIN_INFO", requestData);
	}
	
	public Map<String, Object> getLoginInfo(String userId, String systemId) {

		Map<String, Object> requestData = new HashMap<String, Object>();

		requestData.put("P_USER_ID", userId);
		requestData.put("P_SYSTEM_ID", systemId);
		requestData.put("P_SECURITY_KEY", KEY);

		return sqlSession.selectOne(NAME_SPACE + "GET_SYSTEM_LOGIN_INFO", requestData);
	}

	@SuppressWarnings("unchecked")
	public  Map<String, Object> initOrder(HttpSession session, Map<String, Object> requestData) {
		Map<String, Object> returnData = new HashMap<String, Object>();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			List<Map<String, Object>> orderList = objectMapper.readValue((String) requestData.get("data"), List.class);
			
			/* 슈라인 List */
	        List<Map<String, Object>> shulineList = orderList.stream()
	                .filter(order -> ComConstant.NAGEUMSHOP.equals(order.get("P_COMPANY_NAME")))
	                .collect(Collectors.toList());
	        
	        /* Feel 사입 List */
	        List<Map<String, Object>> feelList = orderList.stream()
	                .filter(order -> ComConstant.MODERNBLANCO.equals(order.get("P_COMPANY_NAME")) || ComConstant.YAMIBLING.equals(order.get("P_COMPANY_NAME")))
	                .collect(Collectors.toList());
	        
	        if(feelList.size() > 0) {
	        	feelList.sort(Comparator.comparing(o -> (String) o.get("P_NAME"))); // 고객명 기준으로 정렬
	        	fellProcess(session, requestData, returnData, feelList);
	        }

	        if(shulineList.size() > 0) {
	        	shulineList.sort(Comparator.comparing(o -> (String) o.get("P_NAME"))); // 고객명 기준으로 정렬
	        	shulineProcess(session, requestData, returnData, shulineList);
	        }
	        
	        
		} catch (Exception e) {
			LOG.error("initOrder Exception :: {}", e);
		}
		
		return returnData;
	    
	}
	
	public void fellProcess(HttpSession session, Map<String, Object> requestData, Map<String, Object> returnData, List<Map<String, Object>> orderList) {
		try {
			if (orderList.size() > 0) {
				Map<String, Object> feelLoginInfo = getLoginInfo(session, ComConstant.FEEL);

				/* 1. 필사입을 통한 사입 진행 */
				Feel feel = new Feel();
				feel.login(feelLoginInfo);
				feel.feelProcess(returnData, orderList);
				
				/* 2. 사입 성공 시 배송 진행 */
				if((boolean) returnData.get("RESULT")) {
					Map<String, Object> ecountLoginInfo = getLoginInfo(session, ComConstant.ECOUNT);
					
					Ecount ecount = new Ecount();
					ecount.login(ecountLoginInfo);
					ecount.ecountProcess(returnData, orderList);
				}
				
				if((boolean) returnData.get("RESULT")) {
					insertOrderInfo(orderList);
				}
			} else {
				LinkageUtil.setReult(returnData, false, "주문 정보 없음", "주문 정보가 없습니다!\n다시 확인해주세요.");
			}

		} catch (Exception e) {
			LOG.error("orderToFeel Exception :: {}", e);
		}
	}
	
	public void shulineProcess(HttpSession session, Map<String, Object> requestData, Map<String, Object> returnData, List<Map<String, Object>> orderList) {
		try {
			if (orderList.size() > 0) {
				Map<String, Object> feelLoginInfo = getLoginInfo(session, ComConstant.SHULINE);

				/* 1. 필사입을 통한 사입 진행 */
				Shuline shuline = new Shuline();
				shuline.login(feelLoginInfo);
				shuline.shulineProcess(returnData, orderList);
			} else {
				LinkageUtil.setReult(returnData, false, "주문 정보 없음", "주문 정보가 없습니다!\n다시 확인해주세요.");
			}

		} catch (Exception e) {
			LOG.error("orderToFeel Exception :: {}", e);
		}
	}
	
	public void insertOrderInfo(List<Map<String, Object>> orderList) {
		sqlSession.insert(NAME_SPACE + "INSERT_ORDER_INFO", orderList);
	}
	
	public String setTrackingNumber(Map<String, Object> loginInfo, Map<String, String> requestData) {
		StringBuilder sb = new StringBuilder();
		boolean setResult = false;

		Map<String, Object> orderInfo = sqlSession.selectOne(NAME_SPACE + "GET_ORDER_INFO", requestData);
		requestData.put("P_PRODUCT_ORDER_ID", orderInfo.get("PRODUCT_ORDER_ID").toString());
		
		String authToken = naverStore.getAuthToken(loginInfo);
		
		if(!CommonUtil.isNullOrEmpty(authToken)) {
			setResult = naverStore.setTrackingNumber(authToken, requestData);
			requestData.put("P_RESULT", setResult ? "T" : "F");
		} else {
			LOG.error("setTrackingNumber Error :: authToken 발급 실패");
			requestData.put("P_RESULT", "F");
		}
		sb.append(orderInfo.get("ORDERER_NAME"));
		sb.append("(");
		sb.append(orderInfo.get("RECEIVER_NAME"));
		sb.append(") : ");
		sb.append(setResult ? "송장 등록 성공" : "송장 등록 실패");
		sb.append("[");
		sb.append(requestData.get("TRACKING_NUMBER"));
		sb.append("]\n");
		return sb.toString();
	}
	
	public void updateOrderInfo(Map<String, String> requestData) {
		sqlSession.insert(NAME_SPACE + "UPDATE_ORDER_INFO", requestData);
	}

}
