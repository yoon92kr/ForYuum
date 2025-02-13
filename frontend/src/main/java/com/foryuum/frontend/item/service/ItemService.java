package com.foryuum.frontend.item.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public Map<String, Object> orderAndDeliver(HttpSession session, Map<String, Object> requestData) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			List<Map<String, Object>> orderList = objectMapper.readValue((String) requestData.get("data"), List.class);
		    orderList.sort(Comparator.comparing(o -> (String) o.get("P_NAME"))); // 고객명 기준으로 정렬

			if (orderList.size() > 0) {
				Map<String, Object> feelLoginInfo = getLoginInfo(session, ComConstant.FEEL);

				/* 1. 필사입을 통한 사입 진행 */
				Feel feel = new Feel();
				feel.login(feelLoginInfo);
				feel.feelProcess(resultMap, orderList);
				
				/* 2. 사입 성공 시 배송 진행 */
				if((boolean) resultMap.get("RESULT")) {
					Map<String, Object> ecountLoginInfo = getLoginInfo(session, ComConstant.ECOUNT);
					
					Ecount ecount = new Ecount();
					ecount.login(ecountLoginInfo);
					ecount.ecountProcess(resultMap, orderList);
				}
			} else {
				resultMap.put("RESULT", false);
				resultMap.put("RESULT_MSG", "요청 정보가 없습니다.");
			}

		} catch (Exception e) {
			LOG.error("orderToFeel Exception :: {}", e);
		}

		return resultMap;
	}

}
