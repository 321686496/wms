package com.hongan.template.security.service.tmpl;

import com.hongan.template.base.service.IRedisValue;
import com.hongan.template.security.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisRoleServiceTmpl implements IRoleService {

    static final String CacheKey = "PL:CACHE:ROLE:";

    @Autowired
    IRedisValue<String> redisValue;

    @Override
    public boolean checkAuthorize(String role, String moduleKey, String action) {
        //暂时屏蔽数据权限
        return true;
//        String permissionKey = moduleKey + "-" + action;
//        String key = CacheKey + role;
//        Boolean aBoolean = redisValue.existSet(key, permissionKey);
//        return aBoolean;
    }

    @Override
    public boolean checkButtonRole(String role, String moduleKey, String action) {
        String permissionKey = moduleKey + "-" + action;
        String key = CacheKey + role;
        return redisValue.existSet(key, permissionKey);
    }

    @Override
    public void cacheRolePermissions(String roleKey, List<String> permissions) {
        String key = CacheKey + roleKey;
        redisValue.delete(key);
        String permissionsStr = String.join(",", permissions);
        if (permissions != null && permissions.size() > 0) {
            redisValue.addSet(key, permissionsStr.split(","));
        }
    }
//    @Override
//    public void cacheRolePermissions(String roleKey, List<String> permissions) {
//        String key = CacheKey + roleKey;
//        redisValue.delete(key);
//        if (permissions != null && permissions.size() > 0) {
//            redisValue.addSet(key, permissions.toArray(new String[0]));
//        }
//    }

    @Override
    public void removeRole(String roleKey) {
        String key = CacheKey + roleKey;
        redisValue.delete(key);
    }
}
