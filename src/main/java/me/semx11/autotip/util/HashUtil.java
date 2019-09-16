package me.semx11.autotip.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String getNextSalt() {
        return new BigInteger(130, RANDOM).toString(32);
    }

    public static String hash(String str) {
        try {
            byte[] digest = digest(str);
            return new BigInteger(digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static byte[] digest(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
        return md.digest(strBytes);
    }
}
