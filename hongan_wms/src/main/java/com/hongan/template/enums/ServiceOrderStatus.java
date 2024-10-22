package com.hongan.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author: zyp
 * @Date: 2021/11/26 0026
 */
public enum ServiceOrderStatus {
    WaitReceive("WaitReceive", "待接单"),
    InProgress("InProgress", "进行中"),
    Completed("Completed", "已完成"),
    Reward("Reward", "已打赏"),
    Cancel("Cancel", "已取消"),
    ;
    @EnumValue
    String msg;
    String remark;

    ServiceOrderStatus(String msg, String remark) {
        this.msg = msg;
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
