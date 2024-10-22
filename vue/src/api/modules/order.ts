import http from "@/api";

/**
 * 订单管理
 */

// 分页查询订单列表
export const getReportOrderList = (data: any) => {
	return http.get(`/admin/wms/report/order`, data, { headers: { noLoading: true } });
};

// 新增单个订单
export const saveReportOrderOne = (data: any) => {
	return http.post(`/admin/wms/report/order`, data);
};

// 查询单个订单
export const getReportOrderOne = (data: any) => {
	return http.get(`/admin/wms/report/order/one`, data);
};

// 查询单个订单明细
export const getReportOrderItem = (data: any) => {
	return http.get(`/admin/wms/report/order/item`, data);
};

//合并订单
export const mergeReportOrder = (data: any) => {
	return http.put(`/admin/wms/report/order/merge`, data);
};

// 删除单个订单
export const deleteReportOrderOne = (data: any) => {
	return http.delete(`/admin/wms/report/order`, data);
};

// * 删除订单批量
export const deleteReportOrderBatch = (data: { ids: number[] }) => {
	return http.delete<Batch.BatchResultVo>(`/admin/wms/report/order/batch`, data);
};

// 获取订单pdf
export const getReportOrderPdfOne = (data: any) => {
	return http.get(`/admin/wms/report/order/pdf`, data);
};

// 获取合并订单详情
export const getMergeReportOrderOne = (data: any) => {
	return http.get(`/admin/wms/report/order/one/merge`, data);
};
