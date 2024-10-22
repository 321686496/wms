package com.hongan.template.external.service.impl;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.DataCheckUtils;
import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.external.enums.VoiceNotifyTemplate;
import com.hongan.template.external.service.IExternalTencentCloudNotifyVoiceService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vms.v20200902.VmsClient;
import com.tencentcloudapi.vms.v20200902.models.SendTtsVoiceRequest;
import com.tencentcloudapi.vms.v20200902.models.SendTtsVoiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统语音通知配置表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-10
 */
@Slf4j
@Service
public class ExternalTencentCloudNotifyVoiceServiceImpl implements IExternalTencentCloudNotifyVoiceService {

    @Autowired
    private IHonganConfigService configService;


    @Override
    public void sendVoiceNotify(String phone, VoiceNotifyTemplate type) throws BaseException {
        DataCheckUtils.checkParam(phone, "手机号错误！");
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential(configService.getValue("external_tencentcloud_notify_voice_accessKey"),
                    configService.getValue("external_tencentcloud_notify_voice_accessSecret"));
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            VmsClient client = new VmsClient(cred, configService.getValue("external_tencentcloud_notify_voice_region"), clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendTtsVoiceRequest req = new SendTtsVoiceRequest();
            req.setTemplateId(configService.getValue("external_tencentcloud_notify_voice_template_" + type.name()));
            req.setCalledNumber("+86" + phone);
            req.setVoiceSdkAppid(configService.getValue("external_tencentcloud_notify_voice_sdkAppid"));
            // 返回的resp是一个SendTtsVoiceResponse的实例，与请求对象对应
            SendTtsVoiceResponse resp = client.SendTtsVoice(req);
            // 输出json格式的字符串回包
            log.debug("语音通知调用结果：手机号：{}，通知类型：{},调用结果：{}", phone, type.getRemark(), SendTtsVoiceResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            log.error("语音通知调用失败：手机号：{}，通知类型：{},失败原因：{}", phone, type.getRemark(), e.toString());
        }
    }

}
