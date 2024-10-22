<template>
	<vxe-pulldown v-model="showPull" transfer>
		<template #default>
			<el-input
				v-model="labelName"
				:clearable="clearable"
				:placeholder="placeholder"
				:style="{ width: width }"
				@focus="show"
				@clear="onClear"
				@input="onSearchInput"
			>
				<template #append v-if="canAdd">
					<el-button type="primary" :icon="Plus" @click="onAdd" />
				</template>
			</el-input>
		</template>
		<template #dropdown>
			<el-cascader-panel
				ref="cascaderPanelRef"
				v-model="cascaderPanelValue"
				:options="cascaderOptions"
				v-bind="$attrs"
				@change="onCascaderPanelChange"
			/>
		</template>
	</vxe-pulldown>
</template>
<script setup lang="ts" name="ElPlCascader">
import { ref, computed } from "vue";
import { Plus } from "@element-plus/icons-vue";
import useFormat18n from "@/hooks/useFormat18n";
import { valueTransInt, getFlatArr } from "@/utils/util";

interface ElPlCascaderProp {
	modelValue?: any;
	label?: string;
	mode: "select" | "cascader";
	placeholder?: string;
	clearable?: boolean;
	canAdd?: boolean;
	width?: string;
	options?: treeOption[];
	action?: (params: any) => Promise<any>;
	actionData?: any;
}

const emit = defineEmits(["update:modelValue", "update:label", "add", "change"]);

const plCascaderProps = withDefaults(defineProps<ElPlCascaderProp>(), {
	modelValue: "",
	label: "",
	mode: "cascader",
	clearable: true,
	canAdd: false,
	width: "100%",
	placeholder: useFormat18n("common.pleaseSelect"),
	options: () => []
});

// const labelName = ref("");
const showPull = ref(false);
const cascaderPanelRef = ref();
const cascaderFullOptions = ref<treeOption[]>([]);
const cascaderOptions = ref<treeOption[]>([]);
const cascaderPanelValue = computed({
	get() {
		return plCascaderProps.modelValue;
	},
	set(newVal) {
		emit("update:modelValue", newVal);
		close();
	}
});
const cascaderFullFlatOptions = computed(() => getFlatArr(cascaderFullOptions.value));

const labelName = computed({
	get() {
		return plCascaderProps.label;
	},
	set(value) {
		emit("update:label", value);
	}
});

const setCheckedLabelName = (newVal: any) => {
	if (newVal) {
		if (cascaderPanelRef.value) {
			const checkedNodes = cascaderPanelRef.value.getCheckedNodes();
			labelName.value = (checkedNodes && checkedNodes[0] && checkedNodes[0].pathLabels.join("/")) || "";
		} else {
			if (Array.isArray(newVal)) {
				labelName.value = newVal
					.map(id => {
						const hasOne = cascaderFullFlatOptions.value.find(item => item.value == id);
						return (hasOne && hasOne.label) || "";
					})
					.join("/");
			} else {
				const checkedOption = cascaderFullFlatOptions.value.find(item => item.value == newVal);
				labelName.value = (checkedOption && checkedOption.label) || "";
			}
		}
	} else {
		labelName.value = "";
	}
};

const onCascaderPanelChange = (value: any) => {
	setCheckedLabelName(value);
	emit("change", value, {
		label: labelName.value,
		value
	});
};

const removeChildren = (list: any[]) => {
	const resultList = list.map(item => {
		const newItem = {
			...item
		};
		delete newItem.children;
		return newItem;
	});
	return resultList;
};

const getOptions = (): Promise<void> => {
	return new Promise(resolve => {
		if (plCascaderProps.options && plCascaderProps.options.length) {
			const list = plCascaderProps.mode === "select" ? removeChildren(plCascaderProps.options) : plCascaderProps.options;
			cascaderOptions.value = list;
			cascaderFullOptions.value = list;
			resolve();
			return;
		}
		if (plCascaderProps.action) {
			plCascaderProps.action(plCascaderProps.actionData || {}).then(res => {
				if (res.status === 0) {
					const list = valueTransInt(res.data || []);
					cascaderOptions.value = list;
					cascaderFullOptions.value = list;
					resolve();
				}
			});
		}
	});
};

const show = () => {
	getOptions();
	showPull.value = true;
};
const close = () => {
	showPull.value = false;
};
const onClear = () => {
	cascaderPanelValue.value = undefined;
};
const onAdd = () => {
	close();
	emit("add");
};
const onSearchInput = (e: any) => {
	cascaderOptions.value = cascaderFullFlatOptions.value.filter(item => item.label.toLowerCase().includes(e.toLowerCase()));
	// const res: any = cascaderPanelRef.value?.getFlattedNodes(true)?.filter((node: any) => {
	// 	if (node.isDisabled) return false;
	// 	node.calcText(true, "/");
	// 	return node.text.includes(e);
	// });
	// console.log("res:", res);
};
</script>
<style lang="scss">
.vxe-pulldown--panel {
	z-index: 30000 !important;
}
.el-cascader-menu {
	width: 100%;
}
</style>
