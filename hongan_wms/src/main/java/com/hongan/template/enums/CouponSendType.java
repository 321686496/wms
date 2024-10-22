package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author: zyp
 * @Date: 2022/2/25 0025
 */
public enum CouponSendType {
    ManualClaim("ManualClaim", "手动领取"),
    RegisterSend("RegisterSend", "注册赠送"),
    ;
    @EnumValue
    String msg;
    String remark;

    CouponSendType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
