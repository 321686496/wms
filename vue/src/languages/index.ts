import { createI18n } from "vue-i18n";
import zh from "./modules/zh";
import fr from "./modules/fr";
import { getBrowserLang } from "@/utils/util";

const storageData = JSON.parse(localStorage.getItem("GlobalState") || "{}");
let lang = storageData.language || getBrowserLang();
const i18n = createI18n({
	legacy: false, // 如果要支持 compositionAPI，此项必须设置为 false
	locale: lang || "zh", // 设置语言类型
	globalInjection: true, // 全局注册$t方法
	messages: {
		zh,
		fr
	}
});

export default i18n;
