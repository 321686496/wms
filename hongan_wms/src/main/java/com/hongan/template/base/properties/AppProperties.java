package com.hongan.template.base.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: Properties
*/

@Data
@SpringBootConfiguration
@PropertySource(factory = YamlPropertiesFactory.class, value = {"classpath:app.yml"})
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private CookieProperties cookie = new CookieProperties();
    private SessionProperties session = new SessionProperties();
    private SwaggerProperties swagger = new SwaggerProperties();
    private TokenProperties token = new TokenProperties();
    private CaptchaProperties captcha = new CaptchaProperties();
}
