package com.yw56.javaservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

public class EncoderHandler {
    private static final String ALGORITHM = "MD5";
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    public static String encode(String algorithm, String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    public static Map<String,Object> getSignature(String token){
        Random random = new Random();
        int max=9999;
        int min=1000;
        int s = random.nextInt(max)%(max-min+1) + min;
        String nonce=String.valueOf(s);
        String timestamp=String.valueOf(System.currentTimeMillis());
        String[] str = {token, timestamp, nonce };
        Arrays.sort(str); // 字典序排序
        String bigStr = str[0] + str[1] + str[2];
        // SHA1加密
        String signature = EncoderHandler.encode("SHA1", bigStr).toLowerCase();
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("timestamp", timestamp);
        result.put("nonce", nonce);
        result.put("signature", signature);
        return result;
    }
}
