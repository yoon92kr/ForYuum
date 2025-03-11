package com.foryuum.frontend.linkage.service;

import com.foryuum.frontend.common.service.WebDriverConfig;
import com.foryuum.frontend.common.util.LinkageUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ecount {

	private static final Logger LOG = LoggerFactory.getLogger(Ecount.class);
	private final WebDriver webDriver;
	private final WebDriverWait wait;
	private final Actions actions;
	private final JavascriptExecutor jsExecutor;

	public Ecount() {
		this.webDriver = WebDriverConfig.webDriver();
		wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		actions = new Actions(webDriver);
	    jsExecutor = (JavascriptExecutor) webDriver;
	}

	public void login(Map<String, Object> loginInfo) {
		webDriver.get("https://c-portal.ecount.com");

		if (isLoginPage()) {
			WebElement companyInput = webDriver.findElement(By.id("com_code"));
			companyInput.sendKeys(loginInfo.get("LOGIN_ID").toString());

			WebElement loginInput = webDriver.findElement(By.id("id"));
			loginInput.sendKeys(loginInfo.get("LOGIN_SUB_ID").toString());

			WebElement passwordInput = webDriver.findElement(By.id("passwd"));
			passwordInput.sendKeys(loginInfo.get("LOGIN_PASSWORD").toString());

			WebElement loginButton = webDriver.findElement(By.id("save"));
			loginButton.click();
		}
	}
	
	public void ecountProcess(Map<String, Object> returnData, List<Map<String, Object>> orderList) {
		try {
			String lastDeliverInfo = "";
			Thread.sleep(3000);
			WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("group3slipSavePrint")));
			/* 1. 배송 정보 입력 */
			for (int i = 0 ; i < orderList.size(); i++) {
				Map<String, Object> map = orderList.get(i);
				
		        /* 품목 코드 [001] */
				checkAndInput(i, "prod_cd", "001");
		        
		        /* 배송 방법 [고객발송] */
		        WebElement add_cd_01 = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("tr[data-key='" + i + "'] td[data-columnid='ADD_CD_01']")));
		        add_cd_01.click();
		        
		        WebElement add_cd_01_button = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.grid-input-holder.edit_container.edit-state div#edit div.control-set div.control button")));
		        add_cd_01_button.click();
		        
	            WebElement customerDeliveryOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='고객발송']")));
	            customerDeliveryOption.click();
		        
		        /* 보내시는분 상호 */
	            checkAndInput(i, "remarks", map.get("P_COMPANY_NAME").toString());
		        
		        /* 받으시는분 성함 */
		        if(!lastDeliverInfo.equals(map.get("P_NAME").toString() + "_" + map.get("P_ADDRESS").toString())) {
		        	checkAndInput(i, "p_remarks2", map.get("P_NAME").toString());
		        	
		        	/* 받으시는분 전화번호 */
		        	checkAndInput(i, "p_remarks3", map.get("P_MOBILE").toString());
		        	
		        	/* 받으시는분 주소 */
		        	checkAndInput(i, "ADD_TXT_01", map.get("P_ADDRESS").toString());
		        	
		        	/* 배송메모 */
		        	checkAndInput(i, "ADD_TXT_02", map.get("P_NOTE").toString());
		        }
		        
		        /* 도매 상호명 */
		        checkAndInput(i, "ADD_TXT_03", map.get("P_VENDOR_NAME").toString());
		        
		        /* 도매 상품명 */
		        checkAndInput(i, "ADD_TXT_04", map.get("P_VENDOR_ITEM_NAME").toString());
		        
		        /* 옵션 */
		        checkAndInput(i, "ADD_TXT_05", map.get("P_VENDOR_ITEM_OPTION").toString());
		        
		        /* 수량 */
		        checkAndInput(i, "qty", map.get("P_COUNT").toString());
		        
		        lastDeliverInfo = map.get("P_NAME").toString() + "_" + map.get("P_ADDRESS").toString(); // 이전 배송 정보
	        }
			
			/* 2. 저장 요청 */
			saveButton.click();
			Thread.sleep(500);
			saveButton.click();
			
			/* 3. 저장 결과 확인 */
			WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#textcontainer-_36d0amq1 > span")));
	        int resultCount = Integer.valueOf(result.getText());
	        
	        if(resultCount > 0) {
	        	StringBuilder sb = new StringBuilder();
	        	sb.append(returnData.get("RESULT_VALUE"));
	        	sb.append("\n 배송 건수 : ");
	        	sb.append(resultCount);
	        	sb.append("건");
	        	
	        	returnData.put("RESULT_VALUE", sb.toString());
	        }
	
		} catch (Exception e) {
			LOG.error("ecountProcess Exception :: {}", e);
			LinkageUtil.setReult(returnData, false, "배송 요청 실패",  "뭔가 이상합니다.\n 아빠를 불러주세요!");
		} finally {
			logout();
		}
	}

	public void moveCompleteDeliver() {
		webDriver.get(webDriver.getCurrentUrl() + "#menuType=6&amp;menuSeq=9003&amp;groupSeq=1&amp;prgId=E040204");
	}

	public void moveTrackingInfo() {
		webDriver.get(webDriver.getCurrentUrl() + "#menuType=6&menuSeq=9008&groupSeq=1&prgId=E040305&depth=3");
	}
	
	public void logout() {
		webDriver.get("https://c-portalab.ecount.com/login/logout");
		webDriver.quit();
	}

	private boolean isLoginPage() {
		boolean result = false;
		try {
			WebElement loginFooter = webDriver.findElement(By.id("privacyPolicy"));
			if (loginFooter.getText().equals("개인정보처리방침")) {
				result = true;
			}
		} catch (NoSuchElementException e) {
			LOG.error("isLoginPage Exception :: {}", e);
		}

		return result;
	}
	
	private void checkAndInput(int idx, String columnId, String value) throws NoSuchElementException {
	    WebElement clickElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("tr[data-key='" + idx + "'] td[data-columnid='" + columnId + "'] span")));
	    
	    try {
			jsExecutor.executeScript("arguments[0].click();", clickElement);
			processInput(idx, columnId, value);
	    } catch (Exception we) {
	        LOG.error("JavaScript Click Exception :: {}", columnId);

	        try {
				clickElement.click();
				processInput(idx, columnId, value);
	        } catch (Exception ae) {
	            LOG.error("WebElement Click Exception :: {}", columnId);
	            
	            try {
					actions.moveToElement(clickElement).click().perform();
					processInput(idx, columnId, value);
	            } catch (Exception je) {
	                LOG.error("Action Click Exception :: {}", columnId);
	                throw new NoSuchElementException("can't find input :: " + columnId, je);
	            }
	        }
	    }
	}
	
	private void processInput(int idx, String columnId, String value) {
	    WebElement inputElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("tr[data-key='" + idx + "'] td[data-columnid='" + columnId + "'] input")));
	    inputElement.clear();
	    inputElement.sendKeys(value);
	} 
	
	public List<Map<String, String>> getTrackingNumber() {
		List<Map<String, String>> returnData = new ArrayList<Map<String, String>>();

		try {
			Thread.sleep(3000);
			moveTrackingInfo();
			Thread.sleep(3000);
			WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("searchGroup")));
			
			Thread.sleep(1000);
			searchBtn.click();
			
			WebElement table = wait.until(ExpectedConditions.elementToBeClickable(By.id("grid-main")));
			List<WebElement> trs = table.findElements(By.tagName("tr"));

			for (WebElement tr : trs) {
			    List<WebElement> tds = tr.findElements(By.tagName("td"));
			    if (tds.size() == 4) {
		    		String shippingDt = tds.get(0).findElement(By.tagName("span")).getText();
		    		String receiverName = tds.get(1).findElement(By.tagName("span")).getText();
		    		String trackingNum = tds.get(2).findElement(By.tagName("span")).getText();
		    		
		    		Map<String, String> map = new HashMap<String, String>();
	    			map.put("P_RESULT", trackingNum.matches("\\d+") ? "T" : "F");
		    		map.put("P_SHIPPING_DT", shippingDt);
		    		map.put("P_RECEIVER_NAME", receiverName);
		    		map.put("TRACKING_NUMBER", trackingNum);
		    		
		    		returnData.add(map);
			    }
			}
		}  catch (Exception e) {
			LOG.error("getTrackingNumber Exception :: {}", e);
		} finally {
			logout();
		}
		
		return returnData;
	}

}
