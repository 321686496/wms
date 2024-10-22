<template>
	<el-form
		ref="ruleFormRef"
		:label-width="props.labelWidth"
		:label-position="props.labelPosition"
		label-suffix=" :"
		:model="formData"
	>
		<el-row :gutter="props.gutter">
			<el-col
				v-for="item in props.columns"
				:key="item.field"
				v-show="itemShow(item.hidden)"
				v-bind="breakPoint || { span: item.span || 24 }"
			>
				<el-form-item v-if="itemShow(item.unmount)" :label="item.label" :rules="item.rules" :prop="item.field">
					<form-input :formData="formData" :item="item" :formType="formType" :width="item.width"></form-input>
					<template #label>
						<div>
							<el-tooltip v-if="item.titleHelp" effect="dark" :content="item.titleHelp" placement="top-start">
								<el-icon><QuestionFilled /></el-icon>
							</el-tooltip>
							{{ item.label }}
						</div>
					</template>
				</el-form-item>
			</el-col>
		</el-row>
	</el-form>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { FormColumn } from "../FormDialog/interface/index";
import { BreakPoint } from "@/components/Grid/interface";

interface RanderFormProp {
	formType?: string;
	labelWidth?: string;
	labelPosition?: "left" | "right" | "top";
	formData?: object;
	gutter?: number;
	columns: FormColumn[];
	breakPoint?: Record<BreakPoint, number>;
}

const ruleFormRef = ref();
const props = withDefaults(defineProps<RanderFormProp>(), {
	labelWidth: "110px",
	formType: "add",
	labelPosition: "right",
	gutter: 18,
	columns: () => [],
	formData: () => ({})
});

const itemShow = (hidden: undefined | boolean | string[]) => {
	if (Array.isArray(hidden)) {
		return !hidden.includes(props.formType);
	}
	return !hidden;
};

defineExpose({
	ruleFormRef
});
</script>

<style lang="scss"></style>
