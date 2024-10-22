<template>
	<div class="table-box">
		<ProTable ref="proTable" :title="pageTitle" :pagination="false" :indent="20" :columns="columns" :requestApi="getMenuList">
			<template #tableHeader>
				<el-button type="primary" @click="openDialog('add')">{{ $t("common.add") }}</el-button>
			</template>
			<template #name="scope">
				<span>{{ scope.row.name ? $t(`menu.${scope.row.name}`) : "" }}</span>
			</template>
			<template #metaIcon="scope">
				<el-icon v-if="scope.row.metaIcon">
					<component :is="scope.row.metaIcon"></component>
				</el-icon>
			</template>
			<template #enable="scope">
				<el-switch v-model="scope.row.enable" @change="onEnableChange($event, scope.row)" />
			</template>
			<!-- 表格操作 -->
			<template #operation="scope">
				<el-button type="primary" size="small" @click="openDialog('update', { ...scope.row })">{{
					$t("common.update")
				}}</el-button>
				<el-button type="danger" size="small" @click="onDelete(scope.row)">{{ $t("common.delete") }}</el-button>
			</template>
		</ProTable>
		<add-menu-dialog ref="formDialogRef" />
	</div>
</template>

<script setup lang="ts" name="system_menu">
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { getMenuList, deleteMenuOne, addMenuOne, updateMenuOne, enableMenuOne } from "@/api/modules/system";
import { ColumnProps } from "@/components/ProTable/interface";
import { SystemVo } from "@/typings/modules/system";
import { MenuTypeEnum } from "@/enums/systemEnum";
import { ArrayStringEnum } from "@/utils/util";
import { YesNoStatusList } from "@/utils/serviceDict";
import { useHandleData } from "@/hooks/useHandleData";
import useFormat18n from "@/hooks/useFormat18n";
import AddMenuDialog from "./components/AddMenuDialog.vue";

let menuTypeList = ArrayStringEnum(MenuTypeEnum);
const pageTitle = useFormat18n("system.menu");
const proTable = ref();
const formDialogRef = ref();

// 表格配置项
const columns: ColumnProps<SystemVo.MenuVo>[] = [
	{
		prop: "name",
		label: "名称",
		align: "left",
		fixed: "left",
		width: 220,
		search: { el: "input" }
	},
	{
		prop: "menuType",
		label: "类型",
		tag: true,
		enum: menuTypeList
	},
	{
		prop: "metaIcon",
		label: "图标",
		width: 70
	},
	{
		prop: "path",
		label: "路径"
	},
	{
		prop: "perms",
		label: "权限标识"
	},
	{
		prop: "priority",
		label: "排序值",
		sortable: true
	},
	{
		prop: "enable",
		label: "是否启用"
	},
	{
		prop: "metaIsKeepAlive",
		label: "是否缓存",
		tag: true,
		enum: YesNoStatusList
	},
	{ prop: "operation", label: "操作", fixed: "right", width: 180 }
];

// 删除
const onDelete = async (rowData: SystemVo.MenuVo) => {
	await useHandleData(deleteMenuOne, { id: rowData.id || 0 }, useFormat18n(["common.delete", "system.menu"]));
	proTable.value.getTableList();
};

// 新增/编辑
const openDialog = (type: "update" | "add", rowData?: SystemVo.MenuVo) => {
	if (type === "update" && rowData) {
		delete rowData.children;
		delete rowData.createdAt;
		delete rowData.updatedAt;
	}
	let params = {
		title: useFormat18n(`common.${type}`) + pageTitle,
		rowData: {
			...(rowData || {})
		},
		formType: type,
		api: type === "add" ? addMenuOne : updateMenuOne,
		// getTableList: proTable.value.getTableList
		getTableList: () => {}
	};
	formDialogRef.value.acceptParams(params);
};

// 禁用/启用
const onEnableChange = (val: boolean, rowData: SystemVo.MenuVo) => {
	enableMenuOne({
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
