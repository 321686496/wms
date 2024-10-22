package com.hongan.template.base.properties;


import lombok.Data;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: CookieProperties
 */

@Data
public class CookieProperties {
    private String path = "/";
    private Boolean https = false;
    private Boolean httpOnly = true;
    private String domain = "";
//    private String domain = "natappfree.cc";
    private int expires = 3600 * 24 * 30;
}
