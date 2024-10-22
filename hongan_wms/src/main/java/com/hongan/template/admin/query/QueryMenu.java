package com.hongan.template.admin.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.admin.entity.HonganMenu;
import com.hongan.template.admin.enums.MenuType;
import com.hongan.template.enums.ModuleType;
import com.hongan.template.base.entity.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: zyp
 * @Date: 2021/11/17 0017
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryMenu extends BaseQuery<HonganMenu> {
    private List<MenuType> menuType;
    private List<Long> inIds;
    private String roleKey;
    private Boolean appletMetaIsHide;
    private String notLike;

    @Override
    public QueryWrapper<HonganMenu> toWrapper() {
        return super.toWrapper()
                .in(menuType != null && menuType.size() > 0, "menu_type", menuType)
                .in(inIds != null && inIds.size() > 0, "id", inIds)
                .notLike(notEmpty(notLike), "path", notLike)
                .eq(appletMetaIsHide != null, "applet_meta_is_hide", appletMetaIsHide)
                .orderByDesc("priority", "id")
                ;
    }
}
