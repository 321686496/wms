<template>
	<el-drawer v-model="drawerVisible" :title="$t('system.layoutSettings')" size="300px">
		<!-- 布局切换 -->
		<el-divider class="divider" content-position="center">
			<el-icon><Notification /></el-icon>
			{{ $t("system.layoutSwitching") }}
		</el-divider>
		<div class="layout-box">
			<el-tooltip effect="dark" :content="$t('system.portrait')" placement="top" :show-after="200">
				<div
					:class="['layout-item layout-vertical', themeConfig.layout == 'vertical' ? 'is-active' : '']"
					@click="changeLayout('vertical')"
				>
					<div class="layout-dark"></div>
					<div class="layout-container">
						<div class="layout-light"></div>
						<div class="layout-content"></div>
					</div>
					<el-icon v-if="themeConfig.layout == 'vertical'"><CircleCheckFilled /></el-icon>
				</div>
			</el-tooltip>
			<el-tooltip effect="dark" :content="$t('system.classical')" placement="top" :show-after="200">
				<div
					:class="['layout-item layout-classic', themeConfig.layout == 'classic' ? 'is-active' : '']"
					@click="changeLayout('classic')"
				>
					<div class="layout-dark"></div>
					<div class="layout-container">
						<div class="layout-light"></div>
						<div class="layout-content"></div>
					</div>
					<el-icon v-if="themeConfig.layout == 'classic'"><CircleCheckFilled /></el-icon>
				</div>
			</el-tooltip>
			<el-tooltip effect="dark" :content="$t('system.transverse')" placement="top" :show-after="200">
				<div
					:class="['layout-item layout-transverse', themeConfig.layout == 'transverse' ? 'is-active' : '']"
					@click="changeLayout('transverse')"
				>
					<div class="layout-dark"></div>
					<div class="layout-content"></div>
					<el-icon v-if="themeConfig.layout == 'transverse'"><CircleCheckFilled /></el-icon>
				</div>
			</el-tooltip>
			<el-tooltip effect="dark" :content="$t('system.columns')" placement="top" :show-after="200">
				<div
					:class="['layout-item layout-columns', themeConfig.layout == 'columns' ? 'is-active' : '']"
					@click="changeLayout('columns')"
				>
					<div class="layout-dark"></div>
					<div class="layout-light"></div>
					<div class="layout-content"></div>
					<el-icon v-if="themeConfig.layout == 'columns'"><CircleCheckFilled /></el-icon>
				</div>
			</el-tooltip>
		</div>
		<br />

		<!-- 全局主题 -->
		<el-divider class="divider" content-position="center">
			<el-icon><ColdDrink /></el-icon>
			{{ $t("system.globalTheme") }}
		</el-divider>
		<div class="theme-item">
			<span>{{ $t("system.themeColors") }}</span>
			<el-color-picker v-model="themeConfig.primary" :predefine="colorList" @change="changePrimary" />
		</div>
		<div class="theme-item">
			<span>{{ $t("system.darkMode") }}</span>
			<SwitchDark></SwitchDark>
		</div>
		<div class="theme-item">
			<span>{{ $t("system.grayMode") }}</span>
			<el-switch v-model="themeConfig.isGrey" @change="changeGreyOrWeak($event, 'grey')" />
		</div>
		<div class="theme-item">
			<span>{{ $t("system.colorWeaknessMode") }}</span>
			<el-switch v-model="themeConfig.isWeak" @change="changeGreyOrWeak($event, 'weak')" />
		</div>
		<br />

		<!-- 界面设置 -->
		<el-divider class="divider" content-position="center">
			<el-icon><Setting /></el-icon>
			{{ $t("system.interfaceSettings") }}
		</el-divider>
		<div class="theme-item">
			<span>{{ $t("system.collapseMenu") }}</span>
			<el-switch v-model="themeConfig.isCollapse" />
		</div>
		<div class="theme-item">
			<span>{{ $t("system.breadcrumbs") }}</span>
			<el-switch v-model="themeConfig.breadcrumb" />
		</div>
		<div class="theme-item">
			<span>{{ $t("system.breadcrumbIcon") }}</span>
			<el-switch v-model="themeConfig.breadcrumbIcon" />
		</div>
		<div class="theme-item">
			<span>{{ $t("system.labelBar") }}</span>
			<el-switch v-model="themeConfig.tabs" />
		</div>
		<div class="theme-item">
			<span>{{ $t("system.labelBarIcon") }}</span>
			<el-switch v-model="themeConfig.tabsIcon" />
		</div>
		<div class="theme-item">
			<span>{{ $t("system.footer") }}</span>
			<el-switch v-model="themeConfig.footer" />
		</div>
	</el-drawer>
</template>

<script setup lang="ts">
import { ref, computed, watch } from "vue";
import { useTheme } from "@/hooks/useTheme";
import { GlobalStore } from "@/stores";
import { LayoutType } from "@/stores/interface";
import { DEFAULT_PRIMARY } from "@/config/config";
import SwitchDark from "@/components/SwitchDark/index.vue";
import mittBus from "@/utils/mittBus";

const { changePrimary, changeGreyOrWeak } = useTheme();

// 预定义主题颜色
const colorList = [
	DEFAULT_PRIMARY,
	"#DAA96E",
	"#0C819F",
	"#409EFF",
	"#27ae60",
	"#ff5c93",
	"#e74c3c",
	"#fd726d",
	"#f39c12",
	"#9b59b6"
];

const globalStore = GlobalStore();
const themeConfig = computed(() => globalStore.themeConfig);

// 切换布局方式
const changeLayout = (val: LayoutType) => {
	globalStore.setThemeConfig({ ...themeConfig.value, layout: val });
};

// 监听布局变化，在 body 上添加相对应的 layout class
watch(
	() => themeConfig.value.layout,
	() => {
		const body = document.body as HTMLElement;
		body.setAttribute("class", themeConfig.value.layout);
	},
	{ immediate: true }
);

// 打开主题设置
const drawerVisible = ref(false);
mittBus.on("openThemeDrawer", () => (drawerVisible.value = true));
</script>

<style scoped lang="scss">
@import "./index.scss";
</style>
