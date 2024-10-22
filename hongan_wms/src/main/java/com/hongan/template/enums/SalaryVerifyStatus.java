package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum SalaryVerifyStatus {
    NotVerify("NotVerify", "未审核"),
    Audit("Audit", "已审核"),
    IsSend("IsSend", "已发放"),
    ;

    @EnumValue
    String msg;
    String remark;

    SalaryVerifyStatus(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getMsg() {
        return msg;
    }

    public String getRemark() {
        return remark;
    }
}
