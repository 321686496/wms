import { Upload } from "@/api/interface/index";
import { QiniuTokenDataProps } from "@/stores/interface";
import http from "@/api";
import axios from "axios";

/**
 * @name 文件上传模块
 */
// * 图片上传
export const uploadImg = (params: FormData) => {
	return http.post<Upload.ResFileUrl>(`/admin/upload/img`, params);
};

// * 视频上传
export const uploadVideo = (params: FormData) => {
	return http.post<Upload.ResFileUrl>(`/admin/upload/file`, params);
};

// * 文件上传
export const uploadFile = (params: FormData) => {
	return http.post<Upload.ResFileUrl>(`/admin/upload/file`, params);
};

/**
 * @name 七牛云文件上传模块
 */

// * 获取token
export const getQiNiuTokenApi = () => {
	return http.get<QiniuTokenDataProps>(`/base/api/qiniuyun/token`);
};
// * 图片上传
export const uploadQiNiuImg = (params: FormData) => {
	return axios({
		url: "https://up-z1.qiniup.com",
		method: "post",
		headers: {
			"Content-Type": "multipart/form-data"
		},
		data: params
	});
};

// * 保存上传记录
export const saveUploadRecordApi = (data: any) => {
	return http.post(`/admin/system/file/upload`, data, { headers: { noLoading: true } });
};

/**
 * @name 以图识物上传模块
 */
// * 以图识物上传
export const uploadSearchImg = (params: FormData) => {
	return http.post<any>(`/admin/basic/materialSku/base/picture/search`, params);
};
