package com.hongan.template.security.controller;

import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.security.service.ISessionManageService;
import com.hongan.template.security.common.TokenUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "账号登陆控制接口")
@RestController
@RequestMapping("/admin/auth")
public class AdminSecurityController {
    @Autowired
    private ISessionManageService sessionManage;

    @ApiOperation("校验是否有其他用户登陆")
    @GetMapping
    public BaseResponse checkLoginUser(HttpServletRequest request, Authentication auth) {
        TokenUser user = (TokenUser) auth.getPrincipal();
        Boolean res = sessionManage.checkLoginUser(request, user);
        return new BaseResponse().success(res);
    }

    @ApiOperation("清除其他用户")
    @PostMapping("/removeOther")
    public BaseResponse clearOtherLoginUser(HttpServletRequest request, Authentication auth) {
        TokenUser user = (TokenUser) auth.getPrincipal();
        sessionManage.clearOtherLoginUser(request, user);
        return new BaseResponse().success();
    }
}
