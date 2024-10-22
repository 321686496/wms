<template>
	<div class="login-container flx-center">
		<SwitchDark class="dark" />
		<div class="login-box">
			<div class="login-left">
				<img src="https://p8-cdn.ponlay.com/mp-icon/login_left.png" alt="login" />
			</div>
			<div class="login-form">
				<div class="login-logo">
					<img class="login-icon" v-if="sysConfig.system_logo" :src="sysConfig.system_logo" alt="" />
					<h2 class="logo-text">{{ sysConfig.system_name }}</h2>
				</div>
				<Login v-if="mode === 'login'" @forget="mode = 'forget'" @register="mode = 'register'" @appoint="mode = 'appoint'" />
				<Forget v-else-if="mode === 'forget'" @login="mode = 'login'" />
				<Register v-else-if="mode === 'register'" @login="mode = 'login'" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts" name="login">
import { ref } from "vue";
import SwitchDark from "@/components/SwitchDark/index.vue";
import Login from "./components/Login/index.vue";
import Forget from "./components/Forget/index.vue";
import Register from "./components/Register/index.vue";
import useSysConfig from "@/hooks/useSysConfig";
const { sysConfig } = useSysConfig();

const mode = ref("login"); // login 登录 forget 找回密码 register 注册 appoint 预约
</script>

<style scoped lang="scss">
@import "./index.scss";
</style>
