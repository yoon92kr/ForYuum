package com.foryuum.frontend.linkage.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foryuum.frontend.common.ComConstant;
import com.foryuum.frontend.common.util.CommonUtil;
import com.foryuum.frontend.common.util.LinkageUtil;
import com.foryuum.frontend.item.service.ItemService;
import com.foryuum.frontend.linkage.vo.Delivery;
import com.foryuum.frontend.linkage.vo.NaverResponse;
import com.foryuum.frontend.linkage.vo.NaverShippingInfo;
import com.foryuum.frontend.linkage.vo.Order;
import com.foryuum.frontend.linkage.vo.ProductOrder;

import jakarta.annotation.Resource;
import net.sf.json.JSONObject;

@Service("naverStore")
public class NaverStore {

	private static final Logger LOG = LoggerFactory.getLogger(NaverStore.class);

	@Resource(name = "itemService")
	private ItemService itemService;
	
	public void getItemInfoByItemOrderNo (Map<String, Object> loginInfo, Map<String, Object> returnData, Map<String, Object> requestData) {
		String authToken = getAuthToken(loginInfo);
		
		if(!CommonUtil.isNullOrEmpty(authToken)) {
			getItemInfo(authToken, requestData, returnData);
		} else {
			LinkageUtil.setReult(returnData, false, "주문 정보 조회 실패",  "뭔가 이상합니다.\n 아빠를 불러주세요!");
		}
	}
	
	public String getAuthToken(Map<String, Object> loginInfo) {
		String authToken = "";
		
		try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            long timestamp = Instant.now().toEpochMilli();
            String clientId = loginInfo.get("LOGIN_ID").toString();
            String clientSecret = loginInfo.get("LOGIN_PASSWORD").toString();
            
            // 요청 본문 데이터 생성
            MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
            bodyParams.add("client_id", clientId);
            bodyParams.add("timestamp", String.valueOf(timestamp));
            bodyParams.add("grant_type", "client_credentials");
            bodyParams.add("client_secret_sign", generateSignature(clientId, clientSecret, timestamp));
            bodyParams.add("type", "SELF");

            // RestTemplate 생성
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyParams, headers);

