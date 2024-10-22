<template>
	<div>
		<el-button @click="openDrawer" type="primary">查看</el-button>
		<el-drawer append-to-body v-model="drawerVisible" title="详情" size="60%" destroy-on-close>
			<template #header>
				<div class="flx-justify-between">
					<div></div>
					<div class="fullscreen" style="margin-right: 10px">
						<i :class="['iconfont', isFullscreen ? 'icon-suoxiao' : 'icon-fangda']" class="toolBar-icon" @click="toggle"></i>
					</div>
				</div>
			</template>
			<div class="editor_wrap">
				<WangEditor
					ref="editor_wrap_ref"
					hide-tool-bar
					disabled
					:height="isFullscreen ? '100vh' : 'calc(100vh - 160px)'"
					v-model:value="docContent"
				/>
			</div>
		</el-drawer>
	</div>
</template>

<script setup lang="ts" name="EditorDrawer">
import { ref } from "vue";
import { useFullscreen } from "@vueuse/core";
import WangEditor from "./index.vue";

interface DrawerProp {
	modelValue: string;
}

// 接受父组件参数
const props = withDefaults(defineProps<DrawerProp>(), {
	modelValue: ""
});

const editor_wrap_ref = ref();
const { toggle, isFullscreen } = useFullscreen(editor_wrap_ref);
const drawerVisible = ref(false);
const docContent = ref("");

const openDrawer = () => {
	docContent.value = props.modelValue || "";
	drawerVisible.value = true;
};
</script>

<style lang="scss" scoped>
.editor_wrap {
	.editor-disabled {
		cursor: default !important;
	}
	:deep(.editor-box) {
		border: 0;
	}
	:deep(.table-container) {
		border: 0;
	}
}
</style>
