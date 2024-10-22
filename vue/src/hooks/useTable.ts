import { Table } from "./interface";
import { reactive, ref, computed, toRefs } from "vue";
import { extractStringBetweenBackticks } from "@/utils/util";
import http from "@/api";
import useFormat18n from "@/hooks/useFormat18n";
import useTenantConfig from "./useTenantConfig";

/**
 * @description table 页面操作方法封装
 * @param {Function} api 获取表格数据 api 方法(必传)
 * @param {Object} initParam 获取数据初始化参数(非必传，默认为{})
 * @param {Boolean} isPageable 是否有分页(非必传，默认为true)
 * @param {Function} dataCallBack 对后台返回的数据进行处理的方法(非必传)
 * @param {Boolean} showSummary 是否请求汇总
 * */
export const useTable = (
	api: (params: any) => Promise<any>,
	initParam: any = {},
	isPageable: boolean = true,
	dataCallBack?: (data: any) => any,
	showSummary?: boolean
) => {
	const { tenantConfig } = useTenantConfig();

	const state = reactive<Table.TableStateProps>({
		// 表格数据
		tableData: [],
		// 分页数据
		pageable: {
			// 当前页数
			current: 1,
			// 每页显示条数
			pageSize: tenantConfig.defaultPageSize || 10,
			// 总条数
			total: 0
		},
		// 查询参数(只包括查询)
		searchParam: {},
		// 初始化默认的查询参数
		searchInitParam: {},
		// 总参数(包含分页和查询参数)
		totalParam: {},
		// 表尾合计
		additional: {}
	});

	// 增加loading
	const tableLoading = ref(false);

	/**
	 * @description 分页查询参数(只包括分页和表格字段排序,其他排序方式可自行配置)
	 * */
	const pageParam = computed({
		get: () => {
			return {
				current: state.pageable.current,
				pageSize: state.pageable.pageSize
			};
		},
		set: (newVal: any) => {
			console.log("我是分页更新之后的值", newVal);
		}
	});

	// 初始化的时候需要做的事情就是 设置表单查询默认值 && 获取表格数据(reset函数的作用刚好是这两个功能)
	// onMounted(() => {
	// 	reset();
	// });

	/**
	 * @description 获取表格数据
	 * @return void
	 * */
	const getTableList = async (getSummary: boolean = true) => {
		tableLoading.value = true;
		try {
			// 先把初始化参数和分页参数放到总参数里面
			Object.assign(state.totalParam, initParam, isPageable ? pageParam.value : {});
			if (showSummary && getSummary) {
				getSummaryAdditional();
			}
			let data = await api(state.totalParam);
			dataCallBack && (data = dataCallBack(data));
			state.tableData = isPageable ? data.data : data.data;
			// 解构后台返回的分页数据 (如果有分页更新分页信息)
			if (isPageable) {
				const { current, pageSize, total } = data.pagination;
				updatePageable({ current, pageSize, total });
			}
			// 更新表尾合计
			// if (data.additional) {
			// 	state.additional = data.additional;
			// }
			tableLoading.value = false;
		} catch (error) {
			console.log(error);
			tableLoading.value = false;
		}
	};

	/**
	 *
	 * @param param 请求表尾合计数据
	 * @returns
	 */
	const getSummaryAdditional = async () => {
		const listApiUrl = extractStringBetweenBackticks(api + "")[0];
		const summaryUrl = `${listApiUrl}/statistics`;
		const result = await http.get(summaryUrl, state.totalParam, { headers: { noLoading: true } });
		if (result.status === 0 && result.data) {
			state.additional = result.data;
		}
	};

	/**
	 *
	 * @param param 表尾合计方法
	 * @returns
	 */
	const getSummaries = (param: any) => {
		const additional = state.additional || {};
		const keyArr = Object.keys(additional);
		const { columns } = param;
		const sums: any[] = [];
		sums[0] = useFormat18n("common.total");
		if (keyArr && keyArr.length) {
			keyArr.forEach(rowKey => {
				const columnIndex: number = columns.findIndex((column: any) => column.property == rowKey);
				if (columnIndex !== -1) {
					sums[columnIndex] = additional[rowKey];
				}
			});
		}
		return sums;
	};

	/**
	 * @description 更新查询参数
	 * @return void
	 * */
	const updatedTotalParam = () => {
		state.totalParam = {};
		// 处理查询参数，可以给查询参数加自定义前缀操作
		let nowSearchParam: { [key: string]: any } = {};
		// 防止手动清空输入框携带参数（这里可以自定义查询参数前缀）
		for (let key in state.searchParam) {
			// * 某些情况下参数为 false/0 也应该携带参数
			if (state.searchParam[key] || state.searchParam[key] === false || state.searchParam[key] === 0) {
				nowSearchParam[key] = state.searchParam[key];
			}
			// 处理数组类型的查询条件，转为逗号分割
			if (Array.isArray(state.searchParam[key])) {
				const valArr = state.searchParam[key];
				const strVal = valArr.join(",");
				nowSearchParam[key] = strVal;
			}
			// 处理单据时间筛选
			if (key === "start" && nowSearchParam[key]) {
				const timeArr = nowSearchParam[key].split(",");
				if (timeArr && timeArr.length == 2) {
					nowSearchParam.start = timeArr[0];
					nowSearchParam.end = timeArr[1];
				}
			}
			// 处理包含与不包含
			if (typeof state.searchParam[key] == "string" && state.searchParam[key].indexOf("mode=in") !== -1) {
				nowSearchParam[key] = state.searchParam[key].split("=")[2];
			}
			if (typeof state.searchParam[key] == "string" && state.searchParam[key].indexOf("mode=notIn") !== -1) {
				let upperKey = `${key.charAt(0).toUpperCase()}${key.slice(1)}`;
				nowSearchParam[`notIn${upperKey}`] = state.searchParam[key].split("=")[2];
				delete nowSearchParam[key];
			}
		}
		const formDataWrap = {
			...nowSearchParam
		};
		Object.assign(state.totalParam, formDataWrap, isPageable ? pageParam.value : {});
	};

	/**
	 * @description 更新分页信息
	 * @param {Object} resPageable 后台返回的分页数据
	 * @return void
	 * */
	const updatePageable = (resPageable: Table.Pageable) => {
		Object.assign(state.pageable, resPageable);
	};

	/**
	 * @description 表格数据查询
	 * @return void
	 * */
	const search = async () => {
		state.pageable.current = 1;
		state.tableData = [];
		updatedTotalParam();
		await getTableList();
	};

	/**
	 * @description 刷新数据查询
	 * @return void
	 * */
	const refresh = () => {
		updatedTotalParam();
		getTableList();
	};

	/**
	 * @description 表格数据重置
	 * @return void
	 * */
	const reset = () => {
		state.pageable.current = 1;
		state.tableData = [];
		state.searchParam = {};
		// 重置搜索表单的时，如果有默认搜索参数，则重置默认的搜索参数
		Object.keys(state.searchInitParam).forEach(key => {
			state.searchParam[key] = state.searchInitParam[key];
		});
		updatedTotalParam();
		getTableList();
	};

	/**
	 * @description 每页条数改变
	 * @param {Number} val 当前条数
	 * @return void
	 * */
	const handleSizeChange = (val: number) => {
		state.pageable.current = 1;
		state.pageable.pageSize = val;
		getTableList(false);
	};

	/**
	 * @description 当前页改变
	 * @param {Number} val 当前页
	 * @return void
	 * */
	const handleCurrentChange = async (val: number) => {
		state.pageable.current = val;
		await getTableList(false);
	};

	return {
		...toRefs(state),
		tableLoading,
		getSummaries,
		getTableList,
		getSummaryAdditional,
		search,
		reset,
		refresh,
		handleSizeChange,
		handleCurrentChange
	};
};
