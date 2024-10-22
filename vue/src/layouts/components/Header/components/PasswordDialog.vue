<template>
	<el-dialog v-model="dialogVisible" :title="$t('header.changePassword')" width="70vw" draggable>
		<el-form :model="pwdForm" :rules="pwdRules" ref="pwdFormRef" label-width="120px">
			<el-form-item :label="$t('header.oldPassword')" prop="oldPassword">
				<el-input v-model="pwdForm.oldPassword" :placeholder="$t('header.oldPassword')" type="password" show-password />
			</el-form-item>
			<el-form-item :label="$t('header.newPassword')" prop="newPassword">
				<el-input v-model="pwdForm.newPassword" :placeholder="$t('header.newPassword')" type="password" show-password />
			</el-form-item>
			<el-form-item :label="$t('header.passwordConfirmation')" prop="passwordConfirmation">
				<el-input
					v-model="pwdForm.passwordConfirmation"
					:placeholder="$t('header.passwordConfirmation')"
					type="password"
					show-password
				/>
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

<script setup lang="ts">
import { ref } from "vue";
import { updatePwd } from "@/api/modules/login";
import { ElMessage, FormInstance } from "element-plus";
import useFormat18n from "@/hooks/useFormat18n";
import useSysConfig from "@/hooks/useSysConfig";

const { sysConfig } = useSysConfig();
const dialogVisible = ref(false);
const pwdFormRef = ref<FormInstance>();
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

const pwdRules = ref({
	oldPassword: [{ required: true, message: useFormat18n(["common.pleaseInput", "header.oldPassword"]), trigger: "change" }],
	newPassword: [
		{ required: true, message: useFormat18n(["common.pleaseInput", "header.newPassword"]), trigger: "change" },
		{ validator: pwdValidator, message: useFormat18n("error.PasswordFormatError"), trigger: "blur" }
	],
	passwordConfirmation: [
		{ required: true, message: useFormat18n(["common.pleaseInput", "header.passwordConfirmation"]), trigger: "change" },
		{ validator: pwdValidator, message: useFormat18n("error.PasswordFormatError") },
		{
			validator: (rule: any, value: any, callback: any) => {
				if (value !== pwdForm.value.newPassword) {
					callback(new Error(useFormat18n("login.pwdNotSame")));
				} else {
					callback();
				}
			},
			message: useFormat18n("login.pwdNotSame")
		}
	]
});

const pwdForm = ref({
	oldPassword: "",
	newPassword: "",
	passwordConfirmation: ""
});

const subLoading = ref(false);
const onSubmit = () => {
	pwdFormRef.value!.validate(async (valid: boolean) => {
		if (valid) {
			subLoading.value = true;
			updatePwd({
				...pwdForm.value
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
		}
	});
};

defineExpose({ openDialog });
</script>
