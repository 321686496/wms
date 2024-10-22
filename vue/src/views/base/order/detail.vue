<template>
	<div class="table-box page-card detail_page">
		<ProTable
			ref="proTable"
			:pagination="false"
			:title="useFormat18n('base.product')"
			:columns="columns"
			:initParam="{ pid: id }"
			:requestApi="getReportOrderItem"
		>
			<template #tableHeader>
				<el-space>
					<el-button type="primary" @click="onBack">{{ useFormat18n("common.back") }}</el-button>
					<el-button type="primary" @click="onExport">{{ useFormat18n("common.export") }}</el-button>
				</el-space>
			</template>
			<template #materialName="scope">
				<span>{{ `${scope.row.materialName}(${scope.row.materialCode})` }}</span>
			</template>
		</ProTable>
	</div>
</template>

<script setup lang="ts" name="base_order_detail">
import { ref, computed } from "vue";
import { useRoute } from "vue-router";
import { getReportOrderItem, getReportOrderPdfOne } from "@/api/modules/order";
import { GlobalStore } from "@/stores";
import router from "@/routers";
import useFormat18n from "@/hooks/useFormat18n";

const route = useRoute();
const globalStore = GlobalStore();
const id = computed(() => (route.query && route.query.id) || "");

// 表格配置项
const columns = ref<ColumnProps[]>([
	{
		prop: "shopName",
		label: useFormat18n("base.shopName")
	},
	{
		prop: "materialName",
		label: useFormat18n("base.productName")
	},
	{
		prop: "materialCode",
		label: useFormat18n("base.productCode")
	},
	{
		prop: "typeId",
		label: useFormat18n("type.id")
	},
	{
		prop: "typeName",
		label: useFormat18n("base.typeName")
	},
	{
		prop: "stockNumber",
		label: useFormat18n("base.stockNumber")
	},
	{
		prop: "reportNumber",
		label: useFormat18n("base.reportNumber")
	},
	{
		prop: "remark",
		label: useFormat18n("common.remark")
	}
]);

const onBack = () => {
	router.back();
};

const language = computed((): string => globalStore.language);

const onExport = () => {
	getReportOrderPdfOne({
		id: id.value,
		language: language.value
	}).then((res: any) => {
		const url: string = res.data || "";
		if (url) {
			// 跳转并发送url请求
			window.location.href = url;
		}
	});
};
</script>

<style lang="scss" scoped>
.detail_page {
	:deep(.cell) {
		font-weight: bold;
	}
}
</style>
