<template>
	<el-image
		v-bind="$attrs"
		:src="src || zhanwei"
		:z-index="999999"
		preview-teleported
		:preview-src-list="[src]"
		:style="{
			width,
			height
		}"
	>
	</el-image>
</template>

<script setup lang="ts">
import zhanwei from "@/assets/images/zhanwei.png";
interface CusImageProp {
	src?: string;
	width?: string;
	height?: string;
}
withDefaults(defineProps<CusImageProp>(), {
	src: "",
	width: "40px",
	height: "40px"
});
</script>

<style lang="scss"></style>
