package com.hongan.template.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hongan.template.admin.enums.RoleDataScope;
import com.hongan.template.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
@ApiModel(value = "Role对象", description = "")
@NoArgsConstructor
@AllArgsConstructor
public class HonganRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public HonganRole(String name, String roleType, String roleKey, String remark) {
        this.name = name;
        this.roleType = roleType;
        this.roleKey = roleKey;
        this.remark = remark;
        this.isDefault = true;
    }

    @ApiModelProperty(value = "是否默认角色(true:是/false:否)")
    private Boolean isDefault;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色类型")
    private String roleType;

    @ApiModelProperty(value = "角色关键字")
    private String roleKey;

    @ApiModelProperty(value = "角色介绍")
    private String remark;

    @ApiModelProperty(value = "角色菜单ID列表")
    @TableField(exist = false)
    private List<Long> menuIds;

    @ApiModelProperty(value = "角色权限")
    @TableField(exist = false)
    private List<String> permissions;


    @ApiModelProperty(value = "角色菜单权限")
    @TableField(exist = false)
    private List<HonganMenu> menus;

}
