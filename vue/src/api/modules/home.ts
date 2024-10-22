import http from "@/api";

/**
 * 首页统计管理
 */

// 查询首页数据统计
export const getHomeNumberStatistics = (data: any) => {
	return http.get(`/admin/statistics/index/order`, data, { headers: { noLoading: true } });
};

// 查询首页商城订单折线图
export const getHomeMallOrderStatistics = (data: any) => {
	return http.get(`/admin/statistics/index/order/line`, data, { headers: { noLoading: true } });
};

// 查询首页服务订单折线图
export const getHomeServiceOrderStatistics = (data: any) => {
	return http.get(`/admin/statistics/index/serviceOrder/line`, data, { headers: { noLoading: true } });
};
