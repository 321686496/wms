package com.hongan.template.base.captcha.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hongan.template.base.validation.PhoneValidation;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-03-07 15:00
 * @Description: GetPhoneCaptcha
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneCaptcha {
    @PhoneValidation
    @NotNull
    private String phone;
    private String origin;
    private String captcha;
    private String imgCaptcha;

    private Long proxyId;
}
