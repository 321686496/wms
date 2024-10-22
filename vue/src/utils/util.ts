import { isArray } from "@/utils/is";
import useFormat18n from "@/hooks/useFormat18n";
import useTenantConfig from "@/hooks/useTenantConfig";
import dayjs from "dayjs";

/**
 * @description 获取localStorage
 * @param {String} key Storage名称
 * @return string
 */
export function localGet(key: string) {
	const value = window.localStorage.getItem(key);
	try {
		return JSON.parse(window.localStorage.getItem(key) as string);
	} catch (error) {
		return value;
	}
}

/**
 * @description 存储localStorage
 * @param {String} key Storage名称
 * @param {Any} value Storage值
 * @return void
 */
export function localSet(key: string, value: any) {
	window.localStorage.setItem(key, JSON.stringify(value));
}

/**
 * @description 清除localStorage
 * @param {String} key Storage名称
 * @return void
 */
export function localRemove(key: string) {
	window.localStorage.removeItem(key);
}

/**
 * @description 清除所有localStorage
 * @return void
 */
export function localClear() {
	window.localStorage.clear();
}

/**
 * @description 判断数据类型
 * @param {Any} val 需要判断类型的数据
 * @return string
 */
export function isType(val: any) {
	if (val === null) return "null";
	if (typeof val !== "object") return typeof val;
	else return Object.prototype.toString.call(val).slice(8, -1).toLocaleLowerCase();
}

/**
 * @description 生成唯一 uuid
 * @return string
 */
export function generateUUID() {
	if (typeof crypto === "object") {
		if (typeof crypto.randomUUID === "function") {
			return crypto.randomUUID();
		}
		if (typeof crypto.getRandomValues === "function" && typeof Uint8Array === "function") {
			const callback = (c: any) => {
				const num = Number(c);
				return (num ^ (crypto.getRandomValues(new Uint8Array(1))[0] & (15 >> (num / 4)))).toString(16);
			};
			return "10000000-1000-4000-8000-100000000000".replace(/[018]/g, callback);
		}
	}
	let timestamp = new Date().getTime();
	let performanceNow = (typeof performance !== "undefined" && performance.now && performance.now() * 1000) || 0;
	return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, c => {
		let random = Math.random() * 16;
		if (timestamp > 0) {
			random = (timestamp + random) % 16 | 0;
			timestamp = Math.floor(timestamp / 16);
		} else {
			random = (performanceNow + random) % 16 | 0;
			performanceNow = Math.floor(performanceNow / 16);
		}
		return (c === "x" ? random : (random & 0x3) | 0x8).toString(16);
	});
}

/**
 * 判断两个对象是否相同
 * @param a 要比较的对象一
 * @param b 要比较的对象二
 * @returns 相同返回 true，反之则反
 */
export function isObjectValueEqual(a: { [key: string]: any }, b: { [key: string]: any }) {
	if (!a || !b) return false;
	let aProps = Object.getOwnPropertyNames(a);
	let bProps = Object.getOwnPropertyNames(b);
	if (aProps.length != bProps.length) return false;
	for (let i = 0; i < aProps.length; i++) {
		let propName = aProps[i];
		let propA = a[propName];
		let propB = b[propName];
		if (!b.hasOwnProperty(propName)) return false;
		if (propA instanceof Object) {
			if (!isObjectValueEqual(propA, propB)) return false;
		} else if (propA !== propB) {
			return false;
		}
	}
	return true;
}

/**
 * @description 生成随机数
 * @param {Number} min 最小值
 * @param {Number} max 最大值
 * @return number
 */
export function randomNum(min: number, max: number): number {
	let num = Math.floor(Math.random() * (min - max) + max);
	return num;
}

/**
 * @description 获取当前时间对应的提示语
 * @return string
 */
