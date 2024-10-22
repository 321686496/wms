package com.hongan.template.base.captcha.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-03-07 15:00
 * @Description: 分步骤登录实体
 */


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NameStepPhoneCaptcha {
    // 第一步用户密码登录
    private String imgCaptcha;
    private String name;
    private String password;
    // 步骤 1, 2, 3
    @NotNull
    @Min(1)
    @Max(3)
    private Integer step;
    // 第二步 发送手机验证码
    private String origin;
    // 第三部 验证手机验证码
    private String captcha;
}
