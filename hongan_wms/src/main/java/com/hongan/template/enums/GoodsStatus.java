package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum GoodsStatus {
    OnLine("OnLine", "上架"),
    OffLine("OffLine", "下架"),
    ;
    @EnumValue
    String msg;
    String remark;

    GoodsStatus(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

}
