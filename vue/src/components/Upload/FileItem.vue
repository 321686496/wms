<template>
	<div class="file_item_row flx-justify-between">
		<div class="flx-align-center">
			<el-icon>
				<Paperclip />
			</el-icon>
			&nbsp;&nbsp;
			<el-link type="primary" :title="name" class="file_name" target="_blank" :href="url">{{ name }}</el-link>
		</div>
	</div>
</template>

<script setup lang="ts" name="FileItem">
interface FileItemProp {
	name?: string;
	url?: string;
}

withDefaults(defineProps<FileItemProp>(), {
	name: "",
	url: ""
});
</script>

<style scoped lang="scss">
.file_item_row {
	.file_name {
		max-width: 120px;
		:deep(.el-link__inner) {
			display: inline-block;
			max-width: 100%;
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
		}
	}
}
</style>
