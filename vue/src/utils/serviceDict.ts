import useFormat18n from "@/hooks/useFormat18n";
// * 系统全局字典----------------------------------

/**
 * @description：用户状态
 */
export const userStatusList = [
	{ label: useFormat18n("common.normal"), value: "NORMAL" },
	{ label: useFormat18n("common.lock"), value: "LOCK" }
];

/**
 * @description：是否
 */
export const YesNoStatusList = [
	{ label: useFormat18n("common.yes"), value: true },
	{ label: useFormat18n("common.no"), value: false }
];

/**
 * @description：正确错误
 */
export const CorrectErrorStatusList = [
	{ label: useFormat18n("common.correct"), value: true },
	{ label: useFormat18n("common.error"), value: false }
];

/**
 * @description：是否启用
 */
export const enableStatusList = [
	{ label: useFormat18n("common.yes"), value: true },
	{ label: useFormat18n("common.yes"), value: false }
];

/**
 * @description：是否枚举
 */
export const enableNumStatusList = [
	{ label: useFormat18n("common.yes"), value: 1 },
	{ label: useFormat18n("common.no"), value: 0 }
];

// 包含枚举
export const inValuesList = [
	{ label: useFormat18n("common.contain"), value: "in" },
	{ label: useFormat18n("common.notContain"), value: "notIn" }
];

// 与或枚举
export const andOrValuesList = [
	{ label: useFormat18n("common.and"), value: "&" },
	{ label: useFormat18n("common.or"), value: "|" }
];

/***----------------------------------------------------------
 * @description：系统管理
 */

/**
 * @description：角色类型
 */
export const roleTypeStatusList = [
	{ label: useFormat18n("system.superAdmin"), value: "SUPER" },
	{ label: useFormat18n("system.normalAdmin"), value: "ADMIN" }
];

/**
 * @description：角色数据范围
 */
export const roleDataScopeList = [
	{ label: useFormat18n("system.allData"), value: "AllData" },
	{ label: useFormat18n("system.customData"), value: "CustomData" },
	{ label: useFormat18n("system.departmentData"), value: "ThisDept" },
	{ label: useFormat18n("system.selfData"), value: "SelfData" }
];

/**
 * @description：性别
 */
export const genderValueList = [
	{ label: useFormat18n("system.male"), value: "男" },
	{ label: useFormat18n("system.female"), value: "女" }
];

/**
 * 商城
 */

/**
 * 抽奖奖项
 */
export const prizeTypeList = [
	{ label: "商品", value: "Goods" },
	{ label: "现金", value: "Cash" }
];

// 支付方式
export const payTypeList = [
	{
		label: "微信支付",
		value: "WECHAT"
	},
	{
		label: "余额支付",
		value: "ACCOUNT"
	}
];

/**
 * 商城订单状态
 */
export const mallOrderStatusList = [
	{
		label: "待支付",
		value: "WaitPay"
	},
	{
		label: "待发货",
		value: "PaySuccess"
	},
	{
		label: "待收货",
		value: "Shipped"
	},
	{
		label: "待评价",
		value: "WaitEvaluate"
	},
	{
		label: "已完成",
		value: "Completed"
	},
	{
		label: "部分退款",
		value: "PartRefund"
	},
	{
		label: "全部退款",
		value: "AllRefund"
	},
	{
		label: "已取消",
		value: "Cancel"
	}
];

// 服务订单状态
export const serviceOrderStatusList = [
	{
		label: "待接单",
		value: "WaitReceive"
	},
	{
		label: "进行中",
		value: "InProgress"
	},
	{
		label: "已完成",
		value: "Completed"
	},
	{
		label: "已打赏",
		value: "Reward"
	},
	{
		label: "已取消",
		value: "Cancel"
	}
];

/**
 * @description：商品反馈类型
 */
export const goodsFeedbackTypeList = [
	{ label: "保留商品", value: "Hold" },
	{ label: "下架商品", value: "OffLine" }
];

/**
 * @description：系统消息类型
 */
export const systemMessageTypeList = [
	{ label: "商城订单", value: "MallOrder" },
	{ label: "服务订单", value: "ServiceOrder" },
	{ label: "商品反馈", value: "GoodsFeedback" },
	{ label: "商品订单点赞", value: "MallOrderLike" },
	{ label: "服务订单点赞", value: "ServiceOrderLike" }
];
