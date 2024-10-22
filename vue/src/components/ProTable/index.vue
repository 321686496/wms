<!-- 📚📚📚 Pro-Table 文档: https://juejin.cn/post/7166068828202336263 -->

<template>
	<!-- 查询表单 card -->
	<SearchForm
		:search="search"
		:reset="reset"
		:searchParam="searchParam"
		:columns="searchColumns"
		:searchCol="searchCol"
		v-show="isShowSearch"
	/>

	<!-- 表格内容 card -->
	<div class="card table">
		<!-- 表格头部 操作按钮 -->
		<div class="table-header">
			<div class="header-button-lf">
				<slot name="tableHeader" :selectedListIds="selectedListIds" :selectedList="selectedList" :isSelected="isSelected"></slot>
			</div>
			<div class="header-button-ri" v-if="toolButton">
				<el-button :icon="Refresh" circle @click="getTableList"> </el-button>
				<!-- <el-button :icon="Operation" circle v-if="false" @click="openColSetting"> </el-button> -->
				<el-button :icon="Search" circle v-if="searchColumns && searchColumns.length" @click="isShowSearch = !isShowSearch">
				</el-button>
			</div>
		</div>
		<!-- 表格主体 -->
		<el-table
			ref="tableRef"
			v-bind="$attrs"
			v-loading="needLoading ? tableLoading : false"
			highlight-current-row
			size="small"
			:data="tableData"
			:border="border || false"
			:show-summary="showSummary || false"
			:summary-method="getSummaries"
			:row-key="getRowKeys"
			@selection-change="onSelectionChange"
			@current-change="onCurrentRowChange"
			@row-dblclick="onDbRowClick"
			@sort-change="onSortChange"
		>
			<!-- 默认插槽 -->
			<slot></slot>
			<template v-for="(item, index) in tableColumns" :key="index">
				<!-- selection || index -->
				<el-table-column
					v-bind="item"
					:align="item.align ?? 'center'"
					:reserve-selection="item.type == 'selection'"
					v-if="item.type == 'selection' || item.type == 'index'"
				>
				</el-table-column>
				<!-- expand 支持 tsx 语法 && 作用域插槽 (tsx > slot) -->
				<el-table-column v-bind="item" :align="item.align ?? 'center'" v-if="item.type == 'expand'" v-slot="scope">
					<component :is="item.render" :row="scope.row" v-if="item.render"> </component>
					<slot :name="item.type" :row="scope.row" v-else></slot>
				</el-table-column>
				<!-- other 循环递归 -->
				<TableColumn v-if="!item.type && item.prop && item.isShow" :column="item">
					<template v-for="slot in Object.keys($slots)" #[slot]="scope">
						<slot :name="slot" :row="scope.row" :$index="scope.$index"></slot>
					</template>
				</TableColumn>
			</template>
			<!-- 插入表格最后一行之后的插槽 -->
			<template #append>
				<slot name="append"> </slot>
			</template>
			<!-- 表格无数据情况 -->
			<template #empty>
				<div class="table-empty">
					<slot name="empty">
						<img src="@/assets/images/notData.png" alt="notData" />
						<div>{{ $t("common.noData") }}</div>
					</slot>
				</div>
			</template>
		</el-table>
		<!-- 分页组件 -->
		<slot name="pagination">
			<Pagination
				v-if="pagination"
				:pageable="pageable"
				:handleSizeChange="handleSizeChange"
				:handleCurrentChange="handlePageCurrentChange"
			/>
		</slot>
	</div>
	<!-- 列设置 -->
	<!-- <ColSetting
		v-if="false"
		ref="colRef"
		:type="colSettingType"
		v-model:colSetting="colSetting"
		@update:col-setting="onColSettingChange"
	/> -->
</template>

<script setup lang="ts" name="ProTable">
import { ref, watch, provide, computed, onMounted, onUnmounted } from "vue";
import { useTable } from "@/hooks/useTable";
import { useSelection } from "@/hooks/useSelection";
import { useTableRadio } from "@/hooks/useTableRadio";
import { BreakPoint } from "@/components/Grid/interface";
import { ColumnProps } from "@/components/ProTable/interface";
import { ElTable, TableProps } from "element-plus";
import { Refresh, Search } from "@element-plus/icons-vue";
// import { filterEnum, formatValue, handleProp, handleRowAccordingToProp } from "@/utils/util";
import { TABLE_CACHE_TYPE_BLACK_LIST } from "@/config/config";
import { handleProp, valueTransInt } from "@/utils/util";
import { getTableSearchCache, setTableSearchCache } from "@/hooks/useTableCache";
import SearchForm from "@/components/SearchForm/index.vue";
import Pagination from "./components/Pagination.vue";
// import ColSetting from "./components/ColSetting.vue";
import TableColumn from "./components/TableColumn.vue";
import useTenantConfig from "@/hooks/useTenantConfig";
// import SearchFormItem from "../SearchForm/components/SearchFormItem.vue";

