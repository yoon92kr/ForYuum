package com.foryuum.frontend.common.util;

import java.util.List;
import java.util.Map;

import com.foryuum.frontend.common.ComConstant;

import jakarta.servlet.http.HttpSession;

public class CommonUtil {

	/**
	 * 요청한 값이 Null 또는 비어있는 상태인지 확인
	 * 
	 * @param Object, String, List, Map
	 * @return boolean
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;
		else
			return isNullOrEmpty(obj.toString());
	}

	public static boolean isNullOrEmpty(List<?> list) {
		return list == null || list.isEmpty();
	}

	public static boolean isNullOrEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public static boolean isNullOrEmpty(String value) {
		return value == null || value.isEmpty();
	}

	/* 모바일/PC 구분 및 경로 설정, 기본값은 PC */
	public static String getBasePath(HttpSession session) {
		String basePath = ComConstant.PC_PATH;

		String deviceType = (String) session.getAttribute(ComConstant.DEVICE_TYPE);

		if (ComConstant.MOBILE.equals(deviceType)) {
			basePath = ComConstant.MOBILE_PATH;
		}

		return basePath;

	}
}
