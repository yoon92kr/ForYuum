package com.foryuum.frontend.linkage.service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.KakaoOption;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("coolSMS")
public class CoolSMS {
    private static final Logger LOG = LoggerFactory.getLogger(CoolSMS.class);
    private final static String KEY = System.getProperties().getProperty("jasypt.smsapi.key");
    private final static String SECERT = System.getProperties().getProperty("jasypt.smsapi.secret");
    private final static String FROM = System.getProperties().getProperty("jasypt.smsapi.from");
    private final static String TO = System.getProperties().getProperty("jasypt.smsapi.to");
    private final static String TEMPLATE_ID = System.getProperties().getProperty("jasypt.smsapi.templateid");
    private final static String PFID = System.getProperties().getProperty("jasypt.smsapi.pfid");

    public void sendKakaoTalk(HashMap<String, String> resultMap) {
        DefaultMessageService messageService =  NurigoApp.INSTANCE.initialize(KEY, SECERT, "https://api.coolsms.co.kr");

        KakaoOption kakaoOption = new KakaoOption();
        kakaoOption.setPfId(PFID);
        kakaoOption.setTemplateId(TEMPLATE_ID);

        // disableSms를 true로 설정하실 경우 문자로 대체발송 되지 않습니다.
        // kakaoOption.setDisableSms(true);

        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{date}", resultMap.get("DATE"));
        variables.put("#{totalcount}", resultMap.get("TOTAL_COUNT"));
        variables.put("#{successcount}", resultMap.get("SUCCEESS_COUNT"));
        variables.put("#{failcount}", resultMap.get("FAIL_COUNT"));
        variables.put("#{resultMsg}", resultMap.get("RESULT"));
        kakaoOption.setVariables(variables);

        Message message = new Message();
        message.setFrom(FROM);
        message.setTo(TO);
        message.setKakaoOptions(kakaoOption);

        try {
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException exception) {
            LOG.info(exception.getFailedMessageList().toString());
            LOG.info(exception.getMessage());
        } catch (Exception exception) {
            LOG.info(exception.getMessage());
        }
    }
}
