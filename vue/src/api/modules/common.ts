import http from "@/api";
import type { SmsCodeType } from "@/enums/systemEnum";
import { SysConfigProps } from "@/stores/interface";

/**
 * @name 公共接口
 */

// 获取公共配置
export const queryCommonConfig = () => {
	return http.get<SysConfigProps>(`/base/config`, {}, { headers: { noLoading: true } });
};

// 获取图形验证码
export const queryImgCaptcha = () => {
	return http.get<string>(`/base/imgCaptcha`, {}, { headers: { noLoading: true } });
};

// 获取短信验证码
export const querySmsCode = (params: { type: SmsCodeType; phone: string; imgCaptcha?: string }) => {
	let url = `/base/phoneCaptcha`;
	if (["register", "forget"].includes(params.type)) {
		url = `/base/phoneCaptcha/verify`;
	}
	return http.post(url, params, { headers: { noLoading: true } });
};
