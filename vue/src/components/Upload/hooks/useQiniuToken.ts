import { computed } from "vue";
import dayjs from "dayjs";
import { GlobalStore } from "@/stores";
import { getQiNiuTokenApi } from "@/api/modules/upload";

const useQiniuToken = () => {
	const globalStore = GlobalStore();
	const { qiniuTokenData } = globalStore;

	const checkIsExpired = () => {
		const nowTimeStamp = new Date().getTime();
		const tokenExpireTime = qiniuTokenData.expired || 0;
		return nowTimeStamp > tokenExpireTime;
	};

	const createFileName = (originFileName: string) => {
		const splitArr = originFileName.split(".");
		const ext = splitArr[splitArr.length - 1];
		const tenantId = globalStore.userInfo.tenantId || 0;
		const nowTime = dayjs().format("YYYYMMDDHHmmss");
		return `${tenantId}_${nowTime}.${ext}`;
	};

	const updateQiniuToken = () => {
		if (!qiniuTokenData.token || checkIsExpired()) {
			const nowTimeStamp = new Date().getTime();
			getQiNiuTokenApi().then(res => {
				const tokenData = res.data;
				tokenData.expired = (tokenData.expired - 10) * 1000 + nowTimeStamp;
				globalStore.setQiniuToken(res.data);
			});
		}
	};
	return {
		updateQiniuToken,
		createFileName,
		qiniuTokenData: computed(() => globalStore.qiniuTokenData)
	};
};

export default useQiniuToken;
