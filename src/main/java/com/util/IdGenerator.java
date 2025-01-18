package com.util;

import java.security.SecureRandom;

public class IdGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String NUMERIC = "0123456789";

    public String generateUserId() {
        return "USER-" + generateRandomString(16);
    }

    public String generateContactId() {
        return "CONTACT-" + generateRandomString(16);
    }

    public String generateSessionId() {
        return "SESSION-" + generateRandomString(32);
    }

    public String genenrateClientId() {
        return "CL-" + generateRandomString(32);
    }

    public String generateClientSecret() {
        return generateRandomString(64);
    }

    public String generateAuthCode() {
        return generateRandomString(20);
    }

    public String generateAccessToken() {
        return "AT-" + generateRandomString(40);
    }

    public String generateRefreshToken() {
        return "RT-" + generateRandomString(40);
    }

    private String generateRandomString(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }
        return builder.toString();
    }

//    public String generateNumericString(int length) {
//        StringBuilder builder = new StringBuilder(length);
//        for (int i = 0; i < length; i++) {
//            builder.append(NUMERIC.charAt(random.nextInt(NUMERIC.length())));
//        }
//        return builder.toString();
//    }
//    public static void main(String[] args) {
//		IdGenerator gen = new IdGenerator();
//		System.out.println(gen.generateNumericString(10));
//	}
}
