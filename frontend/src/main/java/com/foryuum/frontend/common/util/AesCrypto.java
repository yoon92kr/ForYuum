package com.foryuum.frontend.common.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.foryuum.frontend.common.exception.CommonException;

@Component
@PropertySource("classpath:config/properties/linkage.properties")
public class AesCrypto {

	private static final Logger LOG = LoggerFactory.getLogger(AesCrypto.class);
	private static final String TRANSFORM = "AES/CBC/PKCS5Padding";
	private static final String PROVIDE = "BC";
	
	private static final byte[] IV = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
	private static final IvParameterSpec IVSPEC = new IvParameterSpec(IV);

	private static String tempStrK;

	@Value("#{environment['aesCrypt.aesKey']}")
	private void setStrAesCryptoKey(String strAesCryptoKey) {
		tempStrK = strAesCryptoKey;
	}

	public static String getTempStrK() {
		return tempStrK;
	}

	public static String encrypt(String plainText) throws CommonException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		Security.addProvider(new BouncyCastleProvider());
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);

		byte[] raw = getTempStrK().getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher;
		byte[] encrypted = null;
		try {
			cipher = Cipher.getInstance(TRANSFORM, PROVIDE);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IVSPEC);
			encrypted = cipher.doFinal(plainText.getBytes());
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			LOG.error("e", e);
		} catch (InvalidKeyException ike) {
			LOG.error("ike", ike);
		} catch (InvalidAlgorithmParameterException iae) {
			LOG.error("iae", iae);
		}

		
		return asHex(encrypted);
	}

	public static String decrypt(String cipherText) throws CommonException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		Security.addProvider(new BouncyCastleProvider());
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		byte[] raw = getTempStrK().getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		byte[] original = null;
		String originalString = null;
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORM, PROVIDE);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, IVSPEC);
			original = cipher.doFinal(fromString(cipherText));
			originalString = new String(original);
		} catch (NoSuchProviderException e) {
			LOG.error("", e);
		} catch (InvalidKeyException ike) {
			LOG.error("ike", ike);
		} catch (InvalidAlgorithmParameterException iae) {
			LOG.error("iae", iae);
		}
		return originalString;
	}

	private static String asHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;

		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10) {
				strbuf.append("0");
			}
			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}

		return strbuf.toString();
	}

	private static byte[] fromString(String hex) {
		int len = hex.length();
		byte[] buf = new byte[(len + 1) / 2];

		int i = 0; 
		int j = 0;
		if (len % 2 == 1) {
			buf[j++] = (byte) fromDigit(hex.charAt(i++));
		}
		while (i < len) {
			buf[j++] = (byte) ((fromDigit(hex.charAt(i++)) << 4) | fromDigit(hex.charAt(i++)));
		}
		return buf;
	}

	private static int fromDigit(char ch) {
		if (ch >= '0' && ch <= '9') {
			return ch - '0';
		}
		if (ch >= 'A' && ch <= 'F') {
			return ch - 'A' + 10;
		}
		if (ch >= 'a' && ch <= 'f') {
			return ch - 'a' + 10;
		}
		throw new IllegalArgumentException("invalid hex digit '" + ch + "'");
	}

}