export function getTimeState() {
	// 获取当前时间
	let timeNow = new Date();
	// 获取当前小时
	let hours = timeNow.getHours();
	// 判断当前时间段
	if (hours >= 6 && hours <= 10) return `早上好 ⛅`;
	if (hours >= 10 && hours <= 14) return `中午好 🌞`;
	if (hours >= 14 && hours <= 18) return `下午好 🌞`;
	if (hours >= 18 && hours <= 24) return `晚上好 🌛`;
	if (hours >= 0 && hours <= 6) return `凌晨好 🌛`;
}

/**
 * @description 获取浏览器默认语言
 * @return string
 */
export function getBrowserLang() {
	let browserLang = navigator.language ? navigator.language : navigator.browserLanguage;
	let defaultBrowserLang = "";
	if (browserLang.toLowerCase() === "cn" || browserLang.toLowerCase() === "zh" || browserLang.toLowerCase() === "zh-cn") {
		defaultBrowserLang = "zh";
	} else {
		defaultBrowserLang = "en";
	}
	return defaultBrowserLang;
}

/**
 * 处理已授权菜单数据，将数据拆分出菜单及按钮两部分
 */
export const handleAuthorizedMenuData = (dataList: any[]): any => {
	let authButtonList: any[] = [];
	const formatMenuData = (list: any[]) => {
		let targetList: any[] = [];
		list.forEach((menuRow: any) => {
			if (menuRow.menuType.includes("Button")) {
				authButtonList.push(menuRow.name);
			} else {
				authButtonList.push(menuRow.name);
				let newMenuRow: any = {
					id: menuRow.id,
					path: menuRow.path,
					name: menuRow.name,
					redirect: menuRow.redirect,
					component: menuRow.component,
					meta: {
						icon: menuRow.metaIcon,
						title: useFormat18n(`menu.${menuRow.name}`),
						activeMenu: menuRow.metaActiveMenu,
						isLink: menuRow.metaIsLink,
						isHide: menuRow.metaIsHide,
						isFull: menuRow.metaIsFull,
						isAffix: menuRow.metaIsAffix,
						isKeepAlive: menuRow.metaIsKeepAlive
					}
				};
				if (menuRow.children && menuRow.children.length) {
					newMenuRow.children = formatMenuData(menuRow.children);
				}
				targetList.push(newMenuRow);
			}
		});
		return targetList;
	};

	let authMenuList = formatMenuData(dataList);
	return {
		authButtonList,
		authMenuList
	};
};

/**
 * @description 递归查询当前路由所对应的路由
 * @param {Array} menuList 所有菜单列表
 * @param {String} path 当前访问地址
 * @return array
 */
export function filterCurrentRoute(menuList: Menu.MenuOptions[], path: string) {
	let result = {};
	for (let item of menuList) {
		if (item.path === path) return item;
		if (item.children) {
			const res = filterCurrentRoute(item.children, path);
			if (Object.keys(res).length) result = res;
		}
	}
	return result;
}

/**
 * @description 扁平化数组对象(主要用来处理路由菜单)
 * @param {Array} menuList 所有菜单列表
 * @return array
 */
/*
export function getFlatArr(menuList: Menu.MenuOptions[]) {
	let newMenuList: Menu.MenuOptions[] = JSON.parse(JSON.stringify(menuList));
	return newMenuList.reduce((pre: Menu.MenuOptions[], current: Menu.MenuOptions) => {
		let flatArr = [...pre, current];
		if (current.children) flatArr = [...flatArr, ...getFlatArr(current.children)];
		return flatArr;
	}, []);
}
*/
export function getFlatArr(menuList: any[]) {
	let newMenuList: any[] = JSON.parse(JSON.stringify(menuList));
	return newMenuList.reduce((pre: any[], current: any) => {
		let flatArr = [...pre, current];
		if (current.children) flatArr = [...flatArr, ...getFlatArr(current.children)];
		return flatArr;
	}, []);
}

