<!-- 分栏布局 -->
<template>
	<el-container class="layout">
		<div class="aside-split">
			<div class="logo flx-center">
				<img v-if="sysConfig.system_logo" :src="sysConfig.system_logo" style="vertical-align: middle" alt="logo" />
			</div>
			<el-scrollbar>
				<div class="split-list">
					<div
						class="split-item"
						:class="{ 'split-active': splitActive === item.path || `/${splitActive.split('/')[1]}` === item.path }"
						v-for="item in menuList"
						:key="item.path"
						@click="changeSubMenu(item)"
					>
						<el-icon>
							<component :is="item.meta.icon"></component>
						</el-icon>
						<span class="title">{{ item.meta.title }}</span>
					</div>
				</div>
			</el-scrollbar>
		</div>
		<el-aside :class="{ 'not-aside': !subMenu.length }" :style="{ width: isCollapse ? '65px' : '180px' }">
			<div class="logo flx-center">
				<span v-show="subMenu.length">{{ isCollapse ? "" : sysConfig.system_shortName }}</span>
			</div>
			<el-scrollbar>
				<el-menu
					:default-active="activeMenu"
					:router="false"
					:collapse="isCollapse"
					:collapse-transition="false"
					:unique-opened="true"
					background-color="#ffffff"
				>
					<SubMenu :menuList="subMenu" />
				</el-menu>
			</el-scrollbar>
		</el-aside>
		<el-container>
			<el-header>
				<ToolBarLeft />
				<ToolBarRight />
			</el-header>
			<Main />
		</el-container>
	</el-container>
</template>

<script setup lang="ts" name="layoutColumns">
import { ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { GlobalStore } from "@/stores";
import { AuthStore } from "@/stores/modules/auth";
import Main from "@/layouts/components/Main/index.vue";
import ToolBarLeft from "@/layouts/components/Header/ToolBarLeft.vue";
import ToolBarRight from "@/layouts/components/Header/ToolBarRight.vue";
import SubMenu from "@/layouts/components/Menu/SubMenu.vue";
import useSysConfig from "@/hooks/useSysConfig";

const route = useRoute();
const router = useRouter();
const authStore = AuthStore();
const globalStore = GlobalStore();
const { sysConfig } = useSysConfig();
const activeMenu = computed(() => (route.meta.activeMenu ? route.meta.activeMenu : route.path));
const menuList = computed(() => authStore.showMenuListGet);
const isCollapse = computed(() => globalStore.themeConfig.isCollapse);

const subMenu = ref<Menu.MenuOptions[]>([]);
const splitActive = ref<string>("");
watch(
	() => [menuList, route],
	() => {
		// 当前菜单没有数据直接 return
		if (!menuList.value.length) return;
		// splitActive.value = route.path;
		const menuItem = menuList.value.filter(
			(item: Menu.MenuOptions) => route.path === item.path || `/${route.path.split("/")[1]}` === item.path
		);
		console.log(menuItem);
		// if (menuItem[0] && menuItem[0].children?.length) {
		// 	subMenu.value = menuItem[0].children;
		// 	return;
		// }
		// subMenu.value = [];
	},
	{
		deep: true,
		immediate: true
	}
);

// 切换 SubMenu
const changeSubMenu = (item: Menu.MenuOptions) => {
	splitActive.value = item.path;
	if (item.children && item.children.length) {
		subMenu.value = item.children;
		return;
	}
	subMenu.value = [];
	router.push(item.path);
};
</script>

<style scoped lang="scss">
@import "./index.scss";
</style>

<style lang="scss">
.columns {
	.el-menu,
	.el-menu--popup {
		.el-menu-item {
			&.is-active {
				background: var(--el-color-primary-light-9);
				&::before {
					position: absolute;
					top: 0;
					right: 0;
					bottom: 0;
					width: 4px;
					content: "";
					background: var(--el-color-primary);
				}
			}
		}
	}
}
</style>
