package com.foryuum.frontend.common.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foryuum.frontend.common.exception.CommonException;
import com.foryuum.frontend.main.controller.HomeController;

public class CipherUtil {

	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

	/**
	 * 1024비트 RSA 키쌍을 생성합니다.
	 */
	public static KeyPair genRSAKeyPair() throws NoSuchAlgorithmException {

		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator gen;
		gen = KeyPairGenerator.getInstance("RSA");
		gen.initialize(1024, secureRandom);
		KeyPair keyPair = gen.genKeyPair();
		return keyPair;
	}

	/**
	 * 복호화
	 * 
	 * @param privateKey
	 * @param securedValue
	 * @return
	 * @throws Exception
	 */
	public static String decryptRSA(PrivateKey privateKey, String securedValue) throws CommonException {
		String decryptedValue = "";
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			byte[] encryptedBytes = hexToByteArray(securedValue);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
			decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
		} catch (Exception e) {
			LOG.error("decryptRsa error {} :: ", e);
		}
		return decryptedValue;
	}

	/**
	 * 16진 문자열을 byte 배열로 변환한다.
	 */
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() % 2 != 0) {
			return new byte[] {};
		}

		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
			bytes[(int) Math.floor(i / 2)] = value;
		}
		return bytes;
	}
}