/**
 * @description 使用递归，过滤需要缓存的路由（暂时没有使用）
 * @param {Array} menuList 所有菜单列表
 * @param {Array} cacheArr 缓存的路由菜单 name ['**','**']
 * @return array
 * */
export function getKeepAliveRouterName(menuList: Menu.MenuOptions[], keepAliveArr: string[] = []) {
	menuList.forEach(item => {
		item.meta.isKeepAlive && item.name && keepAliveArr.push(item.name);
		item.children?.length && getKeepAliveRouterName(item.children, keepAliveArr);
	});
	return keepAliveArr;
}

/**
 * @description 使用递归，过滤出需要渲染在左侧菜单的列表（剔除 isHide == true 的菜单）
 * @param {Array} menuList 所有菜单列表
 * @return array
 * */
export function getShowMenuList(menuList: Menu.MenuOptions[]) {
	let newMenuList: Menu.MenuOptions[] = JSON.parse(JSON.stringify(menuList));
	return newMenuList.filter(item => {
		item.children?.length && (item.children = getShowMenuList(item.children));
		return !item.meta?.isHide;
	});
}

/**
 * @description 使用递归处理路由菜单 path，生成一维数组(第一版本地路由鉴权会用到)
 * @param {Array} menuList 所有菜单列表
 * @param {Array} menuPathArr 菜单地址的一维数组 ['**','**']
 * @return array
 */
export function getMenuListPath(menuList: Menu.MenuOptions[], menuPathArr: string[] = []) {
	menuList.forEach((item: Menu.MenuOptions) => {
		typeof item === "object" && item.path && menuPathArr.push(item.path);
		item.children?.length && getMenuListPath(item.children, menuPathArr);
	});
	return menuPathArr;
}

/**
 * @description 递归找出所有面包屑存储到 pinia/vuex 中
 * @param {Array} menuList 所有菜单列表
 * @param {Object} result 输出的结果
 * @param {Array} parent 父级菜单
 * @returns object
 */
export const getAllBreadcrumbList = (menuList: Menu.MenuOptions[], result: { [key: string]: any } = {}, parent = []) => {
	for (const item of menuList) {
		result[item.path] = [...parent, item];
		if (item.children) getAllBreadcrumbList(item.children, result, result[item.path]);
	}
	return result;
};

/**
 * @description 格式化表格单元格默认值(el-table-column)
 * @param {Number} row 行
 * @param {Number} col 列
 * @param {String} callValue 当前单元格值
 * @return string
 * */
export function defaultFormat(row: number, col: number, callValue: any) {
	// 如果当前值为数组,使用 / 拼接（根据需求自定义）
	if (isArray(callValue)) return callValue.length ? callValue.join(" / ") : "--";
	return callValue ?? "--";
}

/**
 * @description 处理无数据情况
 * @param {String} callValue 需要处理的值
 * @return string
 * */
export function formatValue(callValue: any) {
	// 如果当前值为数组,使用 / 拼接（根据需求自定义）
	if (isArray(callValue)) return callValue.length ? callValue.join(" / ") : "--";
	return callValue ?? "--";
}

/**
 * @description 处理 prop 为多级嵌套的情况(列如: prop:user.name)
 * @param {Object} row 当前行数据
 * @param {String} prop 当前 prop
 * @return any
 * */
export function handleRowAccordingToProp(row: { [key: string]: any }, prop: string) {
	if (!prop.includes(".")) return row[prop] ?? "--";
	prop.split(".").forEach(item => (row = row[item] ?? "--"));
	return row;
}

/**
 * @description 处理 prop，当 prop 为多级嵌套时 ==> 返回最后一级 prop
 * @param {String} prop 当前 prop
 * @return string
 * */
export function handleProp(prop: string) {
	const propArr = prop.split(".");
	if (propArr.length == 1) return prop;
	return propArr[propArr.length - 1];
}

