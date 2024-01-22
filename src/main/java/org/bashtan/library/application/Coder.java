package org.bashtan.library.application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Coder {
    private String string;
    private String hash;

    public Coder(String string) {
        this.string = string;
        try {
            hash = coder(string);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getHash() {
        return hash;
    }

    private static String coder(String str) throws NoSuchAlgorithmException {
        StringBuilder stringBuilder = new StringBuilder();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(str.getBytes());

        for (byte b : bytes) {
            stringBuilder.append(b);
        }
        return String.valueOf(stringBuilder);
    }
//
//    static byte[] crypt(int mode, byte[] value, String secret) throws Exception {
//        byte[] key = secret.getBytes(StandardCharsets.UTF_8);
//        MessageDigest sha = MessageDigest.getInstance("SHA-1");
//        key = sha.digest(key);
//        key = Arrays.copyOf(key, 16);
//        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
//
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        cipher.init(mode, secretKey);
//
//        return cipher.doFinal(value);
//    }

}
