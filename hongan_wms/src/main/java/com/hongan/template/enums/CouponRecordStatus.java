package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author: zyp
 * @Date: 2022/2/17 0017
 */
public enum CouponRecordStatus {
    Available("Available", "未使用"),
    Used("Used", "已使用"),
    Expired("Expired", "已过期"),
    ;
    @EnumValue
    String msg;
    String remark;

    CouponRecordStatus(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
