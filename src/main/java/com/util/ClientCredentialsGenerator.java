package com.util;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class ClientCredentialsGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateClientId() {
        return UUID.randomUUID().toString();
    }

    public static String generateClientSecret() {
        byte[] secretBytes = new byte[32];
        secureRandom.nextBytes(secretBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(secretBytes);
    }

    public static void main(String[] args) {
        String clientId = generateClientId();
        String clientSecret = generateClientSecret();

        System.out.println("Client ID: " + clientId);
        System.out.println("Client Secret: " + clientSecret);
    }
}
