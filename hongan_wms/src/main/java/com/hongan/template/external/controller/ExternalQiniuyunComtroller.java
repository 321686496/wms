package com.hongan.template.external.controller;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.external.service.IQiniuyunService;
import com.hongan.template.external.vo.QiniuToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(tags = "第三方接口-七牛云管理")
@RestController
@RequestMapping("/base/api/qiniuyun")
public class ExternalQiniuyunComtroller {

    @Autowired
    private IQiniuyunService qiniuyunService;

    @ApiOperation("获取七牛云Token")
    @GetMapping("/token")
    public BaseResponse getToken() throws InterruptedException, BaseException, IOException {
        QiniuToken token = qiniuyunService.getToken();
        return new BaseResponse().success(token);
    }
}
