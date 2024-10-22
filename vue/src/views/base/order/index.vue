<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			colSettingType="base_order"
			:title="pageTitle"
			:columns="columns"
			:initParam="{ isMerge: false }"
			:requestApi="getReportOrderList"
		>
			<template #tableHeader="scope">
				<el-space>
					<el-button type="primary" @click="onAdd">{{ useFormat18n("common.add") }}</el-button>
					<el-dropdown :disabled="!scope.isSelected" size="default">
						<el-button type="primary" :disabled="!scope.isSelected">
							{{ useFormat18n("common.batch") }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
						</el-button>
						<template #dropdown>
							<el-dropdown-menu>
								<div class="dropdown-item" @click="onBatch('delete', scope.selectedList)">
									{{ useFormat18n("common.delete") }}
								</div>
								<div class="dropdown-item" @click="onBatch('merge', scope.selectedList)">
									{{ useFormat18n("common.merge") }}
								</div>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</el-space>
			</template>
			<!-- 表格操作 -->
			<template #operation="scope">
				<el-space>
					<el-button type="primary" size="small" @click="onDto(scope.row)">{{ useFormat18n("common.detail") }}</el-button>

					<el-dropdown size="default">
						<el-button type="success" size="small">
							{{ $t("common.more") }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
						</el-button>
						<template #dropdown>
							<el-dropdown-menu>
								<!-- 编辑 -->
								<div class="dropdown-item" @click="onEdit(scope.row)">
									{{ $t("common.edit") }}
								</div>
								<!-- 删除 -->
								<div class="dropdown-item" @click="onDelete(scope.row)">
									{{ $t("common.delete") }}
								</div>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</el-space>
			</template>
		</ProTable>
	</div>
</template>

<script setup lang="ts" name="base_order">
import { ref } from "vue";
import { getReportOrderList, deleteReportOrderOne, deleteReportOrderBatch, mergeReportOrder } from "@/api/modules/order";
import { ColumnProps } from "@/components/ProTable/interface";
import { useHandleData } from "@/hooks/useHandleData";
import router from "@/routers";
import useBatchNotify from "@/hooks/useBatchNotify";
import useFormat18n from "@/hooks/useFormat18n";

const pageTitle = useFormat18n("base.order");
const proTable = ref();

// 表格配置项
const columns = ref<ColumnProps[]>([
	{ type: "selection", fixed: "left", width: 50 },
	{ prop: "operation", label: useFormat18n("common.operate"), width: 150 },
	{
		prop: "billDate",
		label: useFormat18n("common.billDate"),
		minWidth: 120
	},
	{
		prop: "billCode",
		label: useFormat18n("common.billCode"),
		minWidth: 120,
		search: { el: "input" }
	},
	{
		prop: "createAdminName",
		minWidth: 120,
		label: useFormat18n("common.createName")
	}
]);

// 新增
const onAdd = () => {
	router.push("/base/order/add");
};

// 详情
const onDto = (row: any) => {
	router.push({
		path: "/base/order/detail",
		query: {
			id: row.id
		}
	});
};

// 删除
const onDelete = async (rowData: any) => {
	await useHandleData(deleteReportOrderOne, { id: rowData.id || 0 }, useFormat18n("common.delete"));
	proTable.value.getTableList();
};

// 批量删除
const handleDeleteBatch = async (ids: number[]) => {
	try {
		const res: any = await useHandleData(deleteReportOrderBatch, { ids }, useFormat18n("common.delete"));
		proTable.value.getTableList();
		proTable.value.clearSelection();
		if (res.status === 0) {
			useBatchNotify(res.data);
		}
	} catch (error) {}
};

// 批量合并
const handleMergeBatch = async (ids: number[]) => {
	try {
		const res: any = await useHandleData(mergeReportOrder, { ids }, useFormat18n("common.merge"));
		proTable.value.getTableList();
		proTable.value.clearSelection();
		if (res.status === 0) {
			useBatchNotify(res.data);
		}
	} catch (error) {}
};

// 批量操作
const onBatch = (command: string, selectedList: any[]) => {
	switch (command) {
		case "delete":
			handleDeleteBatch(selectedList.map((row: any) => row.id || 0));
			break;
		case "merge":
			handleMergeBatch(selectedList.map((row: any) => row.id || 0));
			break;
		default:
			break;
	}
};

const onEdit = (row: any) => {
	router.push({
		path: "/base/order/add",
		query: {
			id: row.id,
			bc: row.billCode
		}
	});
};
</script>

<style lang="scss"></style>
