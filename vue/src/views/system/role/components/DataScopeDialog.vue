<template>
	<el-dialog v-model="drawerVisible" :title="drawerProps.title" :width="props.width" destroy-on-close>
		<el-form ref="ruleFormRef" :label-width="props.labelWidth" label-suffix=" :" :model="drawerProps.rowData" :rules="formRules">
			<el-form-item :label="useFormat18n(['system.role', 'common.name'])" prop="name">
				<el-input
					v-model="drawerProps.rowData.name"
					:placeholder="useFormat18n(['common.pleaseInput', 'system.role', 'common.name'])"
					clearable
					disabled
				/>
			</el-form-item>
			<el-form-item :label="useFormat18n(['system.role', 'common.character'])" prop="roleKey">
				<el-input
					v-model="drawerProps.rowData.roleKey"
					:placeholder="useFormat18n(['common.pleaseInput', 'system.role', 'common.character'])"
					clearable
					disabled
				/>
			</el-form-item>
			<el-form-item :label="useFormat18n('system.dataScope')" prop="dataScope">
				<el-select
					v-model="drawerProps.rowData.dataScope"
					:placeholder="useFormat18n(['common.pleaseSelect', 'system.dataScope'])"
					clearable
					filterable
					style="width: 100%"
				>
					<el-option v-for="secItem in roleDataScopeList" :key="secItem.value" :label="secItem.label" :value="secItem.value" />
				</el-select>
			</el-form-item>
			<el-form-item
				v-show="drawerProps.rowData.dataScope === 'CustomData'"
				:label="useFormat18n(['system.menu', 'system.permission'])"
				prop="deptIds"
			>
				<el-checkbox v-model="treeCheckProp.expand" @change="handleCheckedTreeExpand">{{
					$t("common.expand") + "/" + $t("common.fold")
				}}</el-checkbox>
				<el-checkbox v-model="treeCheckProp.nodeAll" @change="handleCheckedTreeNodeAll">{{
					$t("common.selectAll") + "/" + $t("common.deselectAll")
				}}</el-checkbox>
				<el-tree
					ref="menuTreeRef"
					class="tree-border"
					style="width: 100%"
					:data="menuTreeData"
					show-checkbox
					check-strictly
					node-key="value"
				>
				</el-tree>
			</el-form-item>
		</el-form>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="drawerVisible = false" :disabled="subLoading">{{ $t("common.cancel") }}</el-button>
				<el-button type="primary" :loading="subLoading" @click="onSubmit">
					{{ $t("common.submit") }}
				</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script setup lang="ts" name="DataScopeDialog">
import { ref } from "vue";
import { ElMessage, FormInstance, FormRules } from "element-plus";
import useFormat18n from "@/hooks/useFormat18n";
import { queryDepartmentTree } from "@/api/modules/select";
import { roleDataScopeList } from "@/utils/serviceDict";
import { getFlatArr } from "@/utils/util";

interface DialogProps {
	width?: string;
	labelWidth?: string;
}

interface DrawerProps {
	title: string;
	rowData?: any;
	formType: string;
	api?: (params: any) => Promise<any>;
	getTableList?: () => Promise<any>;
	handleData?: (e: any) => any;
}
// 弹窗显示
const drawerVisible = ref(false);
const drawerProps = ref<DrawerProps>({
	title: "",
	formType: "add",
	rowData: {}
});
const props = withDefaults(defineProps<DialogProps>(), {
	width: "520px",
	labelWidth: "100px"
});

const ruleFormRef = ref<FormInstance>();
const formRules = ref<FormRules>({
	name: [{ required: true, message: useFormat18n(["common.pleaseInput", "system.role", "common.name"]), trigger: "blur" }],
	roleKey: [
		{ required: true, message: useFormat18n(["common.pleaseInput", "system.role", "common.character"]), trigger: "blur" }
	],
	dataScope: [{ required: true, message: useFormat18n(["common.pleaseSelect", "system.dataScope"]), trigger: "blur" }]
});

const subLoading = ref(false);
const onSubmit = () => {
	ruleFormRef.value!.validate(async (valid: boolean) => {
		if (valid) {
			subLoading.value = true;
			let deptIds = getAllCheckedKeys();
			const params = drawerProps.value.handleData
				? drawerProps.value.handleData(drawerProps.value.rowData)
				: { ...drawerProps.value.rowData, deptIds };
			drawerProps.value.api!(params)
				.then((res: any) => {
					if (res.status === 0) {
						ElMessage.success({ message: `${drawerProps.value.title}${useFormat18n("common.success")}!` });
						subLoading.value = false;
						drawerProps.value.getTableList && drawerProps.value.getTableList();
						drawerVisible.value = false;
					}
				})
				.finally(() => {
					subLoading.value = false;
				});
		}
	});
};

const menuTreeData = ref<treeOption[]>([]);
const menuTreeRef = ref();
const treeCheckProp = ref({
	expand: false, //展开/折叠
	nodeAll: false //全选/全不选
});

const getDeptTreeList = () => {
	queryDepartmentTree().then(res => {
		menuTreeData.value = res.data;
		if (
			drawerProps.value.formType === "update" &&
			drawerProps.value.rowData.deptIds &&
			drawerProps.value.rowData.deptIds.length
		) {
			menuTreeRef.value.setCheckedKeys(drawerProps.value.rowData.deptIds);
		}
	});
};

// 展开/折叠
const handleCheckedTreeExpand = (val: boolean) => {
	let treeList = menuTreeData.value;
	for (let i = 0; i < treeList.length; i++) {
		menuTreeRef.value.store.nodesMap[treeList[i].value].expanded = val;
	}
};

// 全选/全不全
const handleCheckedTreeNodeAll = (val: boolean) => {
	const hasFlatArr = getFlatArr(menuTreeData.value);
	menuTreeRef.value.setCheckedNodes(val ? hasFlatArr : []);
};

// 获取树组件已选择数据
const getAllCheckedKeys = () => {
	return menuTreeRef.value.getCheckedKeys();
};

let defaultRowData = {
	roleType: "SUPER"
};
const acceptParams = (params: DrawerProps): void => {
	getDeptTreeList();
	drawerProps.value = {
		...params,
		rowData: params.formType === "add" ? { ...defaultRowData } : params.rowData
	};
	drawerVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>

<style lang="scss" scoped>
.tree-border {
	border: 1px solid #e5e6e7;
	border-radius: 4px;
}
</style>
