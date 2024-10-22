package com.hongan.template.base.utils;

import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: BaseCrypto
 */

@Slf4j
public class BaseCrypto {
    private static final String HMACTYPE = "HmacSHA1";
    private static final String SECKEY = "h4QkV9N2qO6iJIdHHfRYsJVWPy2IgKX0";
    private static final String ALGORITHM = "AES/CBC/PKCS7Padding";
    private static final String ALGORITHM_TYPE = "AES";
    private static final String HMAC256TYPE = "HmacSHA256";

    public static String hmacSha256(String data, String key) throws BaseException {
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC256TYPE);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), HMAC256TYPE);
            sha256_HMAC.init(secretKey);
            byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            throw BaseException.fromException(e);
        }
    }

    public static String MD5(String data) throws BaseException {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new BaseException(BaseError.ErrorReceiveStatus);
        }
        byte[] bs = md5.digest(data.getBytes());
        return new String(new Hex().encode(bs)).toUpperCase();
    }

    public static String hmac(String raw) throws BaseException {
        try {
            SecretKey secretKey = new SecretKeySpec(SECKEY.getBytes(), HMACTYPE);
            Mac mac = Mac.getInstance(HMACTYPE);
            mac.init(secretKey);
            byte[] res = mac.doFinal(raw.getBytes());
            return Base64.getEncoder().encodeToString(res);
        } catch (Exception e) {
            e.printStackTrace();
            throw BaseException.fromException(e);
        }
    }

    public static Boolean checkHmac(String raw, String hash) throws BaseException {
        try {
            return hmac(raw).equals(hash);
        } catch (Exception e) {
            throw BaseException.fromException(e);
        }
    }

    public static AesEnBody encrypt(String raw) throws BaseException {
        return encrypt(SECKEY, raw);
    }

    // secretKey Base64编码
    public static AesEnBody encrypt(String secretKey, String raw) throws BaseException {
        byte[] keyBytes = toSupportPKCS7Padding(secretKey);
        try {
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM_TYPE);
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            SecureRandom randomSecureRandom = new SecureRandom();
            byte[] iv = new byte[cipher.getBlockSize()];
            randomSecureRandom.nextBytes(iv);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
            byte[] enData = cipher.doFinal(raw.getBytes());

            return new AesEnBody(iv, enData);

        } catch (Exception e) {
            throw BaseException.fromException(e);
        }
    }

    public static String decrypt(AesEnBody en) throws BaseException {
        return decrypt(SECKEY, en);
    }

    // secretKey Base64编码
    public static String decrypt(String secretKey, AesEnBody en) throws BaseException {
        byte[] keyBytes = toSupportPKCS7Padding(secretKey);
        try {
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM_TYPE);
            // 初始化cipher
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(en.getIvByte()));
            byte[] decData = cipher.doFinal(en.getEnDataByte());
            return new String(decData);
        } catch (Exception e) {
            e.printStackTrace();
            throw BaseException.fromException(e);
        }
    }

    // JDK8 默认不支持AES/CBC/PKCS7Padding 需加上bcprov-jdk15on扩展
    public static byte[] toSupportPKCS7Padding(String secretKey) {

        Security.addProvider(new BouncyCastleProvider());
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        int base = 16;
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + 1;
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }
        return keyBytes;
    }

    public static Boolean checkAes(AesEnBody en, String raw) throws BaseException {
        try {
            return decrypt(en).equals(raw);
        } catch (Exception e) {
            throw BaseException.fromException(e);
        }
    }

    public static class AesEnBody {
        private final String iv;
        private final String enData;

        public static AesEnBody valueOf(String data) throws BaseException {
            String[] arr = data.split("\\.");
            if (arr.length != 2) {
                throw new BaseException(BaseError.FormatError);
            }
            return new AesEnBody(arr[0], arr[1]);
        }

        ;

        AesEnBody(byte[] iv, byte[] enData) {
            this.iv = Base64.getEncoder().encodeToString(iv);
            this.enData = Base64.getEncoder().encodeToString(enData);
        }

        public AesEnBody(String data, String iv) {
            this.iv = iv;
            this.enData = data;
        }

        byte[] getIvByte() {
            return Base64.getDecoder().decode(this.iv);
        }

        byte[] getEnDataByte() {
            return Base64.getDecoder().decode(this.enData);
        }

        public String toString() {
            return this.enData + "" + this.iv;
        }

    }

    public static String generateSign(Map<String, Object> data, String key) throws BaseException {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign") || k.equals("paySign")) {
                continue;
            }
            if (null != data.get(k)) {
                sb.append(k).append("=").append(data.get(k)).append("&");
            }
        }
        sb.append("key=").append(key);
        String sign = hmacSha256(sb.toString(), key);
        log.info("签名前原字符串 {}，签名后 {}", sb.toString(), sign);
        return sign;

    }
}
