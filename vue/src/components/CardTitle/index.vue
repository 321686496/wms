<template>
	<div class="title-wrap">
		<span class="title-line"></span>
		<span class="title-txt">{{ title }}</span>
	</div>
</template>

<script setup lang="ts">
interface TitleProp {
	title?: string;
}
withDefaults(defineProps<TitleProp>(), {
	title: ""
});
</script>

<style lang="scss" scoped>
.title-wrap {
	display: flex;
	align-items: center;
	.title-line {
		width: 5px;
		height: 20px;
		margin-right: 10px;
		background-color: var(--el-color-primary);
	}
}
</style>
