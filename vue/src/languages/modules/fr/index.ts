import en_US from "vxe-table/lib/locale/lang/en-US";
import common from "./common";
import error from "./error";
import login from "./login";
import system from "./system";
import menu from "./menu";
import base from "./base";
import type from "./type";

export default {
	...en_US,
	...common,
	login,
	error,
	system,
	menu,
	base,
	type
};
