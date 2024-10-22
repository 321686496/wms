package com.hongan.template.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hongan.template.admin.enums.MenuType;
import com.hongan.template.enums.ModuleType;
import com.hongan.template.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangxd
 * @since 2020-02-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "Menu对象", description = "后台菜单")
@NoArgsConstructor
public class HonganMenu extends BaseEntity {
    private static final long serialVersionUID = 1L;

    public HonganMenu(Long pid, MenuType menuType, Boolean enable, String path, String name, String redirect, String component, String metaIcon,String perms, Integer priority) {
        this.pid = pid;
        this.menuType = menuType;
        this.enable = enable;
        this.path = path;
        this.name = name;
        this.redirect = redirect;
        this.component = component;
        this.metaIcon = metaIcon;
        this.metaIcon = perms;
        this.priority = priority;
    }

    @ApiModelProperty(value = "上级目录ID")
    private Long pid;

    @ApiModelProperty(value = "菜单类型(Catalogue:目录/Menu:菜单/Button:按钮)")
    private MenuType menuType;

    @ApiModelProperty(value = "启用标识（true:正常 false:停用）")
    private Boolean enable;

    @ApiModelProperty(value = "菜单路径")
    private String path;

    @ApiModelProperty(value = "菜单别名")
    private String name;

    @ApiModelProperty(value = "重定向地址")
    private String redirect;

    @ApiModelProperty(value = "视图文件路径")
    private String component;

    @ApiModelProperty(value = "菜单图标")
    private String metaIcon;

    @ApiModelProperty(value = "菜单图标(小程序)")
    private String appletMetaIcon;

    @ApiModelProperty(value = "菜单路径(小程序)")
    private String appletPath;

    @ApiModelProperty(value = "当前路由为详情页时，需要高亮的菜单")
    private String metaActiveMenu;

    @ApiModelProperty(value = "是否外链(true:是/false:否 默认值：false)")
    private Boolean metaIsLink;

    @ApiModelProperty(value = "是否隐藏(true:是/false:否 默认值：false)")
    private Boolean metaIsHide;

    @ApiModelProperty(value = "是否隐藏(小程序)(true:是/false:否 默认值：false)")
    private Boolean appletMetaIsHide;

    @ApiModelProperty(value = "是否全屏(示例：数据大屏页面) (true:是/false:否 默认值：false)")
    private Boolean metaIsFull;

    @ApiModelProperty(value = "是否固定在 tabs nav (true:是/false:否 默认值：true)")
    private Boolean metaIsAffix;

    @ApiModelProperty(value = "是否缓存 (true:是/false:否 默认值：true)")
    private Boolean metaIsKeepAlive;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "子菜单")
    @TableField(exist = false)
    private List<HonganMenu> children;

    public void addChild(HonganMenu child) {
        if (children == null) children = new ArrayList<>();
        children.add(child);
    }

}
