import http from "@/api";

/**
 * @name 测试接口
 */

// 翻译
export const fatchTransLang = (data: any) => {
	return http.post(`/base/translate`, data);
};
