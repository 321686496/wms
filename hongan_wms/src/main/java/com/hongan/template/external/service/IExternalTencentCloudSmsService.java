package com.hongan.template.external.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.external.enums.SmsTemplate;

/**
 * <p>
 * 腾讯云短信配置表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-10
 */
public interface IExternalTencentCloudSmsService {

    /**
     * 发送腾讯云短信验证码
     *
     * @param phone        接收短信的手机号
     * @param templateType 消息模板
     * @param params       参数列表
     * @return
     * @throws JsonProcessingException
     */
    Boolean sendWithTemplate(String phone, SmsTemplate templateType, String[] params) throws JsonProcessingException, BaseException;
}
