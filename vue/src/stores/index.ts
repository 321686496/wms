import { defineStore, createPinia } from "pinia";
import md5 from "js-md5";
import { GlobalState, ThemeConfigProps, AssemblySizeType, QiniuTokenDataProps, RouteInfoProps } from "./interface";
import { DEFAULT_PRIMARY } from "@/config/config";
import { SysConfigProps } from "./interface";
import piniaPersistConfig from "@/config/piniaPersist";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

// defineStore 调用后返回一个函数，调用该函数获得 Store 实体
export const GlobalStore = defineStore({
	// id: 必须的，在所有 Store 中唯一
	id: "GlobalState",
	// state: 返回对象的函数
	state: (): GlobalState => ({
		// token
		token: "",
		// userInfo
		userInfo: {
			nickName: "",
			name: "",
			phone: "",
			roleKey: "",
			roleName: "",
			sex: "男",
			email: "",
			avatar: "",
			realName: "",
			tenantId: 0
		},
		smsSecond: 0, //短信验证码倒计时
		// element组件大小
		assemblySize: "default",
		// language
		language: "",
		langPackage: {
			th: {}
		},
		// themeConfig
		themeConfig: {
			// 当前页面是否全屏
			maximize: false,
			// 布局切换 ==>  纵向：vertical | 经典：classic | 横向：transverse | 分栏：columns
			layout: "vertical",
			// 默认 primary 主题颜色
			primary: DEFAULT_PRIMARY,
			// 深色模式
			isDark: false,
			// 灰色模式
			isGrey: false,
			// 色弱模式
			isWeak: false,
			// 折叠菜单
			isCollapse: true,
			// 面包屑导航
			breadcrumb: false,
			// 面包屑导航图标
			breadcrumbIcon: false,
			// 标签页
			tabs: false,
			// 标签页图标
			tabsIcon: true,
			// 页脚
			footer: true
		},
		// 系统配置
		sysConfig: {
			system_icon: "",
			system_icp: "",
			system_logo: "",
			system_name: "",
			system_shortName: "",
			system_regex_password: "",
			system_regex_phone: "",
			system_title: "",
			system_download_template_brand: "", // 基础资料-品牌导入模板url
			system_download_template_category: "", // 基础资料-分类导入模板url
			system_download_template_supplier: "", //基础资料-供应商导入模板url
			system_download_template_customer: "", //基础资料-客户导入模板url
			system_download_template_stock: "", //基础资料-仓库导入模板url
			system_download_template_location: "", //基础资料-货位模板url
			system_download_template_attribute: "", //基础资料-属性模板url
			system_download_template_material: "", //基础资料-物料模板url
			system_download_template_fee: "", //基础资料-收支项目模板url
			system_download_template_paytype: "", //基础资料-支付方式模板url
			system_download_template_item: "", //供应链-明细模板url
			system_download_template_bom: "", // 基础资料-BOM模板url
			system_upload_type: "location", // 文件上传类型
			system_upload_qiniu_domain: "", // 七牛云上传域名
			system_open_register: "false", //是否开启注册
			system_open_appointment: "false", // 是否开启预约
			system_customer_service_qrcode: "", //预约联系微信二维码
			system_translate_url_th: ""
		},
		// 商户配置
		tenantConfig: {
			id: 0,
			logo: "", //系统Logo
			title: "", //系统名称
			slogan: "", //子标题
			phone: "", //注册手机号
			isLogoff: false, //注销标识
			openShelves: false, // 是否开启货位
			openVerify: false, // 是否开启单据审核
			openVerifyBill: "", //启用审核的单据
			openVerifyBillList: [],
			openNegativeInventory: false, //是否开启负库存
			openDiscount: false, //是否开启优惠
			openTaxRate: false, //是否开启税率
			openSerialNumber: false, // 是否开启序列号
			openBatchNumber: false, // 是否开启批次号
			openDirectOutstock: false, // 是否直接出库
			openConsumablesOutstock: false, // 是否开启耗材出库
			allowPurchaseInstockBeyond: false, //采购入库是否允许超入
			allowSaleOutstockBeyond: false, //销售出库是否允许超出
			numberRetainsSmallDigit: 3, // 数量小数位
			amountRetainsSmallDigit: 2, // 金额小数位
			defaultPageSize: 10, // 表格默认页码尺寸
			allowProductionInstockBeyond: false, //是否允许生产订单超出
			allowProductionClaimBeyond: false, // 是否允许领工超出
			hasPictureSearchModule: false, // 是否有以图识物模块
			pictureSearchType: "NotSelected", // 搜索资源类型
			pictureSearchSimilarity: 0.7, //以图识物相似度过滤(0~1)
			openEntrustMachining: false, // 开启受托加工
			openSaleOrderBomSelect: false,
			moduleListStr: "",
			defaultDateSearch: 7,
			dispatchAutoAudit: false, // 领工派工自动审核
			applyRangeSupplier: false,
			applyRangeCustomer: false,
			applyRangeMaterial: false,
			applyRangeStock: false,
			completionNeedAllPickOutstock: false,
			packageResultLimitSubmit: false,
			autoOpenFlow: true,
			allowPurchaseInstockAdditionalNumber: false,
			allowSaleOutstockAdditionalNumber: false,
			searchCache: false,
			openBomStatistics: false
		},
		// 七牛云token数据
		qiniuTokenData: {
			domain: "",
			expired: 0,
			token: ""
		},
		// 路由拦截路径
		routeInfo: {
			fromPath: "",
			toPath: ""
		}
	}),
	getters: {
		tenantConfigGet: state => state.tenantConfig
	},
	actions: {
		// setToken
		setToken(token: string) {
			this.token = token ? md5(token) : "";
		},
		setUserInfo(userInfo: any) {
			this.userInfo = userInfo;
		},
		// set 租户信息
		setTenantInfo(tenantInfo: any) {
			this.tenantConfig = tenantInfo;
		},
		setSmsSecond(smsSecond: number) {
			this.smsSecond = smsSecond;
		},
		setSysConfig(sysConfig: SysConfigProps) {
			this.sysConfig = sysConfig;
		},
		// setAssemblySizeSize
		setAssemblySizeSize(assemblySize: AssemblySizeType) {
			this.assemblySize = assemblySize;
		},
		// updateLanguage
		updateLanguage(language: string) {
			this.language = language;
		},
		updateLangPackage(langName: string, langValue: any) {
			this.langPackage[langName] = langValue;
		},
		// setThemeConfig
		setThemeConfig(themeConfig: ThemeConfigProps) {
			this.themeConfig = themeConfig;
		},
		setQiniuToken(qiniuTokenData: QiniuTokenDataProps) {
			this.qiniuTokenData = qiniuTokenData;
		},
		setRouteInfo(routeInfo: RouteInfoProps) {
			this.routeInfo = routeInfo;
		}
	},
	persist: piniaPersistConfig("GlobalState")
});

// piniaPersist(持久化)
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

export default pinia;
