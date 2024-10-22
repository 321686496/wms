package com.hongan.template.security.common;

import com.hongan.template.security.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-03-05 14:57
 * @Description: PermissionEvaluator
 */

@Slf4j
@Component
public class PlPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    IRoleService roleService;

    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object o1) {
        TokenUser user = (TokenUser) authentication.getPrincipal();
        String role = user.getRole();
        String roleKey = user.getRoleKey();
        // SUPER 超级管理员不用校验，全部放行
        if ("SUPER".equals(role)) {
            return true;
        }
        return roleService.checkAuthorize(roleKey, (String) o, (String) o1);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        TokenUser user = (TokenUser) authentication.getPrincipal();
        String role = user.getRole();
        String roleKey = user.getRoleKey();

        // SUPER 超级管理员不用校验，全部放行
        if ("SUPER".equals(role)) {
            return true;
        }
        return roleService.checkAuthorize(roleKey, (String) serializable, s);
    }
}
