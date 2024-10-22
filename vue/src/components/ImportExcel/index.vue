<template>
	<el-dialog v-model="dialogVisible" :title="dialogTitle" :destroy-on-close="true" width="580px" draggable>
		<el-form class="drawer-multiColumn-form" label-width="100px">
			<el-form-item :label="$t('common.templateDownload')">
				<el-button type="primary" :icon="Download" @click="downloadTemp">{{ $t("common.download") }}</el-button>
			</el-form-item>
			<el-form-item :label="$t('common.fileUpload')">
				<el-upload
					action="string"
					class="upload"
					:drag="true"
					:limit="excelLimit"
					:multiple="false"
					:show-file-list="true"
					:http-request="uploadExcel"
					:before-upload="beforeExcelUpload"
					:on-exceed="handleExceed"
					:on-error="excelUploadError"
					accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
				>
					<el-icon class="el-icon--upload"><upload-filled /></el-icon>
					<div class="el-upload__text">
						{{ $t("common.dragFileHere") }}/<em>{{ $t("common.clickToUpload") }}</em>
					</div>
					<template #tip>
						<div class="el-upload__tip">{{ $t("common.format") }}：.xls , .xlsx</div>
					</template>
				</el-upload>
			</el-form-item>
		</el-form>
	</el-dialog>
</template>

<script setup lang="tsx" name="ImportExcel">
import { ref, computed } from "vue";
import { Download } from "@element-plus/icons-vue";
import { ElNotification } from "element-plus";
import useFormat18n from "@/hooks/useFormat18n";
import useBatchNotify from "@/hooks/useBatchNotify";

export interface Prop {
	isBatch?: boolean; //是否批量上传
}

export interface ExcelParameterProps {
	title: string; // 标题
	templateUrl: string; // 下载模板的url
	importApi: (params: any) => Promise<any>; // 批量导入的Api
	getTableList?: (p?: any) => Promise<any>; // 获取表格数据的Api
}

// 最大文件上传数
const excelLimit = ref(1);
// dialog状态
const dialogVisible = ref(false);
// 父组件传过来的参数
const parameter = ref<Partial<ExcelParameterProps>>({});
const dialogTitle = computed(() => {
	return useFormat18n(["common.batch", "common.import"]) + parameter.value.title;
});

// 接收父组件参数
const props = withDefaults(defineProps<Prop>(), {
	isBatch: true
});
const acceptParams = (params?: any): void => {
	parameter.value = params;
	dialogVisible.value = true;
};

// Excel 导入模板下载
const downloadTemp = () => {
	if (!parameter.value.templateUrl) return;
	window.open(parameter.value.templateUrl);
};

// 文件上传
const uploadExcel = async (param: any) => {
	let excelFormData = new FormData();
	excelFormData.append("file", param.file);
	try {
		const res = await parameter.value.importApi!(excelFormData);
		if (res.status === 0) {
			if (props.isBatch) {
				useBatchNotify(res.data);
				parameter.value.getTableList && parameter.value.getTableList();
			} else {
				parameter.value.getTableList && parameter.value.getTableList(res.data);
			}
		}
		dialogVisible.value = false;
	} catch (error) {}
};

/**
 * 上传成功后的处理，消息弹出结果提示
 */
/*
const handleTipsResult = (param: Batch.BatchResultVo) => {

	let totalHandle = useFormat18n("common.totalHandle");
	let item = useFormat18n("common.item");
	let success = useFormat18n("common.success");
	let fail = useFormat18n("common.fail");
	let errorRecords = useFormat18n("common.errorRecords");
	let rowNo = useFormat18n("common.rowNo");
	param.errList.forEach((errRow: Batch.errMsgRow) => {
		errRow.errorMsg = useFormat18n(`error.${errRow.errorMsg}`);
	});
	ElNotification({
		title: useFormat18n(["common.upload", "common.success"]),
		duration: 0,
		type: "warning",
		position: "bottom-right",
		message: (
			<div>
				<div>
					{totalHandle} {param.total} {item}，{success} {param.successCount} {item}，{fail} {param.errorCount} {item}
				</div>
				<div style={{ marginBottom: "5px" }}>{errorRecords}:</div>
				{param.errList.map(errRow => (
					<div style={{ display: "flex", gap: "5px" }}>
						<div>
							{rowNo}:{errRow.rowNum}
						</div>
						<div>{errRow.data}</div>
						<div>{errRow.errorMsg}</div>
					</div>
				))}
			</div>
		)
	});
};
*/

/**
 * @description 文件上传之前判断
 * @param file 上传的文件
 * */
const beforeExcelUpload = (file: any) => {
	const isExcel =
		file.type === "application/vnd.ms-excel" || file.type === "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	const fileSize = file.size / 1024 / 1024 < 200;
	if (!isExcel)
		ElNotification({
			title: "温馨提示",
			message: "上传文件只能是 xls / xlsx 格式！",
			type: "warning"
		});
	if (!fileSize)
		ElNotification({
			title: "温馨提示",
			message: "上传文件大小不能超过 200MB！",
			type: "warning"
		});
	return isExcel && fileSize;
};

// 文件数超出提示
const handleExceed = (): void => {
	ElNotification({
		title: "温馨提示",
		message: "最多只能上传一个文件！",
		type: "warning"
	});
};

// 上传错误提示
const excelUploadError = (): void => {
	ElNotification({
		title: "温馨提示",
		message: `批量添加${parameter.value.title}失败，请您重新上传！`,
		type: "error"
	});
};

// 上传成功提示
// const excelUploadSuccess = (): void => {
// 	ElNotification({
// 		title: "温馨提示",
// 		message: `批量添加${parameter.value.title}成功！`,
// 		type: "success"
// 	});
// };

defineExpose({
	acceptParams
});
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>
