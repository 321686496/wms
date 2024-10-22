<template>
	<el-dialog v-model="dialogVisible" :title="$t('header.logOffAccount')" width="500px" draggable>
		<el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
			<el-form-item :label="$t('login.password')" prop="password">
				<el-input v-model="form.password" :placeholder="$t('login.password')" type="password" show-password />
			</el-form-item>
		</el-form>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="dialogVisible = false">{{ $t("common.cancel") }}</el-button>
				<el-button type="primary" :loading="subLoading" @click="onSubmit">{{ $t("common.submit") }}</el-button>
			</span>
		</template>
	</el-dialog>
</template>

<script setup lang="ts" name="LogOffDialog">
import { ref } from "vue";
import { logOffTenantOne } from "@/api/modules/login";
import { ElMessage, ElMessageBox, FormInstance } from "element-plus";
import useFormat18n from "@/hooks/useFormat18n";
import useSysConfig from "@/hooks/useSysConfig";

const { sysConfig } = useSysConfig();
const dialogVisible = ref(false);
const formRef = ref<FormInstance>();
const emit = defineEmits(["success"]);

// openDialog
const openDialog = () => {
	dialogVisible.value = true;
};

const pwdValidator = (rule: any, value: any, callback: any) => {
	let reg = new RegExp(sysConfig.system_regex_password);
	if (reg.test(value)) {
		callback();
	} else {
		callback(new Error());
	}
};

const rules = ref({
	password: [
		{ required: true, message: useFormat18n(["common.pleaseInput", "login.password"]), trigger: "change" },
		{ validator: pwdValidator, message: useFormat18n("error.PasswordFormatError"), trigger: "blur" }
	]
});

const form = ref({
	password: ""
});

const subLoading = ref(false);
const onSubmit = () => {
	formRef.value!.validate(async (valid: boolean) => {
		if (valid) {
			ElMessageBox.confirm(useFormat18n("header.logOffAccountTips"), useFormat18n("common.tips"), {
				confirmButtonText: useFormat18n("common.confirm"),
				cancelButtonText: useFormat18n("common.cancel"),
				type: "warning"
			})
				.then(async () => {
					subLoading.value = true;
					logOffTenantOne({
						...form.value
					})
						.then((res: any) => {
							if (res.status === 0) {
								ElMessage.success({ message: useFormat18n("common.operationSuccess") });
								subLoading.value = false;
								dialogVisible.value = false;
								emit("success");
							}
						})
						.finally(() => {
							subLoading.value = false;
						});
				})
				.catch(() => {});
		}
	});
};

defineExpose({ openDialog });
</script>
