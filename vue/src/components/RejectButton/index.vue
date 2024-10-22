<template>
	<div>
		<el-button type="danger" plain @click="onReject">{{ $t("common.auditReject") }}</el-button>
		<el-dialog v-model="dialogVisible" :title="$t('common.auditReject')" width="480px">
			<div>
				<rander-form :columns="rejectColumns" ref="rejectFormRef" :formData="rejectForm" />
			</div>
			<template #footer>
				<span class="dialog-footer">
					<el-button @click="dialogVisible = false">{{ $t("common.cancel") }}</el-button>
					<el-button type="primary" @click="onSubmit">{{ $t("common.submit") }}</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>
<script setup lang="ts" name="RejectButton">
import { ref } from "vue";
import RanderForm from "@/components/RanderForm/index.vue";
import { FormColumn } from "@/components/FormDialog/interface";
import useFormat18n from "@/hooks/useFormat18n";

const rejectFormRef = ref();
const dialogVisible = ref(false);
const emit = defineEmits(["submit"]);
const rejectForm = ref({
	reason: ""
});
const rejectColumns: FormColumn[] = [
	{
		field: "reason",
		type: "textarea",
		label: useFormat18n("common.rejectReason"),
		span: 24,
		placeholder: useFormat18n(["common.pleaseInput", "common.rejectReason"])
	}
];
const onReject = () => {
	rejectForm.value.reason = "";
	dialogVisible.value = true;
};
const onSubmit = () => {
	emit("submit", rejectForm.value.reason);
	dialogVisible.value = false;
};
</script>
<style lang="scss"></style>
