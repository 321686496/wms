package com.hongan.template.security.service;

import java.util.Collection;
import java.util.List;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2020-02-25 10:56
 * @Description: IRoleService
 */

public interface IRoleService {
    boolean checkAuthorize(String role, String key, String action);

    boolean checkButtonRole(String role, String key, String action);

    void cacheRolePermissions(String roleKey, List<String> permissions);

    void removeRole(String roleKey);
}
