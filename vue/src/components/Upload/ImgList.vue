<template>
	<div class="img_list">
		<cus-image v-for="(item, index) in imgList" :key="index" :src="item"> </cus-image>
	</div>
</template>
<script setup lang="ts" name="ImgList">
import { computed } from "vue";
import CusImage from "../CusImage/index.vue";
interface ImgListProps {
	value: string | string[];
}
const props = withDefaults(defineProps<ImgListProps>(), {
	value: ""
});
const imgList = computed(() => {
	if (typeof props.value == "string") {
		return props.value.split(",");
	} else if (Array.isArray(props.value)) {
		return props.value;
	} else {
		return props.value;
	}
});
</script>
<style lang="scss">
.img_list {
	display: flex;
	gap: 5px;
}
</style>
