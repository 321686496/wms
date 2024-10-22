package com.hongan.template.external.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.DataCheckUtils;
import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.external.service.IExternalTencentCloudSmsService;
import com.hongan.template.external.enums.SmsTemplate;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 腾讯云短信配置表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2023-04-10
 */
@Slf4j
@Service
public class ExternalTencentCloudSmsServiceImpl implements IExternalTencentCloudSmsService {
    @Autowired
    private IHonganConfigService configService;


    /**
     * @param phone  接收通知的电话号码
     * @param type   通知模板类型
     * @param params 通知模版内容里的参数，类似"{'code':'"+code+"'}"中code的值
     * @return
     */
    public Boolean sendWithTemplate(String phone, SmsTemplate type, String[] params) throws JsonProcessingException, BaseException {
        DataCheckUtils.checkParamRegex(phone, configService.getValue("system_regex_phone"), BaseError.PhoneFormatError);
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(configService.getValue("external_tencentcloud_notify_sms_accessKey"),
                    configService.getValue("external_tencentcloud_notify_sms_accessSecret"));
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, configService.getValue("external_tencentcloud_notify_sms_region"), clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {phone};
            req.setPhoneNumberSet(phoneNumberSet1);
            req.setSmsSdkAppId(configService.getValue("external_tencentcloud_notify_sms_sdkAppid"));
            req.setSignName(configService.getValue("external_tencentcloud_notify_sms_sign"));
            req.setTemplateId(configService.getValue("external_tencentcloud_notify_sms_template_" + type.name()));
            req.setTemplateParamSet(params);
            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            String resJson = SendSmsResponse.toJsonString(resp);
            System.out.println(resJson);
            log.debug("腾讯云短信通知调用成功：手机号：{}，通知类型：{},调用结果：{}", phone, type.getRemark(), resJson);
            // 输出json格式的字符串回包
            JSONObject jsonObject = JSONObject.parseObject(resJson);
            JSONArray SendStatusSet = jsonObject.getJSONArray("SendStatusSet");
            JSONObject res = SendStatusSet.getJSONObject(0);
            String code = res.getString("Code");
            return code.equals("Ok");
        } catch (TencentCloudSDKException e) {
            log.error("腾讯云短信通知调用失败：手机号：{}，通知类型：{},失败原因：{}", phone, type.getRemark(), e.toString());
            System.out.println(e.toString());
            return false;
        }
    }
}
