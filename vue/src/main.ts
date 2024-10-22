import { createApp } from "vue";
import App from "./App.vue";
// reset style sheet
import "@/styles/reset.scss";
// CSS common style sheet
import "@/styles/common.scss";
// iconfont css
import "@/assets/iconfont/iconfont.scss";
// element plus
import ElementPlus from "element-plus";
// element icons
import * as Icons from "@element-plus/icons-vue";
// element css
import "element-plus/dist/index.css";
// element dark(内置暗黑模式)
import "element-plus/theme-chalk/dark/css-vars.css";
import "element-plus/theme-chalk/display.css";
// custom element dark(自定义暗黑模式)
import "@/styles/theme/element-dark.scss";
// custom element css
import "@/styles/element.scss";
// custom directives
import directives from "@/directives/index";
// 全局组件
import VXETable from "vxe-table";
import "vxe-table/lib/style.css";
import ProTable from "@/components/ProTable/index.vue";
import FormInput from "@/components/FormInput/index.vue";
import FormDialog from "@/components/FormDialog/index.vue";
import RanderInfo from "@/components/RanderInfo/index.vue";
import RanderForm from "@/components/RanderForm/index.vue";
import CusImage from "@/components/CusImage/index.vue";
import CardTitle from "@/components/CardTitle/index.vue";
import PrimaryLine from "@/components/PrimaryLine/index.vue";
import ImportExcel from "@/components/ImportExcel/index.vue";

// vue Router
import router from "@/routers/index";
// pinia store
import pinia from "@/stores/index";
// vue i18n
import I18n from "@/languages/index";
// svg icons
import "virtual:svg-icons-register";
// errorHandler
import errorHandler from "@/utils/errorHandler";

const app = createApp(App);

app.config.errorHandler = errorHandler;

// 注册element Icons组件
Object.keys(Icons).forEach(key => {
	app.component(key, Icons[key as keyof typeof Icons]);
});

VXETable.setup({
	// 对组件内置的提示语进行国际化翻译
	i18n: (key, args) => I18n.global.t(key, args),
	size: "small",
	loadingText: "loading...",
	table: {
		syncResize: true,
		autoResize: true,
		resizeConfig: {
			refreshDelay: 0
		}
	}
});

// 注册全局组件
app.component("ProTable", ProTable);
app.component("FormInput", FormInput);
app.component("FormDialog", FormDialog);
app.component("CusImage", CusImage);
app.component("RanderInfo", RanderInfo);
app.component("RanderForm", RanderForm);
app.component("PrimaryLine", PrimaryLine);
app.component("CardTitle", CardTitle);
app.component("ImportExcel", ImportExcel);

app.use(router).use(pinia).use(directives).use(I18n).use(ElementPlus).use(VXETable).mount("#app");
