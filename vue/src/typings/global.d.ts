// * Menu
declare namespace Menu {
	interface MenuOptions {
		path: string;
		name: string;
		component?: string | (() => Promise<any>);
		redirect?: string;
		meta: MetaProps;
		children?: MenuOptions[];
	}
	interface MetaProps {
		icon: string;
		title: string;
		activeMenu?: string;
		isLink?: string;
		isHide: boolean;
		isFull: boolean;
		isAffix: boolean;
		isKeepAlive: boolean;
	}
}

// Batch
declare namespace Batch {
	interface errMsgRow {
		data: string;
		errorMsg: string;
		rowNum: number;
	}
	interface BatchResultVo {
		total: number; //总数
		errorCount: number; //失败数
		successCount: number; //成功数
		errList: errMsgRow[]; //失败消息原因
	}
}

// * Vite
declare type Recordable<T = any> = Record<string, T>;

declare interface ViteEnv {
	VITE_API_URL: string;
	VITE_PORT: number;
	VITE_OPEN: boolean;
	VITE_GLOB_APP_TITLE: string;
	VITE_DROP_CONSOLE: boolean;
	VITE_PROXY_URL: string;
	VITE_BUILD_GZIP: boolean;
	VITE_REPORT: boolean;
}

declare interface treeOption {
	label: string;
	value: string | number;
	// value: any;
	children?: treeOption[];
	disabled?: boolean;
}

interface ColumnProps<T = any> extends Partial<Omit<TableColumnCtx<T>, "children" | "renderHeader" | "renderCell">> {
	tag?: boolean; // 是否是标签展示
	isShow?: boolean; // 是否显示在表格当中
	isSearchShow?: boolean; // 是否显示在搜索表单当中
	search?: SearchProps | undefined; // 搜索项配置
	priority?: number; // 排序
	enum?: EnumProps[] | ((params?: any) => Promise<any>); // 枚举类型（渲染值的字典）
	isFilterEnum?: boolean; // 当前单元格值是否根据 enum 格式化（示例：enum 只作为搜索项数据）
	fieldNames?: { label: string; value: string; children?: string }; // 指定 label && value 的 key 值
	headerRender?: (row: ColumnProps) => any; // 自定义表头内容渲染（tsx语法）
	render?: (scope: { row: T }) => any; // 自定义单元格内容渲染（tsx语法）
	_children?: ColumnProps<T>[]; // 多级表头
}
