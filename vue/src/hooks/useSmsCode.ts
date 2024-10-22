import { ref, computed, onMounted, onUnmounted } from "vue";
import { ElMessage } from "element-plus";
import { querySmsCode } from "@/api/modules/common";
import type { SmsCodeType } from "@/enums/systemEnum";
import { GlobalStore } from "@/stores";
import i18n from "@/languages/index";

const useSmsCode = () => {
	const globalStore = GlobalStore();
	const { t } = i18n.global;
	const smsLoading = ref(false);
	let second = ref(0);
	let secondTimer: any = 0;
	const maxSecond = 60; // 最大间隔时间

	const smsBtnStr = computed(() => {
		if (second.value == 0) return t("login.sendSmsCode");
		return `${second.value}${t("login.second")}${t("login.repeat")}`;
	});

	const getSmsCode = (params: { type: SmsCodeType; phone: string; imgCaptcha?: string }) => {
		if (smsLoading.value) return;
		if (params.type === "register" && !params.imgCaptcha) {
			ElMessage({
				message: `${t("common.pleaseInput")}${t("login.captcha")}`,
				type: "error"
			});
			return;
		}
		smsLoading.value = true;
		querySmsCode(params)
			.then(() => {
				second.value = maxSecond;
				startInterval();
				ElMessage({
					message: t("common.sendSuccess"),
					type: "success"
				});
			})
			.finally(() => {
				smsLoading.value = false;
			});
	};

	const startInterval = () => {
		secondTimer = setInterval(() => {
			let newSecond = second.value - 1;
			if (newSecond <= 0) {
				clearInterval(secondTimer);
				secondTimer = 0;
				second.value = 0;
				globalStore.setSmsSecond(0);
			} else {
				second.value = newSecond;
				globalStore.setSmsSecond(second.value);
			}
		}, 1000);
	};

	const endInterval = () => {
		clearInterval(secondTimer);
		secondTimer = 0;
	};

	onMounted(() => {
		second.value = globalStore.smsSecond || 0;
		if (secondTimer) {
			endInterval();
		}
		if (second.value > 0) {
			startInterval();
		}
	});

	onUnmounted(() => {
		endInterval();
	});
	return {
		second,
		smsBtnStr,
		getSmsCode,
		smsLoading
	};
};

export default useSmsCode;
