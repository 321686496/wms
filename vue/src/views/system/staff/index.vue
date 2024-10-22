<template>
	<div class="table-box">
		<ProTable ref="proTable" :title="pageTitle" :columns="columns" :requestApi="getStaffList">
			<template #tableHeader>
				<el-button type="primary" @click="openDialog('add')">{{ $t("common.add") }}</el-button>
			</template>

			<!-- 表格操作 -->
			<template #operation="scope">
				<el-space>
					<el-button type="warning" size="small" @click="onResetPwd(scope.row)">{{
						$t("common.reset") + $t("login.password")
					}}</el-button>
					<el-dropdown size="default">
						<el-button type="success" size="small">
							{{ $t("common.more") }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
						</el-button>
						<template #dropdown>
							<el-dropdown-menu>
								<!-- 编辑 -->
								<div class="dropdown-item" @click="openDialog('update', { ...scope.row })">
									{{ $t("common.edit") }}
								</div>
								<!-- 删除 -->
								<div class="dropdown-item" @click="onDelete(scope.row)">
									{{ $t("common.delete") }}
								</div>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</el-space>
			</template>
		</ProTable>
		<form-dialog width="70vw" :columns="formColumns" ref="formDialogRef" />
	</div>
</template>

<script setup lang="ts" name="system_staff">
import { ref, h } from "vue";
import { ElMessageBox } from "element-plus";
import { getStaffList, addStaffOne, updateStaffOne, deleteStaffOne, resetStaffPwdOne } from "@/api/modules/system";
import { queryRoleAll } from "@/api/modules/select";
import { ColumnProps } from "@/components/ProTable/interface";
import { FormColumn } from "@/components/FormDialog/interface";
import { SystemVo } from "@/typings/modules/system";
import useFormat18n from "@/hooks/useFormat18n";
import { useHandleData } from "@/hooks/useHandleData";
import useSysConfig from "@/hooks/useSysConfig";
import { genderValueList, userStatusList } from "@/utils/serviceDict";
// import { useClipboard } from "@vueuse/core";

const { sysConfig } = useSysConfig();
const pageTitle = "用户";
const proTable = ref();
const formDialogRef = ref();
// const { copy } = useClipboard();

// 表格配置项
const columns: ColumnProps[] = [
	{
		prop: "phone",
		minWidth: 150,
		label: useFormat18n("login.mobile"),
		search: { el: "input" }
	},
	{
		prop: "realName",
		minWidth: 120,
		label: useFormat18n("system.realName")
	},
	{
		prop: "roleId",
		minWidth: 120,
		enum: queryRoleAll,
		search: { el: "select", props: { filterable: true } },
		label: useFormat18n("system.role")
	},
	{
		prop: "shopName",
		minWidth: 120,
		label: useFormat18n("base.shopName")
	},
	{
		prop: "status",
		minWidth: 120,
		label: useFormat18n("common.status"),
		enum: userStatusList,
		search: { el: "select", props: { filterable: true } },
		tag: true
	},
	{
		prop: "sex",
		minWidth: 120,
		label: useFormat18n("system.gender")
	},
	{
		prop: "priority",
		minWidth: 90,
		label: useFormat18n("common.priority")
	},
	{
		prop: "createdAt",
		minWidth: 170,
		label: useFormat18n("common.createdAt")
	},
	{
		prop: "remark",
		minWidth: 120,
		label: useFormat18n("common.remark")
	},
	{ prop: "operation", label: useFormat18n("common.operate"), fixed: "right", width: 160 }
];

// const phoneValidator = (rule: any, value: any, callback: any) => {
// 	let reg = new RegExp(sysConfig.system_regex_phone);
// 	if (reg.test(value)) {
// 		callback();
// 	} else {
// 		callback(new Error());
// 	}
// };

const pwdValidator = (rule: any, value: any, callback: any) => {
	let reg = new RegExp(sysConfig.system_regex_password);
	if (reg.test(value)) {
		callback();
	} else {
		callback(new Error());
	}
};

// 表单配置
const formColumns: FormColumn[] = [
	{
		field: "phone",
		type: "text",
		label: useFormat18n("login.mobile"),
		placeholder: useFormat18n(["common.pleaseInput", "login.mobile"]),
		rules: [
			{ required: true, message: useFormat18n(["common.pleaseInput", "login.mobile"]) }
			// { validator: phoneValidator, message: useFormat18n("error.PhoneFormatError"), trigger: "blur" }
		]
	},
	{
		field: "roleId",
		type: "select",
		label: useFormat18n("system.role"),
		action: queryRoleAll,
		placeholder: useFormat18n(["common.pleaseSelect", "system.role"]),
		rules: [{ required: true, message: useFormat18n(["common.pleaseSelect", "system.role"]) }]
	},
	{
		field: "pwd",
		type: "password",
		unmount: ["update"],
		label: useFormat18n("login.password"),
		placeholder: useFormat18n(["common.pleaseInput", "login.password"]),
		rules: [
			{ required: true, message: useFormat18n(["common.pleaseInput", "login.password"]) },
			{ validator: pwdValidator, message: useFormat18n("error.PasswordFormatError"), trigger: "blur" }
		]
	},
	{
		field: "realName",
		type: "text",
		label: useFormat18n("system.realName"),
		placeholder: useFormat18n(["common.pleaseInput", "system.realName"]),
		rules: [{ required: true, message: useFormat18n(["common.pleaseInput", "system.realName"]) }]
	},
	{
		field: "status",
		type: "radioButton",
		label: useFormat18n("common.status"),
		optionsData: userStatusList
	},
	{
		field: "sex",
		type: "radioButton",
		label: useFormat18n("system.gender"),
		optionsData: genderValueList
	},
	{
		field: "shopName",
		type: "text",
		label: useFormat18n("base.shopName"),
		placeholder: useFormat18n(["common.pleaseInput", "base.shopName"])
	},
	{
		field: "priority",
		type: "number",
		label: useFormat18n("common.priority"),
		extraProps: {
			precision: 0
		}
	},
	{
		field: "remark",
		type: "text",
		label: useFormat18n("common.remark"),
		placeholder: useFormat18n(["common.pleaseInput", "common.remark"])
	}
];

// 新增/编辑
const openDialog = (type: "update" | "add", rowData?: SystemVo.StaffVo) => {
	if (type === "update" && rowData) {
		delete rowData.createdAt;
		delete rowData.updatedAt;
		delete rowData.loginDate;
		delete rowData.pwdUpdateDate;
	}
	let params = {
		title: useFormat18n(`common.${type}`) + pageTitle,
		rowData: {
			...(rowData || {
				sex: "男",
				status: "NORMAL"
			})
		},
		formType: type,
		api: type === "add" ? addStaffOne : updateStaffOne,
		getTableList: proTable.value.getTableList
	};
	formDialogRef.value.acceptParams(params);
};

// 删除
const onDelete = async (rowData: SystemVo.StaffVo) => {
	await useHandleData(deleteStaffOne, { id: rowData.id || 0 }, useFormat18n(["common.delete", "system.staff"]));
	proTable.value.getTableList();
};

/*
const onCopy = (str: string) => {
	copy(str);
	ElMessage({
		message: useFormat18n(["common.copy", "common.success"]),
		type: "success"
	});
};
*/

// 重置密码
const onResetPwd = async (rowData: SystemVo.StaffVo) => {
	const res: any = await useHandleData(
		resetStaffPwdOne,
		{ id: rowData.id || 0 },
		useFormat18n(["common.reset", "login.password"])
	);
	proTable.value.getTableList();
	// const fullMsg = `登录地址：${window.location.host}；登录账号：${rowData.phone}；登录密码：${res.data}；`;
	ElMessageBox({
		title: useFormat18n("common.tips"),
		message: h("div", null, [
			h("p", null, [
				h("span", null, `登录地址：${window.location.host}`)
				// h("span", { class: "link", style: "margin-left: 3px", onClick: () => onCopy(window.location.host) }, "复制")
			]),
			h("p", null, [
				h("span", null, `登录账号：${rowData.phone}`)
				// h("span", { class: "link", style: "margin-left: 3px", onClick: () => onCopy(rowData.phone) }, "复制")
			]),
			h("p", null, [
				h("span", null, `登录密码：${res.data}`)
				// h("span", { class: "link", style: "margin-left: 3px", onClick: () => onCopy(res.data) }, "复制")
			])
			// h("p", null, [h("span", { class: "link", style: "margin-left: 3px", onClick: () => onCopy(fullMsg) }, "一键全部复制")])
		])
	});
};
</script>

<style lang="scss"></style>
