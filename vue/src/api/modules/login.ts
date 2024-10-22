import { Login } from "@/api/interface/index";
import { PORT1 } from "@/api/config/servicePort";
// import DynamicRouter from "@/assets/json/dynamicRouter.json";
// import AuthButtons from "@/assets/json/authButtons.json";
import qs from "qs";
import http from "@/api";

/**
 * @name 登录模块
 */

// 账号密码登录
export const accountLoginApi = (params: Login.AccountLoginForm) => {
	return http.post<Login.ResLogin>(`/auth/admin/name`, params, { headers: { noLoading: true } });
};

// 手机验证码登录
export const mobileLoginApi = (params: Login.PhoneLoginForm) => {
	return http.post<Login.ResLogin>(`/auth/admin/phone`, params, { headers: { noLoading: true } });
};

// 退出登录
export const logoutApi = () => {
	return http.post(`/auth/logout`, {}, { headers: { noLoading: true } });
};

// 注册账号
export const registerApi = (params: Login.RegisterForm) => {
	return http.post(`/base/tenant/register`, params, { headers: { noLoading: true } });
};

// 申请预约
export const appointApi = (params: any) => {
	return http.post(`/base/tenant/appointment`, params, { headers: { noLoading: true } });
};

// 重置密码
export const resetPwdApi = (params: Login.RegisterForm) => {
	return http.put(`/base/tenant/reset/pwd`, params, { headers: { noLoading: true } });
};

// 修改密码
export const updatePwd = (params: Login.UpdatePwdForm) => {
	return http.put(`/admin/current/updatePassword`, params, { headers: { noLoading: true } });
};

// 获取登录用户信息
export const queryUserInfo = () => {
	return http.get(`/admin/current/detail`, {}, { headers: { noLoading: true } });
};

// 获取登录商户信息
export const queryTenantInfo = () => {
	return http.get(`/admin/admin/tenant`, {}, { headers: { noLoading: true } });
};

// 修改登录商户信息
export const updateTenantInfo = (data: any) => {
	return http.put(`/admin/admin/tenant`, data);
};

// 修改登录用户信息
export const updateUserInfo = (data: any) => {
	return http.put(`/admin/current/updateMessage`, data);
};

// * 获取用户已授权菜单列表（包含已授权按钮）
export const getAuthMenuListApi = () => {
	return http.get<any>(`/admin/current/role`, {}, { headers: { noLoading: true } });
	// 如果想让菜单变为本地数据，注释上一行代码，并引入本地 dynamicRouter.json 数据
	// return DynamicRouter;
};

// 清空业务数据
export const clearBillData = (params: { password: string; clearBillData: boolean; clearBasicData: boolean }) => {
	return http.put(`/admin/admin/tenant/clearData`, params);
};

// 注销租户
export const logOffTenantOne = (params: { password: string }) => {
	return http.put(`/admin/admin/tenant/logout`, params);
};

// 查询是否同账号登录
export const querySameAccountLogin = () => {
	return http.get(`/admin/auth`, {}, { headers: { noLoading: true } });
};

// 清除其他用户
export const clearSameAccount = () => {
	return http.post(`/admin/auth/removeOther`, {}, { headers: { noLoading: true } });
};

/**-----------------旧的暂时不用------------------**/

// * 用户登录
export const loginApi = (params: Login.ReqLoginForm) => {
	return http.post<Login.ResLogin>(PORT1 + `/user/pub/login`, params, { headers: { noLoading: true } }); // 正常 post json 请求  ==>  application/json
	return http.post<Login.ResLogin>(PORT1 + `/login`, params, { headers: { noLoading: true } }); // 控制当前请求不显示 loading
	return http.post<Login.ResLogin>(PORT1 + `/login`, {}, { params }); // post 请求携带 query 参数  ==>  ?username=admin&password=123456
	return http.post<Login.ResLogin>(PORT1 + `/login`, qs.stringify(params)); // post 请求携带表单参数  ==>  application/x-www-form-urlencoded
	return http.get<Login.ResLogin>(PORT1 + `/login?${qs.stringify(params, { arrayFormat: "repeat" })}`); // 如果是 get 请求可以携带数组等复杂参数
};

// * 获取按钮权限
export const getAuthButtonListApi = () => {
	return http.get<Login.ResAuthButtons>(PORT1 + `/auth/buttons`, {}, { headers: { noLoading: true } });
	// 如果想让按钮权限变为本地数据，注释上一行代码，并引入本地 authButtons.json 数据
	// return AuthButtons;
};
