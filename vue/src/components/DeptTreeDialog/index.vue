<template>
	<el-dialog v-model="drawerVisible" :title="useFormat18n(['common.select', 'system.department'])" width="520px">
		<div>
			<el-tree
				ref="menuTreeRef"
				class="tree-border"
				style="width: 100%"
				:data="menuTreeData"
				default-expand-all
				show-checkbox
				node-key="value"
			>
			</el-tree>
		</div>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="drawerVisible = false">{{ $t("common.cancel") }}</el-button>
				<el-button type="primary" @click="onSubmit">
					{{ $t("common.submit") }}
				</el-button>
			</span>
		</template>
	</el-dialog>
</template>
<script setup lang="ts" name="DeptTreeDialog">
import { ref, nextTick } from "vue";
import useFormat18n from "@/hooks/useFormat18n";
import { queryDepartmentTree } from "@/api/modules/select";
import { getFlatArr } from "@/utils/util";

// 弹窗显示
const drawerVisible = ref(false);
const emit = defineEmits(["change"]);

const defaultSelectedIds = ref<number[]>([]);
const menuTreeData = ref<treeOption[]>([]);
const menuFlatData = ref<treeOption[]>([]);
const menuTreeRef = ref();

const getTreeList = () => {
	queryDepartmentTree().then(res => {
		menuTreeData.value = res.data;
		menuFlatData.value = getFlatArr(res.data);
		if (defaultSelectedIds.value.length) {
			defaultSelectedIds.value.forEach((deptId: number) => {
				nextTick(() => {
					menuTreeRef.value.setChecked(deptId, true, false);
				});
			});
		}
	});
};

// 获取树组件已选择数据
const getAllCheckedKeys = () => {
	let checkedKeys = menuTreeRef.value.getCheckedKeys();
	return [...checkedKeys];
};

const onSubmit = () => {
	let checkedIds = getAllCheckedKeys();
	const checkedRows = menuFlatData.value.filter(item => checkedIds.includes(item.value));
	emit("change", checkedRows);
	drawerVisible.value = false;
};

const acceptParams = (selected_ids: number[]): void => {
	if (selected_ids && selected_ids.length) {
		defaultSelectedIds.value = selected_ids;
	}
	getTreeList();
	drawerVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>
<style lang="scss"></style>
