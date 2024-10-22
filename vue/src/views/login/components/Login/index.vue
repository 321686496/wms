<template>
	<div class="login-block tabs-wrap">
		<el-tabs v-model="tabActive" stretch>
			<el-tab-pane label="账号密码登录" name="account">
				<AccountLogin @forget="onForget" @register="onRegister" @appoint="onAppoint" />
			</el-tab-pane>
			<el-tab-pane label="手机验证码登录" name="mobile" v-if="false">
				<MobileLogin @forget="onForget" @register="onRegister" />
			</el-tab-pane>
		</el-tabs>
	</div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import AccountLogin from "./components/AccountLogin.vue";
import MobileLogin from "./components/MobileLogin.vue";

// tab栏
const tabActive = ref("account");

// 忘记密码
const emit = defineEmits(["forget", "register", "appoint"]);
const onForget = () => {
	emit("forget");
};
const onRegister = () => {
	emit("register");
};
const onAppoint = () => {
	emit("appoint");
};
</script>

<style scoped lang="scss"></style>
