<template>
	<div class="upload-box">
		<el-upload
			action="#"
			:id="uuid"
			:class="['upload', self_disabled ? 'disabled' : '', drag ? 'no-border' : '']"
			:multiple="false"
			:disabled="self_disabled"
			:show-file-list="false"
			:http-request="handleHttpUpload"
			:before-upload="beforeUpload"
			:on-success="uploadSuccess"
			:on-error="uploadError"
			:accept="fileType.join(',')"
		>
			<template v-if="modelValue">
				<img :src="modelValue" class="upload-image" />
				<div class="upload-handle" @click.stop>
					<div class="handle-icon" @click="editImg" v-if="!self_disabled">
						<el-icon><Edit /></el-icon>
						<span>{{ $t("common.edit") }}</span>
					</div>
					<div class="handle-icon" @click="imgViewVisible = true">
						<el-icon><ZoomIn /></el-icon>
						<span>{{ $t("common.preview") }}</span>
					</div>
					<div class="handle-icon" @click="deleteImg" v-if="!self_disabled">
						<el-icon><Delete /></el-icon>
						<span>{{ $t("common.delete") }}</span>
					</div>
				</div>
			</template>
			<template v-else>
				<div class="upload-empty">
					<slot name="empty">
						<el-icon><Plus /></el-icon>
						<!-- <span>请上传图片</span> -->
					</slot>
				</div>
			</template>
		</el-upload>
		<div class="el-upload__tip">
			<slot name="tip"></slot>
		</div>
		<el-image-viewer v-if="imgViewVisible" @close="imgViewVisible = false" :url-list="[modelValue]" />
	</div>
</template>

<script setup lang="ts" name="UploadImg">
import { ref, computed, inject, onMounted } from "vue";
import { uploadImg, uploadQiNiuImg } from "@/api/modules/upload";
import { generateUUID } from "@/utils/util";
import { ElNotification, formContextKey, formItemContextKey } from "element-plus";
import type { UploadProps, UploadRequestOptions } from "element-plus";
import useFormat18n from "@/hooks/useFormat18n";
import useSysConfig from "@/hooks/useSysConfig";
import useQiniuToken from "./hooks/useQiniuToken";

type FileTypes =
	| "image/apng"
	| "image/bmp"
	| "image/gif"
	| "image/jpg"
	| "image/jpeg"
	| "image/pjpeg"
	| "image/png"
	| "image/svg+xml"
	| "image/tiff"
	| "image/webp"
	| "image/avif"
	| "image/PNG"
	| "image/JPG"
	| "image/JPEG"
	| "image/x-icon";

interface UploadFileProps {
	modelValue: string; // 图片地址 ==> 必传
	api?: (params: any) => Promise<any>; // 上传图片的 api 方法，一般项目上传都是同一个 api 方法，在组件里直接引入即可 ==> 非必传
	drag?: boolean; // 是否支持拖拽上传 ==> 非必传（默认为 true）
	disabled?: boolean; // 是否禁用上传组件 ==> 非必传（默认为 false）
	fileSize?: number; // 图片大小限制 ==> 非必传（默认为 5M）
	fileType?: FileTypes[]; // 图片类型限制 ==> 非必传（默认为 ["image/jpeg", "image/png", "image/gif"]）
	height?: string; // 组件高度 ==> 非必传（默认为 90px）
	width?: string; // 组件宽度 ==> 非必传（默认为 90px）
	borderRadius?: string; // 组件边框圆角 ==> 非必传（默认为 8px）
}

// 接受父组件参数
const props = withDefaults(defineProps<UploadFileProps>(), {
	modelValue: "",
	drag: false,
	disabled: false,
	fileSize: 5,
	fileType: () => [
		"image/PNG",
		"image/JPG",
		"image/JPEG",
		"image/jpeg",
		"image/jpg",
		"image/png",
		"image/gif",
		"image/apng",
		"image/bmp",
		"image/pjpeg",
		"image/webp",
		"image/avif"
	],
	height: "90px",
	width: "90px",
	borderRadius: "8px"
});

// 生成组件唯一id
const uuid = ref("id-" + generateUUID());

// 查看图片
const imgViewVisible = ref(false);
// 获取 el-form 组件上下文
const formContext = inject(formContextKey, void 0);
// 获取 el-form-item 组件上下文
const formItemContext = inject(formItemContextKey, void 0);
// 判断是否禁用上传和删除
const self_disabled = computed(() => {
	return props.disabled || formContext?.disabled;
});

/**
 * @description 图片上传
 * @param options 上传的文件
 * */
interface UploadEmits {
	(e: "update:modelValue", value: string): void;
	(e: "check-validate"): void;
}

const emit = defineEmits<UploadEmits>();

