package com.hongan.template.base.captcha;

/**
 * 发送手机短信通知验证类型
 *
 * 根据短信类型调用短信模板
 */
public enum PhoneNotifyType {
    RESERVATION_ACTIVITY_START_NOTIFY("reservationActivityStartNotify"),//预约活动开始通知
    CAPTCHA("captcha"),//短信验证码通知
    REGISTER_CAPTCHA("registerCaptcha"),//注册验证码通知
    PROXY_NOTIFY("proxyNotify");//后台添加分销商通知

    private String type;

    PhoneNotifyType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
