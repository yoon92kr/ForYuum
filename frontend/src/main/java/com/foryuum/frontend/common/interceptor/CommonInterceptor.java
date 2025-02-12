package com.foryuum.frontend.common.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import com.foryuum.frontend.common.ComConstant;
import com.foryuum.frontend.common.util.SessionUtil;
import com.foryuum.frontend.common.vo.UserInfoVo;
import com.foryuum.frontend.main.service.HomeService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CommonInterceptor implements HandlerInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(CommonInterceptor.class);
	final String[] PASS_URL = {"login.do", "home.do", "loginCheck.do", "logout.do", "loginSecurityCheck.do"};

	@Resource(name = "homeService")
	private HomeService homeService;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnsupportedEncodingException {
		response.setContentType("application/json; charset=UTF-8");
		boolean accessConfirm = Arrays.stream(PASS_URL).anyMatch(request.getServletPath()::contains);

		try {
			HttpSession session = request.getSession();
			UserInfoVo userInfo = SessionUtil.getUserInfo(session);
			
			session.setAttribute(ComConstant.DEVICE_TYPE, checkAccessType(request));
			
			if(!accessConfirm) {
				if (SessionUtil.hasUserInfo(request)) {
					accessConfirm = true;
					LOG.info("사용자 접근 허용 [userId : {}, Request URL : {}]", userInfo.getUserId(), request.getServletPath());
				} else {
					LOG.info("사용자 정보 확인 불가 [client IP : {}, Request URL : {}]", request.getRemoteAddr(), request.getServletPath());
						response.sendRedirect(request.getContextPath() + "/login.do");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accessConfirm;
	}
	
	private String checkAccessType(HttpServletRequest request) {
		String strBrowser = request.getHeader("User-Agent");
		
		return strBrowser.toLowerCase().contains("windows") ? ComConstant.PC : ComConstant.MOBILE;
	}

}
