
package com.foryuum.frontend.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:config/properties/linkage.properties")
public class XssFilter {
	//Cross-site Scripting을 방지하기 위한 정규식
	
	private static String strXssFilter;
	@Value("#{environment['xssFilterString']}")
	private void setValue(String tempStr) {
		strXssFilter = tempStr;
	}
	
	public static String xssFilter(String str){
		String escapeHtml = "";
		if(str != null){
			
			StringBuilder escaped = new StringBuilder(str.length()*2);
			for (int i = 0; i < str.length(); i++) {
				char character = str.charAt(i);
				
				switch (character){
					case '<':
						escaped.append("&lt;");
						break;
					case '>':
						escaped.append("&gt;");
						break;
					case '"':
						escaped.append("&quot;");
						break;
					case '&':
						escaped.append("&amp;");
						break;
					case '\'':
						escaped.append("&#39;");
						break;
					case '(':
						escaped.append("&#40;");
						break;
					case ')':
						escaped.append("&#41;");
						break;
					case '%':
						escaped.append("&#37;");
						break;
					case '\\':
						escaped.append("&#92;");
						break;
					case '#':
						escaped.append("&#35;");
						break;
					case ';':
						escaped.append("&#x3b;");
						break;						
					case '/':
						escaped.append("&#47;");
						break;						
					case '`':
						escaped.append("&#96;");
						break;						
						
					default:
						escaped.append(character);
						break;
				}
			}
			escapeHtml = escaped.toString();
		} else {
			return "";
		} 
		return escapeHtml;
	}
	
	public static String filterReturn(String str) {
		String[] arrStrFilter = strXssFilter.split(",");		
		str = xssFilter(str.toLowerCase());
		for (int i=0; i<arrStrFilter.length; i++) {
			if (str.contains(arrStrFilter[i])) {
				return arrStrFilter[i];
			}
		}
		return "OK";
	}
}
