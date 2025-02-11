package com.foryuum.frontend.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GlobalUtil {
	public static Map<String, Object> convertJSONstringToMap(String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
		});

		return map;
	}

	public static UUID getUUID() {
		UUID strUuid = UUID.randomUUID();
		return strUuid;
	}

	public static String makeRealFileName(String astrFileName) {
		int extensionIdx = astrFileName.lastIndexOf(".");
		if (extensionIdx == -1)
			return getUUID() + "";
		else
			return getUUID() + astrFileName.substring(astrFileName.lastIndexOf("."));
	}
}
