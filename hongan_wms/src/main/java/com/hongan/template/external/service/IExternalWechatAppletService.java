package com.hongan.template.external.service;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.external.vo.WechatSessionKeyResult;
import com.hongan.template.external.enums.ContentType;

import java.io.IOException;

/**
 * <p>
 * 系统微信小程序相关接口 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-19
 */
public interface IExternalWechatAppletService {

    //获取微信接口调用凭证AccessToken
    String getWechatAccessToken() throws IOException, InterruptedException, BaseException;

    /**
     * 根据微信Code 解析sessionKey 和 openId
     */

    WechatSessionKeyResult getSessionKeyAndOpenIdByCode(String code) throws BaseException;

    /**
     * 根据Code解析 用户手机号
     */
    String getUserPhoneNumber(String code) throws IOException, InterruptedException, BaseException;


    String getOpenIdByCode(String code) throws BaseException, IOException, InterruptedException;

    /**
     * 校验文本内容是否合法
     *
     * @param msg 文本内容
     * @return true:合法 false:不合法
     */
    void checkMsg(String msg, ContentType contentType, String openId) throws InterruptedException, BaseException, IOException;
}
