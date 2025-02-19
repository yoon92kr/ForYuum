package com.foryuum.frontend.linkage.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foryuum.frontend.common.service.WebDriverConfig;

public class Shuline {

	private static final Logger LOG = LoggerFactory.getLogger(Shuline.class);
	private final WebDriver webDriver;
	WebDriverWait wait;

	public Shuline() {
		this.webDriver = WebDriverConfig.webDriver();
		wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
	}

	public void login(Map<String, Object> loginInfo) {
		webDriver.get("https://shuline.co.kr/member/login.html");

		if (isLoginPage()) {
			WebElement companyInput = webDriver.findElement(By.id("member_id"));
			companyInput.sendKeys(loginInfo.get("LOGIN_ID").toString());

			WebElement passwordInput = webDriver.findElement(By.id("member_passwd"));
			passwordInput.sendKeys(loginInfo.get("LOGIN_PASSWORD").toString());

			WebElement loginButton = webDriver.findElement(By.cssSelector(".btnSubmit.sizeL.df-lang-button-login"));
			loginButton.click();
		}
	}
	
	public void shulineProcess(Map<String, Object> resultMap, List<Map<String, Object>> orderList) {
		try {
			Actions actions = new Actions(webDriver);
			String lastDeliverInfo = "";
			
			for (int i = 0 ; i < orderList.size(); i++) {
				Map<String, Object> map = orderList.get(i);
				
				/* 최초 주문 정보가 아니고, 직전 주문인과 다를 경우 주문 진행 */
				if(i > 0 && !lastDeliverInfo.equals(map.get("P_NAME").toString() + "_" + map.get("P_ADDRESS").toString())) {
					saveOrder(map);
				}
				
				
				lastDeliverInfo = map.get("P_NAME").toString() + "_" + map.get("P_ADDRESS").toString(); // 이전 배송 정보
			}
			// 	품절 여부 확인
			// 같은 고객 별로 주문 묶고 장바구니 > 결제
	
		} catch (Exception e) {
			LOG.error("ecountProcess Exception :: {}", e);
			
            resultMap.put("RESULT", false);
            resultMap.put("RESULT_MSG", "배송 요청에 실패했습니다.\n 아빠를 불러주세요!");
		} finally {
			logout();
		}
	}

	public void logout() {
		webDriver.get("https://shuline.co.kr/exec/front/Member/logout");
		webDriver.quit();
	}
	
	private Map<String, Object> saveOrder(Map<String, Object> orderInfo) {
		Map<String, Object> orderMap = new HashMap<String, Object>();
		webDriver.get("https://shuline.co.kr/order/orderform.html?basket_type=all_buy");
		
		/* 배송지 - 직접입력 */
		WebElement addressButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ec-jigsaw-tab-shippingInfo-newAddress")));
		addressButton.click();
		
		/* 새로운 배송지 */
		WebElement newaddressButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sameaddr1")));
		newaddressButton.click();
		
		/* 수령인 명 입력 */
        WebElement rname_input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rname")));
        rname_input.sendKeys(orderInfo.get("P_NAME").toString());
        
        /* 주소검색 버튼 */
		WebElement rzipcodeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn_search_rzipcode")));
		rzipcodeButton.click();
		
		
		return orderMap;
	}

	private boolean isLoginPage() {
		boolean result = false;
		try {
	          WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".btnSubmit.sizeL.df-lang-button-login")));
			 result = loginButton != null && loginButton.isDisplayed();
		} catch (NoSuchElementException e) {
			LOG.error("isLoginPage Exception :: {}", e);
		}

		return result;
	}

}