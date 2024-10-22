package com.hongan.template.base.properties;


import lombok.Data;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: CookieProperties
 */

@Data
public class TokenProperties {
    private boolean useCookie;
    private int tokenExpires;
    private String cookieName;
}
