<template>
	<el-dialog v-model="drawerVisible" :title="title" width="85vw" :close-on-click-modal="false" destroy-on-close append-to-body>
		<div class="main-box" style="height: 65vh">
			<TreeFilter
				:canAdd="false"
				:canDel="false"
				:canUpdate="false"
				id="value"
				label="label"
				:title="$t('system.department')"
				:requestApi="queryDepartmentTree"
				@change="changeTreeFilter"
			/>
			<div class="table-box">
				<ProTable
					ref="proTable"
					:requestAuto="false"
					:title="title"
					:columns="columns"
					:initParam="initParam"
					:requestApi="queryAdminList"
					@selection-change="onSelectionChange"
				>
				</ProTable>
			</div>
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

<script setup lang="ts" name="SkuDialog">
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import useFormat18n from "@/hooks/useFormat18n";
import TreeFilter from "@/components/TreeFilter/index.vue";
import { queryAdminList, queryDepartmentTree, queryRoleAll } from "@/api/modules/select";
import { userStatusList } from "@/utils/serviceDict";
import { ColumnProps } from "@/components/ProTable/interface";

const props = withDefaults(defineProps<{ select?: "radio" | "checkbox" }>(), {
	select: "checkbox"
});

// 弹窗显示
const title = "选择负责人";
const proTable = ref();
const drawerVisible = ref(false);
const emit = defineEmits(["select"]);

// 左树形筛选切换
const changeTreeFilter = (val: number) => {
	initParam.deptId = val;
	proTable.value.pageable.current = 1;
	proTable.value && proTable.value.getTableList();
};

// 右卡默认请求参数
const initParam = reactive<any>({
	deptId: 0
});
// 右卡表格配置项
const columns = ref<ColumnProps[]>([
	{ type: "selection", fixed: "left", width: 60 },
	{
		prop: "phone",
		label: useFormat18n("login.mobile"),
		minWidth: 150,
		search: { el: "input" }
	},
	{
		prop: "realName",
		minWidth: 120,
		label: useFormat18n("system.realName")
	},
	{
		prop: "nickname",
		minWidth: 120,
		label: "昵称"
	},
	{
		prop: "roleId",
		minWidth: 120,
		enum: queryRoleAll,
		search: { el: "select", props: { filterable: true } },
		label: useFormat18n("system.role")
	},
	{
		prop: "status",
		minWidth: 120,
		label: useFormat18n("common.status"),
		enum: userStatusList,
		search: { el: "select", props: { filterable: true } },
		tag: true
	},
	{
		prop: "email",
		minWidth: 120,
		label: useFormat18n("login.email")
	},
	{
		prop: "sex",
		minWidth: 120,
		label: useFormat18n("system.gender")
	},
	{
		prop: "createdAt",
		minWidth: 170,
		label: useFormat18n("common.createdAt")
	},
	{
		prop: "remark",
		minWidth: 120,
		label: useFormat18n("common.remark")
	}
]);

const onSelectionChange = (rowArr: any[]) => {
	if (props.select == "radio" && rowArr.length > 1) {
		for (let i = 0; i < rowArr.length - 1; i++) {
			proTable.value.element.toggleRowSelection(rowArr[i], false);
		}
	}
};

// 提交
const onSubmit = () => {
	const checkedList = proTable.value.selectedList || [];
	if (!checkedList.length) {
		ElMessage.error(useFormat18n("common.pleaseSelectOne"));
		return;
	}
	if (props.select == "radio" && checkedList.length > 1) {
		ElMessage.error(useFormat18n("common.selectAtMostOne"));
		return;
	}
	emit("select", checkedList);
	drawerVisible.value = false;
};

const show = (queryParams: any) => {
	if (queryParams) {
		let keys = Object.keys(queryParams);
		for (let paramKey of keys) {
			initParam[paramKey] = queryParams[paramKey];
		}
	}
	drawerVisible.value = true;
};

defineExpose({
	show
});
</script>

<style lang="scss" scoped></style>
