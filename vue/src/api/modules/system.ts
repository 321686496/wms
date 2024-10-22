import http from "@/api";
import { SystemVo } from "@/typings/modules/system";

/**
 * @name 系统管理
 */

/**
 * @name 系统配置
 */

// 查询系统配置
export const getSystemConfig = () => {
	return http.get(`/admin/system/setting`, {}, { headers: { noLoading: true } });
};

// 修改系统配置
export const updateSystemConfig = (data: any) => {
	return http.put(`/admin/system/setting`, data);
};

/**
 * @name 菜单管理
 */

// 分页查询菜单列表
export const getMenuList = (data: any) => {
	return http.get(`/admin/security/menu`, data, { headers: { noLoading: true } });
};

// 新增单个菜单
export const addMenuOne = (data: SystemVo.MenuVo) => {
	return http.post(`/admin/security/menu`, data);
};

// 修改单个菜单
export const updateMenuOne = (data: any) => {
	return http.put(`/admin/security/menu`, data);
};

// 禁用/启用单个菜单
export const enableMenuOne = (data: { id: number; enable: boolean }) => {
	return http.put(`/admin/security/menu/enable`, data);
};

// 删除单个菜单
export const deleteMenuOne = (data: { id: number }) => {
	return http.delete(`/admin/security/menu`, data);
};

/**
 * @name 部门管理
 */

// 分页查询部门列表
export const getDepartmentList = (data: any) => {
	return http.get(`/admin/security/dept`, data);
};

// 新增部门单个
export const addDepartmentOne = (data: SystemVo.DepartmentVo) => {
	return http.post(`/admin/security/dept`, data);
};

// 修改部门单个
export const updateDepartmentOne = (data: SystemVo.DepartmentVo) => {
	return http.put(`/admin/security/dept`, data);
};

// 禁用/启用单个部门
export const enableDepartmentOne = (data: { id: number; enable: boolean }) => {
	return http.put(`/admin/security/dept/enable`, data);
};

// 删除部门单个
export const deleteDepartmentOne = (data: { id: number }) => {
	return http.delete(`/admin/security/dept`, data);
};

/**
 * @name 角色管理
 */

// 分页查询角色列表
export const getRoleList = (data: any) => {
	return http.get(`/admin/security/role`, data);
};

// 新增角色单个
export const addRoleOne = (data: SystemVo.RoleVo) => {
	return http.post(`/admin/security/role`, data);
};

// 修改角色单个
export const updateRoleOne = (data: SystemVo.RoleVo) => {
	return http.put(`/admin/security/role`, data);
};

// 删除角色单个
export const deleteRoleOne = (data: { id: number }) => {
	return http.delete(`/admin/security/role`, data);
};

// 复制角色单个
export const copyRoleOne = (data: { id: number }) => {
	return http.put(`/admin/security/role/copy`, data);
};

// 复制角色批量
export const copyRoleBatch = (data: { ids: number[] }) => {
	return http.put(`/admin/security/role/copy/batch`, data);
};

/**
 * @name 员工管理
 */

// 分页查询员工列表
export const getStaffList = (data: any) => {
	return http.get(`/admin/security/admin`, data);
};

// 新增员工单个
export const addStaffOne = (data: SystemVo.StaffVo) => {
	return http.post(`/admin/security/admin`, data);
};

// 修改员工单个
export const updateStaffOne = (data: SystemVo.StaffVo) => {
	return http.put(`/admin/security/admin`, data);
};

// 修改员工是否接受短信通知
export const updateStaffOrderSmsNotifyOne = (data: any) => {
	return http.put(`/admin/security/admin/newOrderNotify`, data);
};

// 删除员工单个
export const deleteStaffOne = (data: { id: number }) => {
	return http.delete(`/admin/security/admin`, data);
};

// 禁用/启用单个员工
export const enableStaffOne = (data: { id: number; enable: boolean }) => {
	return http.put(`/admin/security/admin/enable`, data);
};

// 重置员工密码
export const resetStaffPwdOne = (data: { id: number }) => {
	return http.put(`/admin/security/admin/reset`, data);
};

/**
 * @name 首页统计
 */

// 查询库存饼图
export const getStockPieApi = (data?: any) => {
	return http.get(`/admin/statistics/index/stock`, data, { headers: { noLoading: true } });
};

