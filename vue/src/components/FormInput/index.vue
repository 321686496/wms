<template>
	<el-input
		v-if="item.type === 'text'"
		clearable
		v-model="formData[item.field]"
		:placeholder="item.placeholder"
		:disabled="disabled"
		:style="{
			width: props.width
		}"
		@change="onChange"
	/>
	<el-input
		v-else-if="item.type === 'textarea'"
		clearable
		resize="none"
		type="textarea"
		v-model="formData[item.field]"
		:placeholder="item.placeholder"
		:disabled="disabled"
		:style="{
			width: props.width
		}"
		:rows="1"
		v-bind="item.extraProps"
		@change="onChange"
	/>
	<el-input-number
		v-else-if="item.type === 'number'"
		v-model="formData[item.field]"
		:placeholder="item.placeholder"
		:disabled="disabled"
		:step="1"
		:precision="0"
		:style="{
			width: props.width
		}"
		v-bind="item.extraProps"
		@change="onChange"
	/>
	<el-input
		v-else-if="item.type === 'password'"
		v-model="formData[item.field]"
		:placeholder="item.placeholder"
		:disabled="disabled"
		type="password"
		show-password
		:style="{
			width: props.width
		}"
	/>
	<el-switch
		v-else-if="item.type === 'switch'"
		v-model="formData[item.field]"
		:placeholder="item.placeholder"
		:disabled="disabled"
	/>
	<el-radio-group v-else-if="item.type === 'radioButton'" v-model="formData[item.field]">
		<el-radio-button v-for="secItem in options" :key="secItem.value" :label="secItem.value">{{ secItem.label }}</el-radio-button>
	</el-radio-group>
	<el-checkbox-group v-else-if="item.type === 'checkbox'" v-model="formData[item.field]">
		<el-checkbox v-for="secItem in options" :key="secItem.value" :label="secItem.value">{{ secItem.label }}</el-checkbox>
	</el-checkbox-group>
	<el-select
		v-else-if="item.type === 'select'"
		v-model="formData[item.field]"
		:placeholder="item.placeholder"
		:disabled="disabled"
		clearable
		filterable
		v-bind="item.extraProps"
		:style="{
			width: props.width
		}"
		@change="onChange"
	>
		<el-option v-for="secItem in options" :key="secItem.value" :label="secItem.label" :value="secItem.value" />
	</el-select>
	<el-cascader
		v-else-if="item.type === 'cascader'"
		v-model="formData[item.field]"
		:placeholder="item.placeholder"
		:disabled="disabled"
		:options="options"
		clearable
		v-bind="item.extraProps"
		:style="{
			width: props.width
		}"
	>
		<template #default="{ data }">
			<span>{{ data.label }}</span>
		</template>
	</el-cascader>
	<el-date-picker
		v-else-if="item.type === 'date' || item.type === 'datetime'"
		v-model="formData[item.field]"
		:placeholder="item.placeholder"
		:disabled="disabled"
		:type="item.type"
		v-bind="item.extraProps"
		:style="{
			width: props.width
		}"
	>
	</el-date-picker>
	<upload-img v-else-if="item.type === 'cover'" v-bind="item.extraProps" v-model="formData[item.field]" />
	<upload-imgs v-else-if="item.type === 'covers'" v-bind="item.extraProps" v-model:fileList="formData[item.field]" />
	<Upload v-else-if="item.type === 'upload'" v-bind="item.extraProps" v-model:fileList="formData[item.field]" />
	<el-rate v-else-if="item.type === 'star'" v-model="formData[item.field]" />
	<WangEditor
		v-else-if="item.type === 'editor'"
		height="400px"
		v-model:value="formData[item.field]"
		:toolbarConfig="toolbarConfig"
	/>
	<el-color-picker v-else-if="item.type === 'color'" v-model="formData[item.field]" />
</template>

<script setup lang="ts" name="FormInput">
import { computed, onMounted, ref, watch } from "vue";
import { FormColumn } from "@/components/FormDialog/interface";
import Upload from "@/components/Upload/upload.vue";
import WangEditor from "@/components/WangEditor/index.vue";
import UploadImg from "@/components/Upload/Img.vue";
import UploadImgs from "@/components/Upload/Imgs.vue";
import { valueTransInt } from "@/utils/util";

interface FormInputProps {
	item: FormColumn;
	width?: string;
	formData: any;
	formType: string;
}

interface SelectOptionType {
	label: string;
	value: string | number;
}

const props = withDefaults(defineProps<FormInputProps>(), {
	formType: "add", // add  update
	width: "100%",
	item: () => ({
		type: "text",
		field: ""
	}),
	formData: {}
});

const disabled = computed(() => {
	if (!props.item || !props.item.disabled) {
		return false;
	}
	if (props.item.disabled === true) {
		return true;
	}
	if (Array.isArray(props.item.disabled) && props.item.disabled.includes(props.formType)) {
		return true;
	}
	if (typeof props.item.disabled == "function") {
		return props.item.disabled(props.formData);
	}
	return false;
});
const options = ref<SelectOptionType[]>([]);
watch(
	() => props.item.optionsData,
	newVal => {
		if (newVal && newVal.length) {
			options.value = newVal;
		}
	}
);

const toolbarConfig = ref({
	excludeKeys: ["uploadVideo", "insertVideo", "group-video", "insertImage"]
});

// 判断 fieldNames 设置 label && value 的 key 值
const fieldNames = computed(() => {
	return {
		label: props.item.fieldNames?.label ?? "label",
		value: props.item.fieldNames?.value ?? "value"
	};
});

const onChange = (e: any) => {
	props.item.onChange && props.item.onChange(e);
};

// 发送请求获取远程的选项数据
const handleRequestOption = () => {
	let actionData = props.item.actionData || {};
	props.item.action &&
		props.item.action(actionData).then((res: any) => {
			if (res.status === 0) {
				const list = valueTransInt(res.data || [], fieldNames.value.label, fieldNames.value.value);
				options.value = list;
			}
		});
};

onMounted(() => {
	if (props.item && props.item.action) {
		handleRequestOption();
	}
	if (props.item && props.item.optionsData) {
		options.value = props.item.optionsData || [];
	}
});
</script>

<style lang="scss"></style>
