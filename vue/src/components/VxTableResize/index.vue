<template>
	<div class="vx_table_resize table" ref="resizeRef">
		<div class="reseizer_table" :style="{ height: `${sizerHeight}px` }">
			<slot></slot>
		</div>
	</div>
</template>
<script setup lang="ts" name="VxTableResize">
import { ref } from "vue";
import { useResizeObserver } from "@vueuse/core";

const resizeRef = ref(null);
const sizerHeight = ref(100);
useResizeObserver(resizeRef, entries => {
	const entry = entries[0];
	const { height } = entry.contentRect;
	sizerHeight.value = height;
});
</script>
<style lang="scss" scoped></style>
