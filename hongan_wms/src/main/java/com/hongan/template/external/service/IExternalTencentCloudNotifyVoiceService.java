package com.hongan.template.external.service;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.external.enums.VoiceNotifyTemplate;

/**
 * <p>
 * 系统语音通知配置表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-10
 */
public interface IExternalTencentCloudNotifyVoiceService {
    /**
     * 发送腾讯云语音通知
     *
     * @param phone          接收通知的手机号
     * @param notifyTemplate 短信通知模板
     */
    void sendVoiceNotify(String phone, VoiceNotifyTemplate notifyTemplate) throws BaseException;
}
