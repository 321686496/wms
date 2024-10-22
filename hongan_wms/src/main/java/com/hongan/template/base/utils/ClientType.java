package com.hongan.template.base.utils;

import javax.servlet.http.HttpServletRequest;

public enum ClientType {
    AndroidApp,
    IphoneApp,
    PadApp,
    WeChatApp,
    WeChatAccount,
    AlipayApp,
    ZijieApp,
    PCBrowser,
    MobileBrowser
    ;
    public static ClientType fromRequest(HttpServletRequest request) {
        String clientType = request.getHeader("Client-Type");
        if (clientType == null || "".equals(clientType.trim())) {
            String userAgent = request.getHeader("User-Agent");
            if (userAgent == null) {
                return PCBrowser;
            }
            return fromUserAgent(userAgent);
        }
        switch (clientType) {
            case "wechatAccount":
                return WeChatAccount;
            case "wechat":
                return WeChatApp;
            case "android":
                return AndroidApp;
            case "iphone":
                return IphoneApp;
            case "pad":
                return PadApp;
            case "alipay":
                return AlipayApp;
            case "zijie":
                return ZijieApp;
            case "mobile":
                return MobileBrowser;
            default:
                return PCBrowser;
        }
    }

    private static ClientType fromUserAgent(String userAgent) {
        String ua = userAgent.toLowerCase();
//        boolean isIphone = ua.contains("iphone");
//        boolean isIpad = ua.contains("ipad");
//        boolean isAndroid = ua.contains("android");
//        boolean isTouch = ua.contains("touch");
//        boolean mobile = ua.contains("mobile");
        if (ua.contains("micromessenger")) {
            return WeChatApp;
        } else if (ua.contains("iphone")) {
            return IphoneApp;
        } else  if (ua.contains("ipad")) {
            return PadApp;
        } else if( ua.contains("android")) {
            return AndroidApp;
        } else if (ua.contains("mobile")) {
            return MobileBrowser;
        } else if (ua.contains("touch")) {
            return MobileBrowser;
        } else {
            return PCBrowser;
        }
    }
}
