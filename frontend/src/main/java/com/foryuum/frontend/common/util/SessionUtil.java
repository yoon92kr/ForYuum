package com.foryuum.frontend.common.util;

import java.util.HashMap;

import com.foryuum.frontend.common.vo.UserInfoVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

	/**
	 * Session에서 attributeName에 해당하는 Map이 존재할 경우 반환. 없는 경우 Null 반환
	 * 
	 * @param Session, String
	 * @return HashMap 또는 Null
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getMap(HttpSession session, String attributeName) {
		HashMap<String, Object> map = null;

		if (!CommonUtil.isNullOrEmpty(session)) {
			Object obj = session.getAttribute(attributeName);

			if (!CommonUtil.isNullOrEmpty(obj) && obj instanceof HashMap) {
				map = (HashMap<String, Object>) obj;
			}

		}

		return map;
	}

	/**
	 * Session에서 sessionMap이 존재할 경우, UserInfo 객체를 반환
	 * 
	 * @param Session, Map
	 * @return UserInfoVo 또는 Null
	 */
	public static UserInfoVo getUserInfo(HttpSession session) {
		UserInfoVo userInfo = null;

		if (!CommonUtil.isNullOrEmpty(session)) {
			HashMap<String, Object> sessionMap = getMap(session, "sessionMap");

			if (!CommonUtil.isNullOrEmpty(sessionMap)) {
				userInfo = (UserInfoVo) sessionMap.get("userInfo");
			}
		}

		return userInfo;
	}

	public static UserInfoVo getUserInfo(HashMap<String, Object> sessionMap) {
		UserInfoVo userInfo = null;

		if (!CommonUtil.isNullOrEmpty(sessionMap)) {
			userInfo = (UserInfoVo) sessionMap.get("userInfo");
		}

		return userInfo;
	}
	
	public static boolean hasUserInfo(HttpServletRequest request) {
		UserInfoVo userInfo = getUserInfo(request.getSession());

		return !CommonUtil.isNullOrEmpty(userInfo);
	}

}
