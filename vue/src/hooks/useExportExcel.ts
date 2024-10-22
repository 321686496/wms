import { ElNotification } from "element-plus";
import table2excel from "js-table2excel";

/**
 * @description 前端请求数据并导出excel
 * @param {Function} api 导出表格的api方法(必传)
 * @param {String} fileName 导出的文件名(必传)
 * @param {Object} params 导出的参数(默认为空对象)
 * @param {Object} columns 导出的列配置
 * @param {Boolean} isNotify 是否有导出消息提示(默认为 true)
 * @return void
 * */
export const useExportExcel = async (
	api: (param: any) => Promise<any>,
	fileName: string,
	params: any = {},
	columns: any[] = [],
	handleData: (param: any) => any,
	isNotify: boolean = true
) => {
	if (isNotify) {
		ElNotification({
			title: "温馨提示",
			message: "如果数据庞大会导致下载缓慢哦，请您耐心等待！",
			type: "info",
			duration: 3000
		});
	}
	try {
		const res = await api(params);
		if (res.rows && res.rows.length) {
			let dataList = handleData ? handleData(res.rows) : res.rows;
			table2excel(columns, dataList, `${fileName}.xlsx`);
		}
	} catch (error) {}
};
