<template>
	<div class="bar_code_wrap" :style="{ width: boxWidth, height: boxHeight, background: boxBackground }">
		<div style="display: inline-block">
			<svg class="bar_code_canvas" ref="barcodeRef"></svg>
		</div>
		<div>
			<slot></slot>
		</div>
	</div>
</template>

<script setup lang="ts" name="BarCode">
import { ref, onMounted, computed, watch } from "vue";
import JsBarcode from "jsbarcode";

interface PropVo {
	boxWidth?: string;
	boxHeight?: string;
	boxBackground?: string;
	value: string; //值
	format?: "CODE39" | "CODE128"; //条形码的类型
	width?: number; //每个条条的宽度，注意这里不是指整个条形码的宽度
	height?: number; //整个条形码的高度
	text?: string;
	fontOptions?: string; //设置条形码文本的粗体和斜体样式 bold / italic / bold italic
	font?: string | number; //设置条形码显示文本的字体
	displayValue?: boolean; //是否显示条形码下面的文字
	textAlign?: "left" | "center" | "right"; //条形码文本的水平对齐方式，和css中的类似： left / center / right
	fontSize?: number; //设置条形码文本的字体大小
	margin?: number; //整个条形码的外面距
	marginLeft?: number; //整个条形码的左边距
	marginTop?: number; //整个条形码的上边距
	marginBottom?: number; //整个条形码的下边距
	marginRight?: number; //整个条形码的右边距
}

const props = withDefaults(defineProps<PropVo>(), {
	boxWidth: "145px",
	boxHeight: "75px",
	boxBackground: "#ffffff",
	value: "",
	format: "CODE128",
	width: 1,
	height: 40,
	margin: 0,
	marginLeft: 0,
	marginTop: 0,
	marginBottom: 0,
	marginRight: 0,
	textAlign: "left",
	displayValue: true,
	fontSize: 12
});

const labelCode = computed(() => props.value);
watch(labelCode, () => {
	render();
});
/*
const props = defineProps({
	//设置文本的字体
	font: [String, Number],
	//设置文本的水平对齐方式
	textAlign: [String],
	//设置文本的垂直位置
	textPosition: [String],
	//设置条形码和文本之间的间距
	textMargin: [String, Number],
	//设置文本的大小
	fontSize: {
		type: [String, Number],
		default: 15
	},
	//设置条和文本的颜色
	lineColor: [String],
	//设置条形码的背景
	background: {
		type: [String],
		default: "#eee"
	},
	//设置条形码周围的空白边距
	margin: [String, Number],
	// 是否在条形码下方显示文字
	displayValue: {
		type: [String, Boolean],
		default: true
	}
});
*/

const settings: any = {
	...props
};

const removeUndefinedProps = (obj: any) => {
	for (let prop in obj) {
		if (obj.hasOwnProperty(prop) && obj[prop] === undefined) {
			delete obj[prop];
		}
	}
};

const barcodeRef = ref(null);

onMounted(() => {
	removeUndefinedProps(settings);
	render();
});

const render = () => {
	JsBarcode(barcodeRef.value, props.value, settings);
};
</script>

<style lang="scss">
.bar_code_canvas {
	width: 100%;
}
</style>
