package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author: zyp
 * @Date: 2022/2/25 0025
 */
public enum UserAccountRecordStatus {
    WaitPay("WaitPay", "等待支付"),
    Success("Success", "成功"),
    Error("Error", "失败"),
    ;
    @EnumValue
    String msg;
    String remark;

    UserAccountRecordStatus(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
