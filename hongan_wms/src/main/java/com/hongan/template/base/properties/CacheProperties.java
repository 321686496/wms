package com.hongan.template.base.properties;

import lombok.Data;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: CacheProperties
 */

@Data
public class CacheProperties {
    private String cacheName;
    private String verifyName;
    private int expires;
    private int verifyExpires;
}