/**
 * @description 根据枚举列表查询当需要的数据（如果指定了 label 和 value 的 key值，会自动识别格式化）
 * @param {String} callValue 当前单元格值
 * @param {Array} enumData 字典列表
 * @param {Array} fieldNames 指定 label && value 的 key 值
 * @param {String} type 过滤类型（目前只有 tag）
 * @return string
 * */
export function filterEnum(
	callValue: any,
	enumData: any[] | undefined,
	fieldNames?: { label: string; value: string },
	type?: string
): string {
	const value = fieldNames?.value ?? "value";
	const label = fieldNames?.label ?? "label";
	let filterData: { [key: string]: any } = {};
	if (Array.isArray(enumData)) filterData = enumData.find((item: any) => item[value] === callValue);
	if (type == "tag") return filterData?.tagType ? filterData.tagType : "";
	return filterData ? filterData[label] : "--";
}

/**
 * 文件对象转base64
 * @param {文件对象} file
 */
export const fileToBase64 = (file: any): Promise<string> => {
	return new Promise((resolve, reject) => {
		const reader = new FileReader();
		reader.addEventListener("loadend", (event: any) => {
			resolve(event.target.result);
		});
		reader.addEventListener("error", err => {
			reject(err);
		});
		reader.readAsDataURL(file);
	});
};

/**
 *  金额展示
 * @param value: number
 * @returns string
 */
export const money = (value: number) => {
	if (value) {
		return (value / 100).toFixed(2);
	} else {
		return `0.00`;
	}
};

/**
 * @description 数组化枚举
 * @param {object} enumData 枚举
 * @param {Array} fieldNames 指定 label && value 的 key 值
 * @return any[]
 * */
export function ArrayNumEnum(enumData: object, fieldNames?: { label: string; value: string }): any[] {
	const value = fieldNames?.value ?? "value";
	const label = fieldNames?.label ?? "label";
	return Object.keys(enumData)
		.filter(v => isNaN(Number(v)))
		.map(name => {
			return {
				[value]: enumData[name as keyof typeof enumData],
				[label]: name
			};
		});
}

/**
 * @description 数组化枚举
 * @param {object} enumData 枚举
 * @param {Array} fieldNames 指定 label && value 的 key 值
 * @return any[]
 * */
export function ArrayStringEnum(enumData: object, fieldNames?: { label: string; value: string }): any[] {
	const value = fieldNames?.value ?? "value";
	const label = fieldNames?.label ?? "label";
	return Object.keys(enumData).map(name => {
		return {
			[value]: enumData[name as keyof typeof enumData],
			[label]: name
		};
	});
}

/**
 * 转换下拉框label表格显示
 * @param value 下拉框值
 * @param options 选项
 * @param mode 类型（单选、多选）
 * @returns
 */
export const formatSelectLabel = (
	value: number | string | number[],
	options: treeOption[],
	mode: "single" | "multiple" = "single"
): string => {
	const singleFormatLabel = (val: number | string): string => {
		let option = options.find((row: treeOption) => row.value == val);
		return option ? option.label : "";
	};
	if (mode === "single" && !Array.isArray(value)) {
		return singleFormatLabel(value);
	}
	if (mode === "multiple" && Array.isArray(value)) {
		return value.map((val: number | string) => singleFormatLabel(val)).join("、");
	}
	return "";
};

/**
 * 下拉框value字符串转数字类型
 * valueTransInt
 *  @param list 下拉框选项
 */
export const valueTransInt = (list: any[], labelName: string = "label", valueName: string = "value") => {
	list.forEach((row: any) => {
		row.label = row[labelName];
		row.value = row[valueName];
		if (!isNaN(row.value - 0)) {
			row.value = row.value - 0;
		}
		if (row.children && row.children.length) {
			row.children = valueTransInt(row.children);
		}
	});
	return list;
};

/**
 * 过滤展示物料业务属性
 * @param rowData 物料行数据
 */
