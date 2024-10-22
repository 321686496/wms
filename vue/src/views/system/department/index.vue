<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			:columns="columns"
			:requestApi="getDepartmentList"
			:pagination="false"
			:indent="20"
			:title="pageTitle"
		>
			<template #tableHeader>
				<el-button type="primary" @click="openDialog('add')">{{ $t("common.add") }}</el-button>
			</template>
			<template #enable="scope">
				<div>
					<el-switch v-model="scope.row.enable" @change="onEnableChange($event, scope.row)" />
				</div>
			</template>
			<!-- 表格操作 -->
			<template #operation="scope">
				<el-button type="primary" size="small" @click="openDialog('update', { ...scope.row })" :disabled="scope.row.isDefault">{{
					$t("common.edit")
				}}</el-button>
				<el-button type="danger" size="small" @click="onDelete(scope.row)">{{ $t("common.delete") }}</el-button>
			</template>
		</ProTable>
		<form-dialog :columns="formColumns" ref="formDialogRef" />
	</div>
</template>

<script setup lang="ts" name="system_department">
import { ref } from "vue";
import { ElMessage } from "element-plus";
import {
	getDepartmentList,
	enableDepartmentOne,
	deleteDepartmentOne,
	addDepartmentOne,
	updateDepartmentOne
} from "@/api/modules/system";
import { queryDepartmentTree } from "@/api/modules/select";
import { ColumnProps } from "@/components/ProTable/interface";
import { FormColumn } from "@/components/FormDialog/interface";
import { SystemVo } from "@/typings/modules/system";
import { YesNoStatusList } from "@/utils/serviceDict";
import { useHandleData } from "@/hooks/useHandleData";
import useFormat18n from "@/hooks/useFormat18n";

const pageTitle = useFormat18n("system.department");
const proTable = ref();
const formDialogRef = ref();

// 表格配置项
const columns: ColumnProps[] = [
	{
		prop: "name",
		label: useFormat18n("system.departmentName"),
		search: { el: "input" },
		align: "left"
	},
	{
		prop: "isDefault",
		label: useFormat18n(["common.isDefault", "system.departmentName"]),
		tag: true,
		enum: YesNoStatusList
	},
	{
		prop: "leader",
		label: useFormat18n("system.leader")
	},
	{
		prop: "phone",
		label: useFormat18n("login.mobile")
	},
	{
		prop: "email",
		label: useFormat18n("login.email")
	},
	{
		prop: "enable",
		label: useFormat18n("common.isEnable")
	},
	{
		prop: "priority",
		label: useFormat18n("common.priority"),
		sortable: true
	},
	{
		prop: "createdAt",
		label: useFormat18n("common.createdAt")
	},
	{ prop: "operation", label: useFormat18n("common.operate"), fixed: "right", width: 180 }
];

// 表单配置
const formColumns: FormColumn[] = [
	{
		field: "pid",
		type: "cascader",
		extraProps: {
			props: {
				checkStrictly: true,
				emitPath: false
			},
			filterable: true
		},
		action: queryDepartmentTree,
		label: useFormat18n(["common.parent", "system.department"]),
		rules: [{ required: true, message: useFormat18n(["common.pleaseSelect", "common.parent", "system.department"]) }]
	},
	{
		field: "name",
		type: "text",
		label: useFormat18n("system.departmentName"),
		placeholder: useFormat18n(["common.pleaseInput", "system.departmentName"]),
		rules: [{ required: true, message: useFormat18n(["common.pleaseInput", "system.departmentName"]) }]
	},
	{
		field: "leader",
		type: "text",
		label: useFormat18n("system.leader"),
		placeholder: useFormat18n(["common.pleaseInput", "system.leader"])
	},
	{
		field: "phone",
		type: "text",
		label: useFormat18n("login.mobile"),
		placeholder: useFormat18n(["common.pleaseInput", "login.mobile"])
	},
	{
		field: "email",
		type: "text",
		label: useFormat18n("login.email"),
		placeholder: useFormat18n(["common.pleaseInput", "login.email"])
	},
	{
		field: "priority",
		type: "number",
		label: useFormat18n("common.priority")
	}
];

// 新增/编辑
const openDialog = (type: "update" | "add", rowData?: SystemVo.DepartmentVo) => {
	if (type === "update" && rowData) {
		delete rowData.children;
		delete rowData.createdAt;
		delete rowData.updatedAt;
	}
	let params = {
		title: useFormat18n(`common.${type}`) + pageTitle,
		rowData: {
			...(rowData || { priority: 0 })
		},
		formType: type,
		api: type === "add" ? addDepartmentOne : updateDepartmentOne,
		getTableList: proTable.value.getTableList
	};
	formDialogRef.value.acceptParams(params);
};

// 删除
const onDelete = async (rowData: SystemVo.DepartmentVo) => {
	await useHandleData(deleteDepartmentOne, { id: rowData.id || 0 }, useFormat18n(["common.delete", "system.department"]));
	proTable.value.getTableList();
};

// 禁用/启用
const onEnableChange = (val: boolean, rowData: SystemVo.MenuVo) => {
	enableDepartmentOne({
		id: (rowData && rowData.id) || 0,
		enable: val
	})
		.then(res => {
			if (res.status === 0) {
				ElMessage({
					message: useFormat18n("common.operationSuccess"),
					type: "success"
				});
			}
		})
		.catch(() => {
			rowData.enable = !val;
		});
};
</script>

<style lang="scss"></style>
