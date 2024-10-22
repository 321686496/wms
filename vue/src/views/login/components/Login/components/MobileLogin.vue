<template>
	<el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" size="large">
		<el-form-item prop="name">
			<el-input v-model="loginForm.name" clearable :placeholder="$t('login.mobile')" @keyup.enter="onSubmit">
				<template #prefix>
					<el-icon class="el-input__icon"><Cellphone /></el-icon>
				</template>
			</el-input>
		</el-form-item>
		<el-form-item prop="captcha" style="margin-bottom: 15px">
			<div class="flx-justify-between" style="gap: 10px; width: 100%">
				<div style="width: 100%">
					<el-input v-model="loginForm.captcha" clearable :placeholder="$t('login.smsCode')" @keyup.enter="onSubmit">
						<template #prefix>
							<el-icon class="el-input__icon"><Bell /></el-icon>
						</template>
					</el-input>
				</div>
				<el-button type="primary" :loading="smsLoading" :disabled="second !== 0" @click="onSmsClick">{{ smsBtnStr }}</el-button>
			</div>
		</el-form-item>
		<div class="password-box flx-justify-between">
			<span> </span>
			<el-link type="primary" @click="forget">{{ $t("login.forgotPassword") }}</el-link>
		</div>
	</el-form>
	<div class="login-btn">
		<el-button @click="onRegister" size="large">{{ $t("login.register") }}</el-button>
		<el-button @click="onSubmit" size="large" type="primary" :loading="loading">
			{{ $t("login.login") }}
		</el-button>
	</div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue";
import { Login } from "@/api/interface";
import { mobileLoginApi } from "@/api/modules/login";
import type { ElForm } from "element-plus";
import useSmsCode from "@/hooks/useSmsCode";
import useFormat18n from "@/hooks/useFormat18n";
import useSysConfig from "@/hooks/useSysConfig";
import useLoginSuccess from "@/views/login/hooks/useLoginSuccess";

const emit = defineEmits(["forget", "register"]);
const { sysConfig } = useSysConfig();

// 定义 formRef（校验规则）
type FormInstance = InstanceType<typeof ElForm>;
const loginFormRef = ref<FormInstance>();
const phoneValidator = (rule: any, value: any, callback: any) => {
	let reg = new RegExp(sysConfig.system_regex_phone);
	if (reg.test(value)) {
		callback();
	} else {
		callback(new Error());
	}
};
const loginRules = reactive({
	name: [
		{ required: true, message: useFormat18n(["common.pleaseInput", "login.mobile"]), trigger: "blur" },
		{ validator: phoneValidator, message: useFormat18n("error.PhoneFormatError"), trigger: "blur" }
	],
	captcha: [{ required: true, message: useFormat18n(["common.pleaseInput", "login.smsCode"]), trigger: "blur" }]
});

const loading = ref(false);
const loginForm = reactive<Login.PhoneLoginForm>({ name: "", captcha: "" });
const onSubmit = () => {
	loginFormRef.value &&
		loginFormRef.value.validate(async valid => {
			if (!valid) return;
			loading.value = true;
			try {
				// 1.执行登录接口
				const data = await mobileLoginApi({ ...loginForm });
				useLoginSuccess(data.data);
			} finally {
				loading.value = false;
			}
		});
};

const onRegister = () => {
	emit("register");
};

const forget = () => {
	emit("forget");
};

// 短信验证码点击
const onSmsClick = async () => {
	// 1. 校验手机号字段
	loginFormRef.value &&
		loginFormRef.value.validateField(["name"], valid => {
			if (valid) {
				// 2. 触发获取短信验证码方法
				getSmsCode({ type: "login", phone: loginForm.name });
			}
		});
};

const { smsLoading, second, smsBtnStr, getSmsCode } = useSmsCode();
</script>

<style scoped lang="scss">
@import "../../../index.scss";
</style>
