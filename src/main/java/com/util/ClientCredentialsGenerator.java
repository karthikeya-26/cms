package com.util;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class ClientCredentialsGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();
    public  String generateClientId() {
        return UUID.randomUUID().toString();
    }

    public  String generateClientSecret() {
        byte[] secretBytes = new byte[32];
        secureRandom.nextBytes(secretBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(secretBytes);
    }

    public static void main(String[] args) {
    	ClientCredentialsGenerator c = new ClientCredentialsGenerator();
        String clientId = c.generateClientId();
        String clientSecret = c.generateClientSecret();

        System.out.println("Client ID: " + clientId);
        System.out.println("Client Secret: " + clientSecret);
    }
}
