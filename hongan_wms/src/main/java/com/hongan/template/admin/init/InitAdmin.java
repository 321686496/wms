package com.hongan.template.admin.init;

import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.admin.entity.HonganDept;
import com.hongan.template.admin.entity.HonganMenu;
import com.hongan.template.admin.entity.HonganRole;
import com.hongan.template.admin.enums.MenuType;
import com.hongan.template.admin.service.IHonganAdminService;
import com.hongan.template.admin.service.IHonganDeptService;
import com.hongan.template.admin.service.IHonganMenuService;
import com.hongan.template.admin.service.IHonganRoleService;
import com.hongan.template.base.security.AutoInitComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author: zyp
 * @Date: 2021/11/16 0016
 */
@Component
public class InitAdmin implements AutoInitComponent {
    @Autowired
    private IHonganAdminService adminService;
    @Autowired
    private IHonganRoleService roleService;
    @Autowired
    private IHonganDeptService deptService;
    @Autowired
    private IHonganMenuService menuService;

    @Override
    public void init(ApplicationContext context) {
        if (adminService.count() < 1) {
            HonganRole supperRole = new HonganRole("超级管理员", "SUPER", "SUPER", "系统预设超级管理员角色，拥有系统所有数据的操作权限");
            supperRole.insert();

            HonganDept dept = new HonganDept("");
            dept.insert();

            HonganAdmin admin = new HonganAdmin(supperRole.getId(), dept.getId(), "18888888888", "admin123");
            admin.insert();
        }
        if (menuService.count() < 1) {
            HonganMenu home = new HonganMenu(0L, MenuType.Menu, true, "/home/index", "home", "", "/home/index", "House", "", 1300);
            home.insert();
            HonganMenu systemMenu = new HonganMenu(0L, MenuType.Catalogue, true, "/system", "system", "/system/menu/index", "", "Setting", "", 600);
            systemMenu.insert();
            HonganMenu systemMenuMenu = new HonganMenu(systemMenu.getId(), MenuType.Menu, true, "/system/menu/index", "system_menu", "", "/system/menu/index", "Grid", "", 599);
            systemMenuMenu.insert();
            HonganMenu systemDeptMenu = new HonganMenu(systemMenu.getId(), MenuType.Menu, true, "/system/department/index", "system_department", "", "/system/department/index", "OfficeBuilding", "", 599);
            systemDeptMenu.insert();
            HonganMenu systemRoleMenu = new HonganMenu(systemMenu.getId(), MenuType.Menu, true, "/system/role/index", "system_role", "", "/system/role/index", "Avatar", "", 598);
            systemRoleMenu.insert();
            HonganMenu systemStaffMenu = new HonganMenu(systemMenu.getId(), MenuType.Menu, true, "/system/staff/index", "system_staff", "", "/system/staff/index", "UserFilled", "", 598);
            systemStaffMenu.insert();
        }
    }
}
