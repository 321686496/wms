<template>
	<div class="register-block tabs-wrap">
		<el-tabs v-model="tabActive" stretch>
			<el-tab-pane :label="$t('login.register')" name="register">
				<el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" size="large">
					<el-form-item prop="phone">
						<el-input v-model="registerForm.phone" clearable :placeholder="$t('login.mobile')" @keyup.enter="onSubmit">
							<template #prefix>
								<el-icon class="el-input__icon"><Cellphone /></el-icon>
							</template>
						</el-input>
					</el-form-item>
					<el-form-item prop="captcha">
						<div class="flx-justify-between" style="gap: 10px; width: 100%">
							<div style="width: 100%">
								<el-input v-model="registerForm.captcha" clearable :placeholder="$t('login.captcha')" @keyup.enter="onSubmit">
									<template #prefix>
										<el-icon class="el-input__icon"><Picture /></el-icon>
									</template>
								</el-input>
							</div>
							<div v-loading="captchaLoading">
								<img :src="captchaUrl" alt="" class="captcha-img" @click="getCaptcha" />
							</div>
						</div>
					</el-form-item>
					<el-form-item prop="phoneCaptcha">
						<div class="flx-justify-between" style="gap: 10px; width: 100%">
							<div style="width: 100%">
								<el-input
									v-model="registerForm.phoneCaptcha"
									clearable
									:placeholder="$t('login.smsCode')"
									@keyup.enter="onSubmit"
								>
									<template #prefix>
										<el-icon class="el-input__icon"><Bell /></el-icon>
									</template>
								</el-input>
							</div>
							<el-button type="primary" :loading="smsLoading" :disabled="second !== 0" @click="onSmsClick">{{
								smsBtnStr
							}}</el-button>
						</div>
					</el-form-item>
					<el-form-item prop="pwd">
						<el-input
							type="password"
							v-model="registerForm.pwd"
							:placeholder="$t('login.password')"
							show-password
							clearable
							@keyup.enter="onSubmit"
						>
							<template #prefix>
								<el-icon class="el-input__icon"><lock /></el-icon>
							</template>
						</el-input>
					</el-form-item>
					<el-form-item prop="pwd2">
						<el-input
							type="password"
							v-model="registerForm.pwd2"
							:placeholder="$t('login.password')"
							show-password
							clearable
							@keyup.enter="onSubmit"
						>
							<template #prefix>
								<el-icon class="el-input__icon"><lock /></el-icon>
							</template>
						</el-input>
					</el-form-item>
				</el-form>
				<div class="login-btn">
					<el-button size="large" @click="login">{{ $t("login.backLogin") }}</el-button>
					<el-button @click="onSubmit" size="large" type="primary" :loading="loading">
						{{ $t("common.submit") }}
					</el-button>
				</div>
			</el-tab-pane>
		</el-tabs>
	</div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import type { ElForm } from "element-plus";
import { ElMessageBox } from "element-plus";
import { Login } from "@/api/interface";
import { registerApi } from "@/api/modules/login";
import useImgCaptcha from "@/views/login/hooks/useImgCaptcha";
import useSmsCode from "@/hooks/useSmsCode";
import useFormat18n from "@/hooks/useFormat18n";
import useSysConfig from "@/hooks/useSysConfig";

const tabActive = ref("register");
const emit = defineEmits(["login"]);
const { sysConfig } = useSysConfig();

// 定义 formRef（校验规则）
type FormInstance = InstanceType<typeof ElForm>;
const registerFormRef = ref<FormInstance>();

const phoneValidator = (rule: any, value: any, callback: any) => {
	let reg = new RegExp(sysConfig.system_regex_phone);
	if (reg.test(value)) {
		callback();
	} else {
		callback(new Error());
	}
};

const pwdValidator = (rule: any, value: any, callback: any) => {
	let reg = new RegExp(sysConfig.system_regex_password);
	if (reg.test(value)) {
		callback();
	} else {
		callback(new Error());
	}
};

const pwd2Validator = (rule: any, value: any, callback: any) => {
	if (value !== registerForm.pwd) {
		callback(new Error());
	} else {
		callback();
	}
};

const registerRules = reactive({
	phone: [
		{ required: true, message: useFormat18n(["common.pleaseInput", "login.mobile"]), trigger: "blur" },
		{ validator: phoneValidator, message: useFormat18n("error.PhoneFormatError"), trigger: "blur" }
	],
	captcha: [{ required: true, message: useFormat18n(["common.pleaseInput", "login.captcha"]), trigger: "blur" }],
	phoneCaptcha: [{ required: true, message: useFormat18n(["common.pleaseInput", "login.smsCode"]), trigger: "blur" }],
	pwd: [
		{ required: true, message: useFormat18n(["common.pleaseInput", "login.password"]), trigger: "blur" },
		{ validator: pwdValidator, message: useFormat18n("error.PasswordFormatError"), trigger: "blur" }
	],
	pwd2: [{ validator: pwd2Validator, message: useFormat18n("login.pwdNotSame"), trigger: "blur" }]
});

const loading = ref(false);
const registerForm = reactive<Login.RegisterForm>({ phone: "", pwd: "", pwd2: "", phoneCaptcha: "", captcha: "" });
const onSubmit = () => {
	registerFormRef.value &&
		registerFormRef.value.validate(async valid => {
			if (!valid) return;
			loading.value = true;
			try {
				// 1.执行注册接口
				await registerApi({ ...registerForm, sourceType: "PC" });
				ElMessageBox.confirm(`${useFormat18n("login.registerSuccess")}`, useFormat18n("common.tips"), {
					confirmButtonText: useFormat18n("common.confirm"),
					cancelButtonText: useFormat18n("common.cancel"),
					type: "success",
					draggable: true
				})
					.then(() => {
						login();
					})
					.catch(() => {
						getCaptcha();
					});
			} finally {
				loading.value = false;
			}
		});
};

// 短信验证码点击
const onSmsClick = async () => {
	// 1. 校验手机号字段
	registerFormRef.value &&
		registerFormRef.value.validateField(["phone", "captcha"], valid => {
			if (valid) {
				// 2. 触发获取短信验证码方法
				getSmsCode({ type: "register", phone: registerForm.phone, imgCaptcha: registerForm.captcha });
			}
		});
};

const { captchaUrl, getCaptcha, captchaLoading } = useImgCaptcha();
const { smsLoading, second, smsBtnStr, getSmsCode } = useSmsCode();

const login = () => {
	emit("login");
};

onMounted(() => {
	getCaptcha();
});
</script>

<style scoped lang="scss">
@import "../../index.scss";
</style>
