<template>
	<div class="card filter">
		<h4 class="title sle flx-justify-between" v-if="title && canAdd">
			<span v-if="title">{{ title }}</span>
			<el-button v-if="canAdd" type="primary" @click="openDialog('add')">{{ $t("common.add") }}</el-button>
		</h4>
		<el-input v-if="showFilter" v-model="filterText" placeholder="关键词搜索" clearable />
		<el-scrollbar :style="{ height: title ? `calc(100% - 95px)` : `calc(100% - 56px)` }">
			<el-tree
				ref="treeRef"
				default-expand-all
				:node-key="id"
				:data="multiple ? treeData : treeAllData"
				:show-checkbox="multiple"
				:check-strictly="false"
				:current-node-key="!multiple ? selected : ''"
				:highlight-current="!multiple"
				:expand-on-click-node="false"
				:check-on-click-node="multiple"
				:props="defaultProps"
				:filter-node-method="filterNode"
				:default-checked-keys="multiple ? selected : []"
				@node-click="handleNodeClick"
				@check="handleCheckChange"
			>
				<template #default="scope">
					<span class="el-tree-node__label">
						<slot :row="scope">
							<div class="flx-justify-between" style="width: 150px">
								<span> {{ scope.node.label }}</span>
								<el-space>
									<el-icon @click="openDialog('update', { ...scope.data })" v-if="canUpdate"><Edit /></el-icon>
									<el-icon @click="onDelete(scope.data)" v-if="canDel"><Delete /></el-icon>
								</el-space>
							</div>
						</slot>
					</span>
				</template>
			</el-tree>
		</el-scrollbar>

		<form-dialog width="480px" label-width="120px" :columns="formColumns" ref="formDialogRef" />
	</div>
</template>

<script setup lang="ts" name="TreeFilter">
import { ref, watch, onBeforeMount } from "vue";
import { ElTree } from "element-plus";
import { FormColumn } from "@/components/FormDialog/interface";
import { useHandleData } from "@/hooks/useHandleData";
import useFormat18n from "@/hooks/useFormat18n";

// 接收父组件参数并设置默认值
interface TreeFilterProps {
	requestApi?: (data?: any) => Promise<any>; // 请求分类数据的 api ==> 非必传
	deleteApi?: (data?: any) => Promise<any>; // 新增分类数据的 api ==> 非必传
	addApi?: (data?: any) => Promise<any>; // 新增分类数据的 api ==> 非必传
	updateApi?: (data?: any) => Promise<any>; // 修改分类数据的 api ==> 非必传
	data?: { [key: string]: any }[]; // 分类数据，如果有分类数据，则不会执行 api 请求 ==> 非必传
	title?: string; // treeFilter 标题 ==> 非必传
	id?: string; // 选择的id ==> 非必传，默认为 “id”
	label?: string; // 显示的label ==> 非必传，默认为 “label”
	multiple?: boolean; // 是否为多选 ==> 非必传，默认为 false
	defaultValue?: any; // 默认选中的值 ==> 非必传
	canAdd?: boolean; //是否可新增
	canUpdate?: boolean; //是否可修改
	canDel?: boolean; // 是否可删除
	hasRoot?: boolean; //是否需要根数据（根数据id是0）
	showFilter?: boolean; //是否可过滤搜索
	formColumns?: FormColumn[]; // 新增表单配置列
	handleData?: (e: any) => any; // 处理新增表单的数据
	handleUpdateData?: (e: any) => any; // 处理编辑表单的数据
}
const props = withDefaults(defineProps<TreeFilterProps>(), {
	id: "id",
	label: "label",
	title: "",
	multiple: false,
	hasRoot: false,
	canAdd: false,
	canUpdate: false,
	canDel: false,
	showFilter: true,
	formColumns: () => []
});

const defaultProps = {
	children: "children",
	label: props.label
};

const formDialogRef = ref();
const filterText = ref<string>("");
const treeRef = ref<InstanceType<typeof ElTree>>();
const treeData = ref<{ [key: string]: any }[]>([]);
const treeAllData = ref<{ [key: string]: any }[]>([]);
// 选中的值
const selected = ref();

const loadData = async () => {
	// 重新接收一下默认值
	if (props.multiple) selected.value = Array.isArray(props.defaultValue) ? props.defaultValue : [props.defaultValue];
	else selected.value = typeof props.defaultValue === "string" ? props.defaultValue : "";

	// 有数据就直接赋值，没有数据就执行请求函数
	if (props.data?.length) {
		treeData.value = props.data;
		treeAllData.value = props.data;
		emit("change", selected.value || 0);
		return;
	}
	const { data: rows } = await props.requestApi!();
	// 处理根数据
	let root = [];
	if (props.hasRoot) {
		root = [
			{
				label: useFormat18n("common.all"),
				value: 0,
				children: rows
			}
		];
	} else {
		root = rows;
	}
	treeData.value = root;
	treeAllData.value = [...root];
	if (root[0]) {
		selected.value = root[0][props.id];
		emit("change", selected.value);
	} else {
		emit("change", 0);
	}
};

onBeforeMount(() => {
	loadData();
});

watch(filterText, val => {
	treeRef.value!.filter(val);
});

// 过滤
const filterNode = (value: string, data: { [key: string]: any }, node: any) => {
	if (!value) return true;
	let parentNode = node.parent,
		labels = [node.label],
		level = 1;
	while (level < node.level) {
		labels = [...labels, parentNode.label];
		parentNode = parentNode.parent;
		level++;
	}
	return labels.some(label => label.indexOf(value) !== -1);
};

interface FilterEmits {
	(e: "change", value: any): void;
}
const emit = defineEmits<FilterEmits>();

// 单选
const handleNodeClick = (data: { [key: string]: any }) => {
	if (props.multiple) return;
	emit("change", data[props.id]);
};

// 多选
const handleCheckChange = () => {
	emit("change", treeRef.value?.getCheckedKeys());
};

// 新增/编辑
const openDialog = (type: "update" | "add", rowData?: any) => {
	if (type === "update" && rowData) {
		delete rowData.createdAt;
		delete rowData.updatedAt;
		delete rowData.children;
		if (props.handleUpdateData) {
			rowData = props.handleUpdateData(rowData);
		}
	}
	let params: any = {
		title: useFormat18n(`common.${type}`) + props.title,
		rowData: {
			...(rowData || { enable: true })
		},
		formType: type,
		api: type === "add" ? props.addApi : props.updateApi,
		getTableList: loadData
	};
	if (props.handleData) {
		params.handleData = props.handleData;
	}
	formDialogRef.value.acceptParams(params);
};

// 删除
const onDelete = async (params: any) => {
	props.deleteApi && (await useHandleData(props.deleteApi, params, useFormat18n("common.delete") + props.title));
	loadData();
};

// 暴露给父组件使用
defineExpose({ treeData, treeAllData });
</script>

<style scoped lang="scss">
@import "./index.scss";
</style>
