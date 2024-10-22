import type { FormItemRule } from "element-plus";

export interface FormColumn {
	field: string; //字段名
	type: string; // 类型
	label?: string; //标签
	width?: string; //宽
	placeholder?: string; //输入提示
	optionsData?: treeOption[]; //选项值列表
	actionData?: object; //请求选项的请求参数
	titleHelp?: string; //提示文字
	action?: (params: any) => Promise<any>; //请求选项的请求接口
	rules?: FormItemRule | FormItemRule[]; //表单校验规则
	extraProps?: {
		//额外的属性
		multiple?: boolean;
		filterable?: boolean;
		optionsData?: treeOption[];
		props?: object;
		accept?: string;
		listType?: string;
		limit?: number;
		min?: number;
		max?: number;
		precision?: number;
		format?: string;
		valueFormat?: string;
		controls?: boolean;
		readonly?: boolean;
		clearable?: boolean;
		[key: string]: any;
	};
	fieldNames?: { label: string; value: string };
	disabled?: boolean | string[] | Function; //是否禁用表单
	span?: number; // flex布局占位数
	hidden?: boolean | string[]; // 是否隐藏
	unmount?: boolean | string[]; // 是否卸载（不渲染）
	onChange?: (e: any) => void;
}
