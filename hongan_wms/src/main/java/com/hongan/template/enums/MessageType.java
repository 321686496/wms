package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum MessageType {
    MallOrder("MallOrder", "商城订单"),
    ServiceOrder("ServiceOrder", "服务订单"),
    ;
    @EnumValue
    String msg;
    String remark;

    MessageType(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
