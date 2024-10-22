export namespace Table {
	export interface Pageable {
		current: number;
		pageSize: number;
		total: number;
	}
	export interface TableStateProps {
		tableData: any[];
		pageable: Pageable;
		searchParam: {
			[key: string]: any;
		};
		searchInitParam: {
			[key: string]: any;
		};
		totalParam: {
			[key: string]: any;
		};
		additional: {
			[key: string]: any;
		};
		icon?: {
			[key: string]: any;
		};
	}
}

export namespace HandleData {
	export type MessageType = "" | "success" | "warning" | "info" | "error";
}

export namespace Format18N {
	export type originType = string | string[];
}
