import { ElMessage } from "element-plus";
import i18n from "@/languages/index";

/**
 * @description: 校验网络请求状态码
 * @param {Number} status
 * @return void
 */
export const checkStatus = (response: any): void => {
	const { status, message } = response.data;
	switch (status) {
		case 400:
			ElMessage.error(i18n.global.t(`error.BadRequest`));
			break;
		case 401:
			ElMessage.error(i18n.global.t(`error.AuthError`));
			break;
		case 403:
			ElMessage.error(i18n.global.t(`error.AccessForbidden`));
			break;
		case 404:
			ElMessage.error(i18n.global.t(`error.NotFound`));
			break;
		case 405:
			ElMessage.error(i18n.global.t(`error.BadRequest`));
			break;
		case 408:
			ElMessage.error(i18n.global.t(`error.TimeOut`));
			break;
		case 500:
			ElMessage.error(i18n.global.t(`error.InternalServerError`));
			break;
		case 502:
			ElMessage.error(i18n.global.t(`error.InternalServerError`));
			break;
		case 503:
			ElMessage.error(i18n.global.t(`error.InternalServerError`));
			break;
		case 504:
			ElMessage.error(i18n.global.t(`error.TimeOut`));
			break;
		default:
			ElMessage.error(i18n.global.t(`error.${message}`));
	}
};
