import http from "@/api";

/**
 * 产品管理
 */

// 分页查询产品列表
export const getSkuList = (data: any) => {
	return http.get(`/admin/wms/material`, data, { headers: { noLoading: true } });
};

// 不分页查询产品列表
export const getSkuSelect = () => {
	return http.get(`/admin/wms/material/list`, {}, { headers: { noLoading: true } });
};

// 新增单个产品
export const addSkuOne = (data: any) => {
	return http.post(`/admin/wms/material`, data);
};

// 修改单个产品
export const updateSkuOne = (data: any) => {
	return http.put(`/admin/wms/material`, data);
};

// 删除单个产品
export const deleteSkuOne = (data: any) => {
	return http.delete(`/admin/wms/material`, data);
};

// * 删除品牌批量
export const deleteSkuBatch = (data: { ids: number[] }) => {
	return http.delete<Batch.BatchResultVo>(`/admin/wms/material/batch`, data);
};
