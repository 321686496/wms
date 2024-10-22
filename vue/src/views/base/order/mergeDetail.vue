<template>
	<div class="table-box card merge_detail_page">
		<el-space>
			<el-button type="primary" @click="onBack">{{ useFormat18n("common.back") }}</el-button>
			<el-button type="primary" @click="onExport">{{ useFormat18n("common.export") }}</el-button>
		</el-space>
		<primary-line type="primary" />
		<div class="table">
			<vxe-table
				border
				align="center"
				:column-config="{ resizable: true }"
				:scroll-y="{ enabled: false }"
				:data="tableData"
				:row-config="{ isCurrent: true, isHover: true }"
				:span-method="mergeRowMethod"
			>
				<vxe-column v-for="(col, colIndex) in headColumns" :key="colIndex" :field="col.field" :title="col.title"></vxe-column>
			</vxe-table>
		</div>
	</div>
</template>

<script setup lang="ts" name="base_order_merge_detail">
import { computed, onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { getReportOrderPdfOne, getMergeReportOrderOne } from "@/api/modules/order";
import { GlobalStore } from "@/stores";
import router from "@/routers";
import useFormat18n from "@/hooks/useFormat18n";

const route = useRoute();
const globalStore = GlobalStore();
const id = computed(() => (route.query && route.query.id) || "");

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
			window.location.href = url;
		}
	});
};

// 通用行合并函数（将相同多列数据合并为一行）
const mergeRowMethod: any = ({ row, _rowIndex, column, visibleData }: any) => {
	const fields = ["field1"];
	const cellValue = row[column.field];
	if (cellValue && fields.includes(column.field)) {
		const prevRow = visibleData[_rowIndex - 1];
		let nextRow = visibleData[_rowIndex + 1];
		if (prevRow && prevRow[column.field] === cellValue) {
			return { rowspan: 0, colspan: 0 };
		} else {
			let countRowspan = 1;
			while (nextRow && nextRow[column.field] === cellValue) {
				nextRow = visibleData[++countRowspan + _rowIndex];
			}
			if (countRowspan > 1) {
				return { rowspan: countRowspan, colspan: 1 };
			}
		}
	}
};

const tableData = ref<any[]>([]);
const headColumns = ref<any[]>([]);

const handleTable = (tableHead: string[], dataList: any[]) => {
	headColumns.value = tableHead.map((headStr: string, headIndex: number) => ({
		title: headStr,
		field: `field${headIndex + 1}`
	}));
	let allTableData: any[] = [];
	for (let i = 0; i < dataList.length; i++) {
		let row: any = {};
		const rowList = dataList[i];
		for (let j = 0; j < rowList.length; j++) {
			row[`field${j + 1}`] = rowList[j];
		}
		allTableData.push(row);
	}
	tableData.value = allTableData;
};

const getOne = () => {
	getMergeReportOrderOne({ id: id.value }).then((res: any) => {
		if (res.data) {
			const { tableHead, items } = res.data;
			handleTable(tableHead || [], items || []);
		}
	});
};

onMounted(() => {
	getOne();
});
</script>

<style lang="scss" scoped>
.merge_detail_page {
	:deep(.vxe-cell) {
		font-weight: bold;
	}
}
</style>
