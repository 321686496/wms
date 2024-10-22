package com.hongan.template.base.service.impl;

import com.hongan.template.base.properties.AppProperties;
import com.hongan.template.base.properties.CookieProperties;
import com.hongan.template.base.service.ICookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-02 15:24
 * @Description: CookieService
 */

@Service
public class CookieServiceTmpl implements ICookieService {

    @Autowired
    AppProperties appProperties;

    @Override
    public String getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void setCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        CookieProperties CookieConst = appProperties.getCookie();
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath(CookieConst.getPath());
        cookie.setHttpOnly(CookieConst.getHttpOnly());
        cookie.setMaxAge(CookieConst.getExpires());
        cookie.setDomain(CookieConst.getDomain());
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    @Override
    public void delCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
