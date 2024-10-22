package com.hongan.template.base.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: YamlPropertiesFactory
 */

@Slf4j
public class YamlPropertiesFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
        Resource resource1 =  resource.getResource();
        boolean a = resource1.exists();
        URI uri = resource1.getURI();
        List<PropertySource<?>> sources = new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource());
        return sources.get(0);
    }

//    @Override
//    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
//        Properties propertiesFromYaml = loadYamlIntoProperties(resource);
//        String sourceName = name != null ? name : resource.getResource().getFilename();
//        return new PropertiesPropertySource(sourceName, propertiesFromYaml);
//
//    }
//
//    private Properties loadYamlIntoProperties(EncodedResource resource) throws FileNotFoundException {
//        try {
//            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
//            factory.setResources(resource.getResource());
//            factory.afterPropertiesSet();
//            return factory.getObject();
//        } catch (IllegalStateException e) {
//            // for ignoreResourceNotFound
//            Throwable cause = e.getCause();
//            if (cause instanceof FileNotFoundException)
//                throw (FileNotFoundException) e.getCause();
//            throw e;
//        }
//    }
}
