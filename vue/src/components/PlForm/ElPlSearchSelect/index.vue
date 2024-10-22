<template>
	<div class="el-pl-search-select">
		<el-popover placement="bottom-start" :hide-after="50" width="335px" trigger="click">
			<template #reference>
				<el-input
					readonly
					v-model="selectedLabels"
					:placeholder="placeholder"
					:style="{
						transform
					}"
				>
				</el-input>
			</template>
			<div class="pl-search-select-body-wrap">
				<el-select :teleported="false" v-model="mode" style="width: 100px" @change="onChange">
					<el-option
						@click.stop=""
						v-for="secItem in inValuesList"
						:key="secItem.value"
						:label="secItem.label"
						:value="secItem.value"
					/>
				</el-select>
				<el-select
					multiple
					:clearable="clearable"
					collapse-tags
					collapse-tags-tooltip
					v-model="selectedValues"
					style="width: 200px"
					:teleported="false"
					filterable
					:placeholder="placeholder"
					@change="onChange"
				>
					<el-option v-for="secItem in options" :key="secItem.value" :label="secItem.label" :value="secItem.value" />
				</el-select>
			</div>
		</el-popover>
	</div>
</template>
<script setup lang="ts" name="ElPlSearchSelect">
import { ref, computed, watch } from "vue";
import { inValuesList } from "@/utils/serviceDict";
interface ElPlSearchSelectProp {
	placeholder?: string;
	options?: any[];
	modelValue?: string;
	clearable?: boolean;
	transform?: string;
}
const props = withDefaults(defineProps<ElPlSearchSelectProp>(), {
	placeholder: "",
	modelValue: "",
	clearable: true,
	options: () => [],
	transform: "translateY(-1px)"
});
const emit = defineEmits(["update:modelValue"]);

const mode = ref("in"); // 匹配模式 // in notIn
const selectedValues = ref<any[]>([]);
const selectedLabels = computed(() => {
	const labelList = props.options.filter(item => selectedValues.value.includes(item.value)).map(item => item.label);
	return labelList.join("、");
});

watch(
	() => props.modelValue,
	newVal => {
		if (!newVal) {
			selectedValues.value = [];
		} else {
			const splitArr = newVal.split("&");
			setTempValue(splitArr);
		}
	}
);

const getStringAfterEqual = (str: string): string => {
	const parts = str.split("=");
	if (parts.length > 1) {
		return parts[1];
	}
	return "";
};

const setTempValue = (splitArr: string[]) => {
	const modeStr = (splitArr && splitArr[0]) || "";
	const valueStr = (splitArr && splitArr[1]) || "";
	if (modeStr.indexOf("mode") !== -1) {
		const newMode = getStringAfterEqual(modeStr);
		if (newMode && newMode !== mode.value) {
			mode.value = newMode;
		}
	}
	if (valueStr) {
		const newValueStr = getStringAfterEqual(valueStr);
		if (newValueStr) {
			let valueList: any[] = newValueStr.split(",");
			// 数组中是纯数字
			if (valueList.every(item => /^\d+$/.test(item))) {
				valueList = valueList.map(item => parseInt(item));
			}
			selectedValues.value = valueList;
		}
	}
};

const onChange = () => {
	const idStr = selectedValues.value.join(",");
	const val = `mode=${mode.value}&value=${idStr}`;
	emit("update:modelValue", val);
};
</script>
<style lang="scss">
.pl-search-select-body-wrap {
	display: flex;
	justify-content: space-between;
}
</style>
