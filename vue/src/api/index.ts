import axios, { AxiosInstance, AxiosError, AxiosRequestConfig, AxiosResponse } from "axios";
import Cookies from "js-cookie";
import { showFullScreenLoading, tryHideFullScreenLoading } from "@/config/serviceLoading";
import { ResultData } from "@/api/interface";
import { ResultEnum } from "@/enums/httpEnum";
import { checkStatus } from "./helper/checkStatus";
import { ElMessage } from "element-plus";
import i18n from "@/languages/index";
import { GlobalStore } from "@/stores";
// import { AuthStore } from "@/stores/modules/auth";
import { LOGIN_URL } from "@/config/config";
import router from "@/routers";

const config = {
	// 默认地址请求地址，可在 .env.*** 文件中修改
	baseURL: import.meta.env.VITE_API_URL as string,
	// baseURL: "http://localhost:9068" as string,
	// 设置超时时间（10s）
	timeout: ResultEnum.TIMEOUT as number,
	// 跨域时候允许携带凭证
	withCredentials: true
};

class RequestHttp {
	service: AxiosInstance;
	public constructor(config: AxiosRequestConfig) {
		// 实例化axios
		this.service = axios.create(config);

		/**
		 * @description 请求拦截器
		 * 客户端发送请求 -> [请求拦截器] -> 服务器
		 * token校验(JWT) : 接受服务器返回的token,存储到vuex/pinia/本地储存当中
		 */
		this.service.interceptors.request.use(
			(config: AxiosRequestConfig) => {
				// * 如果当前请求不需要显示 loading,在 api 服务中通过指定的第三个参数: { headers: { noLoading: true } }来控制不显示loading，参见loginApi
				config.headers!.noLoading || showFullScreenLoading();
				// delete请求处理
				if (config.method === "delete") {
					config.data = {
						...(config.params || {})
					};
					config.params = {};
				}
				return { ...config, headers: { ...config.headers } };
			},
			(error: AxiosError) => {
				return Promise.reject(error);
			}
		);

		/**
		 * @description 响应拦截器
		 *  服务器换返回信息 -> [拦截统一处理] -> 客户端JS获取到信息
		 */
		this.service.interceptors.response.use(
			(response: AxiosResponse) => {
				const { data, config } = response;
				const globalStore = GlobalStore();
				// * 在请求结束后，并关闭请求 loading
				config.headers!.noLoading || tryHideFullScreenLoading();
				// 单独判断响应文件流
				if (response.headers["content-disposition"] === "attachment;") {
					return data;
				}

				// status 登录失效/未登录 40100
				if (data.status == ResultEnum.OVERDUE) {
					ElMessage.error(data.remark || i18n.global.t(`error.${data.message}`));
					// 1.清除Token 重置用户信息
					globalStore.setToken("");
					globalStore.setUserInfo({
						nickName: "",
						name: "",
						phone: "",
						roleKey: "",
						roleName: "",
						tenantId: 0
					});
					// 2.清除 Cookie
					Cookies.remove("PLSESSIONID");
					// 3.重定向到登陆页
					router.replace(LOGIN_URL);
					return Promise.reject(data);
				}

				// * 没权限（status == 40300	)
				if (data.status == ResultEnum.NOAUTH) {
					ElMessage.error(data.remark || i18n.global.t(`error.${data.message}`));
					return Promise.reject(data);
				}

				// * 请求参数错误（status == 40001	)
				if (data.status == ResultEnum.BADREQUEST) {
					ElMessage.error(data.remark || i18n.global.t(`error.${data.message}`));
					return Promise.reject(data);
				}

				// * 全局错误信息拦截
				if (data.status !== ResultEnum.SUCCESS) {
					let msg = data.remark || i18n.global.t(`error.${data.message || "NetworkError"}`);
					if (data.additional) {
						msg += `：${data.additional}`;
					}
					ElMessage.error(msg);
					return Promise.reject(data);
				}
				// * 成功请求（在页面上除非特殊情况，否则不用在页面处理失败逻辑）
				return data;
			},
			async (error: AxiosError) => {
				const { response } = error;
				tryHideFullScreenLoading();
				// 请求超时 && 网络错误单独判断，没有 response
				if (error.message.indexOf("timeout") !== -1) ElMessage.error(i18n.global.t("error.TimeOut"));
				if (error.message.indexOf("Network Error") !== -1 || error.message.indexOf("Request failed") !== -1)
					ElMessage.error(i18n.global.t("error.NetworkError"));
				// 根据响应的错误状态码，做不同的处理
				if (response) checkStatus(response);
				// 服务器结果都没有返回(可能服务器错误可能客户端断网)，断网处理:可以跳转到断网页面
				if (!window.navigator.onLine) router.replace("/500");
				return Promise.reject(error);
			}
		);
	}

	// * 常用请求方法封装
	get<T>(url: string, params?: object, _object = {}): Promise<ResultData<T>> {
		return this.service.get(url, { params, ..._object });
	}
	post<T>(url: string, params?: object, _object = {}): Promise<ResultData<T>> {
		return this.service.post(url, params, _object);
	}
	put<T>(url: string, params?: object, _object = {}): Promise<ResultData<T>> {
		return this.service.put(url, params, _object);
	}
	delete<T>(url: string, params?: any, _object = {}): Promise<ResultData<T>> {
		return this.service.delete(url, { params, ..._object });
	}
	download(url: string, params?: object): Promise<BlobPart> {
		// return this.service.post(url, { params, ..._object, responseType: "blob" });
		return axios({
			url: config.baseURL + url,
			method: "post",
			params: params || {},
			responseType: "blob"
		});
	}
}

export default new RequestHttp(config);
