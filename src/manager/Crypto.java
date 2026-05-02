package manager;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
	
	private static final String KEY = "1234567890123456";
	
	public static String encrypt(String credential) {
	    try {
	        SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, key);

	        byte[] encrypted = cipher.doFinal(credential.getBytes());
	        return Base64.getEncoder().encodeToString(encrypted);

	    } catch (Exception e) {
	        System.out.println("Encryption error");
	        return credential;
	    }
	}
	
	public static String decrypt(String encryptedCredential) {
	    try {
	        SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.DECRYPT_MODE, key);

	        byte[] decoded = Base64.getDecoder().decode(encryptedCredential);
	        return new String(cipher.doFinal(decoded));

	    } catch (Exception e) {
	        System.out.println("Decryption error");
	        return encryptedCredential;
	    }
	}
}
