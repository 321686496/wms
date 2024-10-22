<template>
	<el-dialog v-model="visible" :close-on-click-modal="false" title="新增菜单" width="520px" draggable>
		<el-form ref="menuFormRef" :model="menuProps.rowData" :rules="formRules" label-width="110px">
			<el-form-item label="父级菜单" prop="pid">
				<el-cascader
					style="width: 100%"
					v-model="menuProps.rowData.pid"
					:options="menuTreeData"
					placeholder="父级菜单"
					filterable
					clearable
					:props="{ checkStrictly: true, emitPath: false }"
				/>
			</el-form-item>
			<el-form-item label="菜单类型" prop="menuType">
				<el-radio-group v-model="menuProps.rowData.menuType">
					<el-radio-button v-for="item in menuTypeList" :key="item.value" :label="item.value">{{ item.label }}</el-radio-button>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="权限标识" prop="perms">
				<el-input v-model="menuProps.rowData.perms" placeholder="权限标识" clearable />
			</el-form-item>
			<el-form-item label="菜单别名" prop="name">
				<el-input v-model="menuProps.rowData.name" placeholder="菜单别名" clearable />
			</el-form-item>
			<el-form-item label="菜单路径" prop="path" v-show="!['Button'].includes(menuProps.rowData.menuType)">
				<el-input v-model="menuProps.rowData.path" placeholder="菜单路径" clearable />
			</el-form-item>
			<!-- <el-form-item label="小程序路径" prop="appletPath" v-show="!['Button'].includes(menuProps.rowData.menuType)">
				<el-input v-model="menuProps.rowData.appletPath" placeholder="小程序路径" clearable />
			</el-form-item> -->
			<el-form-item label="重定向路径" prop="redirect" v-show="!['Button'].includes(menuProps.rowData.menuType)">
				<el-input v-model="menuProps.rowData.redirect" placeholder="重定向路径" clearable />
			</el-form-item>
			<el-form-item label="文件路径" prop="component" v-show="!['Button'].includes(menuProps.rowData.menuType)">
				<el-input v-model="menuProps.rowData.component" placeholder="视图文件路径" clearable />
			</el-form-item>
			<el-form-item label="菜单图标" prop="metaIcon" v-show="!['Button'].includes(menuProps.rowData.menuType)">
				<el-input v-model="menuProps.rowData.metaIcon" placeholder="菜单图标" clearable />
			</el-form-item>
			<!-- <el-form-item label="小程序图标" prop="appletMetaIcon" v-show="!['Button'].includes(menuProps.rowData.menuType)">
				<el-input v-model="menuProps.rowData.appletMetaIcon" placeholder="小程序图标" clearable />
			</el-form-item> -->
			<el-form-item label="排序值" prop="priority">
				<el-input-number
					style="width: 100%"
					:precision="0"
					:controls="false"
					:min="0"
					v-model="menuProps.rowData.priority"
					placeholder="排序值"
					clearable
				/>
			</el-form-item>
			<el-row>
				<el-col :span="12">
					<el-form-item label="是否启用" prop="enable">
						<el-switch v-model="menuProps.rowData.enable" />
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="是否隐藏" prop="metaIsHide">
						<el-switch v-model="menuProps.rowData.metaIsHide" />
					</el-form-item>
				</el-col>
			</el-row>
			<el-row v-show="!['Button'].includes(menuProps.rowData.menuType)">
				<el-col :span="12">
					<el-form-item label="是否外链" prop="metaIsLink">
						<el-switch v-model="menuProps.rowData.metaIsLink" />
					</el-form-item>
					<!-- <el-form-item label="小程序隐藏" prop="appletMetaIsHide">
						<el-switch v-model="menuProps.rowData.appletMetaIsHide" />
					</el-form-item> -->
				</el-col>
				<el-col :span="12">
					<el-form-item label="是否全屏" prop="metaIsFull">
						<el-switch v-model="menuProps.rowData.metaIsFull" />
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="是否固定页签" prop="metaIsAffix">
						<el-switch v-model="menuProps.rowData.metaIsAffix" />
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="是否缓存" prop="metaIsKeepAlive">
						<el-switch v-model="menuProps.rowData.metaIsKeepAlive" />
					</el-form-item>
				</el-col>
				<el-col :span="12"> </el-col>
			</el-row>
		</el-form>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="visible = false" :disabled="subLoading">取消</el-button>
				<el-button type="primary" :loading="subLoading" @click="onSubmit"> 提交 </el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { MenuTypeEnum } from "@/enums/systemEnum";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessage } from "element-plus";
import { ArrayStringEnum } from "@/utils/util";
import { SystemVo } from "@/typings/modules/system";
import { queryMenuTree } from "@/api/modules/select";
import useFormat18n from "@/hooks/useFormat18n";

interface AddMenuProps {
	title: string;
	rowData: SystemVo.MenuVo;
	formType: "add" | "update"; // add update
	api?: (params: any) => Promise<any>;
	getTableList?: () => Promise<any>;
}

// 弹窗显示
const visible = ref(false);
let defaultRowData = {
	pid: 0,
	menuType: MenuTypeEnum.菜单,
	path: "",
	appletPath: "",
	appletMetaIcon: "",
	appletMetaIsHide: false,
	name: "",
	redirect: "",
	component: "",
	metaIcon: "",
	metaActiveMenu: "",
	metaIsLink: false,
	metaIsHide: false,
	metaIsFull: false,
	metaIsAffix: false,
	metaIsKeepAlive: false,
	enable: true,
	modules: "",
	perms: "",
	priority: 0
};
const menuProps = ref<AddMenuProps>({
	title: "",
	formType: "add",
	rowData: defaultRowData
});

const menuFormRef = ref<FormInstance>();
const menuTypeList = ArrayStringEnum(MenuTypeEnum);
const menuTreeData = ref<treeOption[]>([]);

const formRules = ref<FormRules>({
	menuType: [{ required: true, message: "请选择菜单类型", trigger: "blur" }],
	perms: [{ required: true, message: "请输入权限标识", trigger: "blur" }],
	name: [{ required: true, message: "请输入菜单别名", trigger: "blur" }],
	modules: [{ required: true, message: "请选择所属模块", trigger: "blur" }]
});

const subLoading = ref(false);
const onSubmit = () => {
	menuFormRef.value &&
		menuFormRef.value.validate(async (valid: boolean) => {
			if (valid) {
				subLoading.value = true;
				const params = menuProps.value.rowData;
				menuProps.value.api!(params)
					.then((res: any) => {
						if (res.status === 0) {
							ElMessage.success({ message: menuProps.value.title + "成功" });
							subLoading.value = false;
							menuProps.value.getTableList && menuProps.value.getTableList();
							visible.value = false;
						}
					})
					.finally(() => {
						subLoading.value = false;
					});
			}
		});
};

const handleI18nData = (list: treeOption[]): treeOption[] => {
	list.forEach((row: treeOption) => {
		row.label = useFormat18n(`menu.${row.label}`);
		if (row.children && row.children.length) {
			row.children = handleI18nData(row.children);
		}
	});
	return list;
};

const getMenuTreeList = () => {
	queryMenuTree().then(res => {
		menuTreeData.value = handleI18nData(res.data);
	});
};

const acceptParams = (param: AddMenuProps) => {
	getMenuTreeList();
	param.rowData = param.formType === "add" ? { ...defaultRowData } : param.rowData;
	menuProps.value = param;
	visible.value = true;
};

defineExpose({
	acceptParams
});
</script>

<style lang="scss"></style>
