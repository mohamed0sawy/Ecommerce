package com.academy.Ecommerce.utility;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private static final String ALGORITHM = "AES";
    private static final String KEY = "xk6LprFMQfV0yjFQ4i3sottMh2XuSNot0qFsLaXzjh0="; // Ensure this is secure and managed appropriately

    // Example IV, should be securely generated and shared between encryption and decryption
    private static final String IV = "1234567890123456";

    public static String encrypt(String value) throws Exception {
        // Ensure the key is decoded correctly from Base64
        byte[] decodedKey = Base64.getDecoder().decode(KEY);
        SecretKeySpec keySpec = new SecretKeySpec(decodedKey, ALGORITHM);

        // Example IV initialization, you should replace with a secure random initialization
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encryptedValue = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    public static String decrypt(String encryptedValue) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(KEY);
        SecretKeySpec keySpec = new SecretKeySpec(decodedKey, ALGORITHM);

        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decryptedValue = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
        return new String(decryptedValue);
    }

}