export const formatMaterialBusinessType = (rowData: any, materialBusinessTypeList: any[]): string => {
	let result: string = "";
	let typeValueList = materialBusinessTypeList.map((typeRow: any) => typeRow.value);
	typeValueList.forEach((typeVal: string) => {
		if (rowData[typeVal]) {
			const item: any = materialBusinessTypeList.find(row => row.value === typeVal);
			if (item && item.label) {
				result += `、${item.label}`;
			}
		}
	});
	if (result) {
		result = result.substring(1);
	}
	return result || "--";
};

/**
 *
 * @param type 根据来源单据类型转换跳转的url
 * @returns
 */
export const formatSourceBillUrl = (type: string) => {
	let obj = {
		InstockOther: "/stock/otherIn/detail",
		InstockOtherAbandon: "/stock/otherIn/detail",
		OutstockOther: "/stock/otherOut/detail",
		OutstockOtherAbandon: "/stock/otherOut/detail",
		StockTransfer: "/stock/transfer/detail",
		StockTransferOutstock: "/stock/transferOut/detail",
		StockTransferInstock: "/stock/transferIn/detail",
		StockCheck: "/stock/check/detail",
		PurchaseOrder: "/purchase/purchaseOrder/detail",
		PurchaseInstock: "/purchase/purchaseIn/detail",
		PurchaseInstockAbandon: "/purchase/purchaseIn/detail",
		PurchaseRefund: "/purchase/purchaseReturn/detail",
		PurchaseRefundOutstock: "/purchase/returnOut/detail",
		PurchaseRefundOutstockAbandon: "/purchase/returnOut/detail",
		PurchaseRefundInstock: "/purchase/exchangeIn/detail",
		SaleQuotation: "/sale/saleQuotation/detail",
		SaleOrder: "/sale/saleOrder/detail",
		SaleOutstock: "/sale/saleOut/detail",
		SaleOutstockAbandon: "/sale/saleOut/detail",
		SaleShip: "/sale/slip/detail",
		SaleRefund: "/sale/saleReturn/detail",
		SaleRefundInstock: "/sale/returnIn/detail",
		SaleRefundInstockAbandon: "/sale/returnIn/detail",
		SaleRefundOutstock: "/sale/exchangeOut/detail",
		CashPayment: "/cash/payment/detail",
		CashReceipt: "/cash/receipt/detail",
		CashFee: "/cash/fee/detail",
		CashIncome: "/cash/income/detail",
		Mrp: "/production/mrp/detail",
		MrpDemand: "/production/demand/detail",
		ProductionOrder: "/production/order/detail",
		ProductionPickOutstock: "/production/pickout/detail",
		ProductionReturnInstock: "/production/returnin/detail",
		ProcedureWork: "/production/execution/detail",
		ProcedureReport: "/production/report/detail",
		ProductionProductInspection: "/production/finishedInspection/detail",
		ProductionProcedureInspection: "/production/processInspection/detail",
		ProductionCompleteInstock: "/production/wipCompletion/detail",
		OutsourceOrder: "/production/outsource/detail",
		OutsourceCompleteInstock: "/production/outsourceWipCompletion/detail",
		OutsourceRefund: "/production/outsourceReturn/detail",
		OutsourceRefundOutstock: "/production/outsourceReturnOut/detail",
		OutsourceRefundInstock: "/production/outsourceExchangeIn/detail",
		EquipmentMaintenance: "/production/maintenanceOrder/detail",
		ProductionCostAccounting: "/production/costAccounting/detail",
		ProductionInstockEntrust: "/production/entrustIn/detail",
		CashInvoiceSale: "/cash/outputInvoice/detail",
		CashInvoicePurchase: "/cash/inputInvoice/detail",
		PurchaseInspection: "/quality/incoming/detail",
		PurchaseInspectionReport: "/quality/incomingReport/detail",
		InspectionSale: "/quality/shipment/detail",
		InspectionSaleReport: "/quality/shipmentReport/detail",
		InspectionPackage: "/quality/boxSticker/detail",
		InspectionProcessReport: "/quality/routingReport/detail",
		InspectionFirstReport: "/quality/firstReport/detail",
		PurchaseRequisition: "/purchase/purchaseRequisition/detail",
		StockLend: "/stock/lend/detail",
		StockReturn: "/stock/revert/detail",
		ProductionOutstockEntrust: "/production/entrustReturn/detail",
		ProductionWasteInstock: "/production/wasteIn/detail"
	};
	return obj[type];
};

