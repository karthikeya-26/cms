package com.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Crypter {

    private static final String SECRET_KEY = "karthikeya123456";

    /**
     * Encrypts the given data using Blowfish encryption.
     *
     * @param data The plaintext data to encrypt.
     * @return The encrypted data as a Base64-encoded string.
     */
    public static String encrypt(String data) {
        try {
            SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error during encryption", e);
        }
    }

    /**
     * Decrypts the given encrypted data using Blowfish encryption.
     *
     * @param encryptedData The Base64-encoded encrypted data.
     * @return The decrypted plaintext data.
     */
    public static String decrypt(String encryptedData) {
        try {
            SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error during decryption", e);
        }
    }

    public static void main(String[] args) {
        String originalData = "Sai Tharun";

        // Encrypt the data
        String encryptedData = encrypt(originalData);
        System.out.println("Encrypted: " + encryptedData);

        // Decrypt the data
        String decryptedData = decrypt(encryptedData);
        System.out.println("Decrypted: " + decryptedData);
    }
}
