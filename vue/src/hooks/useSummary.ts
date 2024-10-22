import useTenantConfig from "@/hooks/useTenantConfig";

/**
 *
 * @param itemList 数据列表
 * @param field 数量字段
 * @returns 合计的数量（保留3位小数）
 */
export const summaryNumber = <T>(itemList: T[] = [], field: string): number => {
	const { tenantConfig } = useTenantConfig();
	let result = itemList.reduce((pre, cur) => {
		return pre + parseFloat(cur[field] || 0);
	}, 0);
	return parseFloat(result.toFixed(tenantConfig.numberRetainsSmallDigit));
};

/**
 * 合计小计金额
 * @param itemList 数据列表
 * @param numField 数量字段
 * @param unitPriceField 金额字段
 * @returns  合计金额（保留2位小数）
 */
export const summaryPriceTotal = <T>(itemList: T[] = [], numField: string, unitPriceField: string): number => {
	const { tenantConfig } = useTenantConfig();
	let result = itemList.reduce((pre, cur) => {
		return pre + parseFloat(cur[numField] || 0) * parseFloat(cur[unitPriceField] || 0);
	}, 0);
	return parseFloat(result.toFixed(tenantConfig.amountRetainsSmallDigit));
};

/**
 *
 * @param itemList 数据列表
 * @param field 金额字段
 * @returns 合计金额（保留2位小数）
 */
export const summaryTaxPriceTotal = <T>(itemList: T[] = [], field: string): number => {
	const { tenantConfig } = useTenantConfig();
	let result = itemList.reduce((pre, cur) => {
		return pre + parseFloat(cur[field] || 0);
	}, 0);
	return parseFloat(result.toFixed(tenantConfig.amountRetainsSmallDigit));
};
