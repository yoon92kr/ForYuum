package com.foryuum.frontend.main.controller;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.foryuum.frontend.common.ComConstant;
import com.foryuum.frontend.common.exception.CommonException;
import com.foryuum.frontend.common.util.CipherUtil;
import com.foryuum.frontend.common.util.CommonUtil;
import com.foryuum.frontend.common.util.SessionUtil;
import com.foryuum.frontend.common.vo.UserInfoVo;
import com.foryuum.frontend.main.service.HomeService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.sf.json.JSONObject;

@Controller
public class HomeController {

	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

	@Resource(name = "homeService")
	private HomeService homeService;


	@GetMapping("/home.do")
	public String home(HttpServletRequest request, HttpSession session) throws CommonException {
		
		if (SessionUtil.hasUserInfo(request)) {
			return "redirect:/dashboard.do";
		} else {
			setRsaInfo(request);
			return "redirect:/login.do";
		}
	}

	@GetMapping("/login.do")
	public String login(HttpServletRequest request, HttpSession session) throws CommonException {
		/* Session 탈취 방지를 위한 초기화*/
		HashMap<String, Object> sessionMap = SessionUtil.getMap(session, "sessionMap");
		session.invalidate();
		// 사용자 정보가 존재할 경우, Main 화면으로 이동한다.
		if (SessionUtil.hasUserInfo(request)) {
			session = request.getSession();
			session.setAttribute("sessionMap", sessionMap);
			return "redirect:/dashboard.do";
		} else {
			setRsaInfo(request);
			return "/login";
		}
	}

	@GetMapping("/logout.do")
	public String doLogout(HttpServletRequest request, HttpSession session) throws Exception {

		if (SessionUtil.hasUserInfo(request)) {
			homeService.insertAccessHis(request, ComConstant.LOGOUT);
		} 
		
		session.invalidate();
		return "redirect:/login.do";

	}

	@ResponseBody
	@PostMapping("/loginCheck.do")
	public JSONObject loginCheck(HttpServletRequest request, HttpSession session, @RequestParam Map<String, Object> requestData) throws CommonException, IOException {
		JSONObject jo = new JSONObject();
		boolean accessConfirm = homeService.checkUserInfo(session, requestData);
		
		if(!accessConfirm) {
			jo.put("resultMsg", ComConstant.LOGIN_ERROR_DEFAULT_MSG);
		}

		jo.put("result", accessConfirm);
		homeService.insertAccessHis(request, accessConfirm ? ComConstant.LOGIN : ComConstant.LOGIN_FAIL);
		return jo;
	}

	@ResponseBody
	@PostMapping("/menuList.do")
	public JSONObject getMenuList(HttpSession session) throws IOException, CommonException {
		JSONObject joReturn = new JSONObject();

		try {
			UserInfoVo userInfo = SessionUtil.getUserInfo(session);
			joReturn.put("returnData", homeService.getMuneListByUser(userInfo.getUserId()));
		} catch (Exception ex) {
			LOG.error("getDetailDeviceCnt Error :: {}", ex.getMessage());
		}

		return joReturn;
	}

	@GetMapping("/dashboard.do")
	public String moveMenu(HttpServletRequest request, HttpSession session) throws IOException, CommonException {
		/* 사용자 정보가 Session에 없는 경우, 로그인 화면으로 리다이렉트 */
		if (!SessionUtil.hasUserInfo(request)) {
			setRsaInfo(request);
			return "redirect:/login.do";
		}

		return CommonUtil.getBasePath(session) + "/dashboard";
	}

	private void setRsaInfo(HttpServletRequest request) throws CommonException {
		// 유저 정보 초기화를 위해 세션을 초기화 시키고 로그인 전까지 privatekey를 session에 담아준다.
		HttpSession newSession = request.getSession();

		// RSA 키쌍을 생성한다.
		try {
			// 로그인 정보 암호화를 위해 담아준다.
			KeyPair keyPair = CipherUtil.genRSAKeyPair();
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();

			// privatekey를 세션에 담아주고 privatekey 재사용을 막기위해 로그인 정보 체크시(loginCheck) 세션에서 삭제해준다.
			newSession.setAttribute("__rsaPrivateKey__", privateKey);

			// 공개키를 문자열로 변환하여 JavaScript로 넘겨준다.
			RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

			request.setAttribute("keyModulus", publicSpec.getModulus().toString(16));
			request.setAttribute("keyExponent", publicSpec.getPublicExponent().toString(16));

		} catch (Exception ex) {
			LOG.error("setRsaInfo Exception :: ", ex);
			throw new CommonException(ex);
		}
	}
}
