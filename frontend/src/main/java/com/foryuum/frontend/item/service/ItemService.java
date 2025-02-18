package com.foryuum.frontend.item.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryuum.frontend.common.ComConstant;
import com.foryuum.frontend.common.util.SessionUtil;
import com.foryuum.frontend.common.vo.UserInfoVo;
import com.foryuum.frontend.linkage.Ecount;
import com.foryuum.frontend.linkage.Feel;
import com.foryuum.frontend.linkage.NaverStore;
import com.foryuum.frontend.linkage.Shuline;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Service("itemService")
public class ItemService {

	private static final Logger LOG = LoggerFactory.getLogger(ItemService.class);
	private final static String NAME_SPACE = "item.";

	@Resource(name = "sqlSession")
	private SqlSessionTemplate sqlSession;

	public Map<String, Object> getItemInfo(Map<String, Object> requestData) {
		return sqlSession.selectOne(NAME_SPACE + "GET_ITEM_INFO", requestData);
	}

	public Map<String, Object> getLoginInfo(HttpSession session, String systemId) {

		UserInfoVo userInfo = SessionUtil.getUserInfo(session);

		Map<String, Object> requestData = new HashMap<String, Object>();

		requestData.put("P_USER_ID", userInfo.getUserId());
		requestData.put("P_SYSTEM_ID", systemId);

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
	
	public  Map<String, Object> checkOrder(HttpSession session) {
		Map<String, Object> returnData = new HashMap<String, Object>();
		
		try {
			Map<String, Object> modernLoginInfo = getLoginInfo(session, ComConstant.MODERN);
			
//			NaverStore naverStore = new NaverStore();
//			if(naverStore.login(modernLoginInfo, returnData)) {
//				Long timestamp = System.currentTimeMillis();
//				naverStore.naverProcess(returnData); // 네이버스토어 로그인 성공 시, 주문목록 조회 시작
//			}
		}catch (Exception e) {
			LOG.error("checkOrder Exception :: {}", e);
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
			} else {
				returnData.put("RESULT", false);
				returnData.put("RESULT_MSG", "요청 정보가 없습니다.");
				returnData.put("RESULT_VALUE", "양식을 다시 한번 확인해주세요!");				
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
				returnData.put("RESULT", false);
				returnData.put("RESULT_MSG", "요청 정보가 없습니다.");
			}

		} catch (Exception e) {
			LOG.error("orderToFeel Exception :: {}", e);
		}
	}

}
