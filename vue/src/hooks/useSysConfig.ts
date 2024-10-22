import { queryCommonConfig } from "@/api/modules/common";
import { GlobalStore } from "@/stores";

const useSysConfig = () => {
	const globalStore = GlobalStore();

	const { sysConfig } = globalStore;
	const getSysConfig = () => {
		queryCommonConfig().then(res => {
			globalStore.setSysConfig(res.data);
			// head icon 处理
			const docIcon = res.data.system_icon;
			if (docIcon) {
				let $favicon = document.createElement("link");
				$favicon.rel = "icon";
				$favicon.href = docIcon;
				document.head.appendChild($favicon);
			}
		});
	};
	return {
		sysConfig,
		getSysConfig
	};
};

export default useSysConfig;
