<template>
	<div v-loading="loading" class="table">
		<vxe-grid v-if="!loading" ref="gridTableRef" v-bind="modelValue" :data="tableData" @current-change="onCurrentChange">
			<template #toolbar_buttons>
				<div class="flx-justify-between" style="width: 100%">
					<slot name="toolbar_buttons_left"></slot>
					<el-space v-if="toolButton">
						<slot name="toolbar_buttons_right"> </slot>
						<el-button v-if="colSettingType" :icon="Operation" circle @click="colRef.openColSetting()"> </el-button>
					</el-space>
				</div>
			</template>
			<template v-for="slot in Object.keys($slots)" #[slot]="scope">
				<slot :name="slot" v-bind="scope"></slot>
			</template>
		</vxe-grid>
	</div>

	<ColSetting
		ref="colRef"
		:requestAuto="false"
		:type="props.colSettingType"
		v-model:colSetting="colSetting"
		@update:col-setting="onColSettingChange"
	>
	</ColSetting>
</template>

<script lang="ts" name="GridEditTable" setup>
import { ref, onMounted, onUnmounted } from "vue";
import { Operation } from "@element-plus/icons-vue";
import { debounce } from "lodash-es";
import ColSetting from "@/components/ProTable/components/ColSetting.vue";

interface GridEditTableProp {
	modelValue?: any;
	toolButton?: boolean; // 是否显示表格功能按钮 ==> 非必传（默认为true）
	colSettingType?: string; //列显示抽屉的单据类型 ==> 非必传
	tableData: any[];
}

// 接受父组件参数，配置默认值
const props = withDefaults(defineProps<GridEditTableProp>(), {
	toolButton: true,
	colSettingType: "",
	tableData: () => [],
	modelValue: {}
});

const emit = defineEmits(["column-change", "current-change"]);
const gridTableRef = ref();

const colRef = ref();
const colSetting = ref<any[]>(
	(props.modelValue.columns || []).filter(
		(item: any) => item.field !== "seq" && item.field !== "operation" && !item.tenantDisabled
	)
);

const loading = ref(true);
const refreshLoading = debounce(() => {
	loading.value = true;
	setTimeout(() => {
		loading.value = false;
	}, 120);
}, 120);

onMounted(() => {
	refreshLoading();
	window.addEventListener("resize", refreshLoading);
});

onUnmounted(() => {
	window.removeEventListener("resize", refreshLoading);
});

const onColSettingChange = (colSettings: any[]) => {
	emit("column-change", colSettings);
};

const onCurrentChange = (scope: any) => {
	emit("current-change", scope);
};

const updateFooter = () => {
	gridTableRef.value.updateFooter();
};

const setCurrentRow = (row: any) => {
	gridTableRef.value.setCurrentRow(row);
};

const scrollToRow = (row: any) => {
	gridTableRef.value.scrollToRow(row);
};

defineExpose({
	updateFooter,
	setCurrentRow,
	scrollToRow
});
</script>

<style lang="scss"></style>