/**
 * 获取24小时制的小时分钟
 */
export const getHourData = (): string[] => {
	let result: string[] = [];

	const initMinute = () => {
		let minutes = [];
		for (let i = 0; i < 60; i++) {
			minutes.push(i < 10 ? `0${i}` : `${i}`);
		}
		return minutes;
	};

	for (let j = 0; j < 24; j++) {
		const minutes = initMinute();
		let hour = j < 10 ? `0${j}` : `${j}`;
		result = [...result, ...minutes.map(minute => `${hour}:${minute}`)];
	}
	return result;
};

/**
 * 初始化日期选择器组件的快捷选项
 */
export const initDatePickerShortcuts = () => {
	const { tenantConfig } = useTenantConfig();
	const searchDays = tenantConfig.defaultDateSearch;

	const todayDate = dayjs(new Date()).format("YYYY-MM-DD");
	const lastSearchDay = dayjs(todayDate).subtract(searchDays, "day").format("YYYY-MM-DD");
	const last1Date = dayjs(todayDate).subtract(1, "day").format("YYYY-MM-DD");
	const last7Date = dayjs(todayDate).subtract(7, "day").format("YYYY-MM-DD");
	const last30Date = dayjs(todayDate).subtract(30, "day").format("YYYY-MM-DD");
	const lastMonthStartDate = dayjs(todayDate).subtract(1, "month").startOf("month").format("YYYY-MM-DD");
	const lastMonthEndDate = dayjs(todayDate).subtract(1, "month").endOf("month").format("YYYY-MM-DD");
	const thisYearStartDate = dayjs(todayDate).startOf("year").format("YYYY-MM-DD");
	const thisYear = parseInt(todayDate.split("-")[0]);
	const lastYear = thisYear - 1;
	const lastYearStartDate = dayjs(`${lastYear}-01-01`).startOf("year").format("YYYY-MM-DD");
	const lastYearEndDate = dayjs(`${lastYear}-01-01`).endOf("year").format("YYYY-MM-DD");
	return {
		datePickerShortcuts: [
			{
				text: useFormat18n("system.today"),
				value: [todayDate, todayDate]
			},
			{
				text: useFormat18n("system.yesterday"),
				value: [last1Date, last1Date]
			},
			{
				text: useFormat18n("system.last7Days"),
				value: [last7Date, todayDate]
			},
			{
				text: useFormat18n("system.last30Days"),
				value: [last30Date, todayDate]
			},
			{
				text: useFormat18n("system.lastMonth"),
				value: [lastMonthStartDate, lastMonthEndDate]
			},
			{
				text: useFormat18n("system.thisYear"),
				value: [thisYearStartDate, todayDate]
			},
			{
				text: useFormat18n("system.lastYear"),
				value: [lastYearStartDate, lastYearEndDate]
			}
		],
		defaultDatePickerShort: [lastSearchDay, todayDate]
	};
};

/**
 * 利用正则截取字符串
 */
export const extractStringBetweenBackticks = (str: string): string[] => {
	let regex;
	if (import.meta.env.MODE === "development") {
		regex = /`([^`]*)`/g;
	} else {
		regex = /"(.*?)"/g;
	}
	let match;
	const matches = [];
	while ((match = regex.exec(str)) !== null) {
		matches.push(match[1]);
	}
	return matches;
};
