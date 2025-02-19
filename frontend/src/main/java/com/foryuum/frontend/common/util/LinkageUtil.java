package com.foryuum.frontend.common.util;

import java.util.Map;

public class LinkageUtil {

	public static void setReult(Map<String, Object> resultMap, boolean result, String title, String text) {
        resultMap.put("RESULT", result);
        resultMap.put("TITLE", title);
        resultMap.put("TEXT", text);
	}
}
