package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum FeedbackType {
    Hold("Hold", "保留"),
    OffLine("OffLine", "下架"),
    ;
    @EnumValue
    String msg;
    String remark;

    FeedbackType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
