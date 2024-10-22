package com.hongan.template.admin.entity;

import com.hongan.template.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色和菜单关联表
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="RoleMenu对象", description="角色和菜单关联表")
public class HonganRoleMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "菜单ID")
    private Long menuId;


}
