/**
 * v-auth
 * 按钮权限指令
 */
import { AuthStore } from "@/stores/modules/auth";
import type { Directive, DirectiveBinding } from "vue";

const auth: Directive = {
	mounted(el: HTMLElement, binding: DirectiveBinding) {
		const { value } = binding;
		const authStore = AuthStore();
		const currentPageRoles: any = authStore.authButtonListGet;
		if (value instanceof Array && value.length) {
			const hasPermission = value.every(item => currentPageRoles.includes(item));
			if (!hasPermission) el.parentNode && el.parentNode.removeChild(el);
		} else {
			if (value.indexOf("|") !== -1) {
				let orValArr = value.split("|");
				const hasPermission = orValArr.some((item: any) => currentPageRoles.includes(item));
				if (!hasPermission) el.parentNode && el.parentNode.removeChild(el);
			} else {
				if (!currentPageRoles.includes(value)) el.parentNode && el.parentNode.removeChild(el);
			}
		}
	}
};

export default auth;
