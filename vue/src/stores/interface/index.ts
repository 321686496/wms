/* GlobalState */
export interface GlobalState {
	token: string;
	userInfo: any;
	assemblySize: AssemblySizeType;
	language: string;
	langPackage: any;
	themeConfig: ThemeConfigProps;
	smsSecond: number;
	sysConfig: SysConfigProps;
	tenantConfig: TenantConfigProps;
	qiniuTokenData: QiniuTokenDataProps;
	routeInfo: RouteInfoProps;
}

/* themeConfigProp */
export interface ThemeConfigProps {
	maximize: boolean;
	layout: LayoutType;
	primary: string;
	isDark: boolean;
	isGrey: boolean;
	isCollapse: boolean;
	isWeak: boolean;
	breadcrumb: boolean;
	breadcrumbIcon: boolean;
	tabs: boolean;
	tabsIcon: boolean;
	footer: boolean;
}

export interface SysConfigProps {
	system_icon?: string; //系统icon
	system_icp: string; //系统底部icp备案号
	system_logo?: string; //系统logo
	system_name: string; //系统全称
	system_shortName: string; //系统简称
	system_regex_password: string; //密码正则
	system_regex_phone: string; //手机号正则
	system_title: string; //系统标题
	system_download_template_brand: string; // 基础资料-品牌导入模板url
	system_download_template_category: string; // 基础资料-分类导入模板url
	system_download_template_supplier: string; // 基础资料-供应商导入模板url
	system_download_template_customer: string; // 基础资料-客户导入模板url
	system_download_template_stock: string; //基础资料-仓库模板url
	system_download_template_location: string; //基础资料-货位模板url
	system_download_template_attribute: string; //基础资料-属性模板url
	system_download_template_material: string; //基础资料-物料模板url
	system_download_template_fee: string; //基础资料-收支项目模板url
	system_download_template_paytype: string; //基础资料-支付方式模板url
	system_download_template_item: string; //供应链-明细模板url
	system_download_template_bom: string; //基础资料-BOM模板url
	system_upload_type: "location" | "qiniuyun"; // 文件上传类型
	system_upload_qiniu_domain: string; // 七牛云域名
	system_open_register: "true" | "false"; //是否开启注册
	system_open_appointment: "true" | "false"; //是否开启预约
	system_customer_service_qrcode: string; //预约联系微信二维码
	system_translate_url_th: string;
}

export interface TenantConfigProps {
	id?: number;
	logo?: string; //系统Logo
	title?: string; //系统名称
	slogan?: string; //子标题
	phone?: string; //注册手机号
	isLogoff: boolean; //注销标识
	openShelves: boolean; // 是否开启货位
	openVerify: boolean; // 是否开启单据审核
	openVerifyBill: string; //启用审核的单据
	openVerifyBillList: string[];
	openNegativeInventory: boolean; //是否开启负库存
	openDiscount: boolean; //是否开启优惠
	openTaxRate: boolean; //是否开启税率
	openSerialNumber: boolean; // 是否开启序列号
	openBatchNumber: boolean; // 是否开启批次号
	openDirectOutstock: boolean; // 是否开启直接出库
	openConsumablesOutstock: boolean; // 是否开启耗材出库
	allowPurchaseInstockBeyond: boolean; //采购入库是否允许超入
	allowSaleOutstockBeyond: boolean; //销售出库是否允许超出
	numberRetainsSmallDigit: number; //数量小数位
	amountRetainsSmallDigit: number; //金额小数位
	defaultPageSize: number; // 表格页码尺寸
	allowProductionInstockBeyond: boolean; //是否允许生产订单超出
	allowProductionClaimBeyond: boolean; // 是否允许领工超出
	hasPictureSearchModule: boolean; //是否有以图识物模块
	pictureSearchType: "NotSelected" | "Similarity" | "Product"; // 搜索资源类型
	pictureSearchSimilarity: number; //相似度
	openEntrustMachining: boolean; // 开启受托加工
	openSaleOrderBomSelect: boolean; // 开启销售订单BOM
	moduleListStr: string;
	defaultDateSearch: number;
	dispatchAutoAudit: boolean;
	applyRangeSupplier: boolean;
	applyRangeCustomer: boolean;
	applyRangeMaterial: boolean;
	applyRangeStock: boolean;
	completionNeedAllPickOutstock: boolean;
	packageResultLimitSubmit: boolean;
	autoOpenFlow: boolean;
	allowPurchaseInstockAdditionalNumber: boolean;
	allowSaleOutstockAdditionalNumber: boolean;
	searchCache: boolean;
	openBomStatistics: boolean;
}

export interface QiniuTokenDataProps {
	domain: string;
	expired: number;
	token: string;
}

export interface RouteInfoProps {
	fromPath: string;
	toPath: string;
}

export type AssemblySizeType = "default" | "small" | "large";

export type LayoutType = "vertical" | "classic" | "transverse" | "columns";

/* tabsMenuProps */
export interface TabsMenuProps {
	icon: string;
	title: string;
	path: string;
	name: string;
	close: boolean;
}

/* TabsState */
export interface TabsState {
	tabsMenuList: TabsMenuProps[];
}

/* AuthState */
export interface AuthState {
	routeName: string;
	authButtonList: {
		[key: string]: string[];
	};
	authMenuList: Menu.MenuOptions[];
}

/* keepAliveState */
export interface keepAliveState {
	keepAliveName: string[];
}