interface ProTableProps extends Partial<Omit<TableProps<any>, "data">> {
	columns: ColumnProps[]; // 列配置项
	requestApi: (params: any) => Promise<any>; // 请求表格数据的api ==> 必传
	requestAuto?: boolean; // 是否挂载就执行
	dataCallback?: (data: any) => any; // 返回数据的回调函数，可以对数据进行处理 ==> 非必传
	title?: string; // 表格标题，目前只在打印的时候用到 ==> 非必传
	pagination?: boolean; // 是否需要分页组件 ==> 非必传（默认为true）
	needLoading?: boolean; //是否需要loading
	initParam?: any; // 初始化请求参数 ==> 非必传（默认为{}）
	initParamChangeReload?: boolean; //初始参数变动是否发请求
	border?: boolean; // 是否带有纵向边框 ==> 非必传（默认为true）
	showSummary?: boolean; // 是否表尾合计 ==> 非必传（默认为false）
	toolButton?: boolean; // 是否显示表格功能按钮 ==> 非必传（默认为true）
	colSettingType?: string; //列显示抽屉的单据类型 ==> 非必传
	selectId?: string; // 当表格数据多选时，所指定的 id ==> 非必传（默认为 id）
	searchCol?: number | Record<BreakPoint, number>; // 表格搜索项 每列占比配置 ==> 非必传 { xs: 1, sm: 2, md: 2, lg: 3, xl: 4 }
	data?: any[];
}

const emit = defineEmits(["db-row-change", "row-change", "selection-change", "key-enter", "init-data"]);

// 接受父组件参数，配置默认值
const props = withDefaults(defineProps<ProTableProps>(), {
	columns: () => [],
	pagination: true,
	initParam: {},
	initParamChangeReload: true,
	border: true,
	needLoading: true,
	showSummary: false,
	toolButton: true,
	requestAuto: true,
	selectId: "id",
	colSettingType: "",
	searchCol: () => ({ xs: 4, sm: 6, md: 6, lg: 7, xl: 8, xxl: 10 })
});

const { tenantConfig } = useTenantConfig();

// 是否显示搜索模块
const isShowSearch = ref(true);

// 表格 DOM 元素
const tableRef = ref<InstanceType<typeof ElTable>>();

// 表格多选 Hooks
const onSelectionChange = (rowArr: any) => {
	selectionChange(rowArr);
	emit("selection-change", rowArr);
};

const { selectionChange, getRowKeys, selectedList, selectedListIds, isSelected } = useSelection(props.selectId);

// 表格单选hooks
const onDbRowClick = (val: any) => {
	currentRowChange(val);
	emit("db-row-change", val);
};
// 表格单行选中
const onCurrentRowChange = (val: any) => {
	currentRowChange(val);
	emit("row-change", val);
};
const { currentRow, currentRowChange } = useTableRadio();

// 表格操作 Hooks
const {
	tableData,
	pageable,
	searchParam,
	searchInitParam,
	getTableList,
	search,
	refresh,
	reset: tableReset,
	handleSizeChange,
	handleCurrentChange,
	getSummaries,
	tableLoading
} = useTable(props.requestApi, props.initParam, props.pagination, props.dataCallback, props.showSummary);

const reset = () => {
	tableRef.value && tableRef.value.clearSort();
	tableReset();
};

// 清空选中数据列表
const clearSelection = () => {
	// 清空多选
	tableRef.value!.clearSelection();
	// 清空单选
	currentRowChange(null);
};

const handlePageCurrentChange = async (val: number) => {
	await handleCurrentChange(val);
	// 表格首行高亮
	if (tableData.value[0]) {
		tableRef.value!.setCurrentRow(tableData.value[0]);
	}
};

// 初始化表格搜索缓存处理
const initTableCache = () => {
	return new Promise(resolve => {
		if (tenantConfig.searchCache && props.colSettingType && !TABLE_CACHE_TYPE_BLACK_LIST.includes(props.colSettingType)) {
			const p: any = getTableSearchCache(props.colSettingType);
			if (p.searchParam) {
				const searchKeys = Object.keys(p.searchParam);
				for (let searchKey of searchKeys) {
					setSearchParam(searchKey, p.searchParam[searchKey]);
				}
			}
			if (p.pageable) {
				setPageable({ current: p.pageable.current, pageSize: p.pageable.pageSize });
			}
			refresh();
			emit("init-data", p);
			resolve(true);
		} else {
			resolve(false);
		}
	});
};

// 初始化请求
onMounted(async () => {
	// 处理表格缓存
	if (await initTableCache()) return;
	// 处理加载表格数据
	props.requestAuto && (await search());
});

onUnmounted(() => {
	const colSettingType = props.colSettingType;
	if (colSettingType) {
		setTableSearchCache(colSettingType, {
			searchParam: searchParam.value,
			pageable: pageable.value
		});
	}
});

// 监听页面 initParam 变化，重新获取表格数据
watch(
	() => props.initParam,
	() => {
		props.initParamChangeReload && getTableList();
	},
	{ deep: true }
);

// 接收 columns 并设置为响应式
const tableColumns = ref<ColumnProps[]>(props.columns.map((column, columnIndex) => ({ ...column, priority: columnIndex })));
// const tableColumns = computed(() => props.columns.map((column, columnIndex) => ({ ...column, priority: columnIndex })));

