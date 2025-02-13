package com.foryuum.frontend.linkage;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foryuum.frontend.common.service.WebDriverConfig;

public class Feel {

	private static final Logger LOG = LoggerFactory.getLogger(Feel.class);
	private final WebDriver webDriver;

	public Feel() {
		this.webDriver = WebDriverConfig.webDriver();
	}

	public void login(Map<String, Object> loginInfo) {
		try {
			webDriver.get("https://feel.sosolution.net/login");
	
			if (isLoginPage()) {
				WebElement emailInput = webDriver.findElement(By.id("login_email"));
				emailInput.sendKeys(loginInfo.get("LOGIN_ID").toString());
	
				WebElement passwordInput = webDriver.findElement(By.id("login_password"));
				passwordInput.sendKeys(loginInfo.get("LOGIN_PASSWORD").toString());
	
				WebElement loginButton = webDriver
						.findElement(By.cssSelector("button.btn.btn-primary.block.full-width.m-b"));
				loginButton.click();
				
		        Actions actions = new Actions(webDriver);
		        
		        Thread.sleep(300);
		        actions.sendKeys(Keys.ESCAPE).perform();
		        Thread.sleep(300);
		        actions.sendKeys(Keys.ESCAPE).perform();
			}
		} catch (Exception e) {
			LOG.error("feel login Exception :: {}", e);
		}
	}
	
	public void feelProcess(Map<String, Object> resultMap, List<Map<String, Object>> orderList) {
		try {
			/* 1. Excel 사입 페이지 이동 */
			moveOrderToExcel();
			
			/* 2. 배열을 양식에 맞게 변환 후, 입력/변환 */
			fillAndSave(orderList);
			
			/* 3. 변환 성공 여부 확인 */
			WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
			WebElement firstAlart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body > div.sweet-alert.visible.showSweetAlert > h2")));
	        String firstAlartText = firstAlart.getText();  // <h2> 태그의 텍스트 가져오기
	        
	        /* 3-1. 변환 실패 시, 실패 매장명 반환 */
	        if (firstAlartText.equals("매칭(등록)되지 않은 매장 발견!")) {
	        	WebElement okButton = webDriver.findElement(By.className("confirm"));
	        	okButton.click();
	        	
	        	StringBuilder sb = new StringBuilder();
	            List<WebElement> rows = webDriver.findElements(By.cssSelector(".table.table-bordered.table-striped tbody tr"));

	            sb.append("매칭되지 않은 매장명 : [ ");
	            for (WebElement row : rows) {
	                WebElement firstColumn = row.findElement(By.tagName("td"));
	                sb.append(firstColumn.getText()).append(" ");
	            }
	            sb.append("]");
	            
	            resultMap.put("RESULT", false);
	            resultMap.put("RESULT_MSG", "매칭되지 않은 매장이 존재합니다.\n 아빠를 불러주세요!");
	            resultMap.put("RESULT_VALUE", sb.toString());
	        } else { 
	        	/* 3-2. 변환 성공 시 사입 요청 수행 */
	        	WebElement okButton = webDriver.findElement(By.className("confirm"));
	        	okButton.click();
	        	
	        	WebElement submitButton = webDriver.findElement(By.id("excel-submit"));
	        	submitButton.click();
	        	
	        	/* 4. 사입 요청 성공 여부 확인 */
				WebElement secondAlart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#page-wrapper > div.wrapper.wrapper-content.tooltip-default > div:nth-child(3) > div.col-lg-4 > div > div.ibox-title > h5")));
		        String secondAlartText = secondAlart.getText();
		        
		        /* 4-1. 요청 성공 시  */
		        if(secondAlartText.equals("오늘의 대납(기록) 예상액(합계액)")) {
		        	
		        	StringBuilder sb = new StringBuilder();
		        	
					WebElement change = webDriver.findElement(By.id("change-box"));
			        int intCharge = Integer.valueOf(change.getText().replace(",", ""));
			        
					WebElement cost = webDriver.findElement(By.id("cost-exp"));
			        int intCost = Integer.valueOf(cost.getText().replace(",", "").replace("원", ""));
			        
			        if(intCharge - intCost >= 0) {
			            DecimalFormat formatter = new DecimalFormat("#,###");
			            String formattedNumber = formatter.format(intCharge - intCost);
			            
			        	sb.append("주문 완료! 필사입 남은 잔액 : ");
			        	sb.append(formattedNumber);
			        	sb.append("원");
			        } else {
			            DecimalFormat formatter = new DecimalFormat("#,###");
			            String formattedNumber = formatter.format(intCost - intCharge);
			            
			        	sb.append("주문 완료!\n");
			        	sb.append("입금 계좌 : 신한 110-510-528482 필사입(김종필)\n");
			        	sb.append("입금 금액 : ");
			        	sb.append(formattedNumber);
			        	sb.append("원");
			        }
			        
		            resultMap.put("RESULT", true);
		            resultMap.put("RESULT_MSG", "사입 요청에 성공했습니다.");
		            resultMap.put("RESULT_VALUE", sb.toString());
			        
		        } else {
		        	/* 4-2. 요청 실패 시, 아직 원인을 모르기 때문에 임시 응답 */
		            resultMap.put("RESULT", false);
		            resultMap.put("RESULT_MSG", "뭔가 이상합니다.\n 아빠를 불러주세요!");
		        }
	        }
			
		} catch (Exception e) {
			LOG.error("feelProcess Exception :: {}", e);
		} finally {
			logout();
		}
	}

	public void moveOrderToExcel() {
		webDriver.get("https://feel.sosolution.net/excel/excel");
	}
	
	private void fillAndSave(List<Map<String, Object>> orderList) {
		
		WebElement textArea = webDriver.findElement(By.id("excel-textarea"));
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("arguments[0].value = arguments[1];", textArea, convertOrderToString(orderList));

		WebElement saveButton = webDriver.findElement(By.id("excel-next"));
		saveButton.click();
	}
	
	public void logout() {
		webDriver.get("https://feel.sosolution.net/login/logout");
		webDriver.quit();
	}

	private boolean isLoginPage() {
		boolean result = false;
		try {
			List<WebElement> elements = webDriver.findElements(By.cssSelector(".text-muted.text-center"));
			result = elements.size() > 0;
		} catch (Exception e) {
			LOG.error("feel isLoginPage Exception :: {}", e);
		}

		return result;
	}

	private String convertOrderToString(List<Map<String, Object>> orderList) {
		StringBuilder sb = new StringBuilder();

		for (Map<String, Object> map : orderList) {
			sb.append(map.get("P_VENDOR_NAME")).append("\t");
			sb.append("주문").append("\t");
			sb.append(map.get("P_VENDOR_ITEM_NAME")).append("\t");
			sb.append(map.get("P_VENDOR_ITEM_OPTION")).append("\t");
			sb.append(" ").append("\t");
			sb.append(map.get("P_COUNT")).append("\t");
			sb.append(map.get("P_VENDOR_PRICE")).append("\t");
			sb.append("\n");
		}

		return sb.toString().trim();
	}

}
