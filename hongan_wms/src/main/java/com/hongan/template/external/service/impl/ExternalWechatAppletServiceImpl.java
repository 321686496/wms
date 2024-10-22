package com.hongan.template.external.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.service.IRedisValue;
import com.hongan.template.base.utils.DataCheckUtils;
import com.hongan.template.base.utils.JacksonObject;
import com.hongan.template.base.utils.OkHttp;
import com.hongan.template.base.utils.RequestUtil;
import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.external.service.IExternalWechatAppletService;
import com.hongan.template.external.vo.WechatSessionKeyResult;
import com.hongan.template.external.vo.WechatVo;
import com.hongan.template.external.constant.WechatAppletUrls;
import com.hongan.template.external.enums.ContentType;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 系统微信相关配置表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-19
 */
@Slf4j
@Service
public class ExternalWechatAppletServiceImpl implements IExternalWechatAppletService {

    private static int WechatSessionKeyExpire = 3600 * 2;

    private static String WechatAccessTokenCache = "WECHAT:MINIAPP:ACCESS_TOKEN";
    static String WechatSessionURL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    IRedisValue<String> cacheValue;

    @Autowired
    private IHonganConfigService configService;

    @Override
    public String getWechatAccessToken() throws IOException, InterruptedException, BaseException {
        String accessToken = cacheValue.getValue(WechatAccessTokenCache);
        //如果缓存中存在，则从缓存中读取
        if (StringUtils.isNotEmpty(accessToken)) return accessToken;
        //否则请求最新的accessToken
        String responseData = RequestUtil.sendGetRequest(WechatAppletUrls.GET_AUTH_TOKEN, null, new HashMap<>() {{
            put("grant_type", "client_credential");
            put("appid", configService.getValue("external_wechat_applet_appid"));
            put("secret", configService.getValue("external_wechat_applet_secret"));
        }});
        JSONObject res = JSONObject.parseObject(responseData);
        if (res.getInteger("errcode") != null && !res.getInteger("errcode").equals(0)) {
            throw new BaseException(20088, String.format("微信API调用失败，原因：%s", res.get("errmsg")));
        }
        accessToken = res.getString("access_token");
        //凭证有效时间
        Integer expires = res.getInteger("expires_in");
        //设置2小时过期
        cacheValue.setValue(WechatAccessTokenCache, accessToken, expires);
        return accessToken;
    }

    @Override
    public WechatSessionKeyResult getSessionKeyAndOpenIdByCode(String code) throws BaseException {
        DataCheckUtils.checkParam(code, "Code参数丢失！");

        String requestUrl = WechatSessionURL;
        OkHttpClient client = OkHttp.getClient();
        RequestBody formBody = new FormBody.Builder()
                .add("appid", configService.getValue("external_wechat_applet_appid"))
                .add("secret", configService.getValue("external_wechat_applet_secret"))
                .add("js_code", code)
                .add("grant_type", "authorization_code")
                .build();
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(formBody)
                .build();
        log.info("根据 code= {} 获取openid & sessionKey，请求参数 {}", code, request);
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String body = Objects.requireNonNull(response.body()).string();
                ObjectMapper mapper = JacksonObject.getJSONMapper();
                WechatSessionKeyResult result = mapper.readValue(body, WechatSessionKeyResult.class);
                log.debug("result:{}", result);
                if (result.getErrCode() != null && result.getErrCode() != 0) {
                    log.error("WxAPIError :: {}", result);
                    throw new BaseException(BaseError.WxAPIError);
                }
                return result;
            } else {
                throw new BaseException(BaseError.WxAPIError);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw BaseException.fromException(e);
        }
    }

    /**
     * 获取用户手机号
     *
     * @param code 手机号调用凭证
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String getUserPhoneNumber(String code) throws IOException, InterruptedException, BaseException {
        //获取接口调用凭证
        String accessToken = getWechatAccessToken();
        WechatVo body = new WechatVo();
        body.setCode(code);
        String responseData = RequestUtil.sendPostRequest(WechatAppletUrls.GET_USER_PHONE, null, new HashMap<>() {{
            put("access_token", accessToken);
            put("code", code);
        }}, (JSONObject) JSONObject.toJSON(body));
        JSONObject res = JSONObject.parseObject(responseData);
        if (res.getInteger("errcode") != null && res.getInteger("errcode") != 0) {
            getWechatAccessToken();
            throw new BaseException(20088, String.format("微信API调用失败，原因：%s", res.get("errmsg")));
        }
        JSONObject phoneInfo = res.getJSONObject("phone_info");
        return phoneInfo.getString("phoneNumber");
    }


    @Override
    public String getOpenIdByCode(String code) throws BaseException, IOException, InterruptedException {
        //获取接口调用凭证
        String accessToken = getWechatAccessToken();
        WechatVo body = new WechatVo();
        body.setCode(code);
        String responseData = RequestUtil.sendPostRequest(WechatAppletUrls.GET_USER_OPENID, null, new HashMap<>() {{
            put("access_token", accessToken);
        }}, (JSONObject) JSONObject.toJSON(body));
        JSONObject res = JSONObject.parseObject(responseData);
        if (res.getInteger("errcode") != null && !res.getInteger("errcode").equals(0)) {
            throw new BaseException(20088, String.format("微信API调用失败，原因：%s", res.get("errmsg")));
        }
        return res.getString("openpid");
    }

    @Override
    public void checkMsg(String msg, ContentType contentType, String openId) throws InterruptedException, BaseException, IOException {
        //获取接口调用凭证
        String accessToken = getWechatAccessToken();
        Map<String, Object> body = new HashMap<>();
        body.put("content", msg);
        body.put("version", contentType.getMsg());
        body.put("scene", "3");
        body.put("openid", openId);
        String responseData = RequestUtil.sendPostRequest(WechatAppletUrls.CHECK_MSG, null, new HashMap<>() {{
            put("access_token", accessToken);
        }}, (JSONObject) JSONObject.toJSON(body));
        JSONObject res = JSONObject.parseObject(responseData);
        if (res.getInteger("errcode") != null && res.getInteger("errcode") != 0) {
            throw new BaseException(20088, String.format("微信API调用失败，原因：%s", res.get("errmsg")));
        }
        JSONObject result = res.getJSONObject("result");
        if (!result.getString("suggest").equals("pass")) {
            throw new BaseException("内容包含违规字符，请修改后重新操作！");
        }
    }
}
