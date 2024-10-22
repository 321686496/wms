import http from "@/api";
/**
 * @name 公共选项模块
 */

/**
 * @name 系统管理
 */

// * 获取菜单列表-树状-全部（已启用）
export const queryMenuTree = () => {
	return http.get<treeOption[]>(`/admin/security/menu/tree`, {}, { headers: { noLoading: true } });
};

// 获取部门列表-树状-全部（已启用）
export const queryDepartmentTree = () => {
	return http.get<treeOption[]>(`/admin/security/dept/tree`, {}, { headers: { noLoading: true } });
};

// 获取部门列表-树状-有权限（已启用）
export const queryDepartmentPermTree = () => {
	return http.get<treeOption[]>(`/admin/security/dept/tree/select`, {}, { headers: { noLoading: true } });
};

// * 获取租户可用菜单列表-树状-租户可用
export const queryTenantMenuTree = () => {
	return http.get<treeOption[]>(`/admin/security/menu/tree/tenant`, {}, { headers: { noLoading: true } });
};

// 获取全部角色
export const queryRoleAll = () => {
	return http.get<treeOption[]>(`/admin/security/role/select`, {}, { headers: { noLoading: true } });
};

// 获取全部管理员
export const queryAdminAll = (data: any) => {
	return http.get<treeOption[]>(`/admin/security/admin/select`, data, { headers: { noLoading: true } });
};

// 获取全部管理员（分页）
export const queryAdminList = (data: any) => {
	return http.get<treeOption[]>(`/admin/security/admin/list`, data, { headers: { noLoading: true } });
};

/**
 * @name 基础资料
 */
