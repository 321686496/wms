package com.hongan.template.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.admin.entity.HonganMenu;
import com.hongan.template.admin.entity.HonganRole;
import com.hongan.template.admin.entity.HonganRoleMenu;
import com.hongan.template.admin.mapper.HonganRoleMenuMapper;
import com.hongan.template.admin.service.IHonganMenuService;
import com.hongan.template.admin.service.IHonganRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongan.template.security.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2023-07-12
 */
@Service
public class HonganRoleMenuServiceImpl extends ServiceImpl<HonganRoleMenuMapper, HonganRoleMenu> implements IHonganRoleMenuService {
    @Autowired
    private IRoleService securityRoleService;
    @Autowired
    private IHonganMenuService menuService;

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void saveRoleMenu(HonganRole role) {
        //删除历史权限
        baseMapper.removeMenuByRoleId( role.getId());
        securityRoleService.removeRole( role.getRoleKey());

        if (role.getMenuIds() == null || role.getMenuIds().size() < 1) return;
        for (Long menuId : role.getMenuIds()) {
            HonganRoleMenu roleMenu = new HonganRoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(menuId);
            roleMenu.insert();
        }
        //查询菜单可访问的接口权限
        List<String> perms = menuService.getMenuPermsByIds(role.getMenuIds());
        //保存新的菜单权限 和新的接口访问权限
        securityRoleService.cacheRolePermissions( role.getRoleKey(), perms);
    }

    @Override
    public List<Long> getRoleMenuIds( Long roleId) {
        List<HonganRoleMenu> menuList = baseMapper.selectList(new QueryWrapper<HonganRoleMenu>()
                .eq("role_id", roleId));
        return menuList.stream()
                .map(HonganRoleMenu::getMenuId)
                .collect(Collectors.toList());
    }

    @Override
    public List<HonganMenu> getRoleMenu(Long roleId){
        List<HonganMenu> res = new ArrayList<>();
        List<HonganRoleMenu> menuList = baseMapper.selectList(new QueryWrapper<HonganRoleMenu>()
                .eq("role_id", roleId));
        for (HonganRoleMenu roleMenu : menuList) {
//            PonlayMenu menu = menuService.selectById(roleMenu.getMenuId());
            HonganMenu menu = menuService.getOne(new QueryWrapper<HonganMenu>()
                    .eq("id", roleMenu.getMenuId()));
            if (menu != null) {
                res.add(menu);
            }
        }
        return res;
    }
}
