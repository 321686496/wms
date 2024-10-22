package com.hongan.template.base.properties;

import lombok.Data;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: CaptchaProperties
 */

@Data
public class CaptchaProperties {
    private String nameSpace;
    private CacheProperties img = new CacheProperties();
    private PhoneProperties phone = new PhoneProperties();
    private CacheProperties email = new CacheProperties();
}
