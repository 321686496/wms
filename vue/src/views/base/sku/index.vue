<template>
	<div class="table-box">
		<ProTable ref="proTable" colSettingType="base_sku" :title="pageTitle" :columns="columns" :requestApi="getSkuList">
			<template #tableHeader="scope">
				<el-space>
					<el-button type="primary" @click="openDialog('add')">{{ useFormat18n("common.add") }}</el-button>
					<el-dropdown :disabled="!scope.isSelected" size="default">
						<el-button type="primary" :disabled="!scope.isSelected">
							{{ useFormat18n("common.batch") }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
						</el-button>
						<template #dropdown>
							<el-dropdown-menu>
								<div class="dropdown-item" @click="onBatch('delete', scope.selectedList)">
									{{ useFormat18n("common.delete") }}
								</div>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
					<el-upload action="#" :before-upload="onUpload">
						<el-button type="primary">Upload</el-button>
					</el-upload>
				</el-space>
			</template>
			<!-- 表格操作 -->
			<template #operation="scope">
				<el-button type="primary" size="small" @click="openDialog('update', { ...scope.row })">{{
					useFormat18n("common.edit")
				}}</el-button>
				<el-button type="danger" size="small" @click="onDelete(scope.row)">{{ useFormat18n("common.delete") }}</el-button>
			</template>
		</ProTable>
		<FormDialog width="70vw" :columns="formColumns" ref="formDialogRef" />
	</div>
</template>

<script setup lang="ts" name="base_sku">
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { getSkuList, addSkuOne, updateSkuOne, deleteSkuOne, deleteSkuBatch } from "@/api/modules/base";
import { ColumnProps } from "@/components/ProTable/interface";
import { FormColumn } from "@/components/FormDialog/interface";
import { useHandleData } from "@/hooks/useHandleData";
import useBatchNotify from "@/hooks/useBatchNotify";
import useFormat18n from "@/hooks/useFormat18n";
import * as XLSX from "xlsx";
import { getSkuTypeList } from "@/api/modules/type";

const readExcelFile = (file: any) => {
	return new Promise((resolve, reject) => {
		const reader = new FileReader();
		reader.onload = (e: any) => {
			const data = new Uint8Array(e.target.result);
			const workbook = XLSX.read(data, { type: "array" });
			const worksheet = workbook.Sheets[workbook.SheetNames[0]];
			const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });
			resolve(jsonData);
		};
		reader.onerror = e => {
			reject(e);
		};
		reader.readAsArrayBuffer(file);
	});
};

const sleepTime = (time: number) => {
	return new Promise(resolve => {
		setTimeout(() => {
			resolve(true);
		}, time);
	});
};

const onUpload = async (file: any) => {
	try {
		const res: any = await readExcelFile(file);
		let arr: any[] = [];
		res.forEach((arrRow: any) => {
			if (arrRow.length === 2) {
				arr.push({
					name: arrRow[0].trim(),
					code: arrRow[1].trim()
				});
			}
		});
		arr.forEach((item: any, index: number) => {
			item.priority = arr.length - index;
		});
		let current = 1;
		// for (let row of arr) {
		// 	let sameArr: any[] = [];
		// 	arr.forEach((v: any, idx: number) => {
		// 		if (v.code == row.code) {
		// 			sameArr.push({
		// 				...v,
		// 				idx
		// 			});
		// 		}
		// 	});
		// 	if (sameArr.length > 1) {
		// 		console.log(sameArr);
		// 		break;
		// 	}
		// }
		for (let one of arr) {
			ElMessage({
				type: "success",
				message: `${current}/${arr.length}`,
				duration: 200
			});
			await sleepTime(200);
			await addSkuOne(one);
			current += 1;
		}
	} catch (error) {
		console.log(error);
	}
	return false;
};

const pageTitle = useFormat18n("base.product");
const proTable = ref();
const formDialogRef = ref();
// 商品类型
const shoppingTypes = ref([]);

// 表格配置项
const columns = ref<ColumnProps[]>([
	{ type: "selection", fixed: "left", width: 60 },
	{
		prop: "name",
		label: useFormat18n("base.productName"),
		minWidth: 120,
		search: { el: "input" }
	},
	{
		prop: "code",
		label: useFormat18n("base.productCode"),
		minWidth: 120,
		search: { el: "input" }
	},
	{
		prop: "typeName",
		label: useFormat18n("base.typeName"),
		minWidth: 120,
		search: { el: "input" }
	},
	{
		prop: "typeNameFr",
		label: useFormat18n("base.typeNameFr"),
		minWidth: 120,
		search: { el: "input" }
	},
	{
		prop: "priority",
		label: useFormat18n("common.priority"),
		sortable: true
	},
	{
		prop: "createdAt",
		minWidth: 150,
		label: useFormat18n("common.createdAt")
	},
	{ prop: "operation", label: useFormat18n("common.operate"), width: 180 }
]);

// 表单配置
const formColumns = ref<FormColumn[]>([
	{
		field: "name",
		type: "text",
		label: useFormat18n("base.productName"),
		rules: [{ required: true, message: useFormat18n("base.productName"), trigger: "blur" }]
	},
	{
		field: "code",
		type: "text",
		label: useFormat18n("base.productCode")
	},
	{
		field: "typeId",
		type: "select",
		optionsData: shoppingTypes.value,
		label: useFormat18n("base.typeName"),
		rules: [{ required: true, message: useFormat18n("base.productName"), trigger: "blur" }]
	},
	{
		field: "priority",
		type: "number",
		label: useFormat18n("common.priority"),
		extraProps: {
			precision: 0
		}
	}
]);

// 删除
const onDelete = async (rowData: any) => {
	await useHandleData(deleteSkuOne, { id: rowData.id || 0 }, useFormat18n("common.delete"));
	proTable.value.getTableList();
};

// 新增/编辑
const openDialog = (type: "update" | "add", rowData?: any) => {
	formColumns.value[2].optionsData = shoppingTypes.value;

	if (type === "update" && rowData) {
		delete rowData.createdAt;
		delete rowData.updatedAt;
	}
	let params = {
		title: useFormat18n(`common.${type}`) + pageTitle,
		rowData: {
			...(rowData || { priority: 0, enable: true, typeId: 1 })
		},
		formType: type,
		api: type == "add" ? addSkuOne : updateSkuOne,
		getTableList: proTable.value.getTableList
	};
	formDialogRef.value.acceptParams(params);
};

// 批量删除
const handleDeleteBatch = async (ids: number[]) => {
	try {
		const res: any = await useHandleData(deleteSkuBatch, { ids }, useFormat18n("common.delete"));
		proTable.value.getTableList();
		proTable.value.clearSelection();
		if (res.status === 0) {
			useBatchNotify(res.data);
		}
	} catch (error) {}
};

// 批量操作
const onBatch = (command: string, selectedList: any[]) => {
	switch (command) {
		case "delete":
			handleDeleteBatch(selectedList.map((row: any) => row.id || 0));
			break;
		default:
			break;
	}
};

// 加载商品类型
const loadTypeList = async () => {
	const res: any = await getSkuTypeList({ pageSize: -1 });
	shoppingTypes.value = res.data.map((i: { classifyName: any; id: any }) => {
		return { label: i.classifyName, value: i.id };
	});
	formColumns.value[2].optionsData = shoppingTypes.value;
};
loadTypeList();
</script>

<style lang="scss"></style>
