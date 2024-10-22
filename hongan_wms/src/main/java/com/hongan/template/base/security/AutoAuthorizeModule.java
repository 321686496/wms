package com.hongan.template.base.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-04-15 10:09
 * @Description: AutoAuthorize 自动创建权限数据表
 */


@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE})
public @interface AutoAuthorizeModule {
    // 如果是一个单词 则为主模块，如果存在-连接符，则分为父子两个模块，父在前，子在后 例如：security-admin
    // 配合preAuthorize 自动构建，则hasPermission 第一个参数需要与 value() 值一致才能正确构建
    String value();
    String name();
}
