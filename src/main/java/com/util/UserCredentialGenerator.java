package com.util;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class UserCredentialGenerator {

	private static final SecureRandom secureRandom = new SecureRandom();
    public  String generateAuthorizationCode() {
        return UUID.randomUUID().toString();
    }

    public  String generateAccessToken() {
        byte[] secretBytes = new byte[32];
        secureRandom.nextBytes(secretBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(secretBytes);
    }
    
    public String generateRefreshToken() {
    	byte[] secretBytes = new byte[64];
    	secureRandom.nextBytes(secretBytes);
    	return Base64.getUrlEncoder().withoutPadding().encodeToString(secretBytes);
    }
}
