package com.hongan.template.external.constant;

/**
 * 微信小程序相关接口
 */
public class WechatAppletUrls {

    //获取Token的URl
    public static final String GET_AUTH_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
    //获取用户手机号的URl
    public static final String GET_USER_PHONE = "https://api.weixin.qq.com/wxa/business/getuserphonenumber";
    //获取用户OpenId的URl
    public static final String GET_USER_OPENID = "https://api.weixin.qq.com/wxa/getpluginopenpid";

    //文本内容安全识别
    public final static String CHECK_MSG = "https://api.weixin.qq.com/wxa/msg_sec_check";

    //获取JS
    public final static String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
}
