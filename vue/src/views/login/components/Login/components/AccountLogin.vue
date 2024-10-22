<template>
	<el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" size="large">
		<el-form-item prop="name">
			<el-input v-model="loginForm.name" clearable :placeholder="$t('login.mobile')" @keyup.enter="onSubmit">
				<template #prefix>
					<el-icon class="el-input__icon"><User /></el-icon>
				</template>
			</el-input>
		</el-form-item>
		<el-form-item prop="password">
			<el-input
				type="password"
				v-model="loginForm.password"
				:placeholder="$t('login.password')"
				show-password
				clearable
				autocomplete="new-password"
				@keyup.enter="onSubmit"
			>
				<template #prefix>
					<el-icon class="el-input__icon"><lock /></el-icon>
				</template>
			</el-input>
		</el-form-item>
		<el-form-item prop="imgCaptcha">
			<div class="flx-justify-between" style="gap: 10px; width: 100%">
				<div style="width: 100%">
					<el-input v-model="loginForm.imgCaptcha" clearable :placeholder="$t('login.captcha')" @keyup.enter="onSubmit">
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
		<div class="password-box flx-justify-between">
			<div></div>
			<el-link v-if="sysConfig.system_open_register == 'true'" type="primary" @click="forget">{{
				$t("login.forgotPassword")
			}}</el-link>
		</div>
	</el-form>
	<div class="login-btn">
		<el-button @click="register" v-if="sysConfig.system_open_register == 'true'" size="large">{{
			$t("login.register")
		}}</el-button>

		<el-button
			@click="onReset"
			v-if="sysConfig.system_open_register == 'false' && sysConfig.system_open_appointment == 'false'"
			size="large"
			>{{ $t("common.reset") }}</el-button
		>
		<el-button @click="onSubmit" size="large" type="primary" :loading="loading">
			{{ $t("login.login") }}
		</el-button>
	</div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { Login } from "@/api/interface";
import { accountLoginApi } from "@/api/modules/login";
import type { ElForm } from "element-plus";
import useImgCaptcha from "@/views/login/hooks/useImgCaptcha";
import useLoginSuccess from "@/views/login/hooks/useLoginSuccess";
import useFormat18n from "@/hooks/useFormat18n";
import useSysConfig from "@/hooks/useSysConfig";

const emit = defineEmits(["forget", "register", "appoint"]);
const { sysConfig } = useSysConfig();
// 是否记住密码
// const rememberPassword = ref(false);

// 图形验证码
const { captchaUrl, getCaptcha, captchaLoading } = useImgCaptcha();

// 定义 formRef（校验规则）
type FormInstance = InstanceType<typeof ElForm>;
const loginFormRef = ref<FormInstance>();
const pwdValidator = (rule: any, value: any, callback: any) => {
	let reg = new RegExp(sysConfig.system_regex_password);
	if (reg.test(value)) {
		callback();
	} else {
		callback(new Error());
	}
};
const loginRules = reactive({
	name: [{ required: true, message: useFormat18n(["common.pleaseInput", "login.mobile"]), trigger: "blur" }],
	password: [
		{ required: true, message: useFormat18n(["common.pleaseInput", "login.password"]), trigger: "blur" },
		{ validator: pwdValidator, message: useFormat18n("error.PasswordFormatError"), trigger: "blur" }
	],
	imgCaptcha: [{ required: true, message: useFormat18n(["common.pleaseInput", "login.captcha"]), trigger: "blur" }]
});

const loading = ref(false);
const loginForm = reactive<Login.AccountLoginForm>({ name: "", password: "", imgCaptcha: "" });
const onSubmit = () => {
	if (loading.value) return;
	loginFormRef.value &&
		loginFormRef.value.validate(async valid => {
			if (!valid) return;
			loading.value = true;
			try {
				// 1.执行登录接口
				const data = await accountLoginApi({ ...loginForm });
				await useLoginSuccess(data.data);
			} catch (err) {
				getCaptcha();
			} finally {
				loading.value = false;
			}
		});
};

// resetForm
const onReset = () => {
	loginForm.name = "";
	loginForm.password = "";
	loginForm.imgCaptcha = "";
};
const register = () => {
	emit("register");
};

const forget = () => {
	emit("forget");
};

onMounted(() => {
	getCaptcha();
});
</script>

<style scoped lang="scss">
@import "../../../index.scss";
</style>
