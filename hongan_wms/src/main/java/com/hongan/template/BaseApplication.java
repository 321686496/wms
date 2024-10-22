package com.hongan.template;

import com.hongan.template.base.security.AutoInitComponent;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@MapperScan("com.hongan.template.*.mapper")
public class BaseApplication {
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(BaseApplication.class, args);
        init(run);
    }

    private static void init(ConfigurableApplicationContext context) {
        // Map<String, IOrderValidChain> beans1 = context.getBeansOfType(IOrderValidChain.class);
        Map<String, AutoInitComponent> beans = context.getBeansOfType(AutoInitComponent.class);
        for (AutoInitComponent component : beans.values()) {
            component.init(context);
        }
    }
//
//    /**
//     * 文件上传配置
//     * @return
//     */
//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        //文件最大
//        factory.setMaxFileSize(DataSize.ofMegabytes(100)); //KB,MB
//        // 设置总上传数据总大小
//        factory.setMaxRequestSize(DataSize.ofMegabytes(200));
//        return factory.createMultipartConfig();
//    }
}
