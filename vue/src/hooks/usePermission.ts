import { AuthStore } from "@/stores/modules/auth";

const usePermission = (direct: string) => {
	const authStore = AuthStore();
	const currentPagePermissions: any = authStore.authButtonListGet;
	const has_add_perm = currentPagePermissions.includes(direct);
	return has_add_perm;
};

export default usePermission;
