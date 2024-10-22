<template>
	<el-dropdown trigger="click" @command="handleSetLanguage">
		<i :class="'iconfont icon-zhongyingwen'" class="toolBar-icon"></i>
		<template #dropdown>
			<el-dropdown-menu>
				<el-dropdown-item :disabled="language && language === 'zh'" command="zh">简体中文</el-dropdown-item>
				<el-dropdown-item :disabled="language === 'fr'" command="fr">French</el-dropdown-item>
			</el-dropdown-menu>
		</template>
	</el-dropdown>
</template>

<script setup lang="ts">
import { computed, onMounted } from "vue";
import { useI18n } from "vue-i18n";
import { GlobalStore } from "@/stores";
import { TabsStore } from "@/stores/modules/tabs";
import { getBrowserLang } from "@/utils/util";

const i18n = useI18n();
const globalStore = GlobalStore();
const tabsStore = TabsStore();
const language = computed((): string => globalStore.language);

// 切换语言
const handleSetLanguage = async (lang: string) => {
	i18n.locale.value = lang;
	globalStore.updateLanguage(lang);
	tabsStore.closeMultipleTab();
	window.location.reload();
};

onMounted(async () => {
	let lang = language.value || getBrowserLang();
	i18n.locale.value = lang;
	globalStore.updateLanguage(lang);
});
</script>
