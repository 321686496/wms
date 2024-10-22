package com.hongan.template.external.service;

import com.hongan.template.base.entity.BaseException;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 微信相关接口 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-19
 */
public interface IExternalWechatService {

    String getAccessToken() throws BaseException, IOException, InterruptedException;

    String getJsApiTicket() throws BaseException, IOException, InterruptedException;

    Map<String, Object> getJsSdkInfo(String url) throws BaseException, IOException, InterruptedException;
}
