package com.hongan.template.base.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ICookieService  {
    String getCookie(HttpServletRequest request, String Cookiename);

    void setCookie(HttpServletResponse response, String cookieName, String cookieValue);

    void delCookie(HttpServletResponse response, String cookieName);
}
