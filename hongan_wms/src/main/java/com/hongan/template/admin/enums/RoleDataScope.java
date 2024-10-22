package com.hongan.template.admin.enums;

/**
 * 角色数据范围标识
 */
public enum RoleDataScope {
    AllData,//全部
    CustomData,//自定义数据权限
    ThisDept,//本部门数据权限
    ThisDeptBelow,//本部门及以下数据权限
    SelfData,//仅本人数据权限
}
