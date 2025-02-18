package com.foryuum.frontend.restApi.naverStore;

import net.sf.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NaverLinkageService {
    @Headers("Content-Type: application/json; charset=utf8")
    @POST("/external/v1/oauth2/token")
    Call<JSONObject> getAuthToken(@Body() JSONObject requestData);
}
