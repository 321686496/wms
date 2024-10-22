package com.hongan.template.security.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hongan.template.admin.enums.RoleDataScope;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.security.ITokenUser;
import com.hongan.template.base.utils.ClientType;
import com.hongan.template.user.entity.HonganUser;
import com.hongan.template.enums.AdminStatus;
import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.enums.ModuleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: TokenUser
 */

@Getter
@Setter
@NoArgsConstructor
public class TokenUser implements ITokenUser {

    private static final long serialVersionUID = -9013004181525699582L;

    public static TokenUser fromUser(HonganAdmin admin, ClientType clientType) {
        TokenUser token = new TokenUser();
        token.id = admin.getId();
        token.name = admin.getName();
        token.avatar = admin.getAvatar();
        token.realName = admin.getRealName();
        token.role = admin.getRole() == null ? "NONE" : admin.getRole();
        token.roleName = admin.getRoleName() == null ? "NONE" : admin.getRoleName();
        token.nickName = admin.getRealName();
        token.phone = admin.getPhone();
        token.password = admin.getPassword();
        token.roleKey = admin.getRoleKey();
        token.status = admin.getStatus();
        token.clientType = clientType;
        token.roleId = admin.getRoleId();
        token.deptId = admin.getDeptId();
        token.deptName = admin.getDeptName();
        token.isInitial = admin.getIsInitial();
        return token;
    }

    public static TokenUser fromUser(HonganUser user, ClientType clientType) {
        TokenUser token = new TokenUser();
        token.id = user.getId();
        token.name = user.getName();
        token.avatar = user.getAvatar();
        token.realName = user.getNickname();
        token.nickName = user.getNickname();
        token.role = user.getRole() == null ? "CUSTOMER" : user.getRole();
        token.phone = user.getPhone();
        token.password = user.getPassword();
        token.status = user.getStatus();
        token.clientType = clientType;
        token.openId = user.getWxOpenId();
        return token;
    }

    protected Long id;
    protected String name;
    protected String realName;
    protected String avatar;
    protected String phone;
    protected String openId;
    protected String role;
    protected String roleName;
    protected String nickName;
    private String roleKey;
    protected AdminStatus status;
    protected String password;
    protected ClientType clientType;
    protected Boolean isInitial;

    //以下为扩展字段
    private Long roleId;
    private Long deptId;
    private String deptName;

    @JsonIgnore
    protected BaseError error;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        // 增加角色权限
        SimpleGrantedAuthority authRole = new SimpleGrantedAuthority("ROLE_" + this.getRole());
        SimpleGrantedAuthority role = new SimpleGrantedAuthority(this.getRole());
        authorities.add(authRole);
        authorities.add(role);
        return authorities;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.getStatus() == AdminStatus.NORMAL;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return this.name;
    }

    @Override
    public String getNickName() {
        return this.nickName;
    }

    @Override
    @JsonIgnore
    public AdminStatus getStatus() {
        return this.status;
    }

}
