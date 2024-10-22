import { TABLE_SEARCH_CACHE } from "@/config/config";

export const setTableSearchCache = (colSettingType: string, data: any) => {
	if (colSettingType) {
		let param: any = window.localStorage.getItem(TABLE_SEARCH_CACHE) || "{}";
		param = JSON.parse(param);
		param[colSettingType] = data;
		window.localStorage.setItem(TABLE_SEARCH_CACHE, JSON.stringify(param));
	}
};

export const getTableSearchCache = (colSettingType: string) => {
	let param: any = window.localStorage.getItem(TABLE_SEARCH_CACHE) || "{}";
	param = JSON.parse(param);
	const result = { ...(param[colSettingType] || {}) };
	delete param[colSettingType];
	window.localStorage.setItem(TABLE_SEARCH_CACHE, JSON.stringify(param));
	return result;
};

export const removeTableSearchCache = (colSettingType: string | string[]) => {
	let param: any = window.localStorage.getItem(TABLE_SEARCH_CACHE) || "{}";
	param = JSON.parse(param);
	if (Array.isArray(colSettingType)) {
		for (let col of colSettingType) {
			delete param[col];
		}
	} else {
		delete param[colSettingType];
	}
	window.localStorage.setItem(TABLE_SEARCH_CACHE, JSON.stringify(param));
};
