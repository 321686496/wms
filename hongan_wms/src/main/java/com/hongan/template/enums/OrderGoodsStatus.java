package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum OrderGoodsStatus {
    Normal("Normal","无退款"),//
    PartialRefund("PartialRefund","部分退款"),
    AllRefund("AllRefund","全部退款"),
    RefundVerify("RefundVerify","退款审核中"),
    ;
    @EnumValue
    String msg;
    String remark;

    OrderGoodsStatus(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

}
