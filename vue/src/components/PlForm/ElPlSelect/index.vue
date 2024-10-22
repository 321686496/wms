<template>
	<vxe-pulldown :disabled="disabled" v-model="showPull" transfer style="transform: translateY(-2px)">
		<template #default>
			<el-input
				v-model="labelName"
				:clearable="clearable"
				:placeholder="placeholder"
				:disabled="disabled"
				:style="{ width: width }"
				@focus="show"
				@clear="onClear"
				@input="onSearchInput"
				@keydown="onKeyDown"
			>
				<template #append v-if="canAdd">
					<el-button
						:disabled="disabled"
						type="primary"
						:icon="addButtonIcon === 'Plus' ? Plus : addButtonIcon === 'Search' ? Search : ''"
						@click="onAdd"
					/>
				</template>
			</el-input>
		</template>
		<template #dropdown>
			<div class="dropdown-table">
				<vxe-grid
					ref="xGrid"
					border
					auto-resize
					show-overflow
					height="280px"
					align="center"
					header-align="center"
					:row-config="{ isHover: true, isCurrent: true }"
					:loading="tableLoading"
					:pager-config="pagerConfig"
					:data="tableData"
					:columns="tableColumns"
					@cell-click="onCellClick"
					@page-change="onPagerChange"
					@current-change="currentRowChange"
				>
				</vxe-grid>
			</div>
		</template>
	</vxe-pulldown>
</template>
<script setup lang="ts" name="ElPlSelect">
import { ref, computed, reactive } from "vue";
import { Plus, Search } from "@element-plus/icons-vue";
import { ElMessage, FormRules } from "element-plus";
import { VxeGridEvents, VxeGridInstance } from "vxe-table";
import { debounce } from "lodash-es";
import { valueTransInt } from "@/utils/util";
import { useTableRadio } from "@/hooks/useTableRadio";
import useFormat18n from "@/hooks/useFormat18n";

interface ElPlSelectProp {
	modelValue?: any;
	label?: string;
	placeholder?: string;
	clearable?: boolean;
	canAdd?: boolean;
	disabled?: boolean;
	width?: string;
	action?: (params: any) => Promise<any>;
	actionData?: any;
	actionRules?: FormRules;
	canSearch?: boolean;
	searchName?: string;
	pagination?: boolean;
	tableColumns?: any[];
	options?: any[];
	labelKey?: string;
	valueKey?: string;
	addButtonIcon?: string;
}

const emit = defineEmits(["update:modelValue", "update:label", "add", "change"]);

const props = withDefaults(defineProps<ElPlSelectProp>(), {
	modelValue: "",
	label: "",
	clearable: true,
	canSearch: true,
	disabled: false,
	canAdd: false,
	width: "100%",
	searchName: "name",
	valueKey: "value",
	labelKey: "label",
	addButtonIcon: "Plus",
	actionData: {},
	pagination: false,
	placeholder: useFormat18n("common.pleaseSelect"),
	tableColumns: () => [],
	options: () => []
});

const xGrid = ref<VxeGridInstance<any>>();
const showPull = ref(false);

const labelName = computed({
	get() {
		return props.label;
	},
	set(value) {
		emit("update:label", value);
	}
});

const selectValue = computed({
	get() {
		return props.modelValue;
	},
	set(value) {
		emit("update:modelValue", value);
	}
});

const show = () => {
	showPull.value = true;
	reset();
};
const close = () => {
	showPull.value = false;
};
const onClear = () => {
	selectValue.value = undefined;
	labelName.value = "";
	emit("change", undefined, {});
};
const onAdd = () => {
	close();
	emit("add");
};
const reset = () => {
	if (!showPull.value) {
		showPull.value = true;
	}
	pagerConfig.currentPage = 1;
	getTableList();
};

const onSearchInput = debounce(() => {
	reset();
}, 360);

/**
 * 键盘事件相关
 */
