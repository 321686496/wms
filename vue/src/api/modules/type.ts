import http from "@/api";

/**
 * 产品类别管理
 */

// 分页查询产品类别列表
export const getSkuTypeList = (data: any) => {
	return http.get(`/admin/wms/material/type`, data, { headers: { noLoading: true } });
};

// 不分页查询产品类别列表
export const getSkuTypeSelect = () => {
	return http.get(`/admin/wms/material/type/list`, {}, { headers: { noLoading: true } });
};

// 新增单个产品类别
export const addSkuTypeOne = (data: any) => {
	return http.post(`/admin/wms/material/type`, data);
};

// 修改单个产品类别
export const updateSkuTypeOne = (data: any) => {
	return http.put(`/admin/wms/material/type`, data);
};

// 删除单个产品类别
export const deleteSkuTypeOne = (data: any) => {
	return http.delete(`/admin/wms/material/type`, data);
};

// * 删除品牌批量
export const deleteSkuTypeBatch = (data: { ids: number[] }) => {
	return http.delete<Batch.BatchResultVo>(`/admin/wms/material/type/batch`, data);
};
