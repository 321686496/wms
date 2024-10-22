package com.hongan.template.base.utils;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-11-21 20:02
 * @Description: EccCtypto
 */

public class EccCtypto implements Serializable {
    /**
     * 加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);

        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ECCEnum.ALGORITHM.toString());

        ECPublicKey pubKey = (ECPublicKey) keyFactory
                .generatePublic(x509KeySpec);

        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(pubKey.getW(),pubKey.getParams());

        Cipher cipher = Cipher.getInstance("ECC");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey, ecPublicKeySpec.getParams());
        return cipher.doFinal(data);
    }


    /**
     * 解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String privateKey) throws Exception {

        byte[] keyBytes = Base64.getDecoder().decode(privateKey);

        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(ECCEnum.ALGORITHM.toString());

        ECPrivateKey priKey = (ECPrivateKey) keyFactory
                .generatePrivate(pkcs8KeySpec);
        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(priKey.getS(),priKey.getParams());

        Cipher cipher = new NullCipher();
        cipher.init(Cipher.DECRYPT_MODE, priKey, ecPrivateKeySpec.getParams());

        return cipher.doFinal(data);
    }




//    /*
//     * 生成秘钥
//     */
//    public static Map<String, String> getGenerateKey() throws NoSuchProviderException, NoSuchAlgorithmException {
////        static {
////            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
////        }
//
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ECCEnum.ALGORITHM.toString(),
//                ECCEnum.PROVIDER.toString());
//        keyPairGenerator.initialize(256, new SecureRandom());
//        KeyPair kp = keyPairGenerator.generateKeyPair();
//        ECPublicKey publicKey = (ECPublicKey) kp.getPublic();
//        ECPrivateKey privateKey = (ECPrivateKey) kp.getPrivate();
//        Map<String, String> map = new HashMap<>();
//
//        map.put(ECCEnum.PRIVATE_KEY.toString(), Base64.getEncoder().encodeToString(privateKey.getEncoded()));
//        map.put(ECCEnum.PUBLIC_KEY.toString(), Base64.getEncoder().encodeToString(publicKey.getEncoded()));
//        return map;
//    }


    public enum ECCEnum {
        ALGORITHM("EC"),
        PROVIDER("BC"),
        PUBLIC_KEY("PUBLIC_KEY"),
        PRIVATE_KEY("PRIVATE_KEY");

        private String value;

        ECCEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }
}