const onKeyDown = (event: KeyboardEvent) => {
	if (!currentRow.value) {
		return;
	}
	const rowSelectId = currentRow.value[props.valueKey];
	const rowIndex = tableData.value.findIndex(row => row[props.valueKey] == rowSelectId);
	switch (event.key) {
		case "ArrowUp":
			if (rowIndex !== 0) {
				const preRowIndex = rowIndex - 1;
				setCurrentRow(tableData.value[preRowIndex]);
			}
			break;
		case "ArrowDown":
			if (rowIndex !== tableData.value.length - 1) {
				const nextRowIndex = rowIndex + 1;
				setCurrentRow(tableData.value[nextRowIndex]);
			}
			break;
		case "Enter":
			onCellClick({ row: currentRow.value });
			break;
	}
};

/**
 * 表格相关
 */
const tableLoading = ref(false);
const { currentRow, currentRowChange } = useTableRadio();
const setCurrentRow = (row: any) => {
	currentRowChange(row);
	xGrid.value?.setCurrentRow(row);
	xGrid.value?.scrollToRow(row);
};
const pagerConfig = reactive({
	total: 0,
	currentPage: 1,
	pageSize: 10,
	enabled: props.pagination
});
const tableData = ref<any[]>([]);
const onPagerChange: VxeGridEvents.PageChange = ({ currentPage, pageSize }) => {
	pagerConfig.currentPage = currentPage;
	pagerConfig.pageSize = pageSize;
	getTableList();
};
const validate = () => {
	return new Promise((resolve, reject) => {
		if (!props.actionRules) {
			resolve(true);
		} else {
			const ruleKeys = Object.keys(props.actionRules);
			let errRule: any = null;
			for (const ruleKey of ruleKeys) {
				if (props.actionRules[ruleKey] && props.actionRules[ruleKey]![0].required && !props.actionData[ruleKey]) {
					errRule = props.actionRules[ruleKey]![0];
					break;
				}
			}
			if (errRule) {
				ElMessage.error(errRule.message);
				tableLoading.value = false;
				reject(false);
			} else {
				resolve(true);
			}
		}
	});
};
const getTableList = async () => {
	tableLoading.value = true;
	let params: any = {};
	if (props.pagination) {
		params.pageSize = pagerConfig.pageSize;
		params.current = pagerConfig.currentPage;
	}
	if (labelName.value) {
		params[props.searchName] = labelName.value;
	}
	params = Object.assign(params, props.actionData);
	// 校验参数
	const validateRes = await validate();
	if (!validateRes) return;

	// 前端数据处理
	if (props.options && props.options.length) {
		// 处理模糊查询
		let fullData = params[props.searchName]
			? props.options.filter(item => item.label.includes(params[props.searchName]))
			: [...props.options];
		// 处理分页
		tableData.value = fullData.slice((params.current - 1) * params.pageSize, params.current * params.pageSize);
		pagerConfig.total = fullData.length;
		tableLoading.value = false;
		if (tableData.value.length) {
			setCurrentRow(tableData.value[0]);
		}
		return;
	}
	if (props.action) {
		try {
			const res: any = await props.action(params);
			if (res.status === 0) {
				const list = valueTransInt(res.data || []);
				tableData.value = list;
				if (props.pagination) {
					pagerConfig.total = (res.pagination && res.pagination.total) || 0;
				}
				if (tableData.value.length) {
					setCurrentRow(tableData.value[0]);
				}
			}
			tableLoading.value = false;
		} catch (error) {
			tableData.value = [];
			tableLoading.value = false;
		}
	}
};

const onCellClick = ({ row }: any) => {
	selectValue.value = row[props.valueKey];
	labelName.value = row[props.labelKey];
	emit("change", row[props.valueKey], row);
	close();
};
</script>
<style lang="scss">
.vxe-pulldown--panel {
	z-index: 30000 !important;
}
.dropdown-table {
	width: 500px;
	height: 280px;
	background-color: #ffffff;
	box-shadow: 0 0 6px 2px rgb(0 0 0 / 10%);
}
</style>
