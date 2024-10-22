package com.hongan.template.external.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.service.IRedisValue;
import com.hongan.template.base.utils.RequestUtil;
import com.hongan.template.base.utils.StringUtils;
import com.hongan.template.external.service.IExternalWechatService;
import com.hongan.template.external.constant.WechatAppletUrls;
import com.hongan.template.external.utils.WeixinUtil;
import com.hongan.template.config.service.IHonganConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ExternalWechatServiceImpl implements IExternalWechatService {

    private static String WechatAccessTokenCache = "WECHAT:ACCESS_TOKEN";
    private static String WechatJsTicketCache = "WECHAT:JsTicket";
    @Autowired
    IRedisValue<String> cacheValue;
    @Autowired
    private IHonganConfigService configService;

    @Override
    public String getAccessToken() throws BaseException, IOException, InterruptedException {
        String accessToken = cacheValue.getValue(WechatAccessTokenCache);
        //如果缓存中存在，则从缓存中读取
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(accessToken)) return accessToken;
        //否则请求最新的accessToken
        String responseData = RequestUtil.sendGetRequest(WechatAppletUrls.GET_AUTH_TOKEN, null, new HashMap<>() {{
            put("grant_type", "client_credential");
            put("appid", configService.getValue("external_wechat_appid"));
            put("secret", configService.getValue("external_wechat_appSecret"));
        }});
        JSONObject res = JSONObject.parseObject(responseData);
        if (res.getInteger("errcode") != null && !res.getInteger("errcode").equals(0)) {
            log.error("微信API调用失败，原因：{}", res.get("errmsg"));
            throw new BaseException(BaseError.WxAPIError);
        }
        accessToken = res.getString("access_token");
        //凭证有效时间
        Integer expires = res.getInteger("expires_in");
        //设置2小时过期
        cacheValue.setValue(WechatAccessTokenCache, accessToken, expires);
        return accessToken;
    }

    @Override
    public String getJsApiTicket() throws BaseException, IOException, InterruptedException {
        // 从redis获取ticket
        String ticket = cacheValue.getValue(WechatJsTicketCache);
        // 若为空，则从微信服务器获取
        if (StringUtils.isEmpty(ticket)) {
            String accessToken = getAccessToken();
            String responseData = RequestUtil.sendGetRequest(WechatAppletUrls.GET_TICKET_URL, null, new HashMap<>() {{
                put("access_token", accessToken);
                put("type", "jsapi");
            }});
            JSONObject res = JSONObject.parseObject(responseData);
            if (res.getInteger("errcode") != null && !res.getInteger("errcode").equals(0)) {
                log.error("微信API调用失败，原因：{}", res.get("errmsg"));
                throw new BaseException(BaseError.WxAPIError);
            }
            ticket = res.getString("ticket");
            cacheValue.setValue(WechatJsTicketCache, ticket, 7000);
            return ticket;
        }
        return ticket;
    }

    @Override
    public Map<String, Object> getJsSdkInfo(String url) throws BaseException, IOException, InterruptedException {
        if (StringUtils.isEmpty(url)) {
            throw new BaseException(BaseError.BadRequest);
        }
        String ticket = getJsApiTicket();
        // 随机字符串
        String noncestr = UUID.randomUUID().toString().replaceAll("-", "");
        // 时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());
        String str = "jsapi_ticket=" + ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        // SHA1签名字符串
        String signature = WeixinUtil.SHA1(str);
        // 返回前端结果
        Map<String, Object> map = new HashMap<>(5);
        map.put("timestamp", timestamp);
        map.put("appId", configService.getValue("external_wechat_appid"));
        map.put("noncestr", noncestr);
        map.put("signature", signature);
        return map;
    }


}
