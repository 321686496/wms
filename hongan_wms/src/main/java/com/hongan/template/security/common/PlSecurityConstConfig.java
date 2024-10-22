package com.hongan.template.security.common;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-02-19 11:16
 * @Description: UploadConfigure
 */

public class PlSecurityConstConfig {
    public static final String AuthErrorKey = "AUTH_ERROR";
    public static final String Login = "/web/login";
    public static final String NameLogin = "/auth/admin/name";
    public static final String AdminPhoneCaptchaLogin = "/auth/admin/phone";
    public static final String ManageNameLogin = "/auth/manage/phone";
    public static final String UserWechatLogin = "/auth/user/wechat";
    public static final String StoreNameLogin = "/auth/store/name";
    public static final String Logout = "/auth/logout";
    public static final String LoginSuccess = "/web/admin";
    public static final String Error = "/error";
    public static final String Base = "/base/**";
    public static final String Assets = "/assets/**";
    public static final String[] Current = {"/store/current/**", "/user/current/**", "/admin/current/**"};
    public static final String TokenName = "PLTOKEN";

    public static final String AccessDeny = "/web/admin/error/403";
    public static final String NotFound = "/web/admin/error/404";
    public static final String ServerError = "/500";
    public static final int ValiditySeconds = 7200;

    public static final String swaggerUI = "/swagger-ui/**";

    public static String[] getAllLoginURL() {
        return new String[]{Login, NameLogin, AdminPhoneCaptchaLogin, ManageNameLogin, UserWechatLogin, StoreNameLogin};
    }

    public static String[] getUserLoginURL() {
        return new String[]{UserWechatLogin};
    }

    public static String[] getStoreLoginURL() {
        return new String[]{StoreNameLogin};
    }

    public static String[] getAdminLoginURL() {
        return new String[]{Login, NameLogin, AdminPhoneCaptchaLogin};
    }

    public static String[] getManageLoginURL() {
        return new String[]{ManageNameLogin};
    }

    public static String[] getAllIgnore() {
        return new String[]{Error, Base, Assets, swaggerUI, ServerError};
    }
}
