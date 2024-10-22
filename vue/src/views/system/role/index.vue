<template>
	<div class="table-box">
		<ProTable ref="proTable" :title="pageTitle" :columns="columns" :requestApi="getRoleList">
			<template #tableHeader="scope">
				<el-space>
					<el-button type="primary" @click="openDialog('add')">{{ $t("common.add") }}</el-button>
					<el-dropdown :disabled="!scope.isSelected" size="default">
						<el-button type="primary" :disabled="!scope.isSelected">
							{{ $t("common.batch") }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
						</el-button>
						<template #dropdown>
							<el-dropdown-menu>
								<div class="dropdown-item" @click="onBatch('copy', scope.selectedList)">
									{{ $t("common.copy") }}
								</div>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</el-space>
			</template>
			<!-- 表格操作 -->
			<template #operation="scope">
				<el-space>
					<el-dropdown size="default">
						<el-button type="success" size="small">
							{{ $t("common.more") }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
						</el-button>
						<template #dropdown>
							<el-dropdown-menu>
								<!-- 编辑 -->
								<div class="dropdown-item" v-if="!scope.row.isDefault" @click="openDialog('update', { ...scope.row })">
									{{ $t("common.edit") }}
								</div>
								<!-- 删除 -->
								<div class="dropdown-item" v-if="!scope.row.isDefault" @click="onDelete(scope.row)">
									{{ $t("common.delete") }}
								</div>
								<!-- 复制 -->
								<div class="dropdown-item" @click="onCopy(scope.row)">
									{{ $t("common.copy") }}
								</div>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</el-space>
			</template>
		</ProTable>
		<add-role-dialog ref="formDialogRef" />
		<data-scope-dialog ref="dataScopeDialogRef" />
	</div>
</template>

<script setup lang="ts" name="system_role">
import { ref } from "vue";
import { getRoleList, addRoleOne, updateRoleOne, deleteRoleOne, copyRoleBatch, copyRoleOne } from "@/api/modules/system";
import { ColumnProps } from "@/components/ProTable/interface";
import { SystemVo } from "@/typings/modules/system";
import { roleTypeStatusList } from "@/utils/serviceDict";
import useFormat18n from "@/hooks/useFormat18n";
import AddRoleDialog from "./components/AddRoleDialog.vue";
import DataScopeDialog from "./components/DataScopeDialog.vue";
import { useHandleData } from "@/hooks/useHandleData";
import useBatchNotify from "@/hooks/useBatchNotify";

const pageTitle = useFormat18n("system.role");
const proTable = ref();
const formDialogRef = ref();
const dataScopeDialogRef = ref();

// 表格配置项
const columns: ColumnProps[] = [
	{ type: "selection", fixed: "left", width: 60 },
	{
		prop: "name",
		label: useFormat18n(["system.role", "common.name"]),
		search: { el: "input" }
	},
	{
		prop: "roleType",
		label: useFormat18n(["system.role", "common.type"]),
		enum: roleTypeStatusList
	},
	{
		prop: "createdAt",
		label: useFormat18n("common.createdAt")
	},
	{
		prop: "remark",
		label: useFormat18n("common.remark")
	},
	{ prop: "operation", label: useFormat18n("common.operate"), fixed: "right", width: 200 }
];

// 新增/编辑
const openDialog = (type: "update" | "add", rowData?: SystemVo.RoleVo) => {
	if (type === "update" && rowData) {
		delete rowData.createdAt;
		delete rowData.updatedAt;
	}
	let params = {
		title: useFormat18n(`common.${type}`) + pageTitle,
		rowData: {
			...(rowData || {
				rowType: "SUPER"
			})
		},
		formType: type,
		api: type === "add" ? addRoleOne : updateRoleOne,
		getTableList: proTable.value.getTableList
	};
	formDialogRef.value.acceptParams(params);
};

// 删除
const onDelete = async (rowData: SystemVo.RoleVo) => {
	await useHandleData(deleteRoleOne, { id: rowData.id || 0 }, useFormat18n(["common.delete", "system.role"]));
	proTable.value.getTableList();
};

// 复制
const onCopy = async (rowData: SystemVo.RoleVo) => {
	await useHandleData(copyRoleOne, { id: rowData.id || 0 }, useFormat18n("common.copy"));
	proTable.value.getTableList();
};

// 处理批量操作
const handleBatch = async (api: (param: any) => Promise<any>, params: any, tips: string) => {
	try {
		const res: any = await useHandleData(api, params, tips);
		proTable.value.getTableList();
		proTable.value.clearSelection();
		if (res.status === 0) {
			useBatchNotify(res.data);
		}
	} catch (error) {}
};

// 批量操作
const onBatch = (command: string, selectedList: SystemVo.RoleVo[]) => {
	const ids: number[] = selectedList.map((row: SystemVo.RoleVo) => row.id || 0);
	let params = {
		ids
	};
	switch (command) {
		case "copy":
			handleBatch(copyRoleBatch, params, useFormat18n("common.copy"));
			break;
		default:
			break;
	}
};
</script>

<style lang="scss"></style>
