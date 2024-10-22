package com.hongan.template.security.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hongan.template.base.captcha.ImgServiceCaptcha;
import com.hongan.template.base.captcha.PhoneServiceCaptcha;
import com.hongan.template.base.captcha.entity.PhoneCaptcha;
import com.hongan.template.base.entity.BaseController;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.entity.BaseResponse;
import com.hongan.template.base.utils.DataCheckUtils;
import com.hongan.template.config.service.IHonganConfigService;
import com.hongan.template.external.enums.SmsTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 短信验证码公共接口
 */

@Api(tags = "公共-短信验证码")
@RequestMapping(value = "/base/phoneCaptcha")
@RestController
public class PhoneCaptchaController extends BaseController {

    @Autowired
    PhoneServiceCaptcha phoneCaptchaService;

    @Autowired
    ImgServiceCaptcha imgCaptchaService;
    @Autowired
    private IHonganConfigService systemConfigService;

    @PostMapping
    @ApiOperation("获取手机验证码(可直接获取)")
    public BaseResponse getCaptcha(HttpServletRequest request, @RequestBody PhoneCaptcha body) throws JsonProcessingException, BaseException {
        DataCheckUtils.checkParamRegex(body.getPhone(), systemConfigService.getValue("system_regex_phone"), BaseError.PhoneFormatError);
        if (!phoneCaptchaService.sendCaptcha(request, body.getPhone(), SmsTemplate.RegisterCaptcha)) {
            throw new BaseException(BaseError.SendPhoneCaptchaError);
        }
        return new BaseResponse().success();
    }

    @PostMapping("/verify")
    @ApiOperation("获取注册手机验证码（需要填写图片验证吗）")
    public BaseResponse getCaptchaVerifyImg(HttpServletRequest request, @RequestBody PhoneCaptcha body) throws JsonProcessingException, BaseException {
        DataCheckUtils.checkParamRegex(body.getPhone(), systemConfigService.getValue("system_regex_phone"), BaseError.PhoneFormatError);
        // 必须先验证图片通过后才能发送手机验证码
        if (!imgCaptchaService.verifyCaptcha(request, body.getImgCaptcha())) {
            throw new BaseException(BaseError.ImgCaptchaError);
        }
        if (!phoneCaptchaService.sendCaptcha(request, body.getPhone(), SmsTemplate.RegisterCaptcha)) {
            throw new BaseException(BaseError.SendPhoneCaptchaError);
        }
        return new BaseResponse().success();
    }

//    @ApiOperation("获取手机验证码(验证用户手机号,一般用于找回密码)")
//    @PostMapping("/verify/user")
//    public BaseResponse getUserVerify(HttpServletRequest request, @RequestBody PhoneCaptcha body) throws JsonProcessingException, BaseException {
//        RegexUtils.checkPhone(body.getPhone());
//
////        // 必须先验证图片通过后才能发送手机验证码
////        if (!imgCaptchaService.verifyCaptchaAndSave(request, body.getImgCaptcha())) {
////            return new BaseResponse().error(BaseError.ImgCaptchaError);
////        }
//        Boolean result = phoneCaptchaService.sendCaptcha(request, body.getPhone(), SmsTemplate.RegisterCaptcha);
//        if (!result) {
//            throw new BaseException("手机验证码发送失败！");
//        }
//        return new BaseResponse().success();
//    }


//    @ApiOperation("获取手机验证码")
//    @PostMapping()
//    public BaseResponse get(
//            HttpServletRequest request,
//            @RequestBody PhoneCaptcha body
//    ) throws JsonProcessingException {
//        // 必须先验证图片通过后才能发送手机验证码
//        if (!imgCaptchaService.verifyCaptchaAndSave(request, body.getImgCaptcha())) {
//            return new BaseResponse().error(BaseError.ImgCaptchaError);
//        }
//
//        Boolean result = phoneCaptchaService.sendCaptcha(request, body.getPhone(), PhoneNotifyType.CAPTCHA);
//        if (!result) {
//            return new BaseResponse().error();
//        }
//        return new BaseResponse().success();
//    }
//
//    @ApiOperation("验证手机验证码")
//    @PutMapping()
//    public BaseResponse verifyCaptcha(
//            HttpServletRequest request,
//            @RequestBody PhoneCaptcha body
//    ) {
//        if (phoneCaptchaService.verify(request, body.getPhone(), body.getCaptcha())) {
//            return new BaseResponse().success();
//        }
//        return new BaseResponse().error(BaseError.PhoneCaptchaError);
//    }
//
//
//
//    @ApiOperation("验证手机验证码(保存验证身份)")
//    @PutMapping("/verify")
//    public BaseResponse setVerify(
//            HttpServletRequest request,
//            @RequestBody PhoneCaptcha body
//    ) {
//        // 验证成功后保存凭证以备后续操作授权
//        if (phoneCaptchaService.verifyAndSave(request, body.getPhone(), body.getCaptcha())) {
//            return new BaseResponse().success();
//        }
//        return new BaseResponse().error(BaseError.PhoneCaptchaError);
//    }

}
