package com.hongan.template.user.controller;


import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.base.security.CurrentController;
import com.hongan.template.user.entity.HonganUser;
import com.hongan.template.user.service.IHonganUserCollectionService;
import com.hongan.template.user.service.IHonganUserService;
import com.hongan.template.enums.AdminStatus;
import com.hongan.template.external.vo.WechatPhoneEnData;
import com.hongan.template.security.common.TokenUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Api(tags = "用户端-个人中心接口")
@RestController
@RequestMapping("/user/current")
public class UserCurrentController extends CurrentController<HonganUser> {
    @Autowired
    private IHonganUserCollectionService userCollectionService;
    @Autowired
    private IHonganUserService userService;

    @ApiOperation(value = "获取当前用户详情信息")
    @GetMapping("/current")
    public BaseResponse adminDetail(Authentication auth) throws BaseException {
        if (auth == null) throw new BaseException(BaseError.AuthError);
        TokenUser user = (TokenUser) auth.getPrincipal();
        HonganUser data = userService.selectById(user.getId());
        if (user.getStatus() != AdminStatus.NORMAL)
            throw new BaseException(20088, "该账号已被冻结！");
        return new BaseResponse().success(data);
    }

    @ApiOperation(value = "更新用户信息")
    @PutMapping("/updateMessage")
    public BaseResponse updateMessage(Authentication auth, @RequestBody HonganUser data) throws InterruptedException, BaseException, IOException {
        TokenUser user = (TokenUser) auth.getPrincipal();
        data.setId(user.getId());
        userService.userUpdateMessage(data);
        return new BaseResponse().success();
    }

    @ApiOperation(value = "微信授权手机号")
    @PutMapping("/wechatUpdatePhone")
    public BaseResponse wechatUpdatePhone(Authentication auth, @RequestBody WechatPhoneEnData body) throws InterruptedException, BaseException, IOException {
        HonganUser user = userService.updateWechatPhone(auth.getName(), body);
        return new BaseResponse().success();
    }
}


