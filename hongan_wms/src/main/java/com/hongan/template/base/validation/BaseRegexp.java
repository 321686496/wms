package com.hongan.template.base.validation;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: BaseRegexp
*/

public interface BaseRegexp {
    // 简单手机号正则（这里只是简单校验是否为 11位，实际规则很复杂并在不断变化）
    String MOBILE = "^([0-9]{1,3}-)?1[0-9]{10}$";
    // 密码正则
    String PASSWORD = "^((?![a-zA-Z]+$)(?![0-9a-z]+$)(?![0-9A-Z]+$)(?![^A-Za-z]+$)(?![^0-9a-z]+$)(?![^0-9A-Z]+$)).{8,}$";
    // 邮箱正则
    String EMAIL = "^[A-Za-z0-9_-\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
}
