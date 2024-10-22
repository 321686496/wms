<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			:dataCallback="dataCallback"
			:pagination="false"
			:title="useFormat18n('base.product')"
			:columns="columns"
			:requestApi="getSkuSelect"
		>
			<template #tableHeader>
				<el-space>
					<el-button type="primary" @click="onBack">{{ useFormat18n("common.back") }}</el-button>
					<el-input v-model="searchValue" :placeholder="useFormat18n('base.productName')"></el-input>
					<p v-if="id">
						<span>{{ useFormat18n("common.billCode") }}：</span>
						<span>{{ billCode }}</span>
					</p>
				</el-space>
			</template>
			<template #materialName="scope">
				<el-tooltip effect="dark" trigger="click" placement="top-start" :content="scope.row.materialCode">
					<span>{{ scope.row.materialName }}</span>
				</el-tooltip>
			</template>
			<template #stockNumber="scope">
				<el-input-number v-model="scope.row.stockNumber" :min="0" style="width: 100px" />
			</template>
			<template #reportNumber="scope">
				<el-input-number v-model="scope.row.reportNumber" :min="0" style="width: 100px" />
			</template>
			<!-- <template #remark="scope">
				<el-input v-model="scope.row.remark" :placeholder="useFormat18n('common.remark')" style="width: 140px" />
			</template> -->
		</ProTable>
		<div class="flx-justify-between pt8">
			<div></div>
			<el-space>
				<el-button type="primary" @click="onSave">{{ $t("common.save") }}</el-button>
			</el-space>
		</div>
	</div>
</template>

<script setup lang="ts" name="base_order_add">
import { ref, computed, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import { getSkuSelect } from "@/api/modules/base";
import { saveReportOrderOne, getReportOrderItem, deleteReportOrderOne } from "@/api/modules/order";
import { ColumnProps } from "@/components/ProTable/interface";
import { useHandleData } from "@/hooks/useHandleData";
import router from "@/routers";
import dayjs from "dayjs";
import useFormat18n from "@/hooks/useFormat18n";
// import { ElLoading } from "element-plus";

const proTable = ref();
const route = useRoute();
const id = computed(() => (route.query && route.query.id) || "");
const billCode = computed(() => (route.query && route.query.bc) || "");

const searchValue = ref("");
// item list
const itemList = ref([]);
const showItemList = ref([]);

// 根据原列表数据更新显示列表
watch(
	() => itemList.value,
	val => {
		showItemList.value = [...val];
	}
);
watch(
	() => showItemList.value,
	val => {
		proTable.value.tableData = [...val];
	}
);
// 搜索
watch(
	() => searchValue.value,
	val => {
		showItemList.value = itemList.value.filter((row: any) => {
			// 不区分字母大小写
			return row.materialName.toLowerCase().indexOf(val.toLowerCase()) > -1;
		});
	}
);

// 表格配置项
const columns = ref<ColumnProps[]>([
	{
		prop: "materialName",
		label: useFormat18n("base.productName")
		// search: { el: "input" }
	},
	// {
	// 	prop: "materialCode",
	// 	label: useFormat18n("base.productCode"),
	// 	minWidth: 120
	// },
	{
		prop: "stockNumber",
		label: useFormat18n("base.stockNumber"),
		showOverflowTooltip: false
	},
	{
		prop: "reportNumber",
		label: useFormat18n("base.reportNumber"),
		showOverflowTooltip: false
	}
	// {
	// 	prop: "remark",
	// 	label: useFormat18n("common.remark"),
	// 	minWidth: 120
	// }
]);

const dataCallback = (data: any) => {
	data.data.forEach((row: any) => {
		row.materialId = row.id;
		delete row.id;
		row.materialName = row.name;
		delete row.name;
		row.materialCode = row.code;
		delete row.code;
		row.reportNumber = 0;
		row.stockNumber = 0;
	});
	itemList.value = data.data;
	return data;
};

const onSave = async () => {
	// const itemList = proTable.value.tableData
	const list = itemList.value
		.filter((row: any) => row.reportNumber != 0 || row.stockNumber != 0)
		.map((row: any) => ({
			materialId: row.materialId,
			materialName: row.materialName,
			materialCode: row.materialCode,
			stockNumber: row.stockNumber,
			reportNumber: row.reportNumber,
			remark: row.remark
		}));
	const params = {
		billDate: dayjs(new Date()).format("YYYY-MM-DD"),
		itemList: list
	};
	await useHandleData(saveReportOrderOne, params, useFormat18n(`common.save`));
	if (id.value) {
		await deleteReportOrderOne({ id: id.value });
	}
	onBack();
};

const onBack = () => {
	router.back();
};

const getOrderOne = () => {
	// ElLoading.service({
	// 	lock: true,
	// 	text: "加载中",
	// 	background: "rgba(0, 0, 0, 0.7)"
	// });
	const interval = setInterval(() => {
		if (!proTable.value.tableData.length) return;
		getReportOrderItem({
			pid: id.value
		}).then((res: any) => {
			const list = res.data || [];
			list.forEach((item: any) => {
				const hasIndex = proTable.value.tableData.findIndex((row: any) => row.materialCode == item.materialCode);
				if (hasIndex > -1) {
					proTable.value.tableData[hasIndex].reportNumber = item.reportNumber;
					proTable.value.tableData[hasIndex].stockNumber = item.stockNumber;
				}
			});
			clearInterval(interval);
		});
	}, 500);
};

onMounted(() => {
	if (id.value) {
		getOrderOne();
	}
});
</script>

<style lang="scss"></style>
