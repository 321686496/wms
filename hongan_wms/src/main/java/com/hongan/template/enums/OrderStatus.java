package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author: zyp
 * @Date: 2021/11/26 0026
 */
public enum OrderStatus {
    WaitPay("WaitPay", "等待支付"),
    PaySuccess("PaySuccess", "待发货"),
    Shipped("Shipped", "待收货(已发货)"),
    WaitEvaluate("WaitEvaluate", "待评价"),
    Completed("Completed", "已完成"),
    PartRefund("PartRefund", "部分退款"),
    AllRefund("AllRefund", "全部退款"),
    Cancel("Cancel", "已取消"),
    ;
    @EnumValue
    String msg;
    String remark;

    OrderStatus(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
