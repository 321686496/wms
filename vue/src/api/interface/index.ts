// * 请求响应参数(不包含data)
export interface Result {
	status: string | number;
	message?: string;
}

// * 请求响应参数(包含data)
export interface ResultData<T = any> extends Result {
	data: T;
}

// * 分页响应参数
export interface ResPage<T> {
	list: T[];
	pageNum: number;
	pageSize: number;
	total: number;
}

// * 分页请求参数
export interface ReqPage {
	pageNum: number;
	pageSize: number;
}

// * 文件上传模块
export namespace Upload {
	export interface ResFileUrl {
		url: string;
		name: string;
	}
}

// * 登录模块
export namespace Login {
	export interface ReqLoginForm {
		username?: string;
		mobile?: string;
		password?: string;
		captcha?: string;
		code?: string;
	}
	export interface AccountLoginForm {
		name: string;
		password: string;
		imgCaptcha: string;
	}
	export interface PhoneLoginForm {
		name: string;
		captcha: string;
	}
	export interface RegisterForm {
		phone: string; //手机号
		phoneCaptcha: string; //短信验证码
		captcha?: string; //图形验证码
		pwd: string; //密码
		pwd2: string; //重复密码
		sourceType?: string;
	}
	export interface UpdatePwdForm {
		oldPassword: string;
		newPassword: string;
		passwordConfirmation?: string;
	}
	export interface ResLogin {
		roleKey: string;
		roleName?: string;
		name: string;
		nickName?: string;
		phone: string;
	}
	export interface ResAuthButtons {
		[key: string]: string[];
	}
}

// * 用户管理模块
export namespace User {
	export interface ReqUserParams extends ReqPage {
		username: string;
		gender: number;
		idCard: string;
		email: string;
		address: string;
		createTime: string[];
		status: number;
	}
	export interface ResUserList {
		id: string;
		username: string;
		gender: string;
		user: {
			detail: {
				age: number;
			};
		};
		idCard: string;
		email: string;
		address: string;
		createTime: string;
		status: number;
		avatar: string;
		children?: ResUserList[];
	}
	export interface ResStatus {
		userLabel: string;
		userValue: number;
	}
	export interface ResGender {
		genderLabel: string;
		genderValue: number;
	}
	export interface ResDepartment {
		id: string;
		name: string;
		children?: ResDepartment[];
	}
	export interface ResRole {
		id: string;
		name: string;
		children?: ResDepartment[];
	}
}
