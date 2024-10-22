import { TabsStore } from "@/stores/modules/tabs";
import { KeepAliveStore } from "@/stores/modules/keepAlive";

const useBack = (removeName: string, removePath: string) => {
	const tabsStore = TabsStore();
	const keepAliveStore = KeepAliveStore();
	keepAliveStore.removeKeepAliveName(removeName);
	tabsStore.removeTabs(removePath, false);
};

export default useBack;