// 查询销售订单统计折线图
export const getSaleStatisticsLineApi = (data?: any) => {
	return http.get(`/admin/statistics/index/sale`, data, { headers: { noLoading: true } });
};

// 查询销售出库单统计折线图
export const getSaleOutStatisticsLineApi = (data?: any) => {
	return http.get(`/admin/statistics/index/sale/outstock`, data, { headers: { noLoading: true } });
};

// 查询采购统计折线图
export const getPurchaseStatisticsLineApi = (data?: any) => {
	return http.get(`/admin/statistics/index/purchase`, data, { headers: { noLoading: true } });
};

// 查询采购入库统计折线图
export const getPurchaseInStatisticsLineApi = (data?: any) => {
	return http.get(`/admin/statistics/index/purchase/instock`, data, { headers: { noLoading: true } });
};

// 查询生产看板统计表
export const getProductionStatisticsApi = (data?: any) => {
	return http.get(`/admin/statistics/production/board`, data, { headers: { noLoading: true } });
};

// 查询角色可用菜单供常用菜单选择
export const getAuthMenuTreeSelect = (data?: any) => {
	return http.get<treeOption[]>(`/admin/security/menu/tree/admin`, data, { headers: { noLoading: true } });
};

// 查询用户首页常用菜单
export const getUserCommonMenuList = (data?: any) => {
	return http.get(`/admin/tenant/index/menu`, data, { headers: { noLoading: true } });
};

// 保存用户首页常用菜单
export const saveUserCommonMenuList = (data?: any) => {
	return http.post(`/admin/tenant/index/menu`, data);
};

// 删除用户首页常用菜单单个
export const deleteUserCommonMenuOne = (data?: any) => {
	return http.delete(`/admin/tenant/index/menu`, data);
};

// 查询资金统计
export const getFundStatisticsApi = (data?: any) => {
	return http.get(`/admin/statistics/index/cash`, data, { headers: { noLoading: true } });
};

// 查询资产统计
export const getAssetStatisticsApi = (data?: any) => {
	return http.get(`/admin/statistics/index/asset`, data, { headers: { noLoading: true } });
};

/**
 * @name 续费管理
 */

// 查询模块套餐
export const getBuPackageList = (data?: any) => {
	return http.get(`/base/saas/package`, data, { headers: { noLoading: true } });
};

// 查询以图识物套餐
export const getImgPackageList = (data?: any) => {
	return http.get(`/base/saas/package/picture`, data, { headers: { noLoading: true } });
};

// 下单（业务模块订单）
export const addBusPackageOrder = (data?: any) => {
	return http.post(`/admin/order`, data);
};

// 再次支付订单（业务模块订单）
export const payAgainBusPackageOrder = (data?: any) => {
	return http.post(`/admin/order/payAgain`, data);
};

// 查询订单支付状态（业务模块订单）
export const queryBusPackageOrderStatus = (data?: any) => {
	return http.get<"Done" | "WaitPay">(`/admin/order/status`, data, { headers: { noLoading: true } });
};

// 取消订单（业务模块订单）
export const cancelBusPackageOrder = (data?: any) => {
	return http.put(`/admin/order/cancel`, data);
};

// 查询我的订单（业务模块订单）
export const getBuPackageMyOrderList = (data?: any) => {
	return http.get(`/admin/order`, data, { headers: { noLoading: true } });
};

// 下单（以图识物订单）
export const addImgPackageOrder = (data?: any) => {
	return http.post(`/admin/order/picture`, data);
};

// 查询订单支付状态（以图识物订单）
export const queryImgPackageOrderStatus = (data?: any) => {
	return http.get<"Done" | "WaitPay">(`/admin/order/picture/status`, data, { headers: { noLoading: true } });
};

// 查询我的订单（以图识物订单）
export const getImgPackageMyOrderList = (data?: any) => {
	return http.get(`/admin/order/picture`, data, { headers: { noLoading: true } });
};

// 取消订单（以图识物订单）
export const cancelImgPackageOrder = (data?: any) => {
	return http.put(`/admin/order/picture/cancel`, data);
};

// 再次支付订单（以图识物订单）
export const payAgainImgPackageOrder = (data?: any) => {
	return http.post(`/admin/order/picture/payAgain`, data);
};
