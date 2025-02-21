package com.foryuum.frontend.main.service;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.foryuum.frontend.common.ComConstant;
import com.foryuum.frontend.common.exception.CommonException;
import com.foryuum.frontend.common.util.CipherUtil;
import com.foryuum.frontend.common.util.CommonUtil;
import com.foryuum.frontend.common.vo.UserInfoVo;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service("homeService")
public class HomeService {

	private static final Logger LOG = LoggerFactory.getLogger(HomeService.class);
	private final static String NAME_SPACE = "home.";
	private final static String KEY = System.getProperties().getProperty("jasypt.security.key");

	@Resource(name = "sqlSession")
	private SqlSessionTemplate sqlSession;

	public boolean checkUserInfo(HttpServletRequest request, Map<String, Object> requestData) {
		boolean accessConfirm = false;

		try {
			HttpSession session = request.getSession();
			PrivateKey privateKey = (PrivateKey) session.getAttribute("__rsaPrivateKey__");
			String userId = CipherUtil.decryptRSA(privateKey, requestData.get("P_USER_ID").toString());
			String password = CipherUtil.decryptRSA(privateKey, requestData.get("P_USER_PASSWORD").toString());

			/* 정보 일치 검증 */
			HashMap<String, Object> paramData = new HashMap<String, Object>();

			paramData.put("P_USER_ID", userId);
			paramData.put("P_USER_PASSWORD", password);
			paramData.put("P_SECURITY_KEY", KEY);
			
			String userName = sqlSession.selectOne(NAME_SPACE + "GET_USER_INFO", paramData);

			if (!CommonUtil.isNullOrEmpty(userName)) {
				HashMap<String, Object> sessionMap = new HashMap<String, Object>();
				UserInfoVo userInfo = new UserInfoVo(userId);
				userInfo.setUserName(userName);

				sessionMap.put("userInfo", userInfo);
				session.setAttribute("sessionMap", sessionMap);

				accessConfirm = true;
			}
			
			insertAccessHis(request, userId, accessConfirm ? ComConstant.LOGIN : ComConstant.LOGIN_FAIL);

		} catch (CommonException ce) {
			LOG.error("checkUserInfo Exception :: {}", ce);
		}

		return accessConfirm;
	}

	public void insertAccessHis(HttpServletRequest request, String userId, int accessType) {
		HttpSession session = request.getSession();
		HashMap<String, Object> paramData = new HashMap<String, Object>();
		
		paramData.put("P_USER_ID", userId);
		paramData.put("P_ACCESS_TYPE", accessType);
		paramData.put("P_IP_ADDRESS", request.getRemoteAddr());
		paramData.put("P_DEVICE_TYPE", session.getAttribute(ComConstant.DEVICE_TYPE));

		sqlSession.insert(NAME_SPACE + "INSERT_ACCESS_HIS", paramData);
	}

	public List<Map<String, Object>> getMuneListByUser(String strUserId) {
		return sqlSession.selectList(NAME_SPACE + "UP_MENU_LIST_SELECT_BY_USER", strUserId);
	}

}
