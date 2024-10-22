import { GlobalStore } from "@/stores";
import { queryTenantInfo } from "@/api/modules/login";

const useTenantConfig = () => {
	const globalStore = GlobalStore();
	const { tenantConfigGet } = globalStore;
	const getTenantConfig = () => {
		return new Promise((resolve, reject) => {
			queryTenantInfo()
				.then((res: any) => {
					const result = {
						...res.data,
						moduleListStr: res.data.moduleList
							.filter((modul: any) => modul.needCheck == false || modul.isExpired == false)
							.map((modul: any) => modul.type)
							.join(","),
						openVerifyBillList: res.data.openVerifyBill ? res.data.openVerifyBill.split(",") : []
					};
					globalStore.setTenantInfo(result);
					resolve(result);
				})
				.catch(() => {
					reject();
				});
		});
	};
	return {
		getTenantConfig,
		tenantConfig: tenantConfigGet
	};
};

export default useTenantConfig;