// 定义 enumMap 存储 enum 值（避免异步请求无法格式化单元格内容 || 无法填充搜索下拉选择）
const enumMap = ref(new Map<string, { [key: string]: any }[]>());
provide("enumMap", enumMap);
const setEnumMap = async (col: ColumnProps) => {
	if (!col.enum) return;
	// 如果当前 enum 为后台数据需要请求数据，则调用该请求接口，并存储到 enumMap
	if (typeof col.enum !== "function") return enumMap.value.set(col.prop!, col.enum!);
	let { data } = await col.enum();
	data = valueTransInt(data || []);
	enumMap.value.set(col.prop!, data);
};

// 扁平化 columns
const flatColumnsFunc = (columns: ColumnProps[], flatArr: ColumnProps[] = []) => {
	columns.forEach(async col => {
		if (col._children?.length) flatArr.push(...flatColumnsFunc(col._children));
		flatArr.push(col);

		// 给每一项 column 添加 isShow && isFilterEnum 默认属性
		col.isShow = col.isShow ?? true;
		col.isFilterEnum = col.isFilterEnum ?? true;

		// 设置 enumMap
		setEnumMap(col);
	});
	return flatArr.filter(item => !item._children?.length);
};

// flatColumns
const flatColumns = computed(() => {
	return flatColumnsFunc(tableColumns.value);
});

// 过滤需要搜索的配置项 && 排序
const searchColumns = computed(() => {
	return flatColumns.value
		?.filter(item => (!!item.isShow || !!item.isSearchShow) && (item.search?.el || item.search?.render))
		.sort((a, b) => a.search!.order! - b.search!.order!);
});

// 设置 搜索表单默认排序 && 搜索表单项的默认值
searchColumns.value?.forEach((column, index) => {
	column.search!.order = column.search?.order ?? index + 2;
	const key = column.search?.key ?? handleProp(column.prop!);
	const defaultValue = column.search?.defaultValue;
	if (defaultValue !== undefined && defaultValue !== null) {
		searchInitParam.value[key] = defaultValue;
		searchParam.value[key] = defaultValue;
	}
});

// 设置搜索表单值
const setSearchParam = (prop: string, value: any) => {
	searchParam.value[prop] = value;
};

// 设置页码
const setPageable = (pager: { current: number; pageSize: number }) => {
	pageable.value.current = pager.current;
	pageable.value.pageSize = pager.pageSize;
};

// 列设置 ==> 过滤掉不需要设置显隐的列
/*
const colRef = ref();
const colSetting = ref<any[]>([]);
colSetting.value = tableColumns.value!.filter(item => {
	return (
		item.type !== "selection" &&
		item.type !== "index" &&
		item.type !== "expand" &&
		item.prop !== "operation" &&
		!item.tenantDisabled
	);
});
const onColSettingChange = (colSettings: ColumnProps[]) => {
	colSettings.forEach(setCol => {
		const hasCol = tableColumns.value.find(tableCol => tableCol.prop === setCol.prop);
		if (hasCol) {
			hasCol.priority = setCol.priority;
		}
	});
	tableColumns.value = [...tableColumns.value].sort((a, b) => (a.priority || 0) - (b.priority || 0));
};
const openColSetting = () => {
	colRef.value.openColSetting();
};
*/

const onSortChange = ({ prop, order }: { prop: string; order: "ascending" | "descending" }) => {
	const keys = Object.keys(searchParam.value || {});
	const sortField = keys.find(field => field.indexOf("Sort") !== -1);
	if (sortField) {
		delete searchParam.value[sortField];
	}
	searchParam.value[`${prop}Sort`] = order;
	search();
};

// 打印表格数据（💥 多级表头数据打印时，只能扁平化成一维数组，printJs 不支持多级表头打印）
/*
const handlePrint = () => {
	printJS({
		printable: printData.value,
		header: props.title && `<div style="display: flex;flex-direction: column;text-align: center"><h2>${props.title}</h2></div>`,
		properties: flatColumns
			.value!.filter(
				item =>
					item.isShow && item.type !== "selection" && item.type !== "index" && item.type !== "expand" && item.prop !== "operation"
			)
			.map((item: ColumnProps) => ({ field: handleProp(item.prop!), displayName: item.label })),
		type: "json",
		gridHeaderStyle:
			"border: 1px solid #ebeef5;height: 45px;font-size: 14px;color: #232425;text-align: center;background-color: #fafafa;",
		gridStyle: "border: 1px solid #ebeef5;height: 40px;font-size: 14px;color: #494b4e;text-align: center"
	});
};
*/

// 暴露给父组件的参数和方法(外部需要什么，都可以从这里暴露出去)
defineExpose({
	element: tableRef,
	tableData,
	searchParam,
	searchInitParam,
	setSearchParam,
	pageable,
	getTableList,
	reset,
	search,
	refresh,
	clearSelection,
	enumMap,
	isSelected,
	selectedList,
	selectedListIds,
	currentRow,
	colSettingType: props.colSettingType
});
</script>
