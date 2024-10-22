<template>
	<div class="tool-bar-ri">
		<div class="header-icon">
			<!-- <AssemblySize id="assemblySize" /> -->
			<a href="https://github.com/">
				<img src="@/assets/images/GitHub.png" alt="#" />
			</a>
			<Language id="language" />
			<!-- <SearchMenu id="searchMenu" /> -->
			<!-- <ThemeSetting id="themeSetting" /> -->
			<!-- <Fullscreen id="fullscreen" /> -->
		</div>
		<!-- <span class="username">{{ userInfo.realName || "" }}</span> -->
		<!-- <span class="username">{{ userInfo.phone || "" }}</span> -->
		<span class="username"></span>
		<Avatar />
	</div>
</template>

<script setup lang="ts">
// import { computed } from "vue";
// import { GlobalStore } from "@/stores";
// import SearchMenu from "./components/SearchMenu.vue";
// import Fullscreen from "./components/Fullscreen.vue";
// import ThemeSetting from "./components/ThemeSetting.vue";
// import AssemblySize from "./components/AssemblySize.vue";
import Avatar from "./components/Avatar.vue";
import Language from "./components/Language.vue";

// const globalStore = GlobalStore();
// const userInfo = computed(() => globalStore.userInfo);
</script>

<style scoped lang="scss">
.tool-bar-ri {
	display: flex;
	align-items: center;
	justify-content: center;

	// margin: 0 30px 0 0;
	img {
		height: 25px;
		object-fit: cover;
	}
	.header-icon {
		display: flex;
		align-items: center;
		& > * {
			margin-left: 21px;
		}
	}
	.username {
		margin-left: 10px;
		font-size: 15px;
	}
}
</style>
