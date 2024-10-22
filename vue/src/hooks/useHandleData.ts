import { ElMessageBox, ElMessage } from "element-plus";
import { HandleData } from "./interface";
import useFormat18n from "@/hooks/useFormat18n";

/**
 * @description 操作单条数据信息(二次确认【删除、禁用、启用、重置密码】)
 * @param {Function} api 操作数据接口的api方法(必传)
 * @param {Object} params 携带的操作数据参数 {id,params}(必传)
 * @param {String} message 提示信息(必传)
 * @param {String} confirmType icon类型(不必传,默认为 warning)
 * @return Promise
 */
export const useHandleData = <P = any, R = any>(
	api: (params: P) => Promise<R>,
	params: Parameters<typeof api>[0],
	message: string,
	confirmType: HandleData.MessageType = "warning"
) => {
	return new Promise((resolve, reject) => {
		ElMessageBox.confirm(`${useFormat18n("common.whetherOrNot")}${message}?`, useFormat18n("common.tips"), {
			confirmButtonText: useFormat18n("common.confirm"),
			cancelButtonText: useFormat18n("common.cancel"),
			type: confirmType,
			draggable: true
		})
			.then(async () => {
				const res = await api(params);
				if (!res) reject(false);
				ElMessage({
					type: "success",
					message: `${message}${useFormat18n("common.success")}!`
				});
				resolve(res);
			})
			.catch(err => {
				if (err !== "cancel") {
					reject(err);
				}
			});
	});
};

/**
 * @description 操作单条数据信息(二次确认【删除、禁用、启用、重置密码】)
 * @param {Function} api 操作数据接口的api方法(必传)
 * @param {Object} params 携带的操作数据参数 {id,params}(必传)
 * @param {String} message 提示信息(必传)
 * @param {String} confirmType icon类型(不必传,默认为 warning)
 * @return Promise
 */
export const usePromptData = <P = any, R = any>(
	api: (params: P) => Promise<R>,
	params: Parameters<typeof api>[0],
	message: string
) => {
	return new Promise((resolve, reject) => {
		ElMessageBox.prompt(`${useFormat18n("common.whetherOrNot")}${message}?`, useFormat18n("common.tips"), {
			confirmButtonText: useFormat18n("common.confirm"),
			cancelButtonText: useFormat18n("common.cancel"),
			type: "warning",
			draggable: true,
			inputType: "textarea",
			inputPlaceholder: useFormat18n(["common.audit", "common.remark"])
		})
			.then(async (event: any) => {
				const reason = event.value || "";
				const res = await api({
					...params,
					reason
				});
				if (!res) reject(false);
				ElMessage({
					type: "success",
					message: `${message}${useFormat18n("common.success")}!`
				});
				resolve(res);
			})
			.catch(err => {
				if (err !== "cancel") {
					reject(err);
				}
			});
	});
};
