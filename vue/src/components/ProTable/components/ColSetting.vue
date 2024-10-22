<template>
	<!-- 列设置 -->
	<el-drawer :title="$t('system.colSetting')" v-model="drawerVisible" size="480px" destroy-on-close append-to-body>
		<div class="table-box drawer_box">
			<div class="table" v-loading="tabLoading">
				<el-table
					height="100%"
					:data="colSetting"
					:border="true"
					row-key="prop"
					default-expand-all
					:tree-props="{ children: '_children' }"
				>
					<el-table-column prop="label" align="center" :label="$t('system.columnName')" />
					<el-table-column prop="isShow" align="center" :label="$t('system.show')" v-slot="scope">
						<el-switch
							:disabled="!!scope.row.disabled"
							v-model="scope.row.isShow"
							@change="
								() => {
									scope.row.visible = scope.row.isShow;
									emit('update:colSetting', colSetting);
								}
							"
						></el-switch>
					</el-table-column>
					<el-table-column prop="priority" align="center" :label="$t('common.priority')" v-slot="scope">
						<el-space>
							<el-button
								type="primary"
								plain
								size="small"
								:disabled="scope.$index == 0"
								:icon="Top"
								circle
								@click="onTop(scope.$index)"
							></el-button>
							<el-button
								type="primary"
								plain
								size="small"
								:disabled="scope.$index == colSetting.length - 1"
								:icon="Bottom"
								circle
								@click="onBottom(scope.$index)"
							></el-button>
						</el-space>
					</el-table-column>
					<template #empty>
						<div class="table-empty">
							<img src="@/assets/images/notData.png" alt="notData" />
							<div>暂无可配置列</div>
						</div>
					</template>
				</el-table>
			</div>
		</div>
		<template #footer v-if="props.type">
			<div style="flex: auto">
				<el-button @click="drawerVisible = false">{{ $t("common.cancel") }}</el-button>
			</div>
		</template>
	</el-drawer>
</template>

<script setup lang="ts" name="ColSetting">
import { ref, onMounted } from "vue";
import { Top, Bottom } from "@element-plus/icons-vue";

type ColSettingProps = {
	colSetting: any[];
	type?: string;
	requestAuto?: boolean; //
};

const props = withDefaults(defineProps<ColSettingProps>(), {
	colSetting: () => [],
	requestAuto: true,
	type: ""
});

const emit = defineEmits(["update:colSetting"]);

// tab栏
const tabActive = ref<"User" | "Tenant">("User");
const tabLoading = ref(false);

const drawerVisible = ref<boolean>(false);
// 打开列设置
const openColSetting = () => {
	// 初始化部分数据
	if (tabActive.value !== "User") {
		tabActive.value = "User";
	}
	drawerVisible.value = true;
};

const swapArray = (arr: any[], index1: number, index2: number) => {
	arr[index1] = arr.splice(index2, 1, arr[index1])[0];
	return arr.map((item: any, index: number) => ({
		...item,
		priority: index
	}));
};

const onTop = (index: number) => {
	if (index == 0) {
		return;
	}
	let oldList: any[] = [...props.colSetting];
	oldList = swapArray(oldList, index - 1, index);
	emit("update:colSetting", oldList);
};

const onBottom = (index: number) => {
	if (index == props.colSetting.length - 1) {
		return;
	}
	let oldList: any[] = [...props.colSetting];
	oldList = swapArray(oldList, index, index + 1);
	emit("update:colSetting", oldList);
};

// 初始化字段权限处理
// 默认前端的disabled值肯定是权限判断而来的（但是后期值会变，这里取权限判断的值存一下）
const initFieldPermission = () => {
	props.colSetting.forEach(col => {
		col.hasPermission = !col.disabled;
	});
};

onMounted(() => {
	if (props.type && props.requestAuto) {
		initFieldPermission();
	}
});

defineExpose({
	openColSetting
});
</script>

<style lang="scss">
.drawer_box {
	height: calc(100% - 50px);
}
</style>
