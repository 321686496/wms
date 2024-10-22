package com.hongan.template.config.init;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.security.AutoInitComponent;
import com.hongan.template.config.entity.HonganConfig;
import com.hongan.template.config.service.IHonganConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化系统配置项目
 */
@Component
public class InitConfig implements AutoInitComponent {
    @Autowired
    private IHonganConfigService configService;

    @Override
    public void init(ApplicationContext context) {
        //填写初始化的属性
        List<HonganConfig> initConfigs = new ArrayList<>();

        initConfigs.add(new HonganConfig("系统名称", "system_name", ""));
        initConfigs.add(new HonganConfig("系统简称", "system_shortName", ""));
        initConfigs.add(new HonganConfig("系统Logo", "system_logo", "https://p8-cdn.ponlay.com/logo1.png"));
        initConfigs.add(new HonganConfig("系统icon", "system_icon", "https://p8-cdn.ponlay.com/icon.ico"));
        initConfigs.add(new HonganConfig("系统域名", "system_domain", ""));
        initConfigs.add(new HonganConfig("系统标题", "system_title", ""));
        initConfigs.add(new HonganConfig("系统ICP备案号", "system_icp", ""));
        initConfigs.add(new HonganConfig("密码正则表达式", "system_regex_password", "^[A-Za-z\\d$@$!%*?&]{6,20}$"));
        initConfigs.add(new HonganConfig("手机号正则表达式", "system_regex_phone", "^1[3456789]\\d{9}$"));
        initConfigs.add(new HonganConfig("腾讯云语音通知配置", "external_tencentcloud_notify_voice_accessKey", ""));
        initConfigs.add(new HonganConfig("腾讯云语音通知配置", "external_tencentcloud_notify_voice_accessSecret", ""));
        initConfigs.add(new HonganConfig("腾讯云语音通知配置", "external_tencentcloud_notify_voice_region", ""));
        initConfigs.add(new HonganConfig("腾讯云语音通知配置", "external_tencentcloud_notify_voice_sdkAppid", ""));
        initConfigs.add(new HonganConfig("腾讯云短信通知配置", "external_tencentcloud_notify_sms_accessKey", ""));
        initConfigs.add(new HonganConfig("腾讯云短信通知配置", "external_tencentcloud_notify_sms_accessSecret", ""));
        initConfigs.add(new HonganConfig("腾讯云短信通知配置", "external_tencentcloud_notify_sms_region", ""));
        initConfigs.add(new HonganConfig("腾讯云短信通知配置", "external_tencentcloud_notify_sms_sdkAppid", ""));
        initConfigs.add(new HonganConfig("腾讯云短信通知配置", "external_tencentcloud_notify_sms_sign", ""));
        initConfigs.add(new HonganConfig("腾讯云语音通知配置(模板)", "external_tencentcloud_notify_voice_template_", ""));
        initConfigs.add(new HonganConfig("腾讯云短信通知配置(模板)(验证码)", "external_tencentcloud_notify_sms_template_RegisterCaptcha", "", "模板内容：您的短信验证码为{1},五分钟内有效。"));
        initConfigs.add(new HonganConfig("腾讯云短信通知配置(模板)(新用户注册)", "external_tencentcloud_notify_sms_template_UserRegisterNotify", "", "模板内容：朋来P8有新用户注册，请及时跟进。"));
        initConfigs.add(new HonganConfig("文件上传配置方式", "system_upload_type", "location", "location:本地  qiniuyun:上传至七牛云"));
        initConfigs.add(new HonganConfig("文件上传配置(本地)", "system_upload_path", "F://work//admin-backend//target//classes//static//"));
        initConfigs.add(new HonganConfig("文件上传配置(本地)", "system_upload_publicPath", "/assets/imgs/"));
        initConfigs.add(new HonganConfig("文件上传配置(本地)", "system_upload_imgUrl", "/assets/imgs/"));
        initConfigs.add(new HonganConfig("文件上传配置(本地)", "system_upload_copyPath", "F://work//admin-backend//src//main//resources//static//assets//imgs//"));
        initConfigs.add(new HonganConfig("文件上传配置(七牛云)", "system_upload_qiniu_domain", "https://p8-cdn.ponlay.com", "七牛云https域名"));
        initConfigs.add(new HonganConfig("文件上传配置(七牛云)", "system_upload_qiniu_bucket", "p8-cdn", "七牛云空间名称"));
        initConfigs.add(new HonganConfig("文件上传配置(七牛云)", "system_upload_qiniu_accessKey", "", "七牛云accesskey"));
        initConfigs.add(new HonganConfig("文件上传配置(七牛云)", "system_upload_qiniu_secretKey", "", "七牛云SecreyKey"));
        initConfigs.add(new HonganConfig("飞蛾云打印机开发者账号", "system_feieyun_prict_user", ""));
        initConfigs.add(new HonganConfig("飞蛾云打印机开发者密钥", "system_feieyun_prict_key", ""));
        initConfigs.add(new HonganConfig("微信公众号配置(AppId)", "external_wechat_appid", ""));
        initConfigs.add(new HonganConfig("微信公众号配置(AppSecret)", "external_wechat_appSecret", ""));
        initConfigs.add(new HonganConfig("微信小程序配置(AppId)", "external_wechat_applet_appid", ""));
        initConfigs.add(new HonganConfig("微信小程序配置(AppSecret)", "external_wechat_applet_secret", ""));
        initConfigs.add(new HonganConfig("百度云图片搜索配置(AppId)", "external_baidu_picture_search_appid", ""));
        initConfigs.add(new HonganConfig("百度云图片搜索配置(Key)", "external_baidu_picture_search_key", ""));
        initConfigs.add(new HonganConfig("百度云图片搜索配置(Secret)", "external_baidu_picture_search_secret", ""));

        initConfigs.add(new HonganConfig("微信支付", "external_wechat_pay", "true", "true:开启、false:关闭"));
        initConfigs.add(new HonganConfig("微信支付方式", "external_wechat_pay_type", "Merchant", "Merchant:商家/ServiceMerchant:服务商"));
        initConfigs.add(new HonganConfig("微信支付配置(支付回调)", "external_wechat_pay_notifyUrl", "/base/api/notify/wechat"));
        initConfigs.add(new HonganConfig("微信支付配置(商户号)", "external_wechat_pay_mchid", ""));
        initConfigs.add(new HonganConfig("微信支付配置(APIV3密钥)", "external_wechat_pay_v3Secret", ""));
        initConfigs.add(new HonganConfig("微信支付配置(私钥路径)", "external_wechat_pay_privateKeyPath", "xxx.pem"));
        initConfigs.add(new HonganConfig("微信支付配置(证书序列号)", "external_wechat_pay_serialNumber", ""));
        initConfigs.add(new HonganConfig("微信支付配置(服务商模式)(商户号)", "external_wechat_proxyPay_mchid", ""));
        initConfigs.add(new HonganConfig("微信支付配置(服务商模式)(商户支付密钥)", "external_wechat_proxyPay_secret", ""));
        initConfigs.add(new HonganConfig("微信支付配置(服务商模式)(商户证书路径)", "external_wechat_proxyPay_certPath", ""));
        initConfigs.add(new HonganConfig("微信支付配置(服务商模式)(服务商AppId)", "external_wechat_proxyPay_spAppid", ""));
        initConfigs.add(new HonganConfig("微信支付配置(服务商模式)(服务商商户号)", "external_wechat_proxyPay_spMchid", ""));
        initConfigs.add(new HonganConfig("微信支付配置(服务商模式)(服务商APIV3密钥)", "external_wechat_proxyPay_spV3Secret", ""));
        initConfigs.add(new HonganConfig("微信支付配置(服务商模式)(服务商API证书私钥路径)", "external_wechat_proxyPay_spPrivateKeyPath", "xxx.p12"));
        initConfigs.add(new HonganConfig("微信支付配置(服务商模式)(服务商证书序列号)", "external_wechat_proxyPay_spSerialNumber", ""));


        for (HonganConfig initConfig : initConfigs) {
            HonganConfig config = configService.getOne(new QueryWrapper<HonganConfig>().eq("config_key", initConfig.getConfigKey()));
            if (config == null) {
                initConfig.insert();
            }
        }
    }
}
