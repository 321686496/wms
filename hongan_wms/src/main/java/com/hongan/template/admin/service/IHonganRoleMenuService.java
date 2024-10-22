package com.hongan.template.admin.service;

import com.hongan.template.admin.entity.HonganMenu;
import com.hongan.template.admin.entity.HonganRole;
import com.hongan.template.admin.entity.HonganRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色和菜单关联表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
public interface IHonganRoleMenuService extends IService<HonganRoleMenu> {

    void saveRoleMenu(HonganRole role);

    List<Long> getRoleMenuIds(Long roleId);

    List<HonganMenu> getRoleMenu(Long roleId);

}
