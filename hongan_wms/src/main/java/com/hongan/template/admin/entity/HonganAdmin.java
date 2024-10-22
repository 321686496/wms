package com.hongan.template.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hongan.template.admin.enums.RoleDataScope;
import com.hongan.template.base.entity.BaseEntity;
import com.hongan.template.enums.AdminStatus;
import com.hongan.template.enums.ModuleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangxd
 * @since 2020-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "Admin对象", description = "")
@NoArgsConstructor
public class HonganAdmin extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public HonganAdmin(Long id) {
        this.setId(id);
    }

    public HonganAdmin(Long roleId, Long deptId, String phone, String password) {
        this.roleId = roleId;
        this.deptId = deptId;
        this.isInitial = true;
        this.name = phone;
        this.phone = phone;
        this.setCryptPassword(password);
        this.status = AdminStatus.NORMAL;
    }

    public HonganAdmin(String name, String phone, String password, String role, String roleName, String roleKey) {
        this.name = name;
        this.phone = phone;
        this.setCryptPassword(password);
        this.role = role;
        this.roleName = roleName;
        this.roleKey = roleKey;
    }

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "初始账号(true:是/false:否)")
    private Boolean isInitial;

    @ApiModelProperty(value = "登录名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "状态：NORMAL:正常/LOCK:禁用")
    private AdminStatus status;

    @JsonIgnore
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "用户性别")
    private String sex;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "最后登录IP")
    private String loginIp;

    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime loginDate;

    @ApiModelProperty(value = "密码最后更新时间")
    private LocalDateTime pwdUpdateDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色类型")
    private String role;
    @TableField(exist = false)
    @ApiModelProperty(value = "角色关键字")
    private String roleKey;
    @TableField(exist = false)
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @TableField(exist = false)
    private String pwd;

    private void setCryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }


}
