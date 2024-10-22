package com.hongan.template.base.properties;

import lombok.Data;

/**
 * 手机验证码服务相关配置信息
 */
@Data
public class PhoneProperties extends CacheProperties {
    private String cacheName;
    private String verifyName;
    private int expires;
    private int verifyExpires;
}
