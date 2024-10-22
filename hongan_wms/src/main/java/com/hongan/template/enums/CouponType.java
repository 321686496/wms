package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author: zyp
 * @Date: 2022/2/25 0025
 */
public enum CouponType {
    NoThreshold("NoThreshold", "无门槛"),
    FullReduction("FullReduction", "满减"),
    ;
    @EnumValue
    String msg;
    String remark;

    CouponType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
