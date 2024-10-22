<template>
	<div class="el-pl-search-select">
		<el-popover placement="bottom-start" :hide-after="50" width="335px" trigger="click">
			<template #reference>
				<el-input
					v-model="selectedValues[0].keyword"
					:clearable="clearable"
					:style="{
						transform
					}"
					:placeholder="placeholder"
					@input="onUpdateBindValue"
				>
				</el-input>
			</template>
			<div>
				<div v-for="(item, index) in selectedValues" :key="index">
					<div class="pl-search-select-body-wrap" v-if="index > 0">
						<el-select
							:teleported="false"
							v-model="mode"
							style="width: 80px; margin-right: 3px"
							:disabled="index > 1"
							@change="onUpdateBindValue"
						>
							<el-option
								@click.stop=""
								v-for="secItem in andOrValuesList"
								:key="secItem.value"
								:label="secItem.label"
								:value="secItem.value"
							/>
						</el-select>
						<el-input
							style="flex: 1"
							v-model="item.keyword"
							clearable
							:placeholder="$t('common.pleaseInput')"
							@input="onUpdateBindValue"
						></el-input>
						<div class="del_icon flx-center" :class="{ del_disabled: index == 1 }" @click="onDeleteRow(index)">
							<el-icon><Close /></el-icon>
						</div>
					</div>
				</div>
				<el-space style="margin-top: 5px">
					<el-button size="small" type="primary" @click="onAddCondition">{{
						useFormat18n(["common.add", "common.condition"])
					}}</el-button>
				</el-space>
			</div>
		</el-popover>
	</div>
</template>
<script setup lang="ts" name="ElPlSearchInput">
import { ref, computed, watch } from "vue";
import { andOrValuesList } from "@/utils/serviceDict";
import useFormat18n from "@/hooks/useFormat18n";

interface ElPlSearchSelectProp {
	placeholder?: string;
	modelValue?: string;
	clearable?: boolean;
	transform?: string;
}
const props = withDefaults(defineProps<ElPlSearchSelectProp>(), {
	placeholder: "",
	modelValue: "",
	clearable: true,
	transform: "translateY(-1px)"
});
const emit = defineEmits(["update:modelValue"]);

const bindValue = computed({
	get() {
		return props.modelValue;
	},
	set(value) {
		emit("update:modelValue", value);
	}
});

watch(
	() => props.modelValue,
	newVal => {
		if (!newVal) {
			selectedValues.value = [{ keyword: "" }, { keyword: "" }];
		}
	}
);

const mode = ref<"&" | "|">("&");

const selectedValues = ref<{ keyword: string }[]>([{ keyword: "" }, { keyword: "" }]);

const selectedLabel = computed(() => {
	const keywords = selectedValues.value.filter(item => !!item.keyword).map(item => item.keyword.trim());
	return keywords.join(mode.value);
});

const onDeleteRow = (index: number) => {
	if (index > 1) {
		selectedValues.value.splice(index, 1);
		onUpdateBindValue();
	}
};

const onUpdateBindValue = () => {
	bindValue.value = selectedLabel.value;
};

const onAddCondition = () => {
	selectedValues.value.push({ keyword: "" });
};
</script>
<style lang="scss">
.pl-search-select-body-wrap {
	display: flex;
	align-items: center;
	margin-bottom: 5px;
	.del_icon {
		padding: 0 4px;
		cursor: pointer;
	}
	.del_disabled {
		cursor: not-allowed;
	}
}
</style>
