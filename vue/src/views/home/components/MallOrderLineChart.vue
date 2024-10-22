<template>
	<div class="card line_block pb0">
		<div class="pb12 flx-justify-between">
			<card-title title="商城订单统计" />
			<el-space>
				<el-date-picker
					v-model="dateTimeArr"
					:start-placeholder="$t('common.startTime')"
					:end-placeholder="$t('common.endTime')"
					:clearable="true"
					format="YYYY-MM-DD"
					value-format="YYYY-MM-DD"
					style="width: 200px"
					type="daterange"
					size="small"
					:shortcuts="datePickerShortcuts"
				/>
				<el-button type="primary" size="small" @click="getData">{{ $t("common.search") }}</el-button>
			</el-space>
		</div>
		<div class="line_echarts_wrap">
			<ECharts :option="option" />
		</div>
	</div>
</template>
<script setup lang="ts" name="MallOrderLineChart">
import { onMounted, ref } from "vue";
import { getHomeMallOrderStatistics } from "@/api/modules/home";
import { ECOption } from "@/components/ECharts/config";
import ECharts from "@/components/ECharts/index.vue";
import { initDatePickerShortcuts } from "@/utils/util";

const { datePickerShortcuts } = initDatePickerShortcuts();
const option = ref<ECOption>({
	tooltip: {
		trigger: "axis",
		axisPointer: {
			type: "cross",
			label: {
				backgroundColor: "#6a7985"
			}
		}
	},
	legend: {
		data: ["订单量", "订单金额"],
		selectedMode: "single",
		textStyle: {
			color: "#a1a1a1"
		}
	},
	grid: {
		left: "3%",
		right: "8%",
		bottom: "3%",
		containLabel: true
	},
	xAxis: [
		{
			type: "category",
			boundaryGap: false,
			data: [],
			axisLabel: {
				color: "#a1a1a1"
			}
		}
	],
	yAxis: [
		{
			type: "value",
			axisLabel: {
				color: "#a1a1a1"
			},
			splitNumber: 6
		},
		{
			type: "value",
			axisLabel: {
				color: "#a1a1a1"
			},
			splitNumber: 6
		}
	],
	series: [
		{
			name: "订单量",
			type: "line",
			stack: "Total",
			areaStyle: {},
			emphasis: {
				focus: "series"
			},
			data: []
		},
		{
			name: "订单金额",
			type: "line",
			stack: "Total",
			areaStyle: {},
			emphasis: {
				focus: "series"
			},
			data: []
		}
	]
});

const dateTimeArr = ref([]);

const getData = () => {
	const param: any = {};
	if (dateTimeArr.value && dateTimeArr.value[0] && dateTimeArr.value[1]) {
		param.start = dateTimeArr.value[0];
		param.end = dateTimeArr.value[1];
	}
	getHomeMallOrderStatistics(param).then((res: any) => {
		if (option.value.xAxis && option.value.xAxis[0]) {
			option.value.xAxis[0].data = res.data.map((v: any) => v.date);
		}
		if (option.value.series && option.value.series[0]) {
			option.value.series[0].data = res.data.map((v: any) => v.count);
		}
		if (option.value.series && option.value.series[1]) {
			option.value.series[1].data = res.data.map((v: any) => v.amount);
		}
	});
};

onMounted(() => {
	getData();
});
</script>
<style lang="scss">
@import "./home.scss";
</style>
