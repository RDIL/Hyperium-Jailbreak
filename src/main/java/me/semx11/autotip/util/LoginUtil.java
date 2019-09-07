package me.semx11.autotip.util;

import java.io.OutputStream;
import java.io.IOException;
import com.google.gson.JsonObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.math.BigInteger;
import java.security.SecureRandom;

public class LoginUtil {
    private static SecureRandom random;

    public static String getNextSalt() {
        return new BigInteger(130, LoginUtil.random).toString(32);
    }

    public static String hash(final String str) {
        try {
            final byte[] digest = digest(str, "SHA-1");
            return new BigInteger(digest).toString(16);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] digest(final String str, final String algorithm) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance(algorithm);
        final byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
        return md.digest(strBytes);
    }

    public static int joinServer(final String token, final String uuid, final String serverHash) {
        try {
            final URL url = new URL("https://sessionserver.mojang.com/session/minecraft/join");
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            final JsonObject obj = new JsonObject();
            obj.addProperty("accessToken", token);
            obj.addProperty("selectedProfile", uuid);
            obj.addProperty("serverId", serverHash);
            final byte[] jsonBytes = obj.toString().getBytes(StandardCharsets.UTF_8);
            conn.setFixedLengthStreamingMode(jsonBytes.length);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.connect();
            try (final OutputStream out = conn.getOutputStream()) {
                out.write(jsonBytes);
            }
            final int responseCode = conn.getResponseCode();
            return responseCode;
        }
        catch (IOException e) {
            return -1;
        }
    }

    static {
        LoginUtil.random = new SecureRandom();
    }
}
