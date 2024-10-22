// * Element 常用表单校验规则
/**
 *  @rule 手机号
 */
export function checkUserName(rule: any, value: any, callback: any) {
	const regexp = /^[a-zA-Z]([-_a-zA-Z0-9]{5,17})$/;
	if (value === "") callback("请输入用户名");
	if (!regexp.test(value)) {
		callback(new Error("用户名以字母开头，长度在6~18，只能包含字母、数字和下划线"));
	} else {
		return callback();
	}
}

/**
 *  @rule 手机号
 */
export function checkPhoneNumber(rule: any, value: any, callback: any) {
	const regexp = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
	if (value === "") callback("请输入手机号码");
	if (!regexp.test(value)) {
		callback(new Error("请输入正确的手机号码"));
	} else {
		return callback();
	}
}

/**
 *  @rule 密码
 */
export function checkPwd(rule: any, value: any, callback: any) {
	const regexp = /^([a-zA-Z0-9_@]){6,18}$/;
	if (value === "") callback("请输入密码");
	if (!regexp.test(value)) {
		callback(new Error("请输入正确的密码格式"));
	} else {
		return callback();
	}
}

/**
 *  @rule 验证码
 */
export function checkVerifyCode(rule: any, value: any, callback: any) {
	const regexp = /^[0-9]{6}$/;
	if (value === "") callback("请输入验证码");
	if (!regexp.test(value)) {
		callback(new Error("请输入正确的验证码格式"));
	} else {
		return callback();
	}
}

/**
 *  @rule 统一社会信用代码
 */
export function checkCreditCode(rule: any, value: any, callback: any) {
	const regexp = /[^_IOZSVa-z\W]{2}\d{6}[^_IOZSVa-z\W]{10}/;
	if (value === "") callback("请输入统一社会信用代码");
	if (!regexp.test(value)) {
		callback(new Error("请输入正确的统一社会信用代码格式"));
	} else {
		return callback();
	}
}
