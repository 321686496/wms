package com.hongan.template.security.service;

import com.hongan.template.security.common.TokenUser;

import javax.servlet.http.HttpServletRequest;

public interface ISessionManageService {
    //查询是否有已经登陆的用户
    Boolean checkLoginUser(HttpServletRequest request, TokenUser user);

    //清空其他已登陆的用户
    void clearOtherLoginUser(HttpServletRequest request, TokenUser user);

}
