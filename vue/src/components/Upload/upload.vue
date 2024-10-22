<template>
	<div class="file_uplaod_wrap">
		<el-upload
			action="#"
			v-model:file-list="fileList"
			:multiple="true"
			:disabled="self_disabled"
			:limit="limit"
			:http-request="handleHttpUpload"
			:before-upload="beforeUpload"
			:on-exceed="handleExceed"
			:on-success="uploadSuccess"
			:on-error="uploadError"
		>
			<el-button :disabled="self_disabled" type="primary">{{ $t("common.upload") }}</el-button>
			<template #file="{ file }">
				<div class="file_row flx-justify-between">
					<file-item :name="file.name" :url="file.url"> </file-item>
					<div class="del_wrap flx-center" v-show="!self_disabled" @click.capture="handleRemove(file)">
						<el-icon>
							<Delete />
						</el-icon>
					</div>
				</div>
			</template>
		</el-upload>
	</div>
</template>

<script setup lang="ts" name="Upload">
import { inject, computed, onMounted } from "vue";
import { Delete } from "@element-plus/icons-vue";
import { uploadFile, uploadQiNiuImg } from "@/api/modules/upload";
import type { UploadProps, UploadFile, UploadRequestOptions } from "element-plus";
import { ElNotification, formContextKey, formItemContextKey } from "element-plus";
import FileItem from "./FileItem.vue";
import useFormat18n from "@/hooks/useFormat18n";
import useSysConfig from "@/hooks/useSysConfig";
import useQiniuToken from "./hooks/useQiniuToken";

type FileTypes =
	| "image/apng"
	| "image/bmp"
	| "image/gif"
	| "image/jpeg"
	| "image/pjpeg"
	| "image/png"
	| "image/svg+xml"
	| "image/tiff"
	| "image/webp"
	| "image/x-icon";

interface UploadFileProps {
	fileList: any[];
	api?: (params: any) => Promise<any>; // 上传图片的 api 方法，一般项目上传都是同一个 api 方法，在组件里直接引入即可 ==> 非必传
	drag?: boolean; // 是否支持拖拽上传 ==> 非必传（默认为 true）
	disabled?: boolean; // 是否禁用上传组件 ==> 非必传（默认为 false）
	limit?: number; // 最大图片上传数 ==> 非必传（默认为 5张）
	fileSize?: number; // 图片大小限制 ==> 非必传（默认为 5M）
	limitFileType?: boolean; // 是否限制文件类型
	fileType?: FileTypes[]; // 图片类型限制 ==> 非必传（默认为 ["image/jpeg", "image/png", "image/gif"]）
	height?: string; // 组件高度 ==> 非必传（默认为 150px）
	width?: string; // 组件宽度 ==> 非必传（默认为 150px）
	borderRadius?: string; // 组件边框圆角 ==> 非必传（默认为 8px）
}

const props = withDefaults(defineProps<UploadFileProps>(), {
	fileList: () => [],
	drag: true,
	disabled: false,
	limit: 10,
	fileSize: 500,
	limitFileType: false,
	fileType: () => ["image/jpeg", "image/png", "image/gif"],
	height: "150px",
	width: "150px",
	borderRadius: "8px"
});

// 获取 el-form 组件上下文
const formContext = inject(formContextKey, void 0);
// 获取 el-form-item 组件上下文
const formItemContext = inject(formItemContextKey, void 0);
// 判断是否禁用上传和删除
const self_disabled = computed(() => {
	return props.disabled || formContext?.disabled;
});

const { sysConfig } = useSysConfig();
const { qiniuTokenData, updateQiniuToken, createFileName } = useQiniuToken();

// const fileList = ref<UploadUserFile[]>(props.fileList);
const fileList = computed({
	get() {
		return props.fileList;
	},
	set(val) {
		emit("update:fileList", val);
	}
});

/**
 * @description 文件上传之前判断
 * @param rawFile 上传的文件
 * */
const beforeUpload: UploadProps["beforeUpload"] = rawFile => {
	const imgSize = rawFile.size / 1024 / 1024 < props.fileSize;
	const imgType = props.fileType;
	if (props.limitFileType && !imgType.includes(rawFile.type as FileTypes))
		ElNotification({
			title: "温馨提示",
			message: "上传图片不符合所需的格式！",
			type: "warning"
		});
	if (!imgSize)
		ElNotification({
			title: "温馨提示",
			message: `上传附件大小不能超过 ${props.fileSize}M！`,
			type: "warning"
		});
	return imgSize;
};

/**
 * @description 图片上传
 * @param options 上传的文件
 * */
const handleHttpUpload = async (options: UploadRequestOptions) => {
	let formData = new FormData();
	formData.append("file", options.file);
	const originFileName = options.file.name;
	if (sysConfig.system_upload_type === "qiniuyun") {
		formData.append("token", qiniuTokenData.value.token);
		formData.append("fileName", createFileName(originFileName));
		formData.append("fileBinaryData", options.file);
	}
	try {
		const api = sysConfig.system_upload_type === "qiniuyun" ? uploadQiNiuImg : uploadFile;
		const { data } = await api(formData);
		let url = "";
		if (sysConfig.system_upload_type === "qiniuyun") {
			url = `${qiniuTokenData.value.domain}/${data.data.key}`;
		} else {
			url = data.url;
		}
		data.url = url;
		options.onSuccess(data);
	} catch (error) {
		options.onError(error as any);
	}
};

// 删除文件
const handleRemove = (uploadFile: any) => {
	const filterArr = fileList.value.filter(item => item.url !== uploadFile.url || item.name !== uploadFile.name);
	fileList.value = filterArr;
	emit("update:fileList", filterArr);
};

// 图片上传成功
interface UploadEmits {
	(e: "update:fileList", value: any[]): void;
}
const emit = defineEmits<UploadEmits>();
const uploadSuccess = (response: { url: string } | undefined, uploadFile: UploadFile) => {
	if (!response) return;
	uploadFile.url = response.url;
	emit("update:fileList", fileList.value);
	// 调用 el-form 内部的校验方法（可自动校验）
	formItemContext?.prop && formContext?.validateField([formItemContext.prop as string]);
	ElNotification({
		title: useFormat18n("common.tips"),
		message: useFormat18n(["common.upload", "common.success"]),
		type: "success"
	});
};

// 图片上传错误提示
const uploadError = () => {
	ElNotification({
		title: useFormat18n("common.tips"),
		message: useFormat18n(["common.upload", "common.fail"]),
		type: "error"
	});
};

// 文件数超出提示
const handleExceed = () => {
	ElNotification({
		title: "温馨提示",
		message: `当前最多只能上传 ${props.limit} 个，请移除后上传！`,
		type: "warning"
	});
};

// 图片预览
// const viewImageUrl = ref("");
// const imgViewVisible = ref(false);
// const handlePictureCardPreview: UploadProps["onPreview"] = uploadFile => {
// 	viewImageUrl.value = uploadFile.url!;
// 	imgViewVisible.value = true;
// };
onMounted(() => {
	if (sysConfig.system_upload_type === "qiniuyun") {
		updateQiniuToken();
	}
});
</script>

<style scoped lang="scss">
.file_uplaod_wrap {
	:deep(.el-upload-list__item) {
		width: 100% !important;
		height: 20px !important;
	}
	.file_row {
		.del_wrap {
			padding-left: 5px;
			cursor: pointer;
		}
	}
}
</style>
