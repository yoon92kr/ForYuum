package com.foryuum.frontend.common.util;

import java.util.Properties;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig implements TextEncryptor {

	public StringEncryptor stringEncryptor() {
		// JVM Argument 에서 가져오도록 한다.
		Properties props = System.getProperties();
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(props.getProperty("jasypt.encrypt.pwkey"));
		config.setPoolSize("1");
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setStringOutputType("base64");
		config.setKeyObtentionIterations("1000");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		encryptor.setConfig(config);
		return encryptor;
	}

	// Test Class를 위해서 암호를 파라메터로 입력하는 경우
	public StringEncryptor stringEncryptor(String args) {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(args);
		config.setPoolSize("1");
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setStringOutputType("base64");
		config.setKeyObtentionIterations("1000");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		encryptor.setConfig(config);
		return encryptor;
	}

	@Override
	public String encrypt(String message) {
		// TODO Auto-generated method stub
		return stringEncryptor().encrypt(message);
	}

	/*
	 * @desc : 직접 아래 메소드를 사용할 때에는 "ENC(", ")"를 제거하고 복호화 해야함
	 * 
	 * @example : JasyptConfig jn = new JasyptConfig()
	 * jn.stringEncryptor().decrypt(pt.substring(pt.indexOf("ENC(")+4,
	 * pt.lastIndexOf(")"))
	 */
	@Override
	public String decrypt(String encryptedMessage) {
		// TODO Auto-generated method stub
		return stringEncryptor().decrypt(encryptedMessage);
	}
}