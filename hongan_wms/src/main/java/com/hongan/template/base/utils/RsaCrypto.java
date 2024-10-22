package com.hongan.template.base.utils;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-11-21 21:26
 * @Description: RsaCrypto
 */

import javax.crypto.Cipher;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaCrypto {
    private static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
    private static final String RSA = "RSA";
    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final String PRIVATEKEY = "";
    private static final String PUBLICKEY = "";
    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    static {
        try {
            publicKey = loadPublicKey(PUBLICKEY);
            privateKey = loadPrivateKey(PRIVATEKEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getAesKey() {
        return PUBLICKEY.substring(193, 225);
    }

    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] pubEncrypt(String message) throws Exception {
        return encrypt(publicKey, message);
    }

    public static byte[] pubDecrypt(byte[] message) throws Exception {
        return decrypt(publicKey, message);
    }

    public static byte[] priEncrypt(String message) throws Exception {
        return encrypt(privateKey, message);
    }

    public static byte[] priDecrypt(byte[] message) throws Exception {
        return decrypt(privateKey, message);
    }

    public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(message.getBytes(UTF8));
    }

    public static byte[] decrypt(PublicKey publicKey, byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(encrypted);
    }

    public static byte[] encrypt(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(message.getBytes(UTF8));
    }

    public static byte[] decrypt(PrivateKey privateKey, byte[] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(encrypted);
    }

    /**
     * 从字符串中加载公钥
     */
    public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = base64Decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = base64Decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public void savePublicFile(PublicKey publicKey, String filename) throws IOException {
        // 得到公钥字符串
        String publicKeyString = base64Encode(publicKey.getEncoded());

        FileWriter fw = new FileWriter(filename);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(publicKeyString);
        bw.close();
    }

    public void savePrivateFile(PrivateKey privateKey, String filename) throws IOException {
        // 得到私钥字符串
        String privateKeyString = base64Encode(privateKey.getEncoded());

        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        bw.write(privateKeyString);
        bw.close();
    }

    private static String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private static byte[] base64Decode(String data) {
        return Base64.getDecoder().decode(data);
    }
}