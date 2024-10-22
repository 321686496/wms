import { MenuTypeEnum, DataScopeEnum, UserStatusEnum } from "@/enums/systemEnum";
export namespace SystemVo {
	// 菜单
	export interface MenuVo {
		id?: number;
		pid: number; //父菜单ID
		menuType: MenuTypeEnum; //菜单类型(目录/菜单/按钮)
		path?: string; //菜单路径
		name: string; //菜单别名
		redirect?: string; //重定向地址
		component?: string; //视图文件路径
		appletMetaIcon?: string; // 小程序图标
		appletPath?: string; // 小程序路径
		appletMetaIsHide?: boolean; //小程序隐藏
		metaIcon?: string; //菜单图标
		metaActiveMenu?: string; //当前路由为详情页时，需要高亮的菜单
		metaIsLink: boolean; //是否外链
		metaIsHide: boolean; //是否隐藏
		metaIsFull: boolean; //是否全屏
		metaIsAffix: boolean; //是否固定在TabsNav
		metaIsKeepAlive: boolean; //是否缓存
		enable: boolean; //是否启用
		modules?: string; //所属模块
		perms: string; //权限标识
		priority: number; //排序值
		createdAt?: number; //创建时间
		updatedAt?: number; //更新时间
		children?: MenuVo[];
	}

	// 部门
	export interface DepartmentVo {
		id?: number;
		pid: number; //父ID
		name: string; //部门名称
		isDefault: boolean; //是否默认部门
		leader?: string; //负责人
		phone?: string; //联系电话
		email?: string; //邮箱
		enable: boolean; //是否启用
		priority: number; //排序值
		createdAt?: number; //创建时间
		updatedAt?: number; //更新时间
		children?: DepartmentVo[];
	}

	// 角色
	export interface RoleVo {
		id?: number;
		isDefault: boolean; //是否默认角色
		name: string; //角色名称
		roleKey: string; //角色标识
		miniappLogin: boolean; //是否允许小程序登录
		dataScope: DataScopeEnum; // 数据权限范围
		remark?: string; //备注
		createdAt?: number; //创建时间
		updatedAt?: number; //更新时间
		menuIds: number[]; //已授权菜单
		deptIds: number[]; //已授权部门
	}

	// 管理员
	export interface StaffVo {
		id?: number;
		name: string; //登录名称
		phone: string; // 手机号
		roleId: number; // 角色ID
		deptId: number; // 部门ID
		isInitial?: boolean; //是否初始账号
		isLogoff?: boolean; //是否注销
		status: UserStatusEnum; //用户状态
		password?: string; //密码
		realName?: string; //真实姓名
		avatar?: string; //头像
		sex?: string; //性别
		email?: string; //邮箱
		loginIp?: string; //最后登录IP
		loginDate?: string; //最后登录时间
		remark?: string; //备注
		createdAt?: string; //创建时间
		updatedAt?: string; //更新时间
		pwdUpdateDate?: string; //密码更新时间
	}

	// 打印机
	export interface PrinterVo {
		id?: number;
		paperSize?: string; //尺寸
		name?: string; //名称
		printSn?: string; //SN
		printKey?: string; //Key
		enable?: boolean; //启用
		createdAt?: string; //创建时间
		updatedAt?: string; //更新时间
	}
	// 会审
	export interface JointReviewVo {
		id?: number;
		billTypes: string; // 单据类型
		billTypesList: string[];
		verifyType: "AnyVerify" | "AllVerify";
		isSuccessivelyVerify: boolean; //是否依次审核
		verifyPersonType: "Role" | "Dept";
		verifyPersonList: {
			id?: number;
			name?: string;
			adminId?: number;
			adminName?: string;
			chapterUrl?: string;
			adminOptions?: any[];
		}[]; //审核人列表
		remark?: string;
		createdAt?: string; //创建时间
		updatedAt?: string; //更新时间
	}
}
