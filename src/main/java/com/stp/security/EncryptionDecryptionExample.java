package com.stp.security;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionDecryptionExample {
	public static String encryption256(String strToEncrypt, String SECRET_KEY, String SALT) {
		try {
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			// IvParameterSpec ivspec = generateIv();

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes()));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

	public static String decrypt256(String strToDecrypt, String SECRET_KEY, String SALT) {
		try {
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			// IvParameterSpec ivspec = generateIv();
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}

	public static String generateHmac(String encryptedInput, String secretKey) {
		byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		Mac mac;
		String hmacHexString = null;
		try {
			mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(secretKeyBytes, "HmacSHA256"));
			hmacHexString = bytesToBase64(mac.doFinal(encryptedInput.getBytes(StandardCharsets.UTF_8)));
			return hmacHexString;
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}

	private static String bytesToBase64(byte[] hash) {
		String hexString = Base64.getEncoder().encodeToString(hash);
		return hexString;
	}

	public static void main(String[] args) {
		String originalString = "vivek Hiware";
		String secretKey = "6FEA14735E4498A90175052342443AF11DAF83B0D93862C1692DF0E3226092F8"; // Add secret Key here
		String salt = "DB65FC256FE33913";
		String encryptedString = encryption256(originalString, secretKey, salt);
		System.out.println("encryptedString  = " + encryptedString);
		String hmac = generateHmac(encryptedString, secretKey);
		System.out.println("hmac = " + hmac);
		String decryptionHmac = generateHmac(encryptedString, secretKey);
		if (hmac.equals(decryptionHmac)) {
			String decryptedString = decrypt256(encryptedString, secretKey, salt);
			System.out.println("decryptedString = " + decryptedString);
		}

		String decrypt256 = decrypt256("e04ca5d47ef3b158b631d4db274741d7FpK87ej9LgPl3ZG+CENfGQ==", secretKey, salt);
		System.out.println(decrypt256);
	}
}
