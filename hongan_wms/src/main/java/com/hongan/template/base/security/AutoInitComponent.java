package com.hongan.template.base.security;


import org.springframework.context.ApplicationContext;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-04-15 10:09
 * @Description: AutoInitComponent 自动初始化
 */


public interface AutoInitComponent {
    // 所有继承该接口的Bean都会自动依次调用以下两个方法，用于将系统初始化，比如初始化用户、权限、菜单等
    void init(ApplicationContext context);
}