            ResponseEntity<String> response = restTemplate.exchange("https://api.commerce.naver.com/external/v1/oauth2/token", HttpMethod.POST, requestEntity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                if (responseBody != null) {
                    JSONObject jResult = JSONObject.fromObject(responseBody);
                    authToken = jResult.getString("access_token");
                    LOG.info("Get NaverStore Auth Token Success");
                } 
            } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
            	LOG.error("getAuthToken Error : 네이버 커머스 서버 다운");
            } else {
            	LOG.error("getAuthToken Error : 토큰 정보 조회 연동 실패");
            }
            
		} catch (Exception ex) {
			LOG.error("getAuthToken Error", ex);
		}
		
		return authToken;
	}
	
	public void getItemInfo (String authToken, Map<String, Object> requestData, Map<String, Object> returnData) {
		try {
			String itemOrderNo = requestData.get("P_ITEM_ORDER_NO").toString(); 
	        List<String> productOrderIds = Arrays.asList(itemOrderNo);
	
	        // 요청 본문 데이터 준비
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("Authorization", "Bearer " + authToken);
	
	        String requestBody = "{ \"productOrderIds\": " + productOrderIds.toString() + ", \"quantityClaimCompatibility\": false }";
	        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
	
	        // RestTemplate을 사용하여 API 요청
	        RestTemplate restTemplate = new RestTemplate();
	        ResponseEntity<String> response = restTemplate.exchange(
	                "https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/query", HttpMethod.POST, entity, String.class);
	
	        // 응답 처리
	        if (response.getStatusCode() == HttpStatus.OK) {
	        	Map<String, Object> convertMap = new HashMap<String, Object>();

	        	String jsonResponse = response.getBody();
	            ObjectMapper objectMapper = new ObjectMapper();
	            NaverResponse naverResponse = objectMapper.readValue(jsonResponse, NaverResponse.class);
	            
	        	ProductOrder productOrder = naverResponse.getData().get(0).getProductOrder();
	        	Delivery delivery = naverResponse.getData().get(0).getDelivery();
	        	Order order = naverResponse.getData().get(0).getOrder();
	        	NaverShippingInfo shippingInfo = productOrder.getShippingAddress();
	        	ZonedDateTime zonedDateTime = ZonedDateTime.parse(order.getOrderDate(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	        	String formattedDate = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	              
	        	requestData.put("P_ITEM_NO", productOrder.getProductId());
	        	Map<String, Object> itemInfo = itemService.getItemInfoByItemNo(requestData);
	        	
	        	convertMap.put("VENDOR_ITEM_NAME", itemInfo.get("VENDOR_ITEM_NAME"));
	        	convertMap.put("VENDOR_PRICE", itemInfo.get("VENDOR_PRICE"));
	        	convertMap.put("ORDER_COUNT", productOrder.getQuantity());
	        	convertMap.put("VENDOR_NAME", itemInfo.get("VENDOR_NAME"));
	        	convertMap.put("ORDER_ITEM_OPTION", productOrder.getProductOption());
	        	
	        	convertMap.put("ORDERER_NAME", order.getOrdererName());
	        	convertMap.put("ORDERER_ID", order.getOrdererId());
	        	convertMap.put("ORDER_DATE", formattedDate);
	        	
	        	convertMap.put("RECEIVER_NAME", shippingInfo.getName());
	        	convertMap.put("RECEIVER_ADDRES", shippingInfo.getDetailedAddress() + " " + shippingInfo.getDetailedAddress());
	        	convertMap.put("RECEIVER_MOBILE", shippingInfo.getTel1());
	        	convertMap.put("SHIPPING_MEMO", CommonUtil.nullCheck(productOrder.getShippingMemo()));
	        	
	        	if(!CommonUtil.isNullOrEmpty(delivery) && !CommonUtil.isNullOrEmpty(delivery.getDeliveryStatus())) {
	        		String deliveryStatus = "";
	        		String deliveryCompany = "";
	        		String trackingNumber = "";
	        		
	        		switch(delivery.getDeliveryStatus()) {
	        		case ComConstant.DELIVERING:
	        			deliveryStatus = ComConstant.DELIVERING_NAME;
	        		case ComConstant.DELIVERED:
	        			deliveryStatus = ComConstant.DELIVERED_NAME;
	        		case ComConstant.PURCHASE_DECIDED:
	        			deliveryStatus = ComConstant.PURCHASE_DECIDED_NAME;
	        			deliveryCompany = CommonUtil.nullCheck(delivery.getDeliveryCompany()).toString();
	        			trackingNumber = CommonUtil.nullCheck(delivery.getTrackingNumber()).toString();
	        			break;
	        			
	        		}
	        		
	        		if(!CommonUtil.isNullOrEmpty(trackingNumber)) {
	        			LinkageUtil.setReult(returnData, true, "배송 정보 존재",  deliveryStatus + " 상태 입니다.\n" + deliveryCompany + " [" + trackingNumber + "]\n 중복 주문이 되지 않도록 주의해 주세요.");
	        		}
	        	}
	        	returnData.put("RESULT_VALUE", convertMap);
	        	LOG.info("Get NaverStore Order Info Success. resultData :: {}", convertMap);
	        	
	        } else {
	        	LinkageUtil.setReult(returnData, false, "주문 정보 조회 실패",  "뭔가 이상합니다.\n 아빠를 불러주세요!");
	        } 
		}  catch (Exception ex) {
			LOG.error("getItemInfo Error", ex);
		}
	}
	
	public boolean setTrackingNumber (String authToken, Map<String, String> requestData) {
		boolean result = false;
		try {
			// 요청 본문 데이터 준비
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + authToken);
			
			ZoneId kstZone = ZoneId.of("Asia/Seoul");
			ZonedDateTime nowKST = ZonedDateTime.now(kstZone);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			String formattedDate = nowKST.format(formatter);
			
			String requestBody = "";
			requestBody += "{";
			requestBody += "\"productOrderId\": \"" + requestData.get("P_PRODUCT_ORDER_ID") + "\"";
			requestBody += ", \"deliveryMethod\": \"DELIVERY\"";
			requestBody += ", \"deliveryCompanyCode\": \"HANJIN\"";
			requestBody += ", \"productOrderId\": \"" + requestData.get("TRACKING_NUMBER") + "\"";
			requestBody += ", \"dispatchDate\": \"" + formattedDate + "\"";
			requestBody += "}";
			
			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
			
			// RestTemplate을 사용하여 API 요청
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(
					"https://api.commerce.naver.com/external/v1/pay-order/seller/product-orders/dispatch", HttpMethod.POST, entity, String.class);
			
			if (response.getStatusCode() == HttpStatus.OK) {
		        ObjectMapper objectMapper = new ObjectMapper();
		        JsonNode successProductOrderIds = objectMapper.readTree(response.getBody())
		                .path("data").path("successProductOrderIds");

		        result = successProductOrderIds.isArray() && successProductOrderIds.size() > 0;
			}
			
		} catch (Exception ex) {
			LOG.error("setTrackingNumber Error", ex);
		}
        return result;
	}
	
    public static String generateSignature(String clientId, String clientSecret, Long timestamp) {
        String password = StringUtils.joinWith("_", clientId, timestamp);
        String hashedPw = BCrypt.hashpw(password, clientSecret);
        return Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
    }
}