// * 系统内置枚举
// 菜单类型
export enum MenuTypeEnum {
	"目录" = "Catalogue",
	"菜单" = "Menu",
	"按钮" = "Button"
}

// 业务模块
export enum BusinessModuleEnum {
	"基础模块" = "Basic"
}

// 数据权限范围
export enum DataScopeEnum {
	"全部数据权限" = 0,
	"自定数据权限" = 1,
	"本部门数据权限" = 2,
	"本部门及以下数据权限" = 3
}

// 用户状态
export enum UserStatusEnum {
	"正常" = "NORMAL",
	"禁用" = "LOCK"
}

// 短信验证码类型
/**
 * login 登录
 * register 注册
 * forget 忘记密码（找回密码）
 */
export type SmsCodeType = "login" | "register" | "forget";
