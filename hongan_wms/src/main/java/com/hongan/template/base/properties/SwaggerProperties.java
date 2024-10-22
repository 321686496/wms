package com.hongan.template.base.properties;

import lombok.Data;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: SwaggerProperties
*/

@Data
public class SwaggerProperties {
    private String basePackage;
    private String title;
    private String description;
    private String version;
    private String author;
    private String url;
    private String email;
    private String license;
    private String licenseUrl;
}
