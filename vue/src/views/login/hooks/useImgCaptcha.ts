import { ref } from "vue";
import { queryImgCaptcha } from "@/api/modules/common";
const useImgCaptcha = () => {
	const captchaLoading = ref(false);
	const captchaUrl = ref("");
	const getCaptcha = () => {
		if (captchaLoading.value) return;
		captchaLoading.value = true;
		queryImgCaptcha()
			.then(res => {
				captchaUrl.value = res.data;
			})
			.finally(() => {
				captchaLoading.value = false;
			});
	};
	return {
		getCaptcha,
		captchaLoading,
		captchaUrl
	};
};

export default useImgCaptcha;
