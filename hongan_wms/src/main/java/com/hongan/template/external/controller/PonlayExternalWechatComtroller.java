package com.hongan.template.external.controller;

import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.external.service.IExternalWechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@Api(tags = "第三方接口-微信公众号API")
@RestController
@RequestMapping("/base/api/wechat")
public class PonlayExternalWechatComtroller {
    @Autowired
    private IExternalWechatService service;

    @ApiOperation("获取微信JsSdkInfo")
    @GetMapping("/jsSdkInfo")
    public BaseResponse getJsApiInfo(String url) throws InterruptedException, BaseException, IOException {
        Map<String, Object> res = service.getJsSdkInfo(url);
        return new BaseResponse().success(res);
    }
}
