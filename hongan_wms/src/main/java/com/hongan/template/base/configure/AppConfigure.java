package com.hongan.template.base.configure;

import com.hongan.template.base.properties.AppProperties;
import com.hongan.template.base.properties.SwaggerProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: FebsConfigure
*/

@Configuration
public class AppConfigure {
//    @Autowired
//    UploadFileConfigure fileConfigure;

    @Autowired
    AppProperties appProperties;

//    @Bean
//    MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setLocation(fileConfigure.getRoot());
//        return factory.createMultipartConfig();
//    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean("ThreadPool")
    public ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("PL-Async-Thread");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        SwaggerProperties swagger = appProperties.getSwagger();
        Info info = new Info().title(swagger.getTitle())
                .description(swagger.getDescription())
                .version(swagger.getVersion())
                .contact(new Contact().name(swagger.getAuthor()).email(swagger.getEmail()).url(swagger.getUrl()))
                .license(new License().name(swagger.getLicense()).url(swagger.getLicenseUrl()))
                ;

        return new OpenAPI().components(new Components()).info(info);
    }
    // 跨域配置
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedHeaders("*")
                        .allowedMethods("*")
                        .allowedOrigins("*");
            }
        };
    }

//    @Bean
//    public Docket swaggerApi() {
//        SwaggerProperties swagger = properties.getSwagger();
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage()))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo(swagger));
//    }
//
//
//    private ApiInfo apiInfo(SwaggerProperties swagger) {
//        return new ApiInfo(
//                swagger.getTitle(),
//                swagger.getDescription(),
//                swagger.getVersion(),
//                null,
//                new Contact(swagger.getAuthor(), swagger.getUrl(), swagger.getEmail()),
//                swagger.getLicense(), swagger.getLicenseUrl(), Collections.emptyList());
//    }
//
}
