package com.hongan.template.base.captcha.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-03-07 15:00
 * @Description: GetPhoneCaptcha
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgCaptcha {
    @NotBlank
    @NotNull
    private String imgCaptcha;
}
