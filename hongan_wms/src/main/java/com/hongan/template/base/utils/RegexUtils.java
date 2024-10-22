package com.hongan.template.base.utils;

import com.hongan.template.base.entity.BaseException;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: zyp
 * @Date: 2022/12/13 0013
 */
public class RegexUtils {
    //    static final String PHONE_REGEX = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0-9])|(18[0,2-9]))\\d{8}$";
    static final String PHONE_REGEX = "^1[3456789]\\d{9}$";

    //校验手机号

    /**
     * 校验手机号
     *
     * @param phone
     * @throws BaseException
     */
    public static void checkPhone(String phone) throws BaseException {
        if (StringUtils.isEmpty(phone))
            throw new BaseException(20088, "请输入手机号！");
        boolean res = phone.matches(PHONE_REGEX);
        if (!res) {
            throw new BaseException(20088, "手机号填写错误！");
        }
    }
}
