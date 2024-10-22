package com.hongan.template.external.vo;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

public class CreateSign02 {
    public static String getToken(WechatMiniAppVo vo, String privateKeyPath) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        //随机字符串
        String nonceStr = vo.getNonceStr();//真！随机字符串
        //时间戳
        long timestamp = System.currentTimeMillis() / 1000;
        //从下往上依次生成
        String message = buildMessage(vo.getAppId(), timestamp, nonceStr, vo.getPackageInfo());
        //签名
        String signature = sign(message.getBytes("utf-8"), privateKeyPath);
        return signature;
    }

    static String sign(byte[] message, String privateKeyPath) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        //签名方式
        Signature sign = Signature.getInstance("SHA256withRSA");
        //私钥，通过MyPrivateKey来获取，这是个静态类可以接调用方法 ，需要的是_key.pem文件的绝对路径配上文件名
        sign.initSign(MyPrivatekey.getPrivateKey(privateKeyPath));
        sign.update(message);

        return Base64.getEncoder().encodeToString(sign.sign());
    }

    /**
     * 按照前端签名文档规范进行排序，\n是换行
     *
     * @param appid
     * @param timestamp
     * @param nonceStr
     * @param prepay_id
     * @return
     */
    static String buildMessage(String appid, long timestamp, String nonceStr, String prepay_id) {
        return appid + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + prepay_id + "\n";
    }
}
