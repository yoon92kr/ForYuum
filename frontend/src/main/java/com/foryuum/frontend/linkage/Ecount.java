package com.foryuum.frontend.linkage;

import java.time.Duration;
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

public class Ecount {

	private static final Logger LOG = LoggerFactory.getLogger(Ecount.class);
	private final WebDriver webDriver;

	public Ecount() {
		this.webDriver = WebDriverConfig.webDriver();
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
	
	public void ecountProcess(Map<String, Object> resultMap, List<Map<String, Object>> orderList) {
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
			Actions actions = new Actions(webDriver);
			String lastDeliverInfo = "";
	
			WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("group3slipSavePrint")));
			/* 1. 배송 정보 입력 */
			for (int i = 0 ; i < orderList.size(); i++) {
				Map<String, Object> map = orderList.get(i);
				
		        /* 품목 코드 [001] */
		        WebElement prod_cd = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr[data-key='" + i + "'] td[data-columnid='prod_cd']")));
		        actions.moveToElement(prod_cd).click().perform();
		        
		        WebElement prod_cd_input = prod_cd.findElement(By.tagName("input"));
		        actions.moveToElement(prod_cd_input).click().perform();
		        prod_cd_input.sendKeys("001");
		        
		        /* 배송 방법 [고객발송] */
		        WebElement add_cd_01 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr[data-key='" + i + "'] td[data-columnid='ADD_CD_01']")));
		        add_cd_01.click();
		        
		        WebElement add_cd_01_button = add_cd_01.findElement(By.cssSelector("div.grid-input-holder.edit_container.edit-state div#edit div.control-set div.control button"));
		        add_cd_01_button.click();
		        
	            WebElement customerDeliveryOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='고객발송']")));
	            customerDeliveryOption.click();
		        
		        /* 보내시는분 상호 [모던블랑코] */
		        WebElement remarks = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr[data-key='" + i + "'] td[data-columnid='remarks']")));
		        actions.moveToElement(remarks).click().perform();
		        
		        WebElement remarks_input = remarks.findElement(By.tagName("input"));
		        actions.moveToElement(remarks_input).click().perform();
		        remarks_input.clear();
		        remarks_input.sendKeys("모던블랑코");
		        
		        /* 받으시는분 성함 */
		        if(!lastDeliverInfo.equals(map.get("P_NAME").toString() + "_" + map.get("P_ADDRESS").toString())) {
		        	WebElement p_remarks2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr[data-key='" + i + "'] td[data-columnid='p_remarks2']")));
		        	actions.moveToElement(p_remarks2).click().perform();
		        	
		        	WebElement p_remarks2_input = p_remarks2.findElement(By.tagName("input"));
		        	actions.moveToElement(p_remarks2_input).click().perform();
		        	p_remarks2_input.sendKeys(map.get("P_NAME").toString());
		        	
		        	/* 받으시는분 전화번호 */
		        	WebElement p_remarks3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr[data-key='" + i + "'] td[data-columnid='p_remarks3']")));
		        	actions.moveToElement(p_remarks3).click().perform();
		        	
		        	WebElement p_remarks3_input = p_remarks3.findElement(By.tagName("input"));
		        	actions.moveToElement(p_remarks3_input).click().perform();
		        	p_remarks3_input.sendKeys(map.get("P_MOBILE").toString());
		        	
		        	/* 받으시는분 주소 */
		        	WebElement add_txt_01 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr[data-key='" + i + "'] td[data-columnid='ADD_TXT_01']")));
		        	actions.moveToElement(add_txt_01).click().perform();
		        	
		        	WebElement add_txt_01_input = add_txt_01.findElement(By.tagName("input"));
		        	actions.moveToElement(add_txt_01_input).click().perform();
		        	add_txt_01_input.sendKeys(map.get("P_ADDRESS").toString());
		        	
		        	/* 배송메모 */
		        	WebElement add_txt_02 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr[data-key='" + i + "'] td[data-columnid='ADD_TXT_02']")));
		        	actions.moveToElement(add_txt_02).click().perform();
		        	
		        	WebElement add_txt_02_input = add_txt_02.findElement(By.tagName("input"));
		        	actions.moveToElement(add_txt_02_input).click().perform();
		        	add_txt_02_input.sendKeys(map.get("P_NOTE").toString());
		        }
		        
		        /* 도매 상호명 */
		        WebElement add_txt_03 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr[data-key='" + i + "'] td[data-columnid='ADD_TXT_03']")));
		        actions.moveToElement(add_txt_03).click().perform();
		        
		        WebElement add_txt_03_input = add_txt_03.findElement(By.tagName("input"));
		        actions.moveToElement(add_txt_03_input).click().perform();
		        add_txt_03_input.sendKeys(map.get("P_VENDOR_NAME").toString());
		        
		        /* 도매 상품명 */
		        WebElement add_txt_04 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr[data-key='" + i + "'] td[data-columnid='ADD_TXT_04']")));
		        actions.moveToElement(add_txt_04).click().perform();
		        
		        WebElement add_txt_04_input = add_txt_04.findElement(By.tagName("input"));
		        actions.moveToElement(add_txt_04_input).click().perform();
		        add_txt_04_input.sendKeys(map.get("P_VENDOR_ITEM_NAME").toString());
		        
		        /* 옵션 */
		        WebElement add_txt_05 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr[data-key='" + i + "'] td[data-columnid='ADD_TXT_05']")));
		        actions.moveToElement(add_txt_05).click().perform();
		        
		        WebElement add_txt_05_input = add_txt_05.findElement(By.tagName("input"));
		        actions.moveToElement(add_txt_05_input).click().perform();
		        add_txt_05_input.sendKeys(map.get("P_VENDOR_ITEM_OPTION").toString());
		        
		        /* 수량 */
		        WebElement qty = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tr[data-key='" + i + "'] td[data-columnid='qty']")));
		        actions.moveToElement(qty).click().perform();
		        
		        WebElement qty_input = qty.findElement(By.tagName("input"));
		        actions.moveToElement(qty_input).click().perform();
		        qty_input.sendKeys(map.get("P_COUNT").toString());
		        
		        lastDeliverInfo = map.get("P_NAME").toString() + "_" + map.get("P_ADDRESS").toString(); // 이전 배송 정보
	        }
			
			/* 2. 저장 요청 */
			saveButton.click();
			Thread.sleep(200);
			saveButton.click();
			
			/* 3. 저장 결과 확인 */
			WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#textcontainer-_36d0amq1 > span")));
	        int resultCount = Integer.valueOf(result.getText());
	        
	        if(resultCount > 0) {
	        	StringBuilder sb = new StringBuilder();
	        	sb.append(resultMap.get("RESULT_VALUE"));
	        	sb.append("\n 배송 건수 : ");
	        	sb.append(resultCount);
	        	sb.append("건");
	        	
	        	resultMap.put("RESULT_VALUE", sb.toString());
	        }
	
		} catch (Exception e) {
			LOG.error("ecountProcess Exception :: {}", e);
			
            resultMap.put("RESULT", false);
            resultMap.put("RESULT_MSG", "배송 요청에 실패했습니다.\n 아빠를 불러주세요!");
		} finally {
			logout();
		}
	}

	public void moveCompleteDeliver() {
		webDriver.get(webDriver.getCurrentUrl() + "#menuType=6&amp;menuSeq=9003&amp;groupSeq=1&amp;prgId=E040204");
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

}