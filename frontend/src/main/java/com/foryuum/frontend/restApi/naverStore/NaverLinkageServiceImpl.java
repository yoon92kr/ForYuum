package com.foryuum.frontend.restApi.naverStore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Service("naverLinkageService")
public class NaverLinkageServiceImpl {
	private static final Logger LOG = LoggerFactory.getLogger(NaverLinkageServiceImpl.class);

	private String restBaseurl = "https://api.commerce.naver.com/";
	private final static int CONNECT_TIMOEOUT = 10;
	private final static int READ_TIMEOUT = 60;
	private final static int WRITE_TIMEOUT = 60;
	
	public NaverLinkageService initService(Logger log) {
		synchronized (log) {
			Retrofit retrofit = null;
			NaverLinkageService restService = null;
			try {
				OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMOEOUT, TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).build();
				retrofit = new Retrofit.Builder().baseUrl(restBaseurl).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
				LOG.info("initService URL : {}", restBaseurl);
				restService = retrofit.create(NaverLinkageService.class);

			} catch (RuntimeException ex) {
				log.error("initService - ex : {}", ex);
				restService = null;
			}

			return restService;
		}
	}
	
	public JSONObject getAuthToken(String clientId, String clientSecret, String accountId, Long timeStamp, Logger log) {
		String password = StringUtils.joinWith("_", clientId, timeStamp);
        String hashedPw = BCrypt.hashpw(password, clientSecret);
        String clientSecretSign = Base64.getUrlEncoder().encodeToString(hashedPw.getBytes(StandardCharsets.UTF_8));
		
		
		JSONObject requestData = new JSONObject();
		requestData.put("client_id", clientId);
		requestData.put("timestamp", timeStamp);
		requestData.put("grant_type", "client_credentials");
		requestData.put("client_secret_sign", clientSecretSign);
		requestData.put("type", "SELLER");
		requestData.put("account_id", accountId);
		
		LOG.info("getAuthToken invoking Start : {}", requestData);

		JSONObject jsResult = null;
		NaverLinkageService linkageService = null;
		try {
			linkageService = initService(log);
			jsResult = linkageService.getAuthToken(requestData).execute().body();

			LOG.info("getLoginInfo invoking End : {}", jsResult == null ? "ResponseData is null" : jsResult);
		} catch (RuntimeException | IOException ex) {
			LOG.error("getLoginInfo Error", ex);
			jsResult = null;
		}

		return jsResult;
	}

	
}
