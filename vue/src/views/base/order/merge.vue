<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			colSettingType="base_order_merge"
			:title="pageTitle"
			:columns="columns"
			:initParam="{ isMerge: true }"
			:requestApi="getReportOrderList"
			showSummary
		>
			<template #tableHeader="scope">
				<el-space>
					<el-dropdown :disabled="!scope.isSelected" size="default">
						<el-button type="primary" :disabled="!scope.isSelected">
							{{ useFormat18n("common.batch") }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
						</el-button>
						<template #dropdown>
							<el-dropdown-menu>
								<div class="dropdown-item" @click="onBatch('delete', scope.selectedList)">
									{{ useFormat18n("common.delete") }}
								</div>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</el-space>
			</template>
			<!-- 表格操作 -->
			<template #operation="scope">
				<el-button type="primary" size="small" @click="onDto(scope.row)">{{ useFormat18n("common.detail") }}</el-button>
				<el-button type="danger" size="small" @click="onDelete(scope.row)">{{ useFormat18n("common.delete") }}</el-button>
			</template>
		</ProTable>
	</div>
</template>

<script setup lang="ts" name="base_order_merge">
import { ref } from "vue";
import { getReportOrderList, deleteReportOrderOne, deleteReportOrderBatch } from "@/api/modules/order";
import { ColumnProps } from "@/components/ProTable/interface";
import { useHandleData } from "@/hooks/useHandleData";
import router from "@/routers";
import useBatchNotify from "@/hooks/useBatchNotify";
import useFormat18n from "@/hooks/useFormat18n";

const pageTitle = useFormat18n("base.order");
const proTable = ref();

// 表格配置项
const columns = ref<ColumnProps[]>([
	{ type: "selection", fixed: "left", width: 60 },
	{ prop: "operation", label: useFormat18n("common.operate"), width: 180 },
	{
		prop: "billDate",
		label: useFormat18n("common.billDate"),
		minWidth: 120
	},
	{
		prop: "billCode",
		label: useFormat18n("common.billCode"),
		minWidth: 130,
		search: { el: "input" }
	},
	{
		prop: "createAdminName",
		minWidth: 150,
		label: useFormat18n("common.createName")
	}
]);

// 详情
const onDto = (row: any) => {
	router.push({
		path: "/base/order/mergeDetail",
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

// 批量操作
const onBatch = (command: string, selectedList: any[]) => {
	switch (command) {
		case "delete":
			handleDeleteBatch(selectedList.map((row: any) => row.id || 0));
			break;
		default:
			break;
	}
};
</script>

<style lang="scss"></style>
