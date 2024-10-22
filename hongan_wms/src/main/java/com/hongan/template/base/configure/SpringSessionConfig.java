package com.hongan.template.base.configure;

import com.hongan.template.base.properties.AppProperties;
import com.hongan.template.base.properties.CookieProperties;
import com.hongan.template.base.properties.SessionProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-02 18:03
 * @Description: SpringSessionConfig
 */

@Slf4j
@Configuration
public class SpringSessionConfig {

    @Autowired
    AppProperties properties;
    @Value("${spring.profiles.active}")
    private String path;

    @Bean
    public CookieSerializer cookieSerializer() {
        log.warn("app properties: {}", properties);
        CookieProperties cookie = properties.getCookie();
        SessionProperties session = properties.getSession();
        log.debug(String.valueOf(cookie));
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();

        serializer.setCookieName(session.getCookieName());
        serializer.setCookiePath(cookie.getPath());
        serializer.setUseHttpOnlyCookie(cookie.getHttpOnly());

//        if (path.startsWith("prod")) {
//            serializer.setUseSecureCookie(true);
//            serializer.setSameSite("NONE");
//            serializer.setDomainName("");
//            serializer.setCookieMaxAge(60 * 60 * 24 * 30);
//            return serializer;
//        } else {
            serializer.setUseSecureCookie(cookie.getHttps());
//        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
            serializer.setDomainName("");
            serializer.setCookieMaxAge(60 * 60 * 24 * 30);
            return serializer;
//        }
    }

}
