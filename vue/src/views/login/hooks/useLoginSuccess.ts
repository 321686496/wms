import { ref, h } from "vue";
import { ElNotification, ElMessageBox, ElRadioGroup, ElRadio } from "element-plus";
import { GlobalStore } from "@/stores";
import { initDynamicRouter } from "@/routers/modules/dynamicRouter";
import { TabsStore } from "@/stores/modules/tabs";
import { KeepAliveStore } from "@/stores/modules/keepAlive";
import { HOME_URL } from "@/config/config";
import { queryUserInfo, querySameAccountLogin, clearSameAccount } from "@/api/modules/login";
import useFormat18n from "@/hooks/useFormat18n";
import useSysConfig from "@/hooks/useSysConfig";
import router from "@/routers";

const useLoginSuccess = async (data: any) => {
	const globalStore = GlobalStore();
	const tabsStore = TabsStore();
	const keepAlive = KeepAliveStore();
	const { sysConfig } = useSysConfig();

	// 1.执行登录接口,设置全局信息
	globalStore.setToken(data.phone || "");
	// 登录用户信息
	queryUserInfo().then(res => {
		if (res.status == 0) {
			globalStore.setUserInfo(res.data);
		}
	});

	// 2.添加动态路由
	await initDynamicRouter();

	// 3.清空 tabs、keepAlive 保留的数据
	tabsStore.closeMultipleTab();
	keepAlive.setKeepAliveName();

	// 4.跳转到首页
	router.push(HOME_URL);
	ElNotification({
		title: useFormat18n("login.welcome"),
		message: `${useFormat18n("login.welcomeLogin")} ${sysConfig.system_name}`,
		type: "success",
		duration: 3000
	});

	// 5.检测是否同账号登录
	querySameAccountLogin().then((res: any) => {
		console.log(res);
		if (false) {
			const accountLoginSelect = ref("delete");
			ElMessageBox({
				title: useFormat18n(["login.login", "common.tips"]),
				message: () => {
					return h("div", null, [
						h("div", null, "您的账号已在其他设备或浏览器登录，是否继续登录？"),
						h(
							ElRadioGroup,
							{
								modelValue: accountLoginSelect.value,
								"onUpdate:modelValue": (val: any) => {
									accountLoginSelect.value = val;
								}
							},
							() => [
								h(
									ElRadio,
									{
										label: "delete"
									},
									() => "登录并剔除其他用户"
								),
								h(
									ElRadio,
									{
										label: "save"
									},
									() => "登录并保留其他用户"
								)
							]
						)
					]);
				}
			}).then(() => {
				if (accountLoginSelect.value === "delete") {
					clearSameAccount();
				}
			});
		}
	});
};

export default useLoginSuccess;
