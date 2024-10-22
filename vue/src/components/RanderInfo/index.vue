<template>
	<div class="render-info">
		<el-form style="width: 100%" :label-width="props.labelWidth" :label-position="props.labelPosition" label-suffix="：">
			<el-row :gutter="10">
				<el-col
					v-for="(item, index) in renderList"
					:key="index"
					v-show="item.isShow"
					v-bind="breakPoint || { span: item.span || 24 }"
				>
					<el-form-item :label="item.label">
						<div class="sle" v-if="!item.type || item.type == 'text'">{{ item.value }}</div>
						<div v-else-if="item.type === 'editor'" v-html="item.value"></div>
						<Upload disabled v-else-if="item.type === 'file'" v-model:fileList="item.value" />
						<ImgList v-else-if="item.type === 'img'" :value="item.value"></ImgList>
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
	</div>
</template>

<script setup lang="ts" name="RanderInfo">
import Upload from "@/components/Upload/upload.vue";
import ImgList from "@/components/Upload/ImgList.vue";
import { ref, computed } from "vue";
import { BreakPoint } from "@/components/Grid/interface";
import { ATTR_FIELDS } from "@/config/config";

export interface RanderItem {
	label: string;
	span: number;
	value: any;
	type?: "text" | "editor" | "file" | "audit" | "img" | "source";
	field?: string;
	hidden?: boolean;
	isShow?: boolean;
	priority?: number;
	disabled?: boolean;
	tenantDisabled?: boolean;
}
interface RanderInfoProp {
	list: RanderItem[];
	labelWidth?: any;
	labelPosition?: string;
	breakPoint?: Record<BreakPoint, number>;
	colSettingType?: string;
}
const props = withDefaults(defineProps<RanderInfoProp>(), {
	list: () => [],
	labelWidth: "100px",
	labelPosition: "left"
});

const colColumns = ref<any[]>([]);

const renderList = computed(() => {
	let list: RanderItem[] = props.list.map(item => {
		item.isShow = !item.hidden;

		const hasCol = colColumns.value.find(v => v.prop == item.field);
		if (hasCol) {
			if (ATTR_FIELDS.includes(hasCol.prop)) {
				item.isShow = item.isShow || hasCol.isShow;
			} else {
				item.isShow = item.isShow && hasCol.isShow;
			}
			item.priority = hasCol.priority || 0;
			item.disabled = hasCol.disabled || false;
		}
		return item;
	});
	return list.sort((a, b) => (a.priority || 0) - (b.priority || 0));
});
</script>

<style lang="scss" scoped>
.render-info {
	:deep(.el-form-item__label) {
		padding-right: 0;
		font-weight: bold;
	}
	:deep(.el-form-item) {
		margin-bottom: 3px;
	}
}
</style>
