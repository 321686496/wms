import { ElNotification, ElMessageBox } from "element-plus";
import useFormat18n from "@/hooks/useFormat18n";

/**
 * @description 接收数据流生成blob，创建链接，下载文件
 * @param {Function} api 导出表格的api方法(必传)
 * @param {String} tempName 导出的文件名(必传)
 * @param {Object} params 导出的参数(默认为空对象)
 * @param {Boolean} isNotify 是否有导出消息提示(默认为 true)
 * @param {String} fileType 导出的文件格式(默认为.xlsx)
 * @return void
 * */
export const useDownload = async (
	api: (param: any) => Promise<any>,
	tempName: string,
	params: any = {},
	isNotify: boolean = true,
	fileType: string = ".xls"
) => {
	if (isNotify) {
		ElNotification({
			title: useFormat18n("common.tips"),
			message: useFormat18n("common.downloadTipsPleaseWait"),
			type: "info",
			duration: 3000
		});
	}
	try {
		const res = await api(params);
		const blob = new Blob([res.data]);
		// 兼容 edge 不支持 createObjectURL 方法
		if ("msSaveOrOpenBlob" in navigator) return window.navigator.msSaveOrOpenBlob(blob, tempName + fileType);
		const blobUrl = window.URL.createObjectURL(blob);
		const exportFile = document.createElement("a");
		exportFile.style.display = "none";
		exportFile.download = `${tempName}${fileType}`;
		exportFile.href = blobUrl;
		document.body.appendChild(exportFile);
		exportFile.click();
		// 去除下载对 url 的影响
		document.body.removeChild(exportFile);
		window.URL.revokeObjectURL(blobUrl);
	} catch (error) {}
};

/**
 * 导出excel
 */
export const useExportExcel = (api: (param: any) => Promise<any>, tempName: string, params: any = {}) => {
	const excelParams: any = {};
	for (let key in params) {
		// * 某些情况下参数为 false/0 也应该携带参数
		if (params[key] || params[key] === false || params[key] === 0) {
			excelParams[key] = params[key];
		}
		// 处理数组类型的查询条件，转为逗号分割
		if (Array.isArray(params[key])) {
			const valArr = params[key];
			const strVal = valArr.join(",");
			excelParams[key] = strVal;
		}
		// 处理单据时间筛选
		if (key === "start" && excelParams[key]) {
			const timeArr = excelParams[key].split(",");
			if (timeArr && timeArr.length == 2) {
				excelParams.start = timeArr[0];
				excelParams.end = timeArr[1];
			}
		}
		// 处理包含与不包含
		if (typeof params[key] == "string" && params[key].indexOf("mode=in") !== -1) {
			excelParams[key] = params[key].split("=")[2];
		}
		if (typeof params[key] == "string" && params[key].indexOf("mode=notIn") !== -1) {
			let upperKey = `${key.charAt(0).toUpperCase()}${key.slice(1)}`;
			excelParams[`notIn${upperKey}`] = params[key].split("=")[2];
			delete excelParams[key];
		}
	}
	ElMessageBox.confirm(useFormat18n("common.confirmExport"), useFormat18n("common.tips"), { type: "warning" }).then(() =>
		useDownload(api, tempName, excelParams)
	);
};
