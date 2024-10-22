package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum PrizeType {
    Goods("Goods", "商品"),
    Cash("Cash", "现金"),
    ;

    @EnumValue
    String msg;
    String remark;

    PrizeType(String msg, String remark) {
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
