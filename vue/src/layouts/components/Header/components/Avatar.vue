<template>
	<el-dropdown trigger="click">
		<div class="avatar">
			<!-- <img src="@/assets/images/avatar.gif" alt="avatar" /> -->
			<img
				:src="userInfo.avatar || 'https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png'"
				alt="avatar"
			/>
		</div>
		<template #dropdown>
			<el-dropdown-menu>
				<el-dropdown-item @click="openDialog('passwordRef')">
					<el-icon><Edit /></el-icon>{{ $t("header.changePassword") }}
				</el-dropdown-item>
				<el-dropdown-item @click="logout" divided>
					<el-icon><SwitchButton /></el-icon>{{ $t("header.logout") }}
				</el-dropdown-item>
			</el-dropdown-menu>
		</template>
	</el-dropdown>
	<!-- infoDialog -->
	<InfoDialog ref="infoRef"></InfoDialog>
	<!-- passwordDialog -->
	<PasswordDialog ref="passwordRef" @success="appReset"></PasswordDialog>
	<ClearDataDialog ref="clearDialogRef" />
	<LogOffDialog ref="logOffDialogRef" @success="appReset" />
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import Cookies from "js-cookie";
import { GlobalStore } from "@/stores";
import { LOGIN_URL } from "@/config/config";
import { logoutApi } from "@/api/modules/login";
import { useRouter } from "vue-router";
import { ElMessageBox, ElMessage } from "element-plus";
import InfoDialog from "./InfoDialog.vue";
import PasswordDialog from "./PasswordDialog.vue";
import useFormat18n from "@/hooks/useFormat18n";
import ClearDataDialog from "./ClearDataDialog.vue";
import LogOffDialog from "./LogOffDialog.vue";

const router = useRouter();
const globalStore = GlobalStore();
const userInfo = computed(() => globalStore.userInfo);

// 退出登录
const logout = () => {
	ElMessageBox.confirm(useFormat18n(["common.whetherOrNot", "header.logout"]) + "?", useFormat18n("common.tips"), {
		confirmButtonText: useFormat18n("common.confirm"),
		cancelButtonText: useFormat18n("common.cancel"),
		type: "warning"
	})
		.then(async () => {
			await logoutApi();
			ElMessage.success(useFormat18n(["header.logout", "common.success"]));
			appReset();
		})
		.catch(() => {});
};

const appReset = () => {
	// 1.清除Token 重置用户信息
	globalStore.setToken("");
	globalStore.setUserInfo({
		nickName: "",
		name: "",
		phone: "",
		roleKey: "",
		roleName: "",
		tenantId: 0
	});
	// 2.清除 Cookie
	Cookies.remove("PLSESSIONID");
	// 3.重定向到登陆页
	router.replace(LOGIN_URL);
};

interface DialogExpose {
	openDialog: () => void;
}
const infoRef = ref<null | DialogExpose>(null);
const passwordRef = ref<null | DialogExpose>(null);
// 打开修改密码和个人信息弹窗
const openDialog = (refName: string) => {
	if (refName == "infoRef") infoRef.value?.openDialog();
	else passwordRef.value?.openDialog();
};

// 清空业务数据
const clearDialogRef = ref();

// 注销账号
const logOffDialogRef = ref();
</script>

<style scoped lang="scss">
.avatar {
	width: 40px;
	height: 40px;
	overflow: hidden;
	cursor: pointer;
	border-radius: 50%;
	img {
		width: 100%;
		height: 100%;
	}
}
</style>
