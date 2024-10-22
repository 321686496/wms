package com.hongan.template.external.utils;

import com.hongan.template.base.service.IRedisValue;
import com.hongan.template.config.service.IHonganConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class WeixinUtil {
    private String appId = "wx10baa5333dcc1572";
    private String appSecret = "c86f4d0531c080b3500c06b388610c49";

    private static String WechatAccessTokenCache = "WECHAT:ACCESS_TOKEN";
    private static String WechatJsTicketCache = "WECHAT:JsTicket";
    @Autowired
    IRedisValue<String> cacheValue;
    @Autowired
    private IHonganConfigService configService;


    public final static String ERRCODE = "errcode";
    public final static String ACCESS_TOKEN = "access_token";
    public final static String TICKET = "ticket";
//
//    /**
//     * @return java.lang.String
//     * @author lhc
//     * @description // 获取微信accessToken
//     * @date 2022/8/11 10:29
//     * @params []
//     **/
//    public String getAccessToken() throws BaseException, IOException, InterruptedException {
//        String accessToken = cacheValue.getValue(WechatAccessTokenCache);
//        //如果缓存中存在，则从缓存中读取
//        if (org.apache.commons.lang3.StringUtils.isNotEmpty(accessToken)) return accessToken;
//        //否则请求最新的accessToken
//        String responseData = RequestUtil.sendGetRequest(WechatUrls.GET_AUTH_TOKEN, null, new HashMap<>() {{
//            put("grant_type", "client_credential");
//            put("appid", configService.getValue("external_wechat_appid"));
//            put("secret", configService.getValue("external_wechat_appSecret"));
//        }});
//        JSONObject res = JSONObject.parseObject(responseData);
//        if (res.getInteger("errcode") != null && !res.getInteger("errcode").equals(0)) {
//            throw new BaseException(20088, String.format("微信API调用失败，原因：%s", res.get("errmsg")));
//        }
//        accessToken = res.getString("access_token");
//        //凭证有效时间
//        Integer expires = res.getInteger("expires_in");
//        //设置2小时过期
//        cacheValue.setValue(WechatAccessTokenCache, accessToken, expires);
//        return accessToken;
//    }
//
//    /**
//     * @return cn.hutool.json.JSONObject
//     * @author lhc
//     * @description // 获取微信jssdk授权
//     * @date 2022/8/11 10:30
//     * @params []
//     **/
//    public String jsapiTicket() throws InterruptedException, BaseException, IOException {
//        // 从redis获取ticket
//        String ticket = (String) cacheValue.getValue(WechatJsTicketCache);
//        // 若为空，则从微信服务器获取
//        if (StringUtils.isEmpty(ticket)) {
//            String accessToken = getAccessToken();
//
//            String responseData = RequestUtil.sendGetRequest(WechatUrls.GET_TICKET_URL, null, new HashMap<>() {{
//                put("access_token", accessToken);
//                put("type", "jsapi");
//            }});
//            JSONObject res = JSONObject.parseObject(responseData);
//            if (res.getInteger("errcode") != null && !res.getInteger("errcode").equals(0)) {
//                throw new BaseException(20088, String.format("微信API调用失败，原因：%s", res.get("errmsg")));
//            }
//            ticket = res.getString("ticket");
//            cacheValue.setValue(WechatJsTicketCache, accessToken, 7000);
//            return ticket;
//        }
//        return ticket;
//    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
