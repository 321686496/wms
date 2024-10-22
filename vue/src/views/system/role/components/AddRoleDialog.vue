<template>
	<el-dialog v-model="drawerVisible" :title="drawerProps.title" :width="props.width" destroy-on-close>
		<el-form ref="ruleFormRef" :label-width="props.labelWidth" label-suffix=" :" :model="drawerProps.rowData" :rules="formRules">
			<el-form-item :label="useFormat18n(['system.role', 'common.name'])" prop="name">
				<el-input
					v-model="drawerProps.rowData.name"
					:placeholder="useFormat18n(['common.pleaseInput', 'system.role', 'common.name'])"
					clearable
				/>
			</el-form-item>
			<el-form-item :label="useFormat18n(['system.role', 'common.type'])" prop="roleType">
				<el-radio-group v-model="drawerProps.rowData.roleType">
					<el-radio-button v-for="item in roleTypeStatusList" :key="item.value" :label="item.value">{{
						item.label
					}}</el-radio-button>
				</el-radio-group>
			</el-form-item>
			<el-form-item
				v-show="drawerProps.rowData.roleType === 'ADMIN'"
				:label="useFormat18n(['system.menu', 'system.permission'])"
				prop="menuIds"
			>
				<el-checkbox v-model="treeCheckProp.expand" @change="handleCheckedTreeExpand">{{
					$t("common.expand") + "/" + $t("common.fold")
				}}</el-checkbox>
				<el-checkbox v-model="treeCheckProp.nodeAll" @change="handleCheckedTreeNodeAll">{{
					$t("common.selectAll") + "/" + $t("common.deselectAll")
				}}</el-checkbox>
				<el-tree
					v-loading="treeLoading"
					ref="menuTreeRef"
					class="tree-border"
					style="width: 100%"
					:data="menuTreeData"
					show-checkbox
					node-key="value"
				>
				</el-tree>
			</el-form-item>
			<el-form-item :label="useFormat18n('common.remark')" prop="remark">
				<el-input
					v-model="drawerProps.rowData.remark"
					:placeholder="useFormat18n(['common.pleaseInput', 'common.remark'])"
					clearable
				/>
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

<script setup lang="ts" name="AddRoleDialog">
import { ref, nextTick } from "vue";
import { ElMessage, FormInstance, FormRules } from "element-plus";
import useFormat18n from "@/hooks/useFormat18n";
import { queryTenantMenuTree } from "@/api/modules/select";
import { roleTypeStatusList } from "@/utils/serviceDict";
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
	// roleKey: [
	// 	{ required: true, message: useFormat18n(["common.pleaseInput", "system.role", "common.character"]), trigger: "blur" }
	// ],
	roleType: [{ required: true, message: useFormat18n(["common.pleaseSelect", "system.role", "common.type"]), trigger: "blur" }]
	// dataScope: [{ required: true, message: useFormat18n(["common.pleaseSelect", "system.dataScope"]), trigger: "blur" }]
});

const subLoading = ref(false);
const onSubmit = () => {
	ruleFormRef.value!.validate(async (valid: boolean) => {
		if (valid) {
			subLoading.value = true;
			let menuIds = getAllCheckedKeys();
			const params = drawerProps.value.handleData
				? drawerProps.value.handleData(drawerProps.value.rowData)
				: { ...drawerProps.value.rowData, menuIds };
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

const handleI18nData = (list: treeOption[]): treeOption[] => {
	list.forEach((row: treeOption) => {
		row.label = useFormat18n(`menu.${row.label}`);
		if (row.children && row.children.length) {
			row.children = handleI18nData(row.children);
		}
	});
	return list;
};
const treeLoading = ref(false);
const getMenuTreeList = () => {
	treeLoading.value = true;
	queryTenantMenuTree()
		.then(res => {
			menuTreeData.value = handleI18nData(res.data);
			const mustCheckedIds = [];
			if (menuTreeData.value[0]) {
				menuTreeData.value[0].disabled = true;
				mustCheckedIds.push(menuTreeData.value[0].value);
				menuTreeData.value[0]?.children?.forEach(item => {
					if (["单据丢失", "联查结果"].includes(item.label)) {
						item.disabled = true;
						mustCheckedIds.push(item.value);
					}
				});
			}
			if (
				drawerProps.value.formType === "update" &&
				drawerProps.value.rowData.menuIds &&
				drawerProps.value.rowData.menuIds.length
			) {
				drawerProps.value.rowData.menuIds.forEach((menuId: string) => {
					nextTick(() => {
						menuTreeRef.value.setChecked(menuId, true, false);
					});
				});
			}
			// 处理首页、联查结果、单据丢失3个页面必选
			mustCheckedIds?.forEach((menuId: any) => {
				nextTick(() => {
					menuTreeRef.value.setChecked(menuId, true, false);
				});
			});
		})
		.finally(() => {
			treeLoading.value = false;
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
	if (val) {
		menuTreeRef.value.setCheckedNodes(hasFlatArr);
	} else {
		menuTreeRef.value.setCheckedNodes(hasFlatArr.filter(item => ["单据丢失", "联查结果"].includes(item.label)));
	}
};

// 获取树组件已选择数据
const getAllCheckedKeys = () => {
	let checkedKeys = menuTreeRef.value.getCheckedKeys();
	let halfCheckedKeys = menuTreeRef.value.getHalfCheckedKeys();
	return [...checkedKeys, ...halfCheckedKeys];
};

let defaultRowData = {
	roleType: "SUPER"
};
const acceptParams = (params: DrawerProps): void => {
	getMenuTreeList();
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
	height: 500px;
	overflow-y: auto;
	border: 1px solid #e5e6e7;
	border-radius: 4px;
}
</style>
