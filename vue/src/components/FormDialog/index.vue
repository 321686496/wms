<template>
	<vxe-modal
		v-model="drawerVisible"
		:title="drawerProps.title"
		show-zoom
		:width="props.width"
		:height="props.height"
		transfer
		destroy-on-close
	>
		<el-form
			ref="ruleFormRef"
			:label-width="props.labelWidth"
			label-suffix=" :"
			:model="drawerProps.rowData"
			:disabled="drawerProps.isView"
			:hide-required-asterisk="drawerProps.isView"
		>
			<el-row>
				<el-col
					v-for="item in props.columns"
					:key="item.field"
					:span="item.span || 24"
					v-show="!item.hidden || !(item.hidden as string[]).includes(drawerProps.formType)"
				>
					<el-form-item
						v-if="!item.unmount || !(item.unmount as string[]).includes(drawerProps.formType)"
						:label="item.label"
						:rules="item.rules"
						:prop="item.field"
					>
						<form-input :formData="drawerProps.rowData" :item="item" :formType="drawerProps.formType"></form-input>
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
		<div class="flx-justify-between pt8">
			<div></div>
			<span class="dialog-footer">
				<el-button @click="drawerVisible = false" :disabled="subLoading">{{ $t("common.cancel") }}</el-button>
				<el-button v-show="!drawerProps.isView" type="primary" :loading="subLoading" @click="onSubmit">
					{{ $t("common.submit") }}
				</el-button>
			</span>
		</div>
	</vxe-modal>
</template>

<script setup lang="ts" name="FormDialog">
import { ref } from "vue";
import { FormColumn } from "./interface/index";
import { ElMessage, FormInstance } from "element-plus";
import useFormat18n from "@/hooks/useFormat18n";

interface DialogProps {
	width?: string;
	height?: string;
	labelWidth?: string;
	columns: FormColumn[];
}

interface DrawerProps {
	title: string;
	rowData?: any;
	formType: string;
	api?: (params: any) => Promise<any>;
	getTableList?: () => Promise<any>;
	handleData?: (e: any) => any;
	isView?: boolean;
}
// 弹窗显示
const drawerVisible = ref(false);
const drawerProps = ref<DrawerProps>({
	title: "",
	formType: "add",
	rowData: {},
	isView: false
});
const props = withDefaults(defineProps<DialogProps>(), {
	width: "520px",
	height: "auto",
	labelWidth: "100px",
	columns: () => []
});

const ruleFormRef = ref<FormInstance>();

const subLoading = ref(false);
const onSubmit = () => {
	ruleFormRef.value!.validate(async (valid: boolean) => {
		if (valid) {
			subLoading.value = true;
			const params = drawerProps.value.handleData
				? drawerProps.value.handleData(drawerProps.value.rowData)
				: { ...drawerProps.value.rowData };
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

const acceptParams = (params: DrawerProps): void => {
	drawerProps.value = params;
	drawerVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>

<style lang="scss"></style>
