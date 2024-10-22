package com.hongan.template.security.controller;

import com.hongan.template.base.captcha.ImgServiceCaptcha;
import com.hongan.template.base.entity.BaseController;
import com.hongan.template.base.entity.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 图片验证码公共接口
 */

@Api(tags = "公共-图形验证码")
@RequestMapping(value = "/base/imgCaptcha")
@RestController
public class ImgCaptchaController extends BaseController {

    @Autowired
    ImgServiceCaptcha imgCaptchaService;

    @ApiOperation("获取图形验证码")
    @GetMapping
    public BaseResponse get(HttpServletRequest request) {
        String captcha = imgCaptchaService.captcha(request);
        return new BaseResponse().success(captcha);
    }

//    @ApiOperation("验证图形验证码")
//    @PostMapping
//    public BaseResponse verifyCaptcha(HttpServletRequest request, @NotBlank() String imgCaptcha) {
//        if (imgCaptchaService.verifyCaptchaAndSave(request, imgCaptcha)) {
//            return new BaseResponse().success();
//        }
//        return new BaseResponse().error(BaseError.ImgCaptchaError);
//    }
}
