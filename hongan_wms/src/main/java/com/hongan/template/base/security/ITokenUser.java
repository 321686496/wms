package com.hongan.template.base.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hongan.template.enums.AdminStatus;
import com.hongan.template.base.utils.ClientType;
import com.hongan.template.enums.ModuleType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: ITokenUser
 */


public interface ITokenUser extends UserDetails {
    Long getId();

    String getName();

    String getNickName();

    String getRealName();

    String getRole();

    String getRoleKey();

    String getRoleName();

    String getPhone();

    String getAvatar();

    Boolean getIsInitial();

    AdminStatus getStatus();

    @JsonIgnore
    ClientType getClientType();

    @JsonIgnore
    String getPassword();
}
