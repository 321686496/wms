package com.hongan.template.external.enums;

/**
 * @Author: zyp
 * @Date: 2022/4/21 0021
 */
public enum SmsTemplate {
    RegisterCaptcha("注册验证码", "您的手机验证码为"),//注册验证码
    UserRegisterNotify("新用户注册通知", "朋来P8有新用户注册，请及时跟进。"),//注册验证码
    ;

    private String remark;
    private String content;

    //
    SmsTemplate(String remark, String content) {
        this.remark = remark;
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }
}
