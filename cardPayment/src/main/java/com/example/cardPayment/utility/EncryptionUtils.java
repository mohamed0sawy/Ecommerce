package com.example.cardPayment.utility;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EncryptionUtils {

    private static final Logger logger = Logger.getLogger(EncryptionUtils.class.getName());
    private static final String ALGORITHM = "AES";
    private static final SecretKey SECRET_KEY;

    static {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(128);
            SECRET_KEY = keyGen.generateKey();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error initializing the encryption key", e);
            throw new RuntimeException("Error initializing the encryption key", e);
        }
    }

    public static String encrypt(String value) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error encrypting value: " + value, e);
            throw new RuntimeException("Error encrypting value", e);
        }
    }

    public static String decrypt(String encryptedValue) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            byte[] decodedValue = Base64.getDecoder().decode(encryptedValue);
            byte[] decryptedValue = cipher.doFinal(decodedValue);
            return new String(decryptedValue);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error decrypting value: " + encryptedValue, e);
            throw new RuntimeException("Error decrypting value", e);
        }
    }
}
