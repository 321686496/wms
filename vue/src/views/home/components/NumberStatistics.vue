<template>
	<div class="card fund_statistics">
		<div class="pb12 flx-justify-between">
			<card-title title="数据统计" />
			<el-space>
				<el-radio-group class="hidden-lg-and-down" v-model="dateButton" size="small" @change="onButtonChange">
					<el-radio-button v-for="secItem in dateButtonList" :key="secItem.value" :label="secItem.value">{{
						secItem.label
					}}</el-radio-button>
				</el-radio-group>
				<el-date-picker
					v-model="dateTimeArr"
					:start-placeholder="$t('common.startTime')"
					:end-placeholder="$t('common.endTime')"
					:clearable="true"
					format="YYYY-MM-DD"
					value-format="YYYY-MM-DD"
					style="width: 180px"
					type="daterange"
					size="small"
					:shortcuts="datePickerShortcuts"
				/>
				<el-button type="primary" size="small" @click="getData">{{ $t("common.search") }}</el-button>
			</el-space>
		</div>
		<div class="statistics_item_list">
			<div class="card statistics_item">
				<span class="statistics_item_label">商品数量</span>
				<span class="statistics_item_value">{{ countData.goodsCount }}</span>
			</div>
			<div class="card statistics_item">
				<span class="statistics_item_label">商城订单数量</span>
				<span class="statistics_item_value">{{ countData.mallOrderCount }}</span>
			</div>
			<div class="statistics_item">
				<span class="statistics_item_label">商城订单金额</span>
				<span class="statistics_item_value">{{ countData.mallOrderAmount }}</span>
			</div>
			<div class="statistics_item">
				<span class="statistics_item_label">服务订单数量</span>
				<span class="statistics_item_value">{{ countData.serviceOrderCount }}</span>
			</div>
			<div class="statistics_item">
				<span class="statistics_item_label">服务打赏金额</span>
				<span class="statistics_item_value">{{ countData.serviceOrderAmount }}</span>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts" name="NumberStatistics">
import { onMounted, ref } from "vue";
import useRecentDate from "../hooks/useRecentDate";
import { getHomeNumberStatistics } from "@/api/modules/home";
import { initDatePickerShortcuts } from "@/utils/util";

const { datePickerShortcuts } = initDatePickerShortcuts();

const dateTimeArr = ref<string[]>([]);
const countData = ref({
	goodsCount: 0,
	mallOrderCount: 0,
	serviceOrderCount: 0,
	mallOrderAmount: 0,
	serviceOrderAmount: 0
});

/**
 * 日期范围筛选按钮
 */
const dateButton = ref<string | null>("");
const dateButtonList = ref<treeOption[]>([]);
// 初始化日期按钮列表
const initDateButtonList = () => {
	dateButtonList.value = useRecentDate();
	dateButton.value = dateButtonList.value[0].value as string;
	dateTimeArr.value = dateButton.value.split("~");
	getData();
};
const onButtonChange = (val: any) => {
	dateTimeArr.value = val.split("~");
	getData();
};

const deptId = ref<number | undefined>(undefined);

const getData = () => {
	const param: any = {};
	const dateTimeList = Array.isArray(dateTimeArr.value) ? dateTimeArr.value : [];
	if (dateTimeList[0] && dateTimeList[1]) {
		const start = dateTimeList[0];
		const end = dateTimeList[1];
		param.start = start;
		param.end = end;
		const hasButton = dateButtonList.value.find(item => item.value == `${start}~${end}`);
		if (hasButton) {
			dateButton.value = hasButton.value as string;
		} else {
			dateButton.value = null;
		}
	} else {
		dateButton.value = "";
	}
	if (deptId.value) {
		param.deptId = deptId.value;
	}
	getHomeNumberStatistics(param).then((res: any) => {
		countData.value = res.data;
	});
};

onMounted(() => {
	initDateButtonList();
});
</script>

<style lang="scss">
@import "./home.scss";
</style>
