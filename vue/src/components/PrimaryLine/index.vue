<template>
	<div :class="['primary_line', type]" :style="{ margin }"></div>
</template>
<script setup lang="ts">
interface TitleProp {
	margin?: string;
	type?: "primary" | "default";
}
withDefaults(defineProps<TitleProp>(), {
	// margin: "12px 0",
	margin: "8px 0",
	type: "default"
});
</script>
<style lang="scss" scoped>
.primary_line {
	width: 100%;
	height: 1.5px;
	background-color: var(--el-border-color);
}
.primary {
	background-color: var(--el-color-primary);
}
</style>
