package com.hongan.template.base.captcha.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-03-07 15:00
 * @Description: GetPhoneCaptcha
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailCaptcha {
    @Email
    @NotNull
    private String email;
    private String origin;
    private String captcha;
    private String imgCaptcha;
}