const { sysConfig } = useSysConfig();
const { qiniuTokenData, updateQiniuToken, createFileName } = useQiniuToken();

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
		const api = sysConfig.system_upload_type === "qiniuyun" ? uploadQiNiuImg : uploadImg;
		const { data } = await api(formData);
		let url = "";
		if (sysConfig.system_upload_type === "qiniuyun") {
			url = `${qiniuTokenData.value.domain}/${data.data.key}`;
		} else {
			url = data.url;
		}
		emit("update:modelValue", url);
		// 调用 el-form 内部的校验方法（可自动校验）
		formItemContext?.prop && formContext?.validateField([formItemContext.prop as string]);
		emit("check-validate");
	} catch (error) {
		options.onError(error as any);
	}
};

/**
 * @description 删除图片
 * */
const deleteImg = () => {
	emit("update:modelValue", "");
};

/**
 * @description 编辑图片
 * */
const editImg = () => {
	const dom = document.querySelector(`#${uuid.value} .el-upload__input`);
	dom && dom.dispatchEvent(new MouseEvent("click"));
};

/**
 * @description 文件上传之前判断
 * @param rawFile 上传的文件
 * */
const beforeUpload: UploadProps["beforeUpload"] = rawFile => {
	const imgSize = rawFile.size / 1024 / 1024 < props.fileSize;
	const imgType = props.fileType;
	if (!imgType.includes(rawFile.type as FileTypes))
		ElNotification({
			title: "温馨提示",
			message: "上传图片不符合所需的格式！",
			type: "warning"
		});
	if (!imgSize)
		ElNotification({
			title: "温馨提示",
			message: `上传图片大小不能超过 ${props.fileSize}M！`,
			type: "warning"
		});
	return imgType.includes(rawFile.type as FileTypes) && imgSize;
};

// 图片上传成功提示
const uploadSuccess = () => {
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

onMounted(() => {
	if (sysConfig.system_upload_type === "qiniuyun") {
		updateQiniuToken();
	}
});
</script>
<style scoped lang="scss">
.is-error {
	.upload {
		:deep(.el-upload),
		:deep(.el-upload-dragger) {
			border: 1px dashed var(--el-color-danger) !important;
			&:hover {
				border-color: var(--el-color-primary) !important;
			}
		}
	}
}
:deep(.disabled) {
	.el-upload,
	.el-upload-dragger {
		cursor: not-allowed !important;
		background: var(--el-disabled-bg-color);
		border: 1px dashed var(--el-border-color-darker) !important;
		&:hover {
			border: 1px dashed var(--el-border-color-darker) !important;
		}
	}
}
.upload-box {
	.no-border {
		:deep(.el-upload) {
			border: none !important;
		}
	}
	:deep(.upload) {
		.el-upload {
			position: relative;
			display: flex;
			align-items: center;
			justify-content: center;
			width: v-bind(width);
			height: v-bind(height);
			overflow: hidden;
			border: 1px dashed var(--el-border-color-darker);
			border-radius: v-bind(borderRadius);
			transition: var(--el-transition-duration-fast);
			&:hover {
				border-color: var(--el-color-primary);
				.upload-handle {
					opacity: 1;
				}
			}
			.el-upload-dragger {
				display: flex;
				align-items: center;
				justify-content: center;
				width: 100%;
				height: 100%;
				padding: 0;
				overflow: hidden;
				background-color: transparent;
				border: 1px dashed var(--el-border-color-darker);
				border-radius: v-bind(borderRadius);
				&:hover {
					border: 1px dashed var(--el-color-primary);
				}
			}
			.el-upload-dragger.is-dragover {
				background-color: var(--el-color-primary-light-9);
				border: 2px dashed var(--el-color-primary) !important;
			}
			.upload-image {
				width: 100%;
				height: 100%;
				object-fit: contain;
			}
			.upload-empty {
				position: relative;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;
				font-size: 12px;
				line-height: 30px;
				color: var(--el-color-info);
				.el-icon {
					font-size: 28px;
					color: var(--el-text-color-secondary);
				}
			}
			.upload-handle {
				position: absolute;
				top: 0;
				right: 0;
				box-sizing: border-box;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;
				width: 100%;
				height: 100%;
				cursor: pointer;
				background: rgb(0 0 0 / 60%);
				opacity: 0;
				transition: var(--el-transition-duration-fast);
				.handle-icon {
					box-sizing: border-box;
					display: flex;
					align-items: center;
					justify-content: center;
					color: aliceblue;
					.el-icon {
						// margin-bottom: 20%;
						font-size: 130%;

						// line-height: 130%;
					}
					span {
						margin-left: 3px;
						font-size: 85%;

						// line-height: 85%;
					}
				}
			}
		}
	}
	.el-upload__tip {
		line-height: 18px;
		text-align: center;
	}
}
</style>
