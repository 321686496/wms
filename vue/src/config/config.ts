// ? 全局不动配置项 只做导出不做修改

// * 首页地址（默认）
export const HOME_URL: string = "/home/index";

// * 登录页地址（默认）
export const LOGIN_URL: string = "/login";

// * 单据丢失页地址（默认）
export const BILL_404_URL: string = "/home/notfound/index";

// * 默认主题颜色
export const DEFAULT_PRIMARY: string = "#409EFF";

// * 路由白名单地址（必须是本地存在的路由 staticRouter.ts）
export const ROUTER_WHITE_LIST: string[] = ["/500"];

// * 高德地图 key
export const AMAP_MAP_KEY: string = "";

// * 高德地图 秘钥
export const AMAP_MAP_SERCRET: string = "";

// * 百度地图 key
export const BAIDU_MAP_KEY: string = "";

// 新增单据表达响应式尺寸
export const ADD_FORM_SIZE = { xl: 4, lg: 6, md: 12, sm: 12, xs: 24 };

// 表格搜索表单缓存
export const TABLE_SEARCH_CACHE = "p8_table_search_cache";

// 搜索表单缓存标识黑名单
export const TABLE_CACHE_TYPE_BLACK_LIST = ["sku_dialog"];

// 附加属性字段
export const ATTR_FIELDS = ["length", "width", "height"];
