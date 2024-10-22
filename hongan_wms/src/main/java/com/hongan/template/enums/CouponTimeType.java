package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author: zyp
 * @Date: 2022/2/25 0025
 */
public enum CouponTimeType {
    Days("Days", "有效天数"),
    Times("Times", "有效时间"),
    ;
    @EnumValue
    String msg;
    String remark;

    CouponTimeType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
