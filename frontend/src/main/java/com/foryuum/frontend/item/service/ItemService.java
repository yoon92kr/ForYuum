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

@Service("itemService")
public class ItemService {

	private static final Logger LOG = LoggerFactory.getLogger(ItemService.class);
	private final static String NAME_SPACE = "item.";

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

}
